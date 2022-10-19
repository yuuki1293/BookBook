package com.yuuki1293.bookbook.common.block.entity

import net.minecraft.core.{BlockPos, NonNullList}
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world._
import net.minecraftforge.common.extensions.IForgeBlockEntity

import scala.jdk.CollectionConverters._

trait BaseBookContainerBlockEntity
  extends IForgeBlockEntity with Container with MenuProvider with Nameable with WorldlyContainer {
  protected var level: Level
  protected var worldPosition: BlockPos

  var items: NonNullList[ItemStack]

  override def getContainerSize: Int = items.size()

  override def isEmpty: Boolean = {
    for (item <- items.asScala) {
      if (!item.isEmpty)
        return false
    }
    true
  }

  override def getItem(pSlot: Int): ItemStack = items.get(pSlot)

  override def removeItem(pSlot: Int, pAmount: Int): ItemStack = ContainerHelper.removeItem(items, pSlot, pAmount)

  override def removeItemNoUpdate(pSlot: Int): ItemStack = ContainerHelper.takeItem(items, pSlot)

  override def setItem(pSlot: Int, pStack: ItemStack): Unit = {
    items.set(pSlot, pStack)
    if (pStack.getCount > getMaxStackSize) {
      pStack.setCount(getMaxStackSize)
    }
  }

  override def stillValid(pPlayer: Player): Boolean = {
    if(level.getBlockEntity(worldPosition) != this)
      return false
    pPlayer.distanceToSqr(worldPosition.getX.toDouble + 0.5D, worldPosition.getY.toDouble + 0.5D, worldPosition.getZ.toDouble + 0.5D) <= 64.0D
  }
}
