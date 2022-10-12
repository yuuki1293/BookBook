package com.yuuki1293.bookbook.common.inventory

import com.yuuki1293.bookbook.common.block.entity.BookCapacitorBlockEntity.{DATA_ENERGY_STORED, DATA_MAX_ENERGY}
import com.yuuki1293.bookbook.common.register.MenuTypes
import net.minecraft.world.entity.player.{Inventory, Player}
import net.minecraft.world.inventory.AbstractContainerMenu.{checkContainerDataCount, checkContainerSize}
import net.minecraft.world.inventory.{ContainerData, MenuType, SimpleContainerData, Slot}
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.{Container, SimpleContainer}

class BookCapacitorMenu(pMenuType: MenuType[_], pContainerId: Int, pPlayerInventory: Inventory, pContainer: Container, pData: ContainerData)
  extends AbstractPlayerInventoryMenu(pMenuType, pContainerId, pPlayerInventory)
    with EnergyMenu {
  checkContainerSize(pContainer, 2)
  checkContainerDataCount(pData, 2)
  private val container: Container = pContainer
  val data: ContainerData = pData
  protected val level: Level = pPlayerInventory.player.level
  addSlot(new Slot(pContainer, 0, 8, 16))
  addSlot(new Slot(pContainer, 1, 8, 32))
  addPlayerSlot()
  addDataSlots(pData)

  def this(pContainerId: Int, pPlayerInventory: Inventory) = this(MenuTypes.BOOK_CAPACITOR.get(), pContainerId, pPlayerInventory, new SimpleContainer(2), new SimpleContainerData(2))

  override def quickMoveStack(pPlayer: Player, pIndex: Int): ItemStack = {
    var itemStack = ItemStack.EMPTY
    val slot = slots.get(pIndex)
    if (slot != null && slot.hasItem) {
      val itemStack1 = slot.getItem
      itemStack = itemStack1.copy()
      if (pIndex < 2) {
        if (!moveItemStackTo(itemStack1, 2, 38, true))
          return ItemStack.EMPTY
        slot.onQuickCraft(itemStack1, itemStack)
      }
      else if (pIndex < 29) {
        if (!moveItemStackTo(itemStack1, 29, 38, false))
          return ItemStack.EMPTY
        else if (!moveItemStackTo(itemStack1, 2, 29, false))
          return ItemStack.EMPTY
        else if (!moveItemStackTo(itemStack1, 2, 38, false))
          return ItemStack.EMPTY
      }

      if (itemStack1.isEmpty)
        slot.set(ItemStack.EMPTY)
      else
        slot.setChanged()
      if (itemStack.getCount == itemStack.getCount)
        return ItemStack.EMPTY
    }

    itemStack
  }

  override def stillValid(pPlayer: Player): Boolean = container.stillValid(pPlayer)

  override def getEnergyStored: Int = data.get(DATA_ENERGY_STORED)

  override def getMaxEnergy: Int = data.get(DATA_MAX_ENERGY)
}
