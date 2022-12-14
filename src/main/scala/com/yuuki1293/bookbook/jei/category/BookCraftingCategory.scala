package com.yuuki1293.bookbook.jei.category

import com.yuuki1293.bookbook.api.crafting.IBookCraftingRecipe
import com.yuuki1293.bookbook.common.BookBook
import com.yuuki1293.bookbook.common.register.Blocks
import com.yuuki1293.bookbook.jei.category.BookCraftingCategory.{TEXTURE, UID}
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.category.IRecipeCategory
import mezz.jei.api.recipe.{IFocusGroup, RecipeIngredientRole}
import net.minecraft.network.chat.{Component, TranslatableComponent}
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

import java.awt.Point

class BookCraftingCategory(implicit helper: IGuiHelper) extends IRecipeCategory[IBookCraftingRecipe] {
  private final val background = helper.createDrawable(TEXTURE, 0, 0, 140, 171)
  private final val icon =
    helper.createDrawableIngredient(
      VanillaTypes.ITEM_STACK,
      new ItemStack(Blocks.BOOK_CRAFTING_CORE.get()))

  override def getTitle: Component =
    new TranslatableComponent("jei.category.bookbook.book_crafting")

  override def getBackground: IDrawable = background

  override def getIcon: IDrawable = icon

  //noinspection ScalaDeprecation
  override def getUid: ResourceLocation = UID

  //noinspection ScalaDeprecation
  override def getRecipeClass: Class[_ <: IBookCraftingRecipe] = classOf[IBookCraftingRecipe]

  override def setRecipe(builder: IRecipeLayoutBuilder, recipe: IBookCraftingRecipe, focuses: IFocusGroup): Unit = {
    builder.addSlot(RecipeIngredientRole.INPUT, 55, 47)
      .addIngredients(recipe.getInputs.get(0))

    builder.addSlot(RecipeIngredientRole.OUTPUT, 55, 150)
      .addItemStack(recipe.getResultItem)

    val inputs = recipe.getInputs
    val count = inputs.size() - 1

    for ((i, pos) <- circlePos(count, 39.0d, new Point(55, 47))) {
      builder.addSlot(RecipeIngredientRole.INPUT, pos.x, pos.y)
        .addIngredients(inputs.get(i + 1))
    }
  }

  //noinspection SameParameterValue
  private def circlePos(count: Int, r: Double, origin: Point): Seq[(Int, Point)] = {
    def toRad(i: Int): Double = -2.0d * math.Pi * i.toDouble / count.toDouble

    for {
      i <- 0 until count
      sita = toRad(i)
      x0 = 0
      y0 = -r
      x = x0 * math.cos(sita) - y0 * math.sin(sita) + origin.x
      y = x0 * math.sin(sita) + y0 * math.cos(sita) + origin.y
    } yield (i, new Point(x.toInt, y.toInt))
  }
}

object BookCraftingCategory {
  private final val TEXTURE = new ResourceLocation(BookBook.MOD_ID, "textures/gui/jei/book_crafting.png")
  final val UID = new ResourceLocation(BookBook.MOD_ID, "book_crafting")

  def apply(implicit helper: IGuiHelper) =
    new BookCraftingCategory
}