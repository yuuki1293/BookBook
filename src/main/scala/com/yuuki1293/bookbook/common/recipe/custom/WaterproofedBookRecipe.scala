package com.yuuki1293.bookbook.common.recipe.custom

import com.google.common.collect.Lists
import com.yuuki1293.bookbook.common.item.BookItem
import com.yuuki1293.bookbook.common.register
import com.yuuki1293.bookbook.common.register.Recipe
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.inventory.CraftingContainer
import net.minecraft.world.item.crafting.{CustomRecipe, RecipeSerializer}
import net.minecraft.world.item.{DyeItem, Item, ItemStack}
import net.minecraft.world.level.Level

import java.util

class WaterproofedBookRecipe(pId: ResourceLocation) extends CustomRecipe(pId) {
  override def matches(pInv: CraftingContainer, pLevel: Level): Boolean = {
    var itemstack: ItemStack = ItemStack.EMPTY
    val list: util.List[ItemStack] = Lists.newArrayList
    for (i <- 0 until pInv.getContainerSize) {
      val itemstack1: ItemStack = pInv.getItem(i)
      if (!itemstack1.isEmpty) if (itemstack1.getItem.isInstanceOf[BookItem]) {
        if (!itemstack.isEmpty) return false
        itemstack = itemstack1
      }
      else {
        if (!itemstack1.getItem.isInstanceOf[DyeItem]) return false
        list.add(itemstack1)
      }
    }
    !itemstack.isEmpty && !list.isEmpty
  }

  /**
   * Returns an Item that is the result of this recipe
   */
  override def assemble(pInv: CraftingContainer): ItemStack = {
    val list: util.List[DyeItem] = Lists.newArrayList
    var itemstack: ItemStack = ItemStack.EMPTY
    for (i <- 0 until pInv.getContainerSize) {
      val itemstack1: ItemStack = pInv.getItem(i)
      if (!itemstack1.isEmpty) {
        val item: Item = itemstack1.getItem
        if (item.isInstanceOf[BookItem]) {
          if (!itemstack.isEmpty) return ItemStack.EMPTY
          itemstack = itemstack1.copy
        }
        else {
          if (!item.isInstanceOf[DyeItem]) return ItemStack.EMPTY
          list.add(item.asInstanceOf[DyeItem])
        }
      }
    }
    if (!itemstack.isEmpty && !list.isEmpty) {
      getResultItem
    } else ItemStack.EMPTY
  }

  override def getResultItem: ItemStack = {
    val itemStack = new ItemStack(register.Items.BOOK.get())
    BookItem.setWaterProof(itemStack, p = true)
    itemStack
  }

  override def canCraftInDimensions(pWidth: Int, pHeight: Int): Boolean = pWidth * pHeight >= 2

  override def getSerializer: RecipeSerializer[_] = Recipe.WATERPROOFED_BOOK.get()

  override def isSpecial: Boolean = false
}