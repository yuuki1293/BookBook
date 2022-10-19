package com.yuuki1293.bookbook.common.block.entity.util

import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraftforge.energy.EnergyStorage

class BookEnergyStorage(pBlockEntity: BlockEntity, pCapacity: Int, pMaxReceive: Int, pMaxExtract: Int, pEnergy: Int) extends EnergyStorage(pCapacity, pMaxReceive, pMaxExtract, pEnergy) {
  val blockEntity: BlockEntity = pBlockEntity

  override def extractEnergy(maxExtract: Int, simulate: Boolean): Int = {
    blockEntity.setChanged()
    super.extractEnergy(maxExtract, simulate)
  }

  override def receiveEnergy(maxReceive: Int, simulate: Boolean): Int = {
    blockEntity.setChanged()
    super.receiveEnergy(maxReceive, simulate)
  }

  def setEnergy(energy: Int): Unit = {
    this.energy = Math.max(0, Math.min(energy, capacity))
  }

  def increase(inc: Int): Unit = energy = Math.min(capacity, energy + inc)
}

object BookEnergyStorage {
  def apply(blockEntity: BlockEntity, capacity: Int, maxReceive: Int, maxExtract: Int, energy: Int): BookEnergyStorage =
    new BookEnergyStorage(blockEntity, capacity, maxReceive, maxExtract, energy)

  def apply(blockEntity: BlockEntity, capacity: Int): BookEnergyStorage =
    new BookEnergyStorage(blockEntity, capacity, capacity, capacity, 0)

  def apply(blockEntity: BlockEntity, capacity: Int, maxTransfer: Int): BookEnergyStorage =
    new BookEnergyStorage(blockEntity, capacity, maxTransfer, maxTransfer, 0)

  def apply(blockEntity: BlockEntity, capacity: Int, maxReceive: Int, maxExtract: Int) =
    new BookEnergyStorage(blockEntity, capacity, maxReceive, maxExtract, 0)
}