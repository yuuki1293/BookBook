package com.yuuki1293.bookbook.common.register

import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.common.item.{BaseCompressedItem, BookItem, DrownedBookItem}
import net.minecraft.world.item.{BlockItem, CreativeModeTab, Item}
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.{DeferredRegister, ForgeRegistries, RegistryObject}

object Items {
  private val itemGroup = new BookBookItemGroup
  private val default = () => new Item.Properties().tab(itemGroup)

  val ITEMS: DeferredRegister[Item] = DeferredRegister.create(ForgeRegistries.ITEMS, BookBook.MODID)

  val BOOK: RegistryObject[BookItem] = ITEMS.register("book", () => new BookItem(default()))
  val DROWNED_BOOK: RegistryObject[DrownedBookItem] = ITEMS.register("drowned_book", () => new DrownedBookItem(default()))
  val COMPRESSED_BOOK_0: RegistryObject[BaseCompressedItem] = registryCBook(0, default())
  val COMPRESSED_BOOK_1: RegistryObject[BaseCompressedItem] = registryCBook(1, default())
  val COMPRESSED_BOOK_2: RegistryObject[BaseCompressedItem] = registryCBook(2, default())
  val COMPRESSED_DROWNED_BOOK_0: RegistryObject[BaseCompressedItem] = registryCDBook(0, default())
  val COMPRESSED_DROWNED_BOOK_1: RegistryObject[BaseCompressedItem] = registryCDBook(1, default())
  val COMPRESSED_DROWNED_BOOK_2: RegistryObject[BaseCompressedItem] = registryCDBook(2, default())

  val BOOKSHELF: RegistryObject[BlockItem] = ITEMS.register(Blocks.BOOKSHELF.getId.getPath, () => new BlockItem(Blocks.BOOKSHELF.get(), default()))
  val DROWNED_BOOKSHELF: RegistryObject[BlockItem] = ITEMS.register(Blocks.DROWNED_BOOKSHELF.getId.getPath, () => new BlockItem(Blocks.DROWNED_BOOKSHELF.get(), default()))
  val BOOK_FURNACE: RegistryObject[BlockItem] = ITEMS.register(Blocks.BOOK_FURNACE.getId.getPath, () => new BlockItem(Blocks.BOOK_FURNACE.get(), default()))

  def registryCompressed[T <: BaseCompressedItem](prefix: String)(tier: Int, properties: Item.Properties)(implicit tag: reflect.ClassTag[T]): RegistryObject[T] = {
    ITEMS.register(s"$prefix$tier", () => tag.runtimeClass.getDeclaredConstructors.head.newInstance(tier, properties).asInstanceOf[T])
  }

  def registryCBook: (Int, Item.Properties) => RegistryObject[BaseCompressedItem] = registryCompressed[BaseCompressedItem]("compressed_book_")

  def registryCDBook: (Int, Item.Properties) => RegistryObject[BaseCompressedItem] = registryCompressed[BaseCompressedItem]("compressed_drowned_book_")

  def registry(): Unit = {
    ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus)
  }
}
