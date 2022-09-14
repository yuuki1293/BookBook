package com.yuuki1293.bookbook.common.register

import com.yuuki1293.bookbook.common.event.CommonEvent
import net.minecraftforge.common.MinecraftForge

object RegisterEvent {
  def registry(): Unit ={
    MinecraftForge.EVENT_BUS.register(new CommonEvent())
  }
}
