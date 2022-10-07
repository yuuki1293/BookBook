package com.yuuki1293.bookbook.common.item

import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.Item
import net.minecraft.world.level.Explosion

class UnstableBookItem(pProperties: Item.Properties) extends Item(pProperties) {
}

object UnstableBookItem {
  /**
   * Cause unstable ingot explosion<br>
   * No checks are made
   * @param entity Player
   */
  def explosion(entity: Entity): Unit = {
    val pos = entity.position()
    entity.level.explode(entity, DamageSource.badRespawnPointExplosion(), null, pos.x, pos.y, pos.z, 5.0F, true, Explosion.BlockInteraction.DESTROY)
  }
}