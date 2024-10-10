package org.rhm;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

// TODO: Once the common subproject uses parchment, add all of the things
// the fabric project already has here.
@SuppressWarnings("unused")
@Mod(CyberRewaredMod.MOD_ID)
public class CyberRewaredNeoForge {
    private static final Logger LOGGER = LogUtils.getLogger();

    public CyberRewaredNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        CyberRewaredMod.init();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        CyberRewaredMod.initClient();
    }
}
