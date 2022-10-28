package com.yuuki1293.bookbook.common.util

import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack

object ContainerUtil {
  /**
   * itemStackをcontainerにできるだけ配置する。
   *
   * @param itemStack 配置したいItemStack
   * @param container 配置先のContainer
   * @param slot      containerのindex
   * @return 完全に配置が完了したならtrueを返す
   */
  def place(itemStack: ItemStack, container: Container, slot: Int): Boolean = {
    place(itemStack, container, slot, slot + 1)
  }

  /**
   * itemStackをcontainerにできるだけ配置する。
   *
   * @param itemStack 配置したいItemStack
   * @param container 配置先のContainer
   * @param start     containerの初めのindex(含まれる)
   * @param end       containerの最後のindex(含まれない)
   * @return 完全に配置が完了したならtrueを返す
   */
  def place(itemStack: ItemStack, container: Container, start: Int, end: Int): Boolean = {
    def done: Boolean = itemStack.isEmpty

    for (i <- start until end) {
      if (!done) {
        val cItem = container.getItem(i)
        if (cItem.isEmpty) {
          container.setItem(i, itemStack.copy())
          itemStack.setCount(0)
        }
        if (ItemStack.isSameItemSameTags(cItem, itemStack)) {
          val dCount = cItem.getMaxStackSize - cItem.getCount
          val moveCount = Math.min(itemStack.getCount, dCount)

          itemStack.shrink(moveCount)
          cItem.shrink(-moveCount)
        }
      }
    }

    done
  }

  /**
   * itemStackをcontainerに配置できるか調べる
   *
   * @param itemStack 配置したいItemStack
   * @param container 配置先のContainer
   * @param slot      containerのindex
   * @return 完全に配置が完了したならtrueを返す
   */
  def canPlace(itemStack: ItemStack, container: Container, slot: Int): Boolean = {
    canPlace(itemStack, container, slot, slot + 1)
  }

  /**
   * itemStackをcontainerに配置できるか調べる
   *
   * @param itemStack 配置したいItemStack
   * @param container 配置先のContainer
   * @param start     containerの初めのindex(含まれる)
   * @param end       containerの最後のindex(含まれない)
   * @return 完全に配置が完了したならtrueを返す
   */
  def canPlace(itemStack: ItemStack, container: Container, start: Int, end: Int): Boolean = {
    val nItemStack = itemStack.copy()

    def done: Boolean = nItemStack.isEmpty

    for (i <- start until end) {
      if (!done) {
        val cItem = container.getItem(i)
        if (cItem.isEmpty) {
          nItemStack.setCount(0)
        }
        if (ItemStack.isSameItemSameTags(cItem, nItemStack)) {
          val dCount = cItem.getMaxStackSize - cItem.getCount
          val moveCount = Math.min(nItemStack.getCount, dCount)

          nItemStack.shrink(moveCount)
        }
      }
    }

    done
  }
}
