package com.yuuki1293.bookbook.common.block.entity

import com.yuuki1293.bookbook.common.block.entity.util.BookEnergyStorage
import com.yuuki1293.bookbook.common.register.BlockEntities
import com.yuuki1293.bookbook.common.util.Ticked
import net.minecraft.core.{BlockPos, Direction, NonNullList}
import net.minecraft.network.chat.Component
import net.minecraft.world.WorldlyContainer
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

  override def getDefaultName: Component = ???

  override def createMenu(pContainerId: Int, pInventory: Inventory): AbstractContainerMenu = ???

  override def getSlotsForFace(pSide: Direction): Array[Int] = ???

  override def canPlaceItemThroughFace(pIndex: Int, pItemStack: ItemStack, pDirection: Direction): Boolean = ???

  override def canTakeItemThroughFace(pIndex: Int, pStack: ItemStack, pDirection: Direction): Boolean = ???

  override def tick(): Unit = ???

  override def getContainerSize: Int = ???

  override def isEmpty: Boolean = ???

  override def getItem(pSlot: Int): ItemStack = ???

  override def removeItem(pSlot: Int, pAmount: Int): ItemStack = ???

  override def removeItemNoUpdate(pSlot: Int): ItemStack = ???

  override def setItem(pSlot: Int, pStack: ItemStack): Unit = ???

  override def stillValid(pPlayer: Player): Boolean = ???

  override def clearContent(): Unit = ???
}
