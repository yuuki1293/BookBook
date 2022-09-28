package com.yuuki1293.bookbook.common;

import com.yuuki1293.bookbook.common.register.*;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BookBook.MODID)
public class BookBook {
    public static final String MODID = "bookbook";
    // Directly reference a slf4j logger

    public BookBook() {
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        Items.registry();
        Blocks.registry();
        BlockEntities.registry(eventBus);
        Events.registry();
        eventBus.addGenericListener(RecipeSerializer.class, this::registerRecipeSerializers);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void registerRecipeSerializers(RegistryEvent.Register<RecipeSerializer<?>> event) {
        Recipe.registry(event.getRegistry());
    }
}
