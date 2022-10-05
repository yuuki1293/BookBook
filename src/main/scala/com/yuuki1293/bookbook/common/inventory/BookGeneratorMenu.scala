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

class BookGeneratorMenu(pMenuType: MenuType[_], pContainerId: Int, pPlayerInventory: Inventory, pContainer: Container, pData: ContainerData) extends AbstractContainerMenu(pMenuType, pContainerId) {
  checkContainerSize(pContainer, 1)
  checkContainerDataCount(pData, 4)
  private val container: Container = pContainer
  val data: ContainerData = pData
  protected val level: Level = pPlayerInventory.player.level
  this.addSlot(new FuelSlot(pContainer, SLOT_FUEL, 80, 35))

  for (i <- 0 until 3) {
    for (j <- 0 until 9) {
      this.addSlot(new Slot(pPlayerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18))
    }
  }

  for (k <- 0 until 9) {
    this.addSlot(new Slot(pPlayerInventory, k, 8 + k * 18, 142))
  }

  this.addDataSlots(pData)

  def this(pContainerId: Int, pPlayerInventory: Inventory) = this(MenuTypes.BOOK_GENERATOR.get(), pContainerId, pPlayerInventory, new SimpleContainer(1), new SimpleContainerData(4))

  override def quickMoveStack(pPlayer: Player, pIndex: Int): ItemStack = {
    var itemStack = ItemStack.EMPTY
    val slot = this.slots.get(pIndex)
    if (slot != null && slot.hasItem) {
      val itemStack1 = slot.getItem
      itemStack = itemStack1.copy
      if (pIndex == 0) {
        if (!this.moveItemStackTo(itemStack1, 1, 37, true))
          return ItemStack.EMPTY
        slot.onQuickCraft(itemStack1, itemStack)
      }
      else if (this.moveItemStackTo(itemStack1, 0, 1, false)) {
        return ItemStack.EMPTY
      }
      else if (pIndex >= 1 && pIndex < 28) {
        if (!this.moveItemStackTo(itemStack1, 28, 37, false))
          return ItemStack.EMPTY
        else if (!this.moveItemStackTo(itemStack1, 1, 28, false))
          return ItemStack.EMPTY
        else if (!this.moveItemStackTo(itemStack1, 1, 37, false))
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

  override def stillValid(pPlayer: Player): Boolean = this.container.stillValid(pPlayer)

  def isFuel(stack: ItemStack): Boolean = ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0

  /**
   * 0% - 13<br>
   * 100% - 0<br>
   * n% - (100 - n) * 13 (rounded down)
   * @return BurnTime * 13 / BurnDuration
   */
  def getBurnProgress: Int = {
    var i = this.data.get(DATA_BURN_DURATION)
    if (i == 0)
      i = 200

    this.data.get(DATA_BURN_TIME) * 13 / i
  }

  def isBurn: Boolean = this.data.get(0) > 0

  /**
   * 0 - Energy is empty<br>
   * 100 - Energy is full
   *
   * @return The percentage of energy as a percentage of 100
   */
  def getEnergyProportion: Int = {
    val energyStored = this.data.get(DATA_ENERGY_STORED)
    val maxEnergy = this.data.get(DATA_MAX_ENERGY)

    if (maxEnergy == 0)
      return 0
    energyStored * 100 / maxEnergy
  }
}