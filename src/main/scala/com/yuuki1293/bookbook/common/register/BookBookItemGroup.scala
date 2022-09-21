package com.yuuki1293.bookbook.common.register

import net.minecraft.network.chat.{Component, TextComponent}
import net.minecraft.world.item.{CreativeModeTab, ItemStack}

class BookBookItemGroup() extends CreativeModeTab("BookBook") {
  override def makeIcon(): ItemStack = Items.DROWNED_BOOK.get().asItem().getDefaultInstance

  override def getDisplayName: Component = new TextComponent("BookBook")
}
