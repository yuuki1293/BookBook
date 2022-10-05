package com.yuuki1293.bookbook.common

import com.yuuki1293.bookbook.common.register._
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BookBook.MODID)
object BookBook {
    val MODID = "bookbook"

    val eventBus: IEventBus = FMLJavaModLoadingContext.get().getModEventBus

    Items.registry(eventBus)
    Blocks.registry(eventBus)
    BlockEntities.registry(eventBus)
    MenuTypes.registry(eventBus)
    Events.registry()
    MinecraftForge.EVENT_BUS.register(this)
}
