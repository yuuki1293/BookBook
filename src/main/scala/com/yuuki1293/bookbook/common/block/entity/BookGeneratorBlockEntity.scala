package com.yuuki1293.bookbook.common.block.entity

import com.yuuki1293.bookbook.common.block.entity.BookGeneratorBlockEntity.TITLE
import com.yuuki1293.bookbook.common.block.entity.util.BookEnergyStorage
import com.yuuki1293.bookbook.common.inventory.BookFurnaceMenu
import com.yuuki1293.bookbook.common.register.BlockEntities
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.{Component, TranslatableComponent}
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.{Inventory, Player}
import net.minecraft.world.inventory.{AbstractContainerMenu, SimpleContainerData}
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.{BaseContainerBlockEntity, BlockEntityTicker}
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.energy.CapabilityEnergy

class BookGeneratorBlockEntity(worldPosition: BlockPos, blockState: BlockState)
  extends BaseContainerBlockEntity(BlockEntities.BOOK_GENERATOR.get(), worldPosition, blockState){

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

  override protected def saveAdditional(pTag: CompoundTag): Unit = {
    super.saveAdditional(pTag)
    pTag.putInt("Progress", this.progress)
    pTag.putInt("Energy", getEnergy)
  }

  private def createEnergyStorage() = {
    new BookEnergyStorage(this, this.capacity, 0, this.maxExtract, 0)
  }

  override def getItem(pSlot: Int): ItemStack = {

  }

  override def getDefaultName: Component = TITLE

  override def createMenu(pContainerId: Int, pPlayerInventory: Inventory, pPlayer: Player): AbstractContainerMenu = {
    new BookFurnaceMenu(pContainerId, pPlayerInventory, new SimpleContainer(1), new SimpleContainerData(2))
  }
}

object BookGeneratorBlockEntity {
  val TITLE = new TranslatableComponent("container.bookbook.book_generator")
}