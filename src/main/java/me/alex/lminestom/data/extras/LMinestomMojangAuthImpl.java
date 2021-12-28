package me.alex.lminestom.data.extras;

import me.alex.lminestom.start.LMinestom;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.MojangAuth;
import org.slf4j.Logger;

public class LMinestomMojangAuthImpl {

    private static final Logger logger = LMinestom.getMainLogger();

    public static void initMojangAuth() {
        if (Boolean.getBoolean("minestom.online-mode.enabled")) {
            logger.info("Trying to enabled Mojang Auth.");
            if (MinecraftServer.isStarted()) {
                logger.warn("You can only enable Mojang Auth before Server start.");
                return;
            }
            if (MojangAuth.isEnabled()) {
                logger.warn("MojangAuth is already enabled!");
                return;
            }

            MojangAuth.init();
            logger.info("Enabled Mojang Auth!");
        }
    }
}
