package com.yuuki1293.bookbook.common.inventory

import com.yuuki1293.bookbook.common.block.entity.BookGeneratorBlockEntity.{DATA_ENERGY_STORED, DATA_MAX_ENERGY}
import com.yuuki1293.bookbook.common.inventory.BookGeneratorMenu.FUEL_SLOT
import com.yuuki1293.bookbook.common.register.MenuTypes
import net.minecraft.world.entity.player.{Inventory, Player}
import net.minecraft.world.inventory.AbstractContainerMenu.{checkContainerDataCount, checkContainerSize}
import net.minecraft.world.inventory._
import net.minecraft.world.level.Level
import net.minecraft.world.{Container, SimpleContainer}

class BookGeneratorMenu(pMenuType: MenuType[_], pContainerId: Int, pPlayerInventory: Inventory, pContainer: Container, pData: ContainerData) extends AbstractContainerMenu(pMenuType, pContainerId) {
  checkContainerSize(pContainer, 1)
  checkContainerDataCount(pData, 4)
  private val container: Container = pContainer
  val data: ContainerData = pData
  protected val level: Level = pPlayerInventory.player.level
  this.addSlot(new FuelSlot(pContainer, FUEL_SLOT, 56, 35))

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

  override def stillValid(pPlayer: Player): Boolean = this.container.stillValid(pPlayer)

  def getBurnProgress: Int = {
    var i = this.data.get(1)
    if (i==0)
      i=200

    this.data.get(0) * 13 / i
  }

  def isBurn: Boolean = this.data.get(0) > 0

  /**
   * 0 - Energy is empty<br>
   * 100 - Energy is full
   * @return The percentage of energy as a percentage of 100
   */
  def getEnergyProportion: Int = this.data.get(DATA_ENERGY_STORED) * 100 / this.data.get(DATA_MAX_ENERGY)
}

object BookGeneratorMenu {
  val FUEL_SLOT = 0
}
