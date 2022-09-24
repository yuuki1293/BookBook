package com.yuuki1293.bookbook.common.register

import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.common.block.{BookFurnaceBlock, BookShelfBlock, DrownedBookShelfBlock}
import net.minecraft.world.level.block.{Block, SoundType}
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.material.Material
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.{DeferredRegister, ForgeRegistries, RegistryObject}

object Blocks {
  val BLOCKS: DeferredRegister[Block] = DeferredRegister.create(ForgeRegistries.BLOCKS, BookBook.MODID)

  val BOOKSHELF: RegistryObject[BookShelfBlock] = BLOCKS.register("bookshelf", () => new BookShelfBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(1.5F).sound(SoundType.WOOD)))
  val DROWNED_BOOKSHELF: RegistryObject[DrownedBookShelfBlock] = BLOCKS.register("drowned_bookshelf", () => new DrownedBookShelfBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(1.5F).sound(SoundType.WOOD)))
  val BOOK_FURNACE: RegistryObject[BookFurnaceBlock] = BLOCKS.register("book_furnace", () => new BookFurnaceBlock(BlockBehaviour.Properties.of(Material.WOOD).requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.WOOD).lightLevel(p => if (p.getValue(BlockStateProperties.LIT)) 13 else 0 )))

  def registry(): Unit = {
    BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus)
  }
}