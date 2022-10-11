package com.yuuki1293.bookbook.common.inventory

import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.{AbstractContainerMenu, MenuType, Slot}

abstract class AbstractPlayerInventoryMenu(pMenuType: MenuType[_], pContainerId: Int, pPlayerInventory: Inventory)
  extends AbstractContainerMenu(pMenuType, pContainerId) {
  for (i <- 0 until 3) {
    for (j <- 0 until 9) {
      addSlot(new Slot(pPlayerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18))
    }
  }

  for (k <- 0 until 9) {
    addSlot(new Slot(pPlayerInventory, k, 8 + k * 18, 142))
  }
}
