package io.github.louis9902.toughenough.init;

import io.github.louis9902.toughenough.ToughEnough;
import io.github.louis9902.toughenough.item.CanteenItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;

import static net.minecraft.util.registry.Registry.ITEM;

public final class ToughEnoughItems {

    public static final CanteenItem CANTEEN_ITEM;

    static {
        CANTEEN_ITEM = register("canteen", new CanteenItem(new Item.Settings().group(ItemGroup.MISC)));
    }

    private static <T extends Item> T register(String name, T item) {
        return Registry.register(ITEM, ToughEnough.identifier(name), item);
    }

    public static void register() {
        // keep for class initialisation (call from initializer)
    }

}