package com.yuuki1293.bookbook.common.recipe.custom

import com.yuuki1293.bookbook.common.item.BookItem
import com.yuuki1293.bookbook.common.register
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.inventory.CraftingContainer
import net.minecraft.world.item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.{CustomRecipe, RecipeSerializer, SimpleRecipeSerializer}
import net.minecraft.world.level.Level

class WaterproofedBookRecipe(pId: ResourceLocation) extends CustomRecipe(pId){
  override def matches(pInv: CraftingContainer, pLevel: Level): Boolean = {
    var book = false
    var orangeDye = false

    for (i <- 0 until pInv.getContainerSize) {
      val itemStack1 = pInv.getItem(i)
      if (!itemStack1.isEmpty) {
        if (itemStack1.is(register.Items.BOOK.get())) {
          if(book){
            return false
          }

          book = true
        } else if (!itemStack1.is(item.Items.ORANGE_DYE)) {
          if (orangeDye){
            return false
          }

          orangeDye = true
        }
      }
    }

    book && orangeDye
  }

  override def assemble(pInv: CraftingContainer): ItemStack = {
    var book = false
    var orangeDye = false

    for (i <- 0 until pInv.getContainerSize) {
      val itemStack1 = pInv.getItem(i)
      if (!itemStack1.isEmpty) {
        if (itemStack1.is(register.Items.BOOK.get())) {
          if (book) {
            return ItemStack.EMPTY
          }

          book = true
        } else if (!itemStack1.is(item.Items.ORANGE_DYE)) {
          if (orangeDye) {
            return ItemStack.EMPTY
          }

          orangeDye = true
        }
      }
    }

    if (book && orangeDye) getResultItem else ItemStack.EMPTY
  }

//  override def getRemainingItems(pContainer: CraftingContainer): NonNullList[ItemStack] = {
//    NonNullList.withSize(pContainer.getContainerSize, ItemStack.EMPTY)
//  }

  override def getSerializer: RecipeSerializer[_] = WaterproofedBookRecipe.getSerializer

  override def canCraftInDimensions(pWidth: Int, pHeight: Int): Boolean = pWidth * pHeight >= 2

  override def isSpecial: Boolean = false

  override def getResultItem: ItemStack = {
    val itemStack = new ItemStack(register.Items.BOOK.get())
    BookItem.setWaterProof(itemStack, p = true)
    itemStack
  }
}

object WaterproofedBookRecipe {
  var SERIALIZER: Option[SimpleRecipeSerializer[WaterproofedBookRecipe]] = None

  def getSerializer: RecipeSerializer[_] = {
    SERIALIZER match {
      case Some(value) => value
      case None =>
        val serializer = new SimpleRecipeSerializer(new WaterproofedBookRecipe(_))
        SERIALIZER = Some(serializer)
        serializer
    }
  }
}