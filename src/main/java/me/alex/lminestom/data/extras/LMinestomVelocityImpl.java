package me.alex.lminestom.data.extras;

import me.alex.lminestom.data.config.LMinestomConfig;
import me.alex.lminestom.data.config.LMinestomDefaultValues;
import me.alex.lminestom.start.LMinestom;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.velocity.VelocityProxy;
import org.slf4j.Logger;

public class LMinestomVelocityImpl {

    private static final Logger logger = LMinestom.getMainLogger();
    private static final LMinestomConfig lMinestomConfig = LMinestom.getDefaultConfig();

    public static void initVelocitySupport() {
        if (Boolean.getBoolean(LMinestomDefaultValues.VelocityModeEnabled.getIdentifier())) {
            String secret = lMinestomConfig.getConfigEntry(LMinestomDefaultValues.VelocitySecretKey);

            logger.info("Trying to enable VelocitySupport.");
            if (secret == null) {
                logger.info("Velocity Support enabled but no secret key!");
                return;
            }

            if (Boolean.getBoolean("minestom.online-mode.enabled")) {
                logger.info("You enabled online mode and velocity/proxy mode, please disabled online mode to use velocity mode.");
                return;
            }

            if (MinecraftServer.isStarted()) {
                logger.warn("You can only enable Velocity Support before Server start.");
                return;
            }

            VelocityProxy.enable(secret);
            logger.info("Velocity Support enabled successfully.");
        }
    }
}
