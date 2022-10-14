package com.yuuki1293.bookbook.common.block

import com.yuuki1293.bookbook.common.block.entity.BookCraftingCoreBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.{Containers, InteractionHand, InteractionResult, MenuProvider}
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.{BlockEntity, BlockEntityTicker, BlockEntityType}
import net.minecraft.world.level.block.state.{BlockBehaviour, BlockState}
import net.minecraft.world.level.block.{Block, EntityBlock}
import net.minecraft.world.phys.BlockHitResult

class BookCraftingCoreBlock(pProperties: BlockBehaviour.Properties) extends Block(pProperties) with EntityBlock {
  override def newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity =
    new BookCraftingCoreBlockEntity(pPos, pState)

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
    if (blockEntity.isInstanceOf[BookCraftingCoreBlockEntity]) {
      player.openMenu(blockEntity.asInstanceOf[MenuProvider])
    }
  }

  override def onRemove(pState: BlockState, pLevel: Level, pPos: BlockPos, pNewState: BlockState, pIsMoving: Boolean): Unit = {
    if (!pState.is(pNewState.getBlock)) {
      val blockEntity = pLevel.getBlockEntity(pPos)
      blockEntity match {
        case entity: BookCraftingCoreBlockEntity =>
          if (pLevel.isInstanceOf[ServerLevel]) {
            Containers.dropContents(pLevel, pPos, entity)
          }
      }

      pLevel.updateNeighbourForOutputSignal(pPos, this)
    }
    super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving)
  }

  override def getTicker[T <: BlockEntity](pLevel: Level, pState: BlockState, pBlockEntityType: BlockEntityType[T]): BlockEntityTicker[T] = {
    if (pLevel.isClientSide)
      null
    else
      (level, pos, state, blockEntity) => BookCraftingCoreBlockEntity.tick(level, pos, state, blockEntity.asInstanceOf[BookCraftingCoreBlockEntity])
  }
}
