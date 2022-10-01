package com.yuuki1293.bookbook.common.block

import com.yuuki1293.bookbook.common.block.entity.BookGeneratorBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.{BlockEntity, BlockEntityTicker, BlockEntityType}
import net.minecraft.world.level.block.state.{BlockBehaviour, BlockState}
import net.minecraft.world.level.block.{Block, EntityBlock}

class BookGeneratorBlock(properties: BlockBehaviour.Properties) extends Block(properties) with EntityBlock {
  override def getTicker[T <: BlockEntity](pLevel: Level, pState: BlockState, pBlockEntityType: BlockEntityType[T]): BlockEntityTicker[T] = {
    if(pLevel.isClientSide)
      null
    else
      (_, _, _, blockEntity) => blockEntity.asInstanceOf[BookGeneratorBlockEntity].tick()
  }

  override def newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity = {
    new BookGeneratorBlockEntity(pPos, pState)
  }
}
