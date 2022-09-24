package com.yuuki1293.bookbook.common.item

import net.minecraft.world.item.Item
import net.minecraft.world.item.Item.Properties

class BaseCompressedItem(val tier: Int, properties: Properties) extends Item(properties) {
  def getTier: Int = tier
}
