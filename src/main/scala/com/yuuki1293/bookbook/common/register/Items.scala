package com.yuuki1293.bookbook.common.register

import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.common.item.{BaseCompressedItem, BookItem, UnstableBookItem}
import net.minecraft.world.item.{BlockItem, Item}
import net.minecraftforge.registries.{DeferredRegister, ForgeRegistries, RegistryObject}

object Items extends AbstractRegister[Item] {
  private val itemGroup = new BookBookItemGroup
  private val default = () => new Item.Properties().tab(itemGroup)

  val REGISTER: DeferredRegister[Item] = DeferredRegister.create(ForgeRegistries.ITEMS, BookBook.MOD_ID)

  val BOOK: RegistryObject[BookItem] = REGISTER.register("book", () => BookItem(default()))
  val DROWNED_BOOK: RegistryObject[Item] = REGISTER.register("drowned_book", () => new Item(default()))
  val COMPRESSED_BOOK_0: RegistryObject[BaseCompressedItem] = registryCBook(0, default())
  val COMPRESSED_BOOK_1: RegistryObject[BaseCompressedItem] = registryCBook(1, default())
  val COMPRESSED_BOOK_2: RegistryObject[BaseCompressedItem] = registryCBook(2, default())
  val UNSTABLE_BOOK: RegistryObject[UnstableBookItem] = REGISTER.register("unstable_book", () => UnstableBookItem(default()))

  val BOOKSHELF: RegistryObject[BlockItem] = REGISTER.register(Blocks.BOOKSHELF.getId.getPath, () => new BlockItem(Blocks.BOOKSHELF.get(), default()))
  val DROWNED_BOOKSHELF: RegistryObject[BlockItem] = REGISTER.register(Blocks.DROWNED_BOOKSHELF.getId.getPath, () => new BlockItem(Blocks.DROWNED_BOOKSHELF.get(), default()))
  val BOOK_FURNACE: RegistryObject[BlockItem] = REGISTER.register(Blocks.BOOK_FURNACE.getId.getPath, () => new BlockItem(Blocks.BOOK_FURNACE.get(), default()))
  val BOOK_GENERATOR: RegistryObject[BlockItem] = REGISTER.register(Blocks.BOOK_GENERATOR.getId.getPath, () => new BlockItem(Blocks.BOOK_GENERATOR.get(), default()))
  val BOOKSHELF_FRAME: RegistryObject[BlockItem] = REGISTER.register(Blocks.BOOKSHELF_FRAME.getId.getPath, () => new BlockItem(Blocks.BOOKSHELF_FRAME.get(), default()))
  val BOOK_CAPACITOR: RegistryObject[BlockItem] = REGISTER.register(Blocks.BOOK_CAPACITOR.getId.getPath, () => new BlockItem(Blocks.BOOK_CAPACITOR.get(), default()))
  val BOOK_CRAFTING_CORE: RegistryObject[BlockItem] = REGISTER.register(Blocks.BOOK_CRAFTING_CORE.getId.getPath, () => new BlockItem(Blocks.BOOK_CRAFTING_CORE.get(), default()))
  val BOOK_STAND: RegistryObject[BlockItem] = REGISTER.register(Blocks.BOOK_STAND.getId.getPath, () => new BlockItem(Blocks.BOOK_STAND.get(), default()))

  def registryCompressed[A <: BaseCompressedItem](prefix: String)(tier: Int, properties: Item.Properties)(implicit tag: reflect.ClassTag[A]): RegistryObject[A] = {
    REGISTER.register(s"$prefix$tier", () => tag.runtimeClass.getDeclaredConstructors.head.newInstance(tier, properties).asInstanceOf[A])
  }

  def registryCBook: (Int, Item.Properties) => RegistryObject[BaseCompressedItem] = registryCompressed[BaseCompressedItem]("compressed_book_")
}
