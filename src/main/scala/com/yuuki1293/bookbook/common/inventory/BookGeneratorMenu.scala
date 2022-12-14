package com.yuuki1293.bookbook.common.inventory

import com.yuuki1293.bookbook.common.block.entity.BookGeneratorBlockEntity._
import com.yuuki1293.bookbook.common.register.MenuTypes
import net.minecraft.world.entity.player.{Inventory, Player}
import net.minecraft.world.inventory.AbstractContainerMenu.{checkContainerDataCount, checkContainerSize}
import net.minecraft.world.inventory._
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraft.world.{Container, SimpleContainer}
import net.minecraftforge.common.ForgeHooks

class BookGeneratorMenu(pMenuType: MenuType[_], pContainerId: Int, pPlayerInventory: Inventory, pContainer: Container, pData: ContainerData)
  extends AbstractPlayerInventoryMenu(pMenuType, pContainerId, pPlayerInventory)
    with EnergyMenu {
  checkContainerSize(pContainer, 1)
  checkContainerDataCount(pData, 4)
  private val container: Container = pContainer
  val data: ContainerData = pData
  protected val level: Level = pPlayerInventory.player.level
  addSlot(new FuelSlot(pContainer, SLOT_FUEL, 80, 35))
  addPlayerSlot()
  addDataSlots(pData)

  override def quickMoveStack(pPlayer: Player, pIndex: Int): ItemStack = {
    var itemStack = ItemStack.EMPTY
    val slot = slots.get(pIndex)
    if (slot != null && slot.hasItem) {
      val itemStack1 = slot.getItem
      itemStack = itemStack1.copy
      if (pIndex == 0) {
        if (!moveItemStackTo(itemStack1, 1, 37, true))
          return ItemStack.EMPTY
        slot.onQuickCraft(itemStack1, itemStack)
      }
      else if (moveItemStackTo(itemStack1, 0, 1, false)) {
        return ItemStack.EMPTY
      }
      else if (pIndex >= 1 && pIndex < 28) {
        if (!moveItemStackTo(itemStack1, 28, 37, false))
          return ItemStack.EMPTY
        else if (!moveItemStackTo(itemStack1, 1, 28, false))
          return ItemStack.EMPTY
        else if (!moveItemStackTo(itemStack1, 1, 37, false))
          return ItemStack.EMPTY
      }

      if (itemStack1.isEmpty)
        slot.set(ItemStack.EMPTY)
      else
        slot.setChanged()
      if (itemStack1.getCount == itemStack.getCount)
        return ItemStack.EMPTY
      slot.onTake(pPlayer, itemStack1)
    }

    itemStack
  }

  override def stillValid(pPlayer: Player): Boolean = container.stillValid(pPlayer)

  def isFuel(stack: ItemStack): Boolean = ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0

  /**
   * 0% - 13<br>
   * 100% - 0<br>
   * n% - (100 - n) * 13 (rounded down)
   *
   * @return BurnTime * 13 / BurnDuration
   */
  def getBurnProgress: Int = {
    var i = data.get(DATA_BURN_DURATION)
    if (i == 0)
      i = 200

    data.get(DATA_BURN_TIME) * 13 / i
  }

  def isBurn: Boolean = data.get(DATA_BURN_TIME) > 0

  override def getEnergyStored: Int = data.get(DATA_ENERGY_STORED)

  override def getMaxEnergy: Int = data.get(DATA_MAX_ENERGY)
}

object BookGeneratorMenu {
  def apply(menuType: MenuType[_], containerId: Int, playerInventory: Inventory, container: Container, data: ContainerData) =
    new BookGeneratorMenu(menuType, containerId, playerInventory, container, data)

  def apply(containerId: Int, playerInventory: Inventory) =
    new BookGeneratorMenu(MenuTypes.BOOK_GENERATOR.get(), containerId, playerInventory, new SimpleContainer(1), new SimpleContainerData(4))
}