package com.yuuki1293.bookbook.common.recipe

import com.google.gson.{JsonObject, JsonSyntaxException}
import com.yuuki1293.bookbook.api.crafting.IBookCraftingRecipe
import net.minecraft.core.NonNullList
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.GsonHelper
import net.minecraft.world.SimpleContainer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting._
import net.minecraft.world.level.Level
import net.minecraftforge.common.util.RecipeMatcher
import net.minecraftforge.registries.ForgeRegistryEntry

class BookCraftingRecipe(
  private final val recipeId: ResourceLocation,
  private final val ingredients: NonNullList[Ingredient],
  private final val output: ItemStack,
  private final val powerCost: Int,
  private final val powerRate: Int)
  extends Recipe[SimpleContainer] with IBookCraftingRecipe {

  override def matches(pContainer: SimpleContainer, pLevel: Level): Boolean = {
    var flag = false

    val input = pContainer.getItem(0)
    flag = ingredients.get(0).test(input)

    val inputs: NonNullList[ItemStack] = NonNullList.create()
    if (flag) {
      for (i <- 0 until pContainer.getContainerSize) {
        inputs.add(pContainer.getItem(i))
      }
    }

    RecipeMatcher.findMatches(inputs, ingredients) != null
  }

  override def assemble(pContainer: SimpleContainer): ItemStack = output.copy()

  override def canCraftInDimensions(pWidth: Int, pHeight: Int): Boolean = true

  override def getResultItem: ItemStack = output

  override def getId: ResourceLocation = recipeId

  override def getSerializer: RecipeSerializer[_] = BookCraftingRecipe.Serializer

  override def getType: RecipeType[_] = BookCraftingRecipe.Type

  def getPowerCost: Int = powerCost

  def getPowerRate: Int = powerRate

  override def getInputs: NonNullList[Ingredient] = ingredients
}

object BookCraftingRecipe {
  def apply(id: ResourceLocation, ingredients: NonNullList[Ingredient], output: ItemStack, powerCost: Int, powerRate: Int) =
    new BookCraftingRecipe(id, ingredients, output, powerCost, powerRate)

  object Type extends RecipeType[BookCraftingRecipe] {}

  object Serializer extends ForgeRegistryEntry[RecipeSerializer[_]] with RecipeSerializer[BookCraftingRecipe] {
    override def fromJson(pRecipeId: ResourceLocation, pSerializedRecipe: JsonObject): BookCraftingRecipe = {
      val inputs: NonNullList[Ingredient] = NonNullList.create()
      val input = Ingredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "input"))

      inputs.add(input)

      val ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients")
      for (i <- 0 until ingredients.size()) {
        val ingredient = Ingredient.fromJson(ingredients.get(i))
        inputs.add(ingredient)
      }

      val output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "result"))

      if (!pSerializedRecipe.has("powerCost"))
        throw new JsonSyntaxException("powerCost is required")

      val powerCost = GsonHelper.getAsInt(pSerializedRecipe, "powerCost")
      val powerRate = GsonHelper.getAsInt(pSerializedRecipe, "powerRate", 10000000)

      new BookCraftingRecipe(pRecipeId, inputs, output, powerCost, powerRate)
    }

    override def fromNetwork(pRecipeId: ResourceLocation, pBuffer: FriendlyByteBuf): BookCraftingRecipe = {
      val size = pBuffer.readVarInt()
      val inputs = NonNullList.withSize(size, Ingredient.EMPTY)

      for (i <- 0 until size) {
        inputs.set(i, Ingredient.fromNetwork(pBuffer))
      }

      val output = pBuffer.readItem()
      val powerCost = pBuffer.readVarInt()
      val powerRate = pBuffer.readVarInt()

      new BookCraftingRecipe(pRecipeId, inputs, output, powerCost, powerRate)
    }

    override def toNetwork(pBuffer: FriendlyByteBuf, pRecipe: BookCraftingRecipe): Unit = {
      import scala.jdk.CollectionConverters._

      pBuffer.writeVarInt(pRecipe.ingredients.size())

      pRecipe.ingredients.asScala.foreach(_.toNetwork(pBuffer))

      pBuffer.writeItem(pRecipe.output)
      pBuffer.writeVarInt(pRecipe.powerCost)
      pBuffer.writeVarInt(pRecipe.powerRate)
    }
  }
}