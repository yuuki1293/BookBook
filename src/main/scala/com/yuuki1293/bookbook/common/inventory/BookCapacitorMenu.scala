package com.yuuki1293.bookbook.common.inventory

import com.yuuki1293.bookbook.common.register.MenuTypes
import net.minecraft.world.{Container, SimpleContainer}
import net.minecraft.world.entity.player.{Inventory, Player}
import net.minecraft.world.inventory.AbstractContainerMenu.{checkContainerDataCount, checkContainerSize}
import net.minecraft.world.inventory.{AbstractContainerMenu, ContainerData, MenuType, SimpleContainerData, Slot}
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class BookCapacitorMenu(pMenuType: MenuType[_], pContainerId: Int, pPlayerInventory: Inventory, pContainer: Container, pData: ContainerData)
  extends AbstractContainerMenu(pMenuType, pContainerId) {
  checkContainerSize(pContainer, 2)
  checkContainerDataCount(pData, 2)
  private val container: Container = pContainer
  val data: ContainerData = pData
  protected val level: Level = pPlayerInventory.player.level
  addSlot(new Slot(pContainer, 0, 8, 16))
  addSlot(new Slot(pContainer, 1, 8, 32))

  for (i <- 0 until 3) {
    for (j <- 0 until 9) {
      addSlot(new Slot(pPlayerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18))
    }
  }

  for (k <- 0 until 9) {
    addSlot(new Slot(pPlayerInventory, k, 8 + k * 18, 142))
  }

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
}
