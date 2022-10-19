package com.yuuki1293.bookbook.common.block.entity

import net.minecraft.core.NonNullList
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.ItemStack
import net.minecraft.world.{Container, MenuProvider, Nameable, WorldlyContainer}
import net.minecraftforge.common.extensions.IForgeBlockEntity

trait BaseBookContainerBlockEntity
  extends IForgeBlockEntity with Container with MenuProvider with Nameable with WorldlyContainer {
  var items: NonNullList[ItemStack]

  def load(pTag: CompoundTag) ={

  }
}
