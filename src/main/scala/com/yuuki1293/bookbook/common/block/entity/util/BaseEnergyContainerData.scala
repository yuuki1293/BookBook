package com.yuuki1293.bookbook.common.block.entity.util

import net.minecraft.world.inventory.ContainerData

class BaseEnergyContainerData(energy: () => Int, capacitor: () => Int) extends ContainerData {
  /**
   * @param pIndex 0 - [[energy]]<br> 1 - [[capacitor]]
   * @return
   */
  override def get(pIndex: Int): Int = {
    pIndex match {
      case 0 => energy()
      case 1 => capacitor()
      case _ => throw new UnsupportedOperationException("Unable to get index: " + pIndex)
    }
  }

  /**
   * Invalid
   */
  @Deprecated
  override def set(pIndex: Int, pValue: Int): Unit = {
    throw new UnsupportedOperationException("Unable to get index: " + pIndex)
  }

  override def getCount: Int = 2
}