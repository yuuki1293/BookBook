package com.yuuki1293.bookbook.common.register

import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.common.block.BlockBookShelf
import net.minecraft.world.level.block.Block
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.{DeferredRegister, ForgeRegistries, RegistryObject}

object RegisterBlock {
  val BLOCKS: DeferredRegister[Block] = DeferredRegister.create(ForgeRegistries.BLOCKS, BookBook.MODID)

  val BOOK_SHELF: RegistryObject[BlockBookShelf] = BLOCKS.register("book_shelf", () => new BlockBookShelf())

  def registry(): Unit = {
    BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus)
  }
}
