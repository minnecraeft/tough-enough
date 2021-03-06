package io.github.minnecraeft.toughenough.init;

import io.github.minnecraeft.toughenough.fluid.PurifiedWater;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class ToughEnoughFluids {

    public static final PurifiedWater PURIFIED_WATER_STILL;
    public static final PurifiedWater PURIFIED_WATER_FLOWING;
    public static final Item PURIFIED_WATER_BUCKET;
    public static final Item PURIFIED_WATER_BOTTLE;
    public static final Block PURIFIED_WATER_BLOCK;

    static {
        PURIFIED_WATER_STILL = RegistryHelpers.register("purified_water_still", new PurifiedWater.Still());
        PURIFIED_WATER_FLOWING = RegistryHelpers.register("purified_water_flowing", new PurifiedWater.Flowing());
        PURIFIED_WATER_BUCKET = RegistryHelpers.register("purified_water_bucket", new BucketItem(PURIFIED_WATER_STILL, ToughEnoughItems.ItemSettings().recipeRemainder(Items.BUCKET).maxCount(1)));
        PURIFIED_WATER_BLOCK = RegistryHelpers.register("purified_water", new FluidBlock(PURIFIED_WATER_STILL, FabricBlockSettings.copyOf(Blocks.WATER)) {
        });
        PURIFIED_WATER_BOTTLE = RegistryHelpers.register("purified_water_bottle", new Item(ToughEnoughItems.ItemSettings().maxCount(1)));
    }

    @SuppressWarnings("EmptyMethod")
    public static void register() {
        // keep for class initialisation (call from initializer)
    }
}
