package com.yuuki1293.bookbook.common.item

import net.minecraft.world.item.Item

class BaseCompressedItem(val pTier: Int, pProperties: Item.Properties) extends Item(pProperties) {
}

object BaseCompressedItem {
  def apply(tier: Int, properties: Item.Properties) = new BaseCompressedItem(tier, properties)
}