package com.yuuki1293.bookbook.common.block

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.{Container, Containers, MenuProvider}
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.{BaseEntityBlock, RenderShape}
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.{BlockBehaviour, BlockState}

abstract class BaseBookContainerBlock[A <: BlockEntity with Container](properties: BlockBehaviour.Properties)
  extends BaseEntityBlock(properties) with BaseBookBlock {
  override def onRemove(pState: BlockState,
                        pLevel: Level,
                        pPos: BlockPos,
                        pNewState: BlockState,
                        pIsMoving: Boolean): Unit = {
    if (!pState.is(pNewState.getBlock)) {
      val blockEntity = pLevel.getBlockEntity(pPos)
      blockEntity match {
        case entity: A =>
          if (pLevel.isInstanceOf[ServerLevel]) {
            Containers.dropContents(pLevel, pPos, entity)
          }
        case _ =>
      }

      pLevel.updateNeighbourForOutputSignal(pPos, this)
    }

    super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving)
  }

  protected def openContainer(pLevel: Level, pPos: BlockPos, pPlayer: Player): Unit = {
    val blockEntity = pLevel.getBlockEntity(pPos)
    if (blockEntity.isInstanceOf[A]) {
      pPlayer.openMenu(blockEntity.asInstanceOf[MenuProvider])
    }
  }

  override def getRenderShape(pState: BlockState): RenderShape = {
    RenderShape.MODEL
  }
}
