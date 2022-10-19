package com.yuuki1293.bookbook.common.block

import com.yuuki1293.bookbook.common.block.entity.BookFurnaceBlockEntity
import com.yuuki1293.bookbook.common.register.BlockEntities
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.sounds.{SoundEvents, SoundSource}
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.{Level, LevelReader}
import net.minecraft.world.level.block.{AbstractFurnaceBlock, RenderShape}
import net.minecraft.world.level.block.AbstractFurnaceBlock.{FACING, LIT, createFurnaceTicker}
import net.minecraft.world.level.block.entity.{BlockEntity, BlockEntityTicker, BlockEntityType}
import net.minecraft.world.level.block.state.{BlockBehaviour, BlockState}

import java.util.Random

class BookFurnaceBlock(properties: BlockBehaviour.Properties)
  extends AbstractFurnaceBlock(properties) with BaseBookBlock {
  override def newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity = {
    new BookFurnaceBlockEntity(pPos, pState)
  }

  override def getTicker[A <: BlockEntity](pLevel: Level, pState: BlockState, pBlockEntityType: BlockEntityType[A]): BlockEntityTicker[A] = {
    createFurnaceTicker(pLevel, pBlockEntityType, BlockEntities.BOOK_FURNACE.get())
  }

  override protected def openContainer(pLevel: Level, pPos: BlockPos, pPlayer: Player): Unit = {
    val blockEntity = pLevel.getBlockEntity(pPos)
    if (blockEntity.isInstanceOf[BookFurnaceBlockEntity]) {
      pPlayer.openMenu(blockEntity.asInstanceOf[MenuProvider])
    }
  }

  override def animateTick(pState: BlockState, pLevel: Level, pPos: BlockPos, pRand: Random): Unit = {
    if (pState.getValue(LIT)) {
      val d0 = pPos.getX.toDouble + 0.5D
      val d1 = pPos.getY.toDouble
      val d2 = pPos.getZ.toDouble + 0.5D
      if (pRand.nextDouble < 0.1D)
        pLevel.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false)

      val direction = pState.getValue(FACING)
      val direction$axis = direction.getAxis
      val d4 = pRand.nextDouble * 0.6D - 0.3D
      val d5 = if (direction$axis eq Direction.Axis.X) direction.getStepX.toDouble * 0.52D else d4
      val d6 = pRand.nextDouble * 6.0D / 16.0D
      val d7 = if (direction$axis eq Direction.Axis.Z) direction.getStepZ.toDouble * 0.52D else d4
      pLevel.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D)
      pLevel.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D)
    }
  }

  override def getRenderShape(pState: BlockState): RenderShape = {
    RenderShape.MODEL
  }

  override def getEnchantPowerBonus(state: BlockState, level: LevelReader, pos: BlockPos): Float = super[BaseBookBlock].getEnchantPowerBonus(state, level, pos)
}