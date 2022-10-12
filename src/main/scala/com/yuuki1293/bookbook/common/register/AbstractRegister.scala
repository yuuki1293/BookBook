package com.yuuki1293.bookbook.common.register

import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister

trait AbstractRegister[A] {
  protected val REGISTER: DeferredRegister[A]

  def registry(implicit eventBus: IEventBus): Unit = REGISTER.register(eventBus)
}
