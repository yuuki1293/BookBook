package com.yuuki1293.bookbook.common.item

import com.yuuki1293.bookbook.common.item.UnstableBookItem._
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.{Item, ItemStack}
import net.minecraft.world.level.{Explosion, Level}

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

    if (pStack.getOrCreateTag().getInt(TAG_AGE) >= 200 && canExplosion) {
      deleteItem(pStack)
      explosion(pEntity)
    }
  }
}

object UnstableBookItem {
  final val TAG_AGE = "AGE"

  /**
   * Cause unstable ingot explosion<br>
   * No checks are made
   *
   * @param entity Player
   */
  def explosion(entity: Entity): Unit = {
    val pos = entity.position()
    entity.level.explode(entity, DamageSource.badRespawnPointExplosion(), null, pos.x, pos.y, pos.z, 5.0F, true, Explosion.BlockInteraction.DESTROY)
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