package com.yuuki1293.bookbook.common.block

import com.yuuki1293.bookbook.common.block.BookCapacitorBlock._
import com.yuuki1293.bookbook.common.block.entity.BookCapacitorBlockEntity
import com.yuuki1293.bookbook.common.register.BlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock.createTickerHelper
import net.minecraft.world.level.block.entity.{BlockEntity, BlockEntityTicker, BlockEntityType}
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.level.block.state.{BlockBehaviour, BlockState, StateDefinition}
import net.minecraft.world.level.block.{Block, HorizontalDirectionalBlock}
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.{InteractionHand, InteractionResult}

class BookCapacitorBlock(pProperties: BlockBehaviour.Properties)
  extends BaseBookContainerBlock[BookCapacitorBlockEntity](pProperties) {
  override def getTicker[A <: BlockEntity](pLevel: Level, pState: BlockState, pBlockEntityType: BlockEntityType[A]): BlockEntityTicker[A] = {
    createTickerHelper(pBlockEntityType, BlockEntities.BOOK_CAPACITOR.get(), BookCapacitorBlockEntity)
  }

  override def newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity = BookCapacitorBlockEntity(pPos, pState)

  override def createBlockStateDefinition(pBuilder: StateDefinition.Builder[Block, BlockState]): Unit = {
    super.createBlockStateDefinition(pBuilder)
    pBuilder.add(FACING)
  }

  override def use(pState: BlockState, pLevel: Level, pPos: BlockPos, pPlayer: Player, pHand: InteractionHand, pHit: BlockHitResult): InteractionResult = {
    if (pLevel.isClientSide)
      InteractionResult.SUCCESS
    else {
      openContainer(pLevel, pPos, pPlayer)
      InteractionResult.CONSUME
    }
  }
}

object BookCapacitorBlock {
  final val FACING: DirectionProperty = HorizontalDirectionalBlock.FACING

  def apply(properties: BlockBehaviour.Properties) = new BookCapacitorBlock(properties)
}