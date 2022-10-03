package com.yuuki1293.bookbook.common.block.entity.util

import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraftforge.energy.EnergyStorage

class BookEnergyStorage(pBlockEntity: BlockEntity, pCapacity: Int, pMaxReceive: Int, pMaxExtract: Int, pEnergy: Int) extends EnergyStorage(pCapacity, pMaxReceive, pMaxExtract, pEnergy) {
  def this(pBlockEntity: BlockEntity, pCapacity: Int) = this(pBlockEntity, pCapacity, pCapacity, pCapacity, 0)

  def this(pBlockEntity: BlockEntity, pCapacity: Int, pMaxTransfer: Int) = this(pBlockEntity, pCapacity, pMaxTransfer, pMaxTransfer, 0)

  def this(pBlockEntity: BlockEntity, pCapacity: Int, pMaxReceive: Int, pMaxExtract: Int) = this(pBlockEntity, pCapacity, pMaxReceive, pMaxExtract, 0)

  val blockEntity: BlockEntity = pBlockEntity
  this.capacity = pCapacity
  this.maxReceive = pMaxReceive
  this.maxExtract = pMaxExtract
  this.energy = Math.max(0, Math.min(pCapacity, pEnergy))

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
