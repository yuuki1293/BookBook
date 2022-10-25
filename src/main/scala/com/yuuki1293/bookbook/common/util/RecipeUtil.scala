package com.yuuki1293.bookbook.common.util

import cats.effect.IO
import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe

object RecipeUtil {
  /**
   * containerのslotとrecipeのgetResultItemを比較してレシピを格納可能なら格納する
   *
   * @param recipe          レシピ
   * @param recipeContainer レシピが格納されているコンテナ
   * @param container       結果を格納するべきコンテナ
   * @param slot            containerのスロット
   * @return 結果が格納できた場合true、失敗した場合falseを返す
   */
  protected def assemble[A <: Container, B <: Container](recipe: Recipe[A], recipeContainer: A, container: B, slot: Int): IO[Boolean] = IO {
    val containerItem = container.getItem(slot)
    val total = containerItem.getCount + recipe.getResultItem.getCount

    if (containerItem.isEmpty) {
      container.setItem(slot, recipe.assemble(recipeContainer))
      true
    }
    else if (ItemStack.isSameItemSameTags(containerItem, recipe.getResultItem)
      && total <= containerItem.getMaxStackSize) {
      containerItem.setCount(total)
      true
    }
    else false
  }
}
