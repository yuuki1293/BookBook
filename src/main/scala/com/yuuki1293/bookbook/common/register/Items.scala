package com.yuuki1293.bookbook.common.register

import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.common.item.{BaseCompressedItem, BookItem}
import net.minecraft.world.item.{BlockItem, Item}
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.{DeferredRegister, ForgeRegistries, RegistryObject}

object Items {
  private val itemGroup = new BookBookItemGroup
  private val default = () => new Item.Properties().tab(itemGroup)

  val ITEMS: DeferredRegister[Item] = DeferredRegister.create(ForgeRegistries.ITEMS, BookBook.MODID)

  val BOOK: RegistryObject[BookItem] = ITEMS.register("book", () => new BookItem(default()))
  val DROWNED_BOOK: RegistryObject[Item] = ITEMS.register("drowned_book", () => new Item(default()))
  val COMPRESSED_BOOK_0: RegistryObject[BaseCompressedItem] = registryCBook(0, default())
  val COMPRESSED_BOOK_1: RegistryObject[BaseCompressedItem] = registryCBook(1, default())
  val COMPRESSED_BOOK_2: RegistryObject[BaseCompressedItem] = registryCBook(2, default())

  val BOOKSHELF: RegistryObject[BlockItem] = ITEMS.register(Blocks.BOOKSHELF.getId.getPath, () => new BlockItem(Blocks.BOOKSHELF.get(), default()))
  val DROWNED_BOOKSHELF: RegistryObject[BlockItem] = ITEMS.register(Blocks.DROWNED_BOOKSHELF.getId.getPath, () => new BlockItem(Blocks.DROWNED_BOOKSHELF.get(), default()))
  val BOOK_FURNACE: RegistryObject[BlockItem] = ITEMS.register(Blocks.BOOK_FURNACE.getId.getPath, () => new BlockItem(Blocks.BOOK_FURNACE.get(), default()))
  val BOOK_GENERATOR: RegistryObject[BlockItem] = ITEMS.register(Blocks.BOOK_GENERATOR.getId.getPath, () => new BlockItem(Blocks.BOOK_GENERATOR.get(), default()))

  def registryCompressed[T <: BaseCompressedItem](prefix: String)(tier: Int, properties: Item.Properties)(implicit tag: reflect.ClassTag[T]): RegistryObject[T] = {
    ITEMS.register(s"$prefix$tier", () => tag.runtimeClass.getDeclaredConstructors.head.newInstance(tier, properties).asInstanceOf[T])
  }

  def registryCBook: (Int, Item.Properties) => RegistryObject[BaseCompressedItem] = registryCompressed[BaseCompressedItem]("compressed_book_")

  def registry(eventBus: IEventBus): Unit = ITEMS.register(eventBus)
}
