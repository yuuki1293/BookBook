package com.yuuki1293.bookbook.common.item

import com.yuuki1293.bookbook.common.entity.BookItemEntity
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.{Component, TranslatableComponent}
import net.minecraft.world.entity.{Entity, EntityType}
import net.minecraft.world.item.Item.Properties
import net.minecraft.world.item.{Item, ItemStack, TooltipFlag}
import net.minecraft.world.level.Level

import java.util

class BookItem(properties: Properties) extends Item(properties) {
  private var waterproof = false

  override def hasCustomEntity(stack: ItemStack) = true

  override def createEntity(level: Level, location: Entity, stack: ItemStack): Entity = {
    val itemBook = new BookItemEntity(EntityType.ITEM, level, location.getX, location.getY, location.getZ, stack)
    itemBook.setDeltaMovement(location.getDeltaMovement)
    itemBook.setPickUpDelay(40)

    itemBook
  }

  override def appendHoverText(pStack: ItemStack, pLevel: Level, pTooltipComponents: util.List[Component], pIsAdvanced: TooltipFlag): Unit = {
    val sWaterProof = if (waterproof) "true" else "false"
    pTooltipComponents.add(new TranslatableComponent("waterproof: %s", sWaterProof).withStyle(ChatFormatting.GRAY))

    super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced)
  }

  def isWaterProof: Boolean = waterproof

  def setWaterProof(p: Boolean): Unit = {
    waterproof = p
  }
}
