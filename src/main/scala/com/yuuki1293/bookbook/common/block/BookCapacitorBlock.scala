package com.yuuki1293.bookbook.common.block

import com.yuuki1293.bookbook.common.block.BookCapacitorBlock._
import com.yuuki1293.bookbook.common.block.entity.BookCapacitorBlockEntity
import com.yuuki1293.bookbook.common.register.BlockEntities
import com.yuuki1293.bookbook.common.util.Ticked
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock.createTickerHelper
import net.minecraft.world.level.block.entity.{BlockEntity, BlockEntityTicker, BlockEntityType}
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.level.block.state.{BlockBehaviour, BlockState, StateDefinition}
import net.minecraft.world.level.block.{BaseEntityBlock, Block, HorizontalDirectionalBlock, RenderShape}
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.{Containers, InteractionHand, InteractionResult, MenuProvider}

class BookCapacitorBlock(pProperties: BlockBehaviour.Properties) extends BaseEntityBlock(pProperties) {
  override def getTicker[A <: BlockEntity](pLevel: Level, pState: BlockState, pBlockEntityType: BlockEntityType[A]): BlockEntityTicker[A] = {
    createTickerHelper(pBlockEntityType, BlockEntities.BOOK_CAPACITOR.get(), BookCapacitorBlockEntity.tick)
  }

  override def newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity = {
    new BookCapacitorBlockEntity(pPos, pState)
  }

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

  protected def openContainer(level: Level, pos: BlockPos, player: Player): Unit = {
    val blockEntity = level.getBlockEntity(pos)
    if (blockEntity.isInstanceOf[BookCapacitorBlockEntity]) {
      player.openMenu(blockEntity.asInstanceOf[MenuProvider])
    }
  }

  override def onRemove(pState: BlockState, pLevel: Level, pPos: BlockPos, pNewState: BlockState, pIsMoving: Boolean): Unit = {
    if (!pState.is(pNewState.getBlock)) {
      val blockEntity = pLevel.getBlockEntity(pPos)
      blockEntity match {
        case entity: BookCapacitorBlockEntity =>
          if (pLevel.isInstanceOf[ServerLevel]) {
            Containers.dropContents(pLevel, pPos, entity)
          }
      }

      pLevel.updateNeighbourForOutputSignal(pPos, this)
    }

    super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving)
  }

  override def getRenderShape(pState: BlockState): RenderShape = {
    RenderShape.MODEL
  }
}

object BookCapacitorBlock {
  final val FACING: DirectionProperty = HorizontalDirectionalBlock.FACING
}