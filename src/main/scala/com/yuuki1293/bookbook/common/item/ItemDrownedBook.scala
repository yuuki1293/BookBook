package com.yuuki1293.bookbook.common.item

import com.yuuki1293.bookbook.common.item.ItemBook.set
import net.minecraft.world.item.{CreativeModeTab, Item}
import net.minecraft.world.item.Item.Properties

class ItemDrownedBook(properties: Properties) extends Item(set(properties)) {
  def this() = this(new Properties)
}

object ItemDrownedBook {
  def set(p: Properties): Properties = {
    p.tab(CreativeModeTab.TAB_MATERIALS)
  }
}