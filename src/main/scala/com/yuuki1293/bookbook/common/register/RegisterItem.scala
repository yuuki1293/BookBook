package com.yuuki1293.bookbook.common.register

import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.common.item.{ItemBook, ItemDrownedBook}
import net.minecraft.world.item.Item
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.{DeferredRegister, ForgeRegistries, RegistryObject}

object RegisterItem {
  val ITEMS: DeferredRegister[Item] = DeferredRegister.create(ForgeRegistries.ITEMS, BookBook.MODID)

  val BOOK: RegistryObject[ItemBook] = ITEMS.register("book", () => new ItemBook)
  val DROWNED_BOOK: RegistryObject[ItemDrownedBook] = ITEMS.register("drowned_book", () => new ItemDrownedBook)

  def registry(): Unit = {
    ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus)
  }
}
