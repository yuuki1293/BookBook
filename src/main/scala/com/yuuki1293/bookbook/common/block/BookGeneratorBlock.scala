package com.yuuki1293.bookbook.common.block

import com.yuuki1293.bookbook.common.block.BookGeneratorBlock._
import com.yuuki1293.bookbook.common.block.entity.BookGeneratorBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.{BlockEntity, BlockEntityTicker, BlockEntityType}
import net.minecraft.world.level.block.state.properties.{BlockStateProperties, BooleanProperty, DirectionProperty}
import net.minecraft.world.level.block.state.{BlockBehaviour, BlockState, StateDefinition}
import net.minecraft.world.level.block.{Block, EntityBlock, HorizontalDirectionalBlock}

class BookGeneratorBlock(properties: BlockBehaviour.Properties) extends Block(properties) with EntityBlock {
  this.registerDefaultState(
    this.stateDefinition.any()
      .setValue(LIT, 0)
  )

  override def getTicker[T <: BlockEntity](pLevel: Level, pState: BlockState, pBlockEntityType: BlockEntityType[T]): BlockEntityTicker[T] = {
    if(pLevel.isClientSide)
      null
    else
      (_, _, _, blockEntity) => blockEntity.asInstanceOf[BookGeneratorBlockEntity].tick()
  }

  override def newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity = {
    new BookGeneratorBlockEntity(pPos, pState)
  }

  override def createBlockStateDefinition(pBuilder: StateDefinition.Builder[Block, BlockState]): Unit = {
    super.createBlockStateDefinition(pBuilder)
    pBuilder.add(FACING, LIT)
  }
}

object BookGeneratorBlock {
  val FACING: DirectionProperty = HorizontalDirectionalBlock.FACING
  val LIT: BooleanProperty = BlockStateProperties.LIT
}