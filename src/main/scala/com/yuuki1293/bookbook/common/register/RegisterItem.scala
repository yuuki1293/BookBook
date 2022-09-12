package com.yuuki1293.bookbook.common.register

import com.yuuki1293.bookbook.common.item.ItemBook
import net.minecraft.world.item.{CreativeModeTab, Item}
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.{DeferredRegister, ForgeRegistries, RegistryObject}

object RegisterItem {
  val ITEMS: DeferredRegister[Item] = DeferredRegister.create(ForgeRegistries.ITEMS, "bookbook")

  val BOOK: RegistryObject[ItemBook] = ITEMS.register("book", () => new ItemBook)

  def registry(): Unit = {
    ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus)
  }
}
