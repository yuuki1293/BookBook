package com.yuuki1293.bookbook.common.block

import com.yuuki1293.bookbook.common.block.entity.BlockEntityBookFurnace
import com.yuuki1293.bookbook.common.register.BlockEntities
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.sounds.{SoundEvents, SoundSource}
import net.minecraft.stats.Stats
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.AbstractFurnaceBlock
import net.minecraft.world.level.block.AbstractFurnaceBlock.{FACING, LIT, createFurnaceTicker}
import net.minecraft.world.level.block.entity.{BlockEntity, BlockEntityTicker, BlockEntityType}
import net.minecraft.world.level.block.state.{BlockBehaviour, BlockState}

import java.util.Random

class BlockBookFurnace(properties: BlockBehaviour.Properties) extends AbstractFurnaceBlock(properties) {
  override def newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity = {
    new BlockEntityBookFurnace(pPos, pState)
  }

  override def getTicker[T <: BlockEntity](pLevel: Level, pState: BlockState, pBlockEntityType: BlockEntityType[T]): BlockEntityTicker[T] = {
    createFurnaceTicker(pLevel, pBlockEntityType, BlockEntities.BOOK_FURNACE.get())
  }

  override protected def openContainer(pLevel: Level, pPos: BlockPos, pPlayer: Player): Unit = {
    val blockEntity = pLevel.getBlockEntity(pPos)
    if (blockEntity.isInstanceOf[BlockEntityBookFurnace]) {
      pPlayer.openMenu(blockEntity.asInstanceOf[MenuProvider])
      pPlayer.awardStat(Stats.INTERACT_WITH_FURNACE)
    }
  }

  override def animateTick(pState: BlockState, pLevel: Level, pPos: BlockPos, pRandom: Random): Unit = {
    if (pState.getValue(LIT)) {
      val d0: Double = pPos.getX + 0.5D
      val d1: Double = pPos.getY
      val d2: Double = pPos.getZ + 0.5D
      if (pRandom.nextDouble() < 0.1D) {
        pLevel.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false)
      }

      val direction = pState.getValue(FACING)
      val direction$axis = direction.getAxis
      val d3: Double = 0.52D
      val d4: Double = pRandom.nextDouble() * 0.6D - 0.3D
      val d5: Double = if (direction$axis == Direction.Axis.X) direction.getStepX.toDouble * 0.5D else d4
      val d6: Double = pRandom.nextDouble() * 6.0D / 16.0D
      val d7: Double = if (direction$axis == Direction.Axis.Z) direction.getStepX.toDouble * 0.5D else d4
      pLevel.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D)
      pLevel.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D)
    }
  }
}