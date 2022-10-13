package com.yuuki1293.bookbook.common.inventory

import com.yuuki1293.bookbook.common.register.MenuTypes
import net.minecraft.world.{Container, SimpleContainer}
import net.minecraft.world.entity.player.{Inventory, Player}
import net.minecraft.world.inventory.AbstractContainerMenu.{checkContainerDataCount, checkContainerSize}
import net.minecraft.world.inventory.{ContainerData, MenuType, SimpleContainerData, Slot}
import net.minecraft.world.level.Level

class BookCraftingCoreMenu(pMenuType: MenuType[_], pContainerId: Int, pPlayerInventory: Inventory, pContainer: Container, pData: ContainerData)
  extends AbstractPlayerInventoryMenu(pMenuType, pContainerId, pPlayerInventory)
    with EnergyMenu {
  checkContainerSize(pContainer, 1)
  checkContainerDataCount(pData, 4)
  private val container: Container = pContainer
  val data: ContainerData = pData
  protected val level: Level = pPlayerInventory.player.level
  addSlot(new Slot(pContainer, 0, 80, 35))
  addPlayerSlot()
  addDataSlots(pData)

  def this(pContainerId: Int, pPlayerInventory: Inventory) = this(MenuTypes.BOOK_CRAFTING_CORE.get(), pContainerId, pPlayerInventory, new SimpleContainer(1), new SimpleContainerData(4))

  override def getEnergyStored: Int = ???

  override def getMaxEnergy: Int = ???

  override def stillValid(pPlayer: Player): Boolean = ???
}