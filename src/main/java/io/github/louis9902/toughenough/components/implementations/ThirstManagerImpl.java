package io.github.louis9902.toughenough.components.implementations;

import io.github.louis9902.toughenough.MyComponents;
import io.github.louis9902.toughenough.components.DrinkableComponent;
import io.github.louis9902.toughenough.components.ThirstManager;
import io.github.louis9902.toughenough.init.Gameplay;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ThirstManagerImpl implements ThirstManager {
    private static final int MAX_THIRST_LEVEL = 20;
    private static final int MIN_THIRST_REGENERATION_LEVEL = 18;
    private static final float THIRST_EXHAUSTION_THRESHOLD = 4.0f;
    private static final float HYDRATION_MULTIPLIER_MAGIC_CONSTANT = 2.0F;

    //thirst is similar to hunger, it has 20 levels
    int thirst = 20;
    //hydration is the amount of water that was consumed beyond the max thirst level
    float hydration = 5;
    //exhaustion is incremented each time the player does something that consumes thirst,
    //when it has reached a threshold the thirst will be decreased and exhaustion will be reset
    float exhaustion = 0;

    //This value represents the time passed since the user took the last time damage because of thirst.
    private int counter;

    //Synced Components need to store the entity they are attached to
    final Entity provider;

    public ThirstManagerImpl(Entity provider) {
        this.provider = provider;
    }

    @Override
    public void drink(ItemStack item) {
        Optional<DrinkableComponent> drink = MyComponents.DRINKABLE.maybeGet(item);
        if (drink.isPresent()) {
            thirst += drink.get().getThirst();
            hydration += drink.get().getHydrationModifier();
            //only call sync at the end and don't use setts to save packets
            sync();
        }
    }

    @Override
    public void update(PlayerEntity player) {
        System.out.println("thirst:" + thirst);
        System.out.println("hydration:" + hydration);
        System.out.println("exhaustion:" + exhaustion);
        System.out.println();

        if (!Gameplay.isThirstEnabled(player.world) || player.isCreative()) return;

        Difficulty difficulty = player.world.getDifficulty();

        // if the exhaustion is higher than the max we normalise
        if (this.exhaustion > THIRST_EXHAUSTION_THRESHOLD) {
            this.exhaustion -= THIRST_EXHAUSTION_THRESHOLD;

            if (this.hydration > 0.0f) {
                // if we have hydration we drain some of it
                this.hydration = Math.max(this.hydration - 1.0f, 0.0f);
            } else if (difficulty != Difficulty.PEACEFUL) {
                // otherwise we drain the thirst
                this.thirst = Math.max(this.thirst - 1, 0);
            }
            // TODO: make food also dependant on thirst and the other way around
            boolean regenerate = player.world.getGameRules().getBoolean(GameRules.NATURAL_REGENERATION);

            if (regenerate && canPlayerRegenerateHealth(player)) {
                // check if player can regenerate naturally
                this.counter++;

                if (this.thirst >= MAX_THIRST_LEVEL && this.hydration > 0.0f) {
                    // if so we want to use the fast regeneration first (hydration)
                    if (this.counter >= 10 /* 0.5 sec */) {
                        float heal = Math.min(this.hydration, 6.0f);
                        player.heal(heal / 6.0f);
                        exhaustion += heal / 6.0F;
                        this.counter = 0;
                    }
                } else if (this.thirst >= MIN_THIRST_REGENERATION_LEVEL) {
                    // if we dont have hydration anymore we drain some of the normal thirst
                    if (this.counter >= 80 /* 4 sec */) {
                        player.heal(1.0F);
                        exhaustion += 6.0F;
                        this.counter = 0;
                    }
                }
            } else if (this.thirst <= 0) {
                // check if player needs to get some damage because of thirst
                this.counter++;
                if (this.counter >= 80 /* 4 sec */) {
                    boolean bypass = player.getHealth() > 10.0F;
                    boolean isHard = difficulty == Difficulty.HARD;
                    boolean isNormal = difficulty == Difficulty.NORMAL && player.getHealth() > 1.0F;
                    if (bypass || isHard || isNormal) {
                        player.damage(DamageSource.STARVE, 1.0F);
                    }
                    this.counter = 0;
                }
            } else {
                // just do nothing here keep counter low
                this.counter = 0;
            }

            //we call sync only once at the end to save packets
            //data definitely changed if first if condition was true
            sync();
        }

    }

    //TODO wir wollten eigentlich diese methode hier verwenden, ist die gut geignet oder sollte die logik in den settern gemacht werden
    public void add(int thirst, float hydration) {
        this.setThirst(Math.min(MAX_THIRST_LEVEL, this.thirst + thirst));
        this.setHydration(Math.min((float) this.thirst, this.hydration + (thirst * hydration * HYDRATION_MULTIPLIER_MAGIC_CONSTANT)));
    }

    private static boolean canPlayerRegenerateHealth(PlayerEntity player) {
        return player.getHealth() > 0.0f && player.getHealth() < player.getMaxHealth();
    }

    @Override
    public @NotNull Entity getEntity() {
        return provider;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        thirst = tag.getInt("thirst");
        hydration = tag.getFloat("hydration");
        exhaustion = tag.getFloat("exhaustion");
        sync();
    }

    @Override
    public @NotNull CompoundTag toTag(CompoundTag tag) {
        tag.putInt("thirst", thirst);
        tag.putFloat("hydration", hydration);
        tag.putFloat("exhaustion", exhaustion);
        return tag;
    }

    //We override this so that callling sync() only transmits the thirst information to the player it
    //belongs to, this is to save network traffic!
    @Override
    public void syncWith(ServerPlayerEntity player) {
        if (player == this.provider) ThirstManager.super.syncWith(player);
    }

    //region Getters and Setters
    @Override
    public int getThirst() {
        return thirst;
    }

    @Override
    public float getHydration() {
        return hydration;
    }

    @Override
    public float getExhaustion() {
        return exhaustion;
    }

    @Override
    public void setThirst(int t) {
        sync();
        thirst = t;
    }

    @Override
    public void setHydration(float h) {
        sync();
        hydration = h;
    }

    @Override
    public void setExhaustion(float e) {
        sync();
        exhaustion = e;
    }
    //endregion
}
