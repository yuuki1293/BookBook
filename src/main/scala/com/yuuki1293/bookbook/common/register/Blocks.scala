package com.yuuki1293.bookbook.common.register

import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.common.block.{BlockBookShelf, BlockDrownedBookShelf}
import net.minecraft.world.level.block.{Block, SoundType}
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.Material
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.{DeferredRegister, ForgeRegistries, RegistryObject}

object Blocks {
  val BLOCKS: DeferredRegister[Block] = DeferredRegister.create(ForgeRegistries.BLOCKS, BookBook.MODID)

  val BOOKSHELF: RegistryObject[BlockBookShelf] = BLOCKS.register("bookshelf", () => new BlockBookShelf(BlockBehaviour.Properties.of(Material.WOOD).strength(1.5F).sound(SoundType.WOOD)))
  val DROWNED_BOOKSHELF: RegistryObject[BlockDrownedBookShelf] = BLOCKS.register("drowned_bookshelf", () => new BlockDrownedBookShelf(BlockBehaviour.Properties.of(Material.WOOD).strength(1.5F).sound(SoundType.WOOD)))

  def registry(): Unit = {
    BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus)
  }
}
