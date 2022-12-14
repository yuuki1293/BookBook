package com.yuuki1293.bookbook.common.inventory

import com.yuuki1293.bookbook.common.block.entity.BookCraftingCoreBlockEntity.{DATA_ENERGY_STORED, DATA_MAX_ENERGY}
import com.yuuki1293.bookbook.common.register.MenuTypes
import net.minecraft.world.{Container, SimpleContainer}
import net.minecraft.world.entity.player.{Inventory, Player}
import net.minecraft.world.inventory.AbstractContainerMenu.{checkContainerDataCount, checkContainerSize}
import net.minecraft.world.inventory.{ContainerData, MenuType, SimpleContainerData, Slot}
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class BookCraftingCoreMenu(pMenuType: MenuType[_], pContainerId: Int, pPlayerInventory: Inventory, pContainer: Container, pData: ContainerData)
  extends AbstractPlayerInventoryMenu(pMenuType, pContainerId, pPlayerInventory)
    with EnergyMenu {
  checkContainerSize(pContainer, 2)
  checkContainerDataCount(pData, 4)
  private val container: Container = pContainer
  val data: ContainerData = pData
  protected val level: Level = pPlayerInventory.player.level
  addSlot(new Slot(pContainer, 0, 80, 26))
  addSlot(new Slot(pContainer, 1, 80, 44) {
    override def mayPlace(pStack: ItemStack): Boolean = false
  })
  addPlayerSlot()
  addDataSlots(pData)

  override def getEnergyStored: Int = data.get(DATA_ENERGY_STORED)

  override def getMaxEnergy: Int = data.get(DATA_MAX_ENERGY)

  override def stillValid(pPlayer: Player): Boolean = container.stillValid(pPlayer)

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
      else if (pIndex < 38) {
        if (!moveItemStackTo(itemStack1, 0, 1, false))
          return ItemStack.EMPTY
        else if (!moveItemStackTo(itemStack1, 29, 38, false))
          return ItemStack.EMPTY
        else if (!moveItemStackTo(itemStack1, 2, 29, false))
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
}

object BookCraftingCoreMenu {
  def apply(menuType: MenuType[_], containerId: Int, playerInventory: Inventory, container: Container, data: ContainerData) =
    new BookCraftingCoreMenu(menuType, containerId, playerInventory, container, data)

  def apply(containerId: Int, playerInventory: Inventory) =
    new BookCraftingCoreMenu(MenuTypes.BOOK_CRAFTING_CORE.get(), containerId, playerInventory, new SimpleContainer(2), new SimpleContainerData(4))
}