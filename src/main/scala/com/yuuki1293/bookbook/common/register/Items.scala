package com.yuuki1293.bookbook.common.register

import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.common.item.{ItemBook, ItemDrownedBook}
import net.minecraft.world.item.Item.Properties
import net.minecraft.world.item.{BlockItem, CreativeModeTab, Item}
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.{DeferredRegister, ForgeRegistries, RegistryObject}

object Items {
  val ITEMS: DeferredRegister[Item] = DeferredRegister.create(ForgeRegistries.ITEMS, BookBook.MODID)

  val BOOK: RegistryObject[ItemBook] = ITEMS.register("book", () => new ItemBook)
  val DROWNED_BOOK: RegistryObject[ItemDrownedBook] = ITEMS.register("drowned_book", () => new ItemDrownedBook)
  val BOOKSHELF: RegistryObject[BlockItem] = ITEMS.register(Blocks.BOOKSHELF.getId.getPath, () => new BlockItem(Blocks.BOOKSHELF.get(), new Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)))
  val DROWNED_BOOKSHELF: RegistryObject[BlockItem] = ITEMS.register(Blocks.DROWNED_BOOKSHELF.getId.getPath, () => new BlockItem(Blocks.DROWNED_BOOKSHELF.get(), new Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)))

  def registry(): Unit = {
    ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus)
  }
}
