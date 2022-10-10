package com.yuuki1293.bookbook.common.block

import com.yuuki1293.bookbook.common.block.BookCapacitorBlock._
import net.minecraft.world.level.block.state.{BlockBehaviour, BlockState, StateDefinition}
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.level.block.{Block, EntityBlock, HorizontalDirectionalBlock}

class BookCapacitorBlock(pProperties: BlockBehaviour.Properties) extends Block(pProperties) with EntityBlock {
  override def createBlockStateDefinition(pBuilder: StateDefinition.Builder[Block, BlockState]): Unit = {
    super.createBlockStateDefinition(pBuilder)
    pBuilder.add(FACING)
  }
}

object BookCapacitorBlock {
  final val FACING: DirectionProperty = HorizontalDirectionalBlock.FACING
}