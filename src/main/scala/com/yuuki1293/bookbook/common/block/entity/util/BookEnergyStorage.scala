package com.yuuki1293.bookbook.common.block.entity.util

import net.minecraft.core.Direction
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraftforge.energy.{CapabilityEnergy, EnergyStorage}
import scala.jdk.OptionConverters._

class BookEnergyStorage(pBlockEntity: BlockEntity,
                        pCapacity: Int,
                        pMaxReceive: Int,
                        pMaxExtract: Int,
                        pEnergy: Int, pEject: Array[Direction] = Direction.values())
  extends EnergyStorage(pCapacity, pMaxReceive, pMaxExtract, pEnergy) {
  val blockEntity: BlockEntity = pBlockEntity
  var eject: Array[Direction] = pEject

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

  def outputEnergy(): Unit = {
    if(getEnergyStored <= 0 || !canExtract)
      return

    val level = blockEntity.getLevel
    val worldPos = blockEntity.getBlockPos

    for (direction <- eject;
         be <- Option(level.getBlockEntity(worldPos.relative(direction)));
         storage <- be.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite).resolve().toScala) {
      val toSend = extractEnergy(maxExtract, simulate = false)
      val received = storage.receiveEnergy(toSend, false)

      setEnergy(
        getEnergyStored + toSend - received
      )
    }
  }
}

object BookEnergyStorage {
  def apply(blockEntity: BlockEntity,
            capacity: Int,
            maxReceive: Int,
            maxExtract: Int,
            energy: Int = 0,
            eject: Array[Direction] = Direction.values()): BookEnergyStorage =
    new BookEnergyStorage(blockEntity, capacity, maxReceive, maxExtract, energy, eject)

  def apply(blockEntity: BlockEntity,
            capacity: Int,
            eject: Array[Direction] = Direction.values()): BookEnergyStorage =
    new BookEnergyStorage(blockEntity, capacity, capacity, capacity, 0, eject)

  def apply(blockEntity: BlockEntity,
            capacity: Int,
            maxTransfer: Int,
            eject: Array[Direction] = Direction.values()): BookEnergyStorage =
    new BookEnergyStorage(blockEntity, capacity, maxTransfer, maxTransfer, 0, eject)
}