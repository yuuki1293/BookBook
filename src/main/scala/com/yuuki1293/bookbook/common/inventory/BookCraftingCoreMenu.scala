package com.yuuki1293.bookbook.common.inventory

import net.minecraft.world.Container
import net.minecraft.world.entity.player.{Inventory, Player}
import net.minecraft.world.inventory.AbstractContainerMenu.{checkContainerDataCount, checkContainerSize}
import net.minecraft.world.inventory.{ContainerData, MenuType}

class BookCraftingCoreMenu(pMenuType: MenuType[_], pContainerId: Int, pPlayerInventory: Inventory, pContainer: Container, pData: ContainerData)
  extends AbstractPlayerInventoryMenu(pMenuType, pContainerId, pPlayerInventory)
    with EnergyMenu {
  checkContainerSize(pContainer, 1)
  checkContainerDataCount(pData, 4)

  override def getEnergyStored: Int = ???

  override def getMaxEnergy: Int = ???

  override def stillValid(pPlayer: Player): Boolean = ???
}