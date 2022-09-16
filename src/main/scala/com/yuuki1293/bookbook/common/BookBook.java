package com.yuuki1293.bookbook.common;

import com.yuuki1293.bookbook.common.register.RegisterBlock;
import com.yuuki1293.bookbook.common.register.RegisterEvent;
import com.yuuki1293.bookbook.common.register.RegisterItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BookBook.MODID)
public class BookBook {
    public static final String MODID = "bookbook";
    // Directly reference a slf4j logger

    public BookBook() {
        RegisterItem.registry();
        RegisterBlock.registry();
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        RegisterEvent.registry();
    }
}
