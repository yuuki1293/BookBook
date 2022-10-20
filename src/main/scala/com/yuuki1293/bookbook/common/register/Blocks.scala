package com.yuuki1293.bookbook.common.register

import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.common.block._
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.{Block, SoundType}
import net.minecraft.world.level.material.Material
import net.minecraftforge.registries.{DeferredRegister, ForgeRegistries, RegistryObject}

object Blocks extends AbstractRegister[Block] {
  private val default = () => BlockBehaviour.Properties.of(Material.WOOD).strength(1.5F).sound(SoundType.WOOD)

  val REGISTER: DeferredRegister[Block] = DeferredRegister.create(ForgeRegistries.BLOCKS, BookBook.MOD_ID)

  val BOOKSHELF: RegistryObject[BookShelfBlock] = REGISTER.register("bookshelf", () => BookShelfBlock(default()))
  val DROWNED_BOOKSHELF: RegistryObject[Block] = REGISTER.register("drowned_bookshelf", () => new Block(default()))
  val BOOK_FURNACE: RegistryObject[BookFurnaceBlock] = REGISTER.register("book_furnace", () => BookFurnaceBlock(default().requiresCorrectToolForDrops().strength(3.5F).lightLevel(p => if (p.getValue(BlockStateProperties.LIT)) 13 else 0)))
  val BOOK_GENERATOR: RegistryObject[BookGeneratorBlock] = REGISTER.register("book_generator", () => BookGeneratorBlock(default().requiresCorrectToolForDrops().strength(3.5F).lightLevel(p => if (p.getValue(BlockStateProperties.LIT)) 13 else 0)))
  val BOOKSHELF_FRAME: RegistryObject[Block] = REGISTER.register("bookshelf_frame", () => new Block(default().strength(1.0F).noOcclusion()))
  val BOOK_CAPACITOR: RegistryObject[BookCapacitorBlock] = REGISTER.register("book_capacitor", () => BookCapacitorBlock(default().requiresCorrectToolForDrops().strength(3.5F)))
  val BOOK_CRAFTING_CORE: RegistryObject[BookCraftingCoreBlock] = REGISTER.register("book_crafting_core", () => BookCraftingCoreBlock(default().requiresCorrectToolForDrops().strength(5.0F)))
  val BOOK_STAND: RegistryObject[BookStandBlock] = REGISTER.register("book_stand", () => BookStandBlock(default().requiresCorrectToolForDrops().strength(3.0F).lightLevel(_ => 7)))
}