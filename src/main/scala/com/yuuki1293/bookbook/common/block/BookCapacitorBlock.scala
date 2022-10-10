package com.yuuki1293.bookbook.common.block

import com.yuuki1293.bookbook.common.block.BookCapacitorBlock._
import com.yuuki1293.bookbook.common.block.entity.BookCapacitorBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.{InteractionHand, InteractionResult, MenuProvider}
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.{BlockEntity, BlockEntityTicker, BlockEntityType}
import net.minecraft.world.level.block.state.{BlockBehaviour, BlockState, StateDefinition}
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.level.block.{Block, EntityBlock, HorizontalDirectionalBlock}
import net.minecraft.world.phys.BlockHitResult

class BookCapacitorBlock(pProperties: BlockBehaviour.Properties) extends Block(pProperties) with EntityBlock {
  override def getTicker[T <: BlockEntity](pLevel: Level, pState: BlockState, pBlockEntityType: BlockEntityType[T]): BlockEntityTicker[T] = {
    if (pLevel.isClientSide)
      null
    else
      (_, _, _, blockEntity) => blockEntity.asInstanceOf[BookCapacitorBlockEntity].tick()
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
}

object BookCapacitorBlock {
  final val FACING: DirectionProperty = HorizontalDirectionalBlock.FACING
}