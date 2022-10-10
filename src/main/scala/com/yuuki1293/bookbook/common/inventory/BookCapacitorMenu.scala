package com.yuuki1293.bookbook.common.inventory

import com.yuuki1293.bookbook.common.register.MenuTypes
import net.minecraft.world.{Container, SimpleContainer}
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu.{checkContainerDataCount, checkContainerSize}
import net.minecraft.world.inventory.{AbstractContainerMenu, ContainerData, MenuType, SimpleContainerData, Slot}
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
}
