package com.yuuki1293.bookbook.common.block.entity

import net.minecraft.core.NonNullList
import net.minecraft.world.item.ItemStack

trait IBookContainerBlockEntity {
  protected var items: NonNullList[ItemStack]
}
