package com.yuuki1293.bookbook.common.block

import net.minecraft.core.BlockPos
import net.minecraft.stats.Stats
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.AbstractFurnaceBlock
import net.minecraft.world.level.block.AbstractFurnaceBlock._
import net.minecraft.world.level.block.entity.{BlockEntity, BlockEntityTicker, BlockEntityType, FurnaceBlockEntity}
import net.minecraft.world.level.block.state.{BlockBehaviour, BlockState}

class BlockBookFurnace(properties: BlockBehaviour.Properties) extends AbstractFurnaceBlock(properties) {
  def newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity = {
    new FurnaceBlockEntity(pPos, pState)
  }

  override def getTicker[T <: BlockEntity](pLevel: Level, pState: BlockState, pBlockEntityType: BlockEntityType[T]): BlockEntityTicker[T] = {
    createFurnaceTicker(pLevel, pBlockEntityType, BlockEntityType.FURNACE)
  }

  protected def openContainer(pLevel: Level, pPos: BlockPos, pPlayer: Player): Unit = {
    val blockEntity = pLevel.getBlockEntity(pPos)

    if (blockEntity.isInstanceOf[FurnaceBlockEntity]) {
      pPlayer.openMenu(blockEntity.asInstanceOf[MenuProvider])
      pPlayer.awardStat(Stats.INTERACT_WITH_FURNACE)
    }
  }
}
