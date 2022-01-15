package me.alex.lminestom.data.extras;

import me.alex.lminestom.data.config.LMinestomConfig;
import me.alex.lminestom.data.config.LMinestomDefaultValues;
import me.alex.lminestom.start.LMinestom;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.optifine.OptifineSupport;
import org.slf4j.Logger;

public class LMinestomOptifinSupportImpl {

    private static final Logger logger = LMinestom.getMainLogger();
    private static final LMinestomConfig lMinestomConfig = LMinestom.getDefaultConfig();

    public static void initOptifineSupport() {
        if (Boolean.getBoolean(lMinestomConfig.getConfigEntry(LMinestomDefaultValues.OptifineSupportEnabled))) {
            logger.info("Trying to enable Optifine Support!");
        }
        if (MinecraftServer.isStarted()) {
            logger.warn("You can only enabled Optifine support before Server start.");
            return;
        }
        if (OptifineSupport.isEnabled()) {
            logger.warn("Optifine support is already enabled!");
            return;
        }

        OptifineSupport.enable();
        logger.info("Enabled Optifine Support");

    }

}
