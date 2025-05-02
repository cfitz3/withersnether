package com.allthemodiumcraft.withernether.registry;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import com.allthemodiumcraft.withernether.item.SealOfTheNetherItem;

import com.allthemodiumcraft.withernether.WithersNether;

public class ModItems {
    // Create a Deferred Register for items
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(WithersNether.MODID);

    // Register items
    public static final DeferredItem<Item> NETHER_SHARD_1 = ITEMS.registerSimpleItem("nether_shard_1",
            new Item.Properties().stacksTo(64).fireResistant());

    public static final DeferredItem<Item> NETHER_SHARD_2 = ITEMS.registerSimpleItem("nether_shard_2",
            new Item.Properties().stacksTo(54).fireResistant());
    
    public static final DeferredItem<Item> NETHER_SHARD_3 = ITEMS.registerSimpleItem("nether_shard_3",
            new Item.Properties().stacksTo(64).fireResistant());

    public static final DeferredItem<Item> NETHER_SHARD_4 = ITEMS.registerSimpleItem("nether_shard_4",
            new Item.Properties().stacksTo(64).fireResistant());

    public static final DeferredItem<Item> NETHER_ESSENCE = ITEMS.registerSimpleItem("nether_essence",
            new Item.Properties().stacksTo(64).fireResistant());

    public static final DeferredItem<Item> NETHER_CORE = ITEMS.registerSimpleItem("nether_core",
            new Item.Properties().stacksTo(1).fireResistant());

    public static final DeferredItem<Item> SEAL_OF_THE_NETHER = ITEMS.register("seal_of_the_nether",
            () -> new SealOfTheNetherItem(new Item.Properties().stacksTo(1).fireResistant()));

    // Call this method during mod initialization to register the items
    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}

