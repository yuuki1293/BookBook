package com.yuuki1293.bookbook.common.item

import com.yuuki1293.bookbook.common.entity.BookItemEntity
import com.yuuki1293.bookbook.common.item.BookItem.setWaterProof
import net.minecraft.ChatFormatting
import net.minecraft.core.NonNullList
import net.minecraft.network.chat.{Component, TranslatableComponent}
import net.minecraft.world.entity.{Entity, EntityType}
import net.minecraft.world.item.Item.Properties
import net.minecraft.world.item.{CreativeModeTab, Item, ItemStack, TooltipFlag}
import net.minecraft.world.level.Level

import java.util

class BookItem(properties: Properties) extends Item(properties) {
  override def fillItemCategory(pCategory: CreativeModeTab, pItems: NonNullList[ItemStack]): Unit = {
    if (this.allowdedIn(pCategory)) {
      val itemStack = new ItemStack(this)
      val stack1 = itemStack.copy()
      val stack2 = itemStack.copy()

      setWaterProof(stack1, p = true)
      setWaterProof(stack2, p = false)

      pItems.add(stack1)
      pItems.add(stack2)
    }
  }

  override def hasCustomEntity(stack: ItemStack) = true

  override def createEntity(level: Level, location: Entity, stack: ItemStack): Entity = {
    val itemBook = new BookItemEntity(EntityType.ITEM, level, location.getX, location.getY, location.getZ, stack)
    itemBook.setDeltaMovement(location.getDeltaMovement)
    itemBook.setPickUpDelay(40)

    itemBook
  }

  override def appendHoverText(pStack: ItemStack, pLevel: Level, pTooltipComponents: util.List[Component], pIsAdvanced: TooltipFlag): Unit = {
    val sWaterProof = if (BookItem.isWaterProof(pStack)) "true" else "false"
    pTooltipComponents.add(new TranslatableComponent("waterproof: %s", sWaterProof).withStyle(ChatFormatting.GRAY))

    super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced)
  }
}

object BookItem{
  def isWaterProof(pStack: ItemStack): Boolean = pStack.getOrCreateTag().getBoolean("waterproof")

  def setWaterProof(pStack: ItemStack, p: Boolean): Unit = {
    pStack.getOrCreateTag().putBoolean("waterproof", p)
  }
}