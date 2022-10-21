package com.yuuki1293.bookbook.common

import com.yuuki1293.bookbook.common.register._
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext

@Mod(BookBook.MOD_ID)
object BookBook {
  final val MOD_ID = "bookbook"

  implicit private val eventBus: IEventBus = FMLJavaModLoadingContext.get().getModEventBus

  Items.registry
  Blocks.registry
  BlockEntities.registry
  MenuTypes.registry

  eventBus.register(RecipeSerializers)
}
