package com.yuuki1293.bookbook.common

import com.yuuki1293.bookbook.common.register._
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext

@Mod(BookBook.MODID)
object BookBook {
    final val MODID = "bookbook"

    implicit private val eventBus: IEventBus = FMLJavaModLoadingContext.get().getModEventBus

    Items.registry
    Blocks.registry
    BlockEntities.registry
    MenuTypes.registry
}
