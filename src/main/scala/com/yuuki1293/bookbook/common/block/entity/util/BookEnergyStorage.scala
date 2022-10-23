package com.yuuki1293.bookbook.common.block.entity.util

import cats.effect._
import net.minecraft.core.Direction
import net.minecraft.world.Container
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.energy.{CapabilityEnergy, EnergyStorage, IEnergyStorage}

import scala.jdk.OptionConverters._

class BookEnergyStorage(pCapacity: Int, pMaxReceive: Int, pMaxExtract: Int, pEnergy: Int)(
  implicit pBlockEntity: BlockEntity
)
  extends EnergyStorage(pCapacity, pMaxReceive, pMaxExtract, pEnergy) {
  private val blockEntity: BlockEntity = pBlockEntity

  override def extractEnergy(maxExtract: Int, simulate: Boolean): Int = {
    blockEntity.setChanged()
    super.extractEnergy(maxExtract, simulate)
  }

  override def receiveEnergy(maxReceive: Int, simulate: Boolean): Int = {
    blockEntity.setChanged()
    super.receiveEnergy(maxReceive, simulate)
  }

  def setEnergy(energy: Int): IO[Unit] = {
    IO {
      this.energy = Math.max(0, Math.min(energy, capacity))
    }
  }

  def increase(inc: Int): IO[Unit] = {
    IO {
      energy = Math.min(capacity, energy + inc)
    }
  }

  def outputEnergy(eject: Array[Direction] = Direction.values()): IO[Unit] = {
    IO {
      if (getEnergyStored > 0 && canExtract) {
        val level = blockEntity.getLevel
        val worldPos = blockEntity.getBlockPos

        for {
          direction <- eject
          relative = worldPos.relative(direction)
          be <- Option(level.getBlockEntity(relative))
          cap = be.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite)
          storage <- cap.resolve().toScala
        }
          transfer(storage)
      } else IO.unit
    }
  }

  def chargeItems(container: Container, startIndex: Int, endIndex: Int): Unit = {
    if (getEnergyStored <= 0
      || !canExtract
      || startIndex >= endIndex
      || container.getContainerSize < endIndex)
      return

    for (i <- startIndex until endIndex) {
      val item = Option(container.getItem(i))
      for (item <- item;
           storage <- item.getCapability(CapabilityEnergy.ENERGY).resolve().toScala) {
        transfer(storage)
      }
    }
  }

  protected def transfer(receiver: IEnergyStorage): IO[Unit] = {
    IO {
      val toSend = extractEnergy(maxExtract, simulate = false)
      val received = receiver.receiveEnergy(toSend, false)

      energy = getEnergyStored + toSend - received
    }
  }
}

object BookEnergyStorage {
  def apply(capacity: Int, maxReceive: Int, maxExtract: Int, energy: Int = 0)(
    implicit blockEntity: BlockEntity
  ): BookEnergyStorage =
    new BookEnergyStorage(capacity, maxReceive, maxExtract, energy)

  def apply(capacity: Int)(
    implicit blockEntity: BlockEntity
  ): BookEnergyStorage =
    new BookEnergyStorage(capacity, capacity, capacity, 0)

  def apply(capacity: Int, maxTransfer: Int)(
    implicit blockEntity: BlockEntity
  ): BookEnergyStorage =
    new BookEnergyStorage(capacity, maxTransfer, maxTransfer, 0)
}