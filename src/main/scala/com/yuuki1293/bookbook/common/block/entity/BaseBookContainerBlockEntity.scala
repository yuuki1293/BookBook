package com.yuuki1293.bookbook.common.block.entity

import net.minecraft.core.{BlockPos, Direction, NonNullList}
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world._
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.extensions.IForgeBlockEntity
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.{CapabilityItemHandler, IItemHandlerModifiable}
import net.minecraftforge.items.wrapper.SidedInvWrapper

import scala.jdk.CollectionConverters._

trait BaseBookContainerBlockEntity
  extends IForgeBlockEntity with Container with MenuProvider with Nameable with WorldlyContainer {
  protected var level: Level
  protected var worldPosition: BlockPos
  protected var remove: Boolean

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
    if (level.getBlockEntity(worldPosition) != this)
      return false
    pPlayer.distanceToSqr(worldPosition.getX.toDouble + 0.5D, worldPosition.getY.toDouble + 0.5D, worldPosition.getZ.toDouble + 0.5D) <= 64.0D
  }

  override def clearContent(): Unit = items.clear()

  protected var handlers: Array[LazyOptional[IItemHandlerModifiable]] = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH)

  override def getCapability[T](cap: Capability[T], side: Direction): LazyOptional[T] = {
    if (!remove && side != null && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      if (side == Direction.UP)
        handlers(0).cast()
      else if (side == Direction.DOWN)
        handlers(1).cast()
      else
        handlers(2).cast()
    }
    else
      super.getCapability(cap)
  }

  def invalidateCaps(): Unit = {
    for (handler <- handlers)
      handler.invalidate()
  }

  def reviveCaps(): Unit = {
    handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH)
  }

  def load(pTag: CompoundTag): Unit ={
    items = NonNullList.withSize(getContainerSize, ItemStack.EMPTY)
    ContainerHelper.loadAllItems(pTag, items)
  }

  protected def saveAdditional(pTag: CompoundTag): Unit = {
    ContainerHelper.saveAllItems(pTag, items)
  }
}
