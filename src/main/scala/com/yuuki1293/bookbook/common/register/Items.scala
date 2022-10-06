package com.yuuki1293.bookbook.common.register

import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.common.item.{BaseCompressedItem, BookItem}
import net.minecraft.world.item.{BlockItem, Item}
import net.minecraftforge.registries.{DeferredRegister, ForgeRegistries, RegistryObject}

object Items extends AbstractRegister[Item] {
  private val itemGroup = new BookBookItemGroup
  private val default = () => new Item.Properties().tab(itemGroup)

  val REGISTER: DeferredRegister[Item] = DeferredRegister.create(ForgeRegistries.ITEMS, BookBook.MODID)

  val BOOK: RegistryObject[BookItem] = REGISTER.register("book", () => new BookItem(default()))
  val DROWNED_BOOK: RegistryObject[Item] = REGISTER.register("drowned_book", () => new Item(default()))
  val COMPRESSED_BOOK_0: RegistryObject[BaseCompressedItem] = registryCBook(0, default())
  val COMPRESSED_BOOK_1: RegistryObject[BaseCompressedItem] = registryCBook(1, default())
  val COMPRESSED_BOOK_2: RegistryObject[BaseCompressedItem] = registryCBook(2, default())

  val BOOKSHELF: RegistryObject[BlockItem] = REGISTER.register(Blocks.BOOKSHELF.getId.getPath, () => new BlockItem(Blocks.BOOKSHELF.get(), default()))
  val DROWNED_BOOKSHELF: RegistryObject[BlockItem] = REGISTER.register(Blocks.DROWNED_BOOKSHELF.getId.getPath, () => new BlockItem(Blocks.DROWNED_BOOKSHELF.get(), default()))
  val BOOK_FURNACE: RegistryObject[BlockItem] = REGISTER.register(Blocks.BOOK_FURNACE.getId.getPath, () => new BlockItem(Blocks.BOOK_FURNACE.get(), default()))
  val BOOK_GENERATOR: RegistryObject[BlockItem] = REGISTER.register(Blocks.BOOK_GENERATOR.getId.getPath, () => new BlockItem(Blocks.BOOK_GENERATOR.get(), default()))

  def registryCompressed[T <: BaseCompressedItem](prefix: String)(tier: Int, properties: Item.Properties)(implicit tag: reflect.ClassTag[T]): RegistryObject[T] = {
    REGISTER.register(s"$prefix$tier", () => tag.runtimeClass.getDeclaredConstructors.head.newInstance(tier, properties).asInstanceOf[T])
  }

  def registryCBook: (Int, Item.Properties) => RegistryObject[BaseCompressedItem] = registryCompressed[BaseCompressedItem]("compressed_book_")
}
