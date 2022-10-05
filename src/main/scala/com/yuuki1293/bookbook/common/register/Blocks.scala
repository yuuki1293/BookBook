package com.yuuki1293.bookbook.common.register

import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.common.block.{BookFurnaceBlock, BookGeneratorBlock, BookShelfBlock, DrownedBookShelfBlock}
import net.minecraft.world.level.block.{Block, SoundType}
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.material.Material
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.{DeferredRegister, ForgeRegistries, RegistryObject}

object Blocks {
  private val default = () => BlockBehaviour.Properties.of(Material.WOOD).strength(1.5F).sound(SoundType.WOOD)

  val BLOCKS: DeferredRegister[Block] = DeferredRegister.create(ForgeRegistries.BLOCKS, BookBook.MODID)

  val BOOKSHELF: RegistryObject[BookShelfBlock] = BLOCKS.register("bookshelf", () => new BookShelfBlock(default()))
  val DROWNED_BOOKSHELF: RegistryObject[DrownedBookShelfBlock] = BLOCKS.register("drowned_bookshelf", () => new DrownedBookShelfBlock(default()))
  val BOOK_FURNACE: RegistryObject[BookFurnaceBlock] = BLOCKS.register("book_furnace", () => new BookFurnaceBlock(default().requiresCorrectToolForDrops().strength(3.5F).lightLevel(p => if (p.getValue(BlockStateProperties.LIT)) 13 else 0)))
  val BOOK_GENERATOR: RegistryObject[BookGeneratorBlock] = BLOCKS.register("book_generator", () => new BookGeneratorBlock(default().requiresCorrectToolForDrops().strength(3.5F).lightLevel(p => if (p.getValue(BlockStateProperties.LIT)) 13 else 0)))

  def registry(implicit eventBus: IEventBus): Unit = BLOCKS.register(eventBus)
}