package com.yuuki1293.bookbook.common.item

import com.yuuki1293.bookbook.common.item.UnstableBookItem._
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.{Component, Style, TranslatableComponent}
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.{Item, ItemStack, TooltipFlag}
import net.minecraft.world.level.{Explosion, Level}

import scala.jdk.CollectionConverters._
import java.util

class UnstableBookItem(pProperties: Item.Properties) extends Item(pProperties) {
  override def inventoryTick(pStack: ItemStack, pLevel: Level, pEntity: Entity, pSlotId: Int, pIsSelected: Boolean): Unit = {
    if (!pEntity.isInstanceOf[Player])
      return

    val tag = pStack.getOrCreateTag()
    if (tag.contains(TAG_AGE)) {
      val age = tag.getInt(TAG_AGE)
      tag.putInt(TAG_AGE, age + 1)
    } else {
      tag.putInt(TAG_AGE, 1)
    }

    if (pStack.getOrCreateTag().getInt(TAG_AGE) >= 200 && canExplosion(pStack, pEntity)) {
      deleteItem(pStack)
      explosion(pEntity)
    }
  }

  override def onDroppedByPlayer(item: ItemStack, player: Player): Boolean = {
    item.getOrCreateTag().remove(TAG_AGE)
    true
  }

  override def appendHoverText(pStack: ItemStack, pLevel: Level, pTooltipComponents: util.List[Component], pIsAdvanced: TooltipFlag): Unit = {
    pTooltipComponents.add(new TranslatableComponent("It will explode in 10 seconds!").withStyle(ChatFormatting.RED))
  }
}

object UnstableBookItem {
  final val TAG_AGE = "Age"

  /**
   * Cause unstable ingot explosion<br>
   * No checks are made
   *
   * @param entity Player
   */
  def explosion(entity: Entity): Unit = {
    val pos = entity.position()
    entity.level.explode(null, DamageSource.badRespawnPointExplosion(), null, pos.x, pos.y, pos.z, 10.0F, true, Explosion.BlockInteraction.DESTROY)
  }

  def canExplosion(pStack: ItemStack, pEntity: Entity): Boolean = {
    if (pStack.getCount <= 0)
      return false
    if (!pEntity.isInstanceOf[Player])
      return false
    if (pEntity.asInstanceOf[Player].isCreative)
      return false
    true
  }

  def deleteItem(pStack: ItemStack): Unit = {
    pStack.setCount(0)
  }
}