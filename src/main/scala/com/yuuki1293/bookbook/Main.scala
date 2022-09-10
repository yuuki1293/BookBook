package com.yuuki1293.bookbook

import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.{LogManager, Logger}

@Mod.EventBusSubscriber
class Main {
  val LOGGER: Logger = LogManager.getLogger("bookbook")
}