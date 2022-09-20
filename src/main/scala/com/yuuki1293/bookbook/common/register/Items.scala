package com.yuuki1293.bookbook.common.register

import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.common.item.{ItemBook, BaseItemCompressed, ItemDrownedBook}
import net.minecraft.world.item.Item.Properties
import net.minecraft.world.item.{BlockItem, CreativeModeTab, Item}
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.{DeferredRegister, ForgeRegistries, RegistryObject}

object Items {
  val ITEMS: DeferredRegister[Item] = DeferredRegister.create(ForgeRegistries.ITEMS, BookBook.MODID)

  val BOOK: RegistryObject[ItemBook] = ITEMS.register("book", () => new ItemBook(new Properties().tab(CreativeModeTab.TAB_MATERIALS)))
  val DROWNED_BOOK: RegistryObject[ItemDrownedBook] = ITEMS.register("drowned_book", () => new ItemDrownedBook(new Properties().tab(CreativeModeTab.TAB_MATERIALS)))
  val COMPRESSED_BOOK_0: RegistryObject[BaseItemCompressed] = registryCBook(0, new Properties().tab(CreativeModeTab.TAB_MATERIALS))
  val COMPRESSED_BOOK_1: RegistryObject[BaseItemCompressed] = registryCBook(1, new Properties().tab(CreativeModeTab.TAB_MATERIALS))
  val COMPRESSED_BOOK_2: RegistryObject[BaseItemCompressed] = registryCBook(2, new Properties().tab(CreativeModeTab.TAB_MATERIALS))

  val BOOKSHELF: RegistryObject[BlockItem] = ITEMS.register(Blocks.BOOKSHELF.getId.getPath, () => new BlockItem(Blocks.BOOKSHELF.get(), new Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)))
  val DROWNED_BOOKSHELF: RegistryObject[BlockItem] = ITEMS.register(Blocks.DROWNED_BOOKSHELF.getId.getPath, () => new BlockItem(Blocks.DROWNED_BOOKSHELF.get(), new Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)))

  def registryCompressed[T <: Item](prefix: String)(tier: Int, properties: Properties): RegistryObject[T] = {
    ITEMS.register(s"$prefix$tier", () => new T(properties))
  }

  def registryCBook: (Int, Properties) => RegistryObject[BaseItemCompressed] = registryCompressed[BaseItemCompressed]("compressed_book_")

  def registry(): Unit = {
    ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus)
  }
}
