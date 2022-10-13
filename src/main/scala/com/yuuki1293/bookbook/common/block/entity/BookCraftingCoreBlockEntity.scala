package com.yuuki1293.bookbook.common.block.entity

import com.yuuki1293.bookbook.common.block.entity.util.BookEnergyStorage
import com.yuuki1293.bookbook.common.register.{BlockEntities, MenuTypes}
import com.yuuki1293.bookbook.common.util.Ticked
import net.minecraft.core.{BlockPos, Direction, NonNullList}
import net.minecraft.network.chat.{Component, TranslatableComponent}
import net.minecraft.world.{ContainerHelper, WorldlyContainer}
import net.minecraft.world.entity.player.{Inventory, Player}
import net.minecraft.world.inventory.{AbstractContainerMenu, ContainerData}
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.util.LazyOptional

class BookCraftingCoreBlockEntity(worldPosition: BlockPos, blockState: BlockState)
  extends BaseContainerBlockEntity(BlockEntities.BOOK_CRAFTING_CORE.get(), worldPosition, blockState)
    with WorldlyContainer with Ticked {
  private val items = NonNullList.withSize(1, ItemStack.EMPTY)
  private val capacity = 100000000
  private val maxReceive = 10000000

  val energyStorage: BookEnergyStorage = createEnergyStorage
  private val energy: LazyOptional[BookEnergyStorage] = LazyOptional.of(() => energyStorage)
  protected val dataAccess: ContainerData = new ContainerData {
    /**
     * @param pIndex 0 - [[getEnergy]]<br>
     *               1 - [[getMaxEnergy]]<br>
     *               2 - [[getProgress]]<br>
     *               3 - [[getGoal]]
     * @return
     */
    override def get(pIndex: Int): Int = {
      pIndex match {
        case 0 => getEnergy
        case 1 => getMaxEnergy
        case 2 => getProgress
        case 3 => getGoal
        case _ => throw new UnsupportedOperationException("Unable to get index: " + pIndex)
      }
    }

    /**
     * Invalid
     */
    @Deprecated
    override def set(pIndex: Int, pValue: Int): Unit = {
      throw new UnsupportedOperationException("Unable to get index: " + pIndex)
    }

    override def getCount: Int = 2
  }

  private def createEnergyStorage = {
    new BookEnergyStorage(this, capacity, maxReceive, 0)
  }

  def getEnergy: Int = energyStorage.getEnergyStored

  def getMaxEnergy: Int = energyStorage.getMaxEnergyStored

  def getProgress: Int = ???

  def getGoal: Int = ???

  override def getDefaultName: Component = new TranslatableComponent("container.book_crafting_core")

  override def createMenu(pContainerId: Int, pInventory: Inventory): AbstractContainerMenu = {
    new BookCraftingCoreMenu(MenuTypes.BOOK_CRAFTING_CORE.get(), pContainerId, pInventory, this, dataAccess)
  }

  override def getSlotsForFace(pSide: Direction): Array[Int] = Array(0)

  override def canPlaceItemThroughFace(pIndex: Int, pItemStack: ItemStack, pDirection: Direction): Boolean = true

  override def canTakeItemThroughFace(pIndex: Int, pStack: ItemStack, pDirection: Direction): Boolean = true

  override def tick(): Unit = ???

  override def getContainerSize: Int = items.size()

  override def isEmpty: Boolean = ???

  override def getItem(pSlot: Int): ItemStack = items.get(pSlot)

  override def removeItem(pSlot: Int, pAmount: Int): ItemStack = ContainerHelper.removeItem(items, pSlot, pAmount)

  override def removeItemNoUpdate(pSlot: Int): ItemStack = ContainerHelper.takeItem(items, pSlot)

  override def setItem(pSlot: Int, pStack: ItemStack): Unit = {
    items.set(pSlot, pStack)
    if (pStack.getCount > getMaxStackSize) {
      pStack.setCount(getMaxStackSize)
    }
  }

  override def stillValid(pPlayer: Player): Boolean = {
    if (level.getBlockEntity(worldPosition) != this){
      false
    } else {
      pPlayer.distanceToSqr(worldPosition.getX.toDouble + 0.5D, worldPosition.getY.toDouble + 0.5D, worldPosition.getZ.toDouble + 0.5D) <= 64.0D
    }
  }

  override def clearContent(): Unit = ???
}
