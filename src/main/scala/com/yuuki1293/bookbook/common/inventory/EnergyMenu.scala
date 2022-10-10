package com.yuuki1293.bookbook.common.inventory

trait EnergyMenu {

  def getEnergyStored: Int

  def getMaxEnergy: Int

  /**
   * 0 - Energy is empty<br>
   * 100 - Energy is full
   *
   * @return The percentage of energy as a percentage of 100
   */
  def getEnergyProportion: Int = {
    val energyStored = getEnergyStored
    val maxEnergy = getMaxEnergy

    if (maxEnergy == 0)
      return 0
    energyStored * 100 / maxEnergy
  }
}
