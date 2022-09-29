package com.yuuki1293.bookbook.common.block.entity.util

import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraftforge.energy.EnergyStorage

class BookEnergyStorage(blockEntity: BlockEntity, capacity: Int, maxReceive: Int, maxExtract: Int, var energy: Int) extends EnergyStorage(capacity, maxReceive, maxExtract, energy) {
  def this(blockEntity: BlockEntity, capacity: Int) = this(blockEntity, capacity, capacity, capacity, 0)

  def this(blockEntity: BlockEntity, capacity: Int, maxTransfer: Int) = this(blockEntity, capacity, maxTransfer, maxTransfer, 0)

  def this(blockEntity: BlockEntity, capacity: Int, maxReceive: Int, maxExtract: Int) = this(blockEntity, capacity, maxReceive, maxExtract, 0)

  override def extractEnergy(maxExtract: Int, simulate: Boolean): Int = {
    this.blockEntity.setChanged()
    super.extractEnergy(maxExtract, simulate)
  }

  override def receiveEnergy(maxReceive: Int, simulate: Boolean): Int = {
    this.blockEntity.setChanged()
    super.receiveEnergy(maxReceive, simulate)
  }

  def setEnergy(energy: Int): Unit = {
    this.energy = Math.max(0, Math.min(energy, this.capacity))
  }
}
