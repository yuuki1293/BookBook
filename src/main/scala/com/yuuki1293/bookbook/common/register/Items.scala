package com.yuuki1293.bookbook.common.register

import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.common.item.{BaseItemCompressed, ItemBook, ItemDrownedBook}
import net.minecraft.world.item.{BlockItem, CreativeModeTab, Item}
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.{DeferredRegister, ForgeRegistries, RegistryObject}

object Items {
  val ITEMS: DeferredRegister[Item] = DeferredRegister.create(ForgeRegistries.ITEMS, BookBook.MODID)

  val BOOK: RegistryObject[ItemBook] = ITEMS.register("book", () => new ItemBook(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)))
  val DROWNED_BOOK: RegistryObject[ItemDrownedBook] = ITEMS.register("drowned_book", () => new ItemDrownedBook(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)))
  val COMPRESSED_BOOK_0: RegistryObject[BaseItemCompressed] = registryCBook(0, new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS))
  val COMPRESSED_BOOK_1: RegistryObject[BaseItemCompressed] = registryCBook(1, new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS))
  val COMPRESSED_BOOK_2: RegistryObject[BaseItemCompressed] = registryCBook(2, new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS))
  val COMPRESSED_DROWNED_BOOK_0: RegistryObject[BaseItemCompressed] = registryCDBook(0, new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS))
  val COMPRESSED_DROWNED_BOOK_1: RegistryObject[BaseItemCompressed] = registryCDBook(1, new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS))
  val COMPRESSED_DROWNED_BOOK_2: RegistryObject[BaseItemCompressed] = registryCDBook(2, new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS))

  val BOOKSHELF: RegistryObject[BlockItem] = ITEMS.register(Blocks.BOOKSHELF.getId.getPath, () => new BlockItem(Blocks.BOOKSHELF.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)))
  val DROWNED_BOOKSHELF: RegistryObject[BlockItem] = ITEMS.register(Blocks.DROWNED_BOOKSHELF.getId.getPath, () => new BlockItem(Blocks.DROWNED_BOOKSHELF.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)))

  def registryCompressed[T <: BaseItemCompressed](prefix: String)(tier: Int, properties: Item.Properties)(implicit tag: reflect.ClassTag[T]): RegistryObject[T] = {
    ITEMS.register(s"$prefix$tier", () => tag.runtimeClass.getDeclaredConstructors.head.newInstance(tier, properties).asInstanceOf[T])
  }

  def registryCBook: (Int, Item.Properties) => RegistryObject[BaseItemCompressed] = registryCompressed[BaseItemCompressed]("compressed_book_")

  def registryCDBook: (Int, Item.Properties) => RegistryObject[BaseItemCompressed] = registryCompressed[BaseItemCompressed]("compressed_drowned_book_")

  def registry(): Unit = {
    ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus)
  }
}
