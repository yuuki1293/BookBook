package com.yuuki1293.bookbook.common.block.entity.util

import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.Connection
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.{ClientGamePacketListener, ClientboundBlockEntityDataPacket}
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.{BlockEntity, BlockEntityType}
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.{CapabilityItemHandler, ItemStackHandler}

class InventoryBlockEntity(pType: BlockEntityType[_], pPos: BlockPos, pBlockState: BlockState, size: Int) extends BlockEntity(pType, pPos, pBlockState) {
  protected var timer: Int = 0
  protected var requiresUpdate: Boolean = false

  val inventory: ItemStackHandler = createInventory()
  protected var handler: LazyOptional[ItemStackHandler] = LazyOptional.of(() => this.inventory)

  def extractItem(slot: Int): ItemStack = {
    val count = getItemInSlot(slot).getCount
    this.requiresUpdate = true
    this.handler.map[ItemStack](_.extractItem(slot, count, false)).orElse(ItemStack.EMPTY)
  }

  override def getCapability[T](cap: Capability[T], side: Direction): LazyOptional[T] = {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
      this.handler.cast()
    else
      super.getCapability(cap, side)
  }

  def getHandler: LazyOptional[ItemStackHandler] = this.handler

  def getItemInSlot(slot: Int): ItemStack = {
    this.handler.map[ItemStack](_.getStackInSlot(slot)).orElse(ItemStack.EMPTY)
  }

  override def getUpdatePacket: Packet[ClientGamePacketListener] = {
    ClientboundBlockEntityDataPacket.create(this)
  }

  override def getUpdateTag: CompoundTag = serializeNBT()

  override def handleUpdateTag(tag: CompoundTag): Unit = {
    super.handleUpdateTag(tag)
    load(tag)
  }

  def insertItem(slot: Int, itemStack: ItemStack): ItemStack = {
    val copy = itemStack.copy()
    itemStack.shrink(copy.getCount)
    this.requiresUpdate = true
    this.handler.map[ItemStack](_.insertItem(slot, copy, false)).orElse(ItemStack.EMPTY)
  }

  override def invalidateCaps(): Unit = {
    super.invalidateCaps()
    this.handler.invalidate()
  }

  override def load(pTag: CompoundTag): Unit = {
    super.load(pTag)
    this.inventory.deserializeNBT(pTag.getCompound("Inventory"))
  }

  override def onDataPacket(net: Connection, pkt: ClientboundBlockEntityDataPacket): Unit = {
    super.onDataPacket(net, pkt)
    handleUpdateTag(pkt.getTag)
  }

  def tick(): Unit = {
    this.timer += 1
    if (this.requiresUpdate && this.level != null) {
      update()
      this.requiresUpdate = false
    }
  }

  def update(): Unit = {
    requestModelDataUpdate()
    setChanged()
    if (this.level != null) {
      this.level.setBlockAndUpdate(this.worldPosition, getBlockState)
    }
  }

  override def saveAdditional(pTag: CompoundTag): Unit = {
    super.saveAdditional(pTag)
    pTag.put("Inventory", this.inventory.serializeNBT())
  }

  private def createInventory(): ItemStackHandler = {
    new ItemStackHandler(this.size) {
      override def extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack = {
        InventoryBlockEntity.this.update()
        super.extractItem(slot, amount, simulate)
      }

      override def insertItem(slot: Int, itemStack: ItemStack, simulate: Boolean): ItemStack = {
        InventoryBlockEntity.this.update()
        super.insertItem(slot, itemStack, simulate)
      }
    }
  }
}
