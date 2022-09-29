package com.yuuki1293.bookbook.common.block.entity

import com.yuuki1293.bookbook.common.block.entity.util.{BookEnergyStorage, InventoryBlockEntity}
import com.yuuki1293.bookbook.common.register.BlockEntities
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.energy.CapabilityEnergy

class BookGeneratorBlockEntity(worldPosition: BlockPos, blockState: BlockState)
  extends InventoryBlockEntity(BlockEntities.BOOK_GENERATOR.get(), worldPosition, blockState, 1) with BlockEntityTicker[BookGeneratorBlockEntity] {

  val energyStorage: BookEnergyStorage = createEnergyStorage()

  private val capacity = 2000
  private val maxExtract = 100
  private var progress = 0
  private var maxProgress = 0
  private val energy: LazyOptional[BookEnergyStorage] = LazyOptional.of(() => this.energyStorage)

  override def getCapability[T](cap: Capability[T], side: Direction): LazyOptional[T] = {
    if (cap == CapabilityEnergy.ENERGY)
      this.energy.cast()
    else
      super.getCapability(cap, side)
  }

  def getEnergy: Int = this.energyStorage.getEnergyStored

  def getEnergyForStack(itemStack: ItemStack): Int = ForgeHooks.getBurnTime(itemStack, RecipeType.SMELTING)

  def getMaxProgress: Int = this.maxProgress

  def getProgress: Int = this.progress

  override def invalidateCaps(): Unit = {
    super.invalidateCaps()
    this.energy.invalidate()
  }

  override def load(pTag: CompoundTag): Unit = {
    super.load(pTag)
    this.progress = pTag.getInt("Progress")
    this.energyStorage.setEnergy(pTag.getInt("Energy"))
  }

  def outputEnergy(): Unit = {
    if (this.energyStorage.getEnergyStored >= this.maxExtract && this.energyStorage.canExtract) {
      for (direction <- Direction.values()) {
        val be = this.level.getBlockEntity(this.worldPosition.relative(direction))
        if (be != null) {
          be.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite).ifPresent(storage => {
            if (be != this && storage.getEnergyStored < storage.getMaxEnergyStored) {
              val toSend = BookGeneratorBlockEntity.this.energyStorage.extractEnergy(this.maxExtract, simulate = false)
              val received = storage.receiveEnergy(toSend, false)

              BookGeneratorBlockEntity.this.energyStorage.setEnergy(
                BookGeneratorBlockEntity.this.energyStorage.getEnergyStored + toSend - received
              )
            }
          })
        }
      }
    }
  }

  override def tick(): Unit = {
    if (this.energyStorage.getEnergyStored <= this.energyStorage.getMaxEnergyStored - 100) {
      if (!getItemInSlot(0).isEmpty && (this.progress <= 0 || this.progress > this.maxProgress)) {
        this.maxProgress = getEnergyForStack(getItemInSlot(0))
        this.inventory.extractItem(0, 1, false)
        this.progress += 1
      } else if (this.progress > 0) {
        this.progress += 1
        if (this.progress >= this.maxProgress) {
          this.progress = 0
          this.energyStorage.setEnergy(this.energyStorage.getEnergyStored + this.maxProgress)
        }
      } else {
        this.progress = 0
        this.maxProgress = 0
      }
    }

    outputEnergy()

    super.tick()
  }

  override def tick(pLevel: Level, pPos: BlockPos, pState: BlockState, pBlockEntity: BookGeneratorBlockEntity): Unit = pBlockEntity.tick()

  override protected def saveAdditional(pTag: CompoundTag): Unit = {
    super.saveAdditional(pTag)
    pTag.putInt("Progress", this.progress)
    pTag.putInt("Energy", getEnergy)
  }

  private def createEnergyStorage() = {
    new BookEnergyStorage(this, this.capacity, 0, this.maxExtract, 0)
  }
}

object BookGeneratorBlockEntity {
  val TITLE = new TranslatableComponent("container.bookbook.book_generator")
}