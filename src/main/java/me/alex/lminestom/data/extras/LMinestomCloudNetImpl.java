package me.alex.lminestom.data.extras;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import me.alex.lminestom.data.config.LMinestomConfig;
import me.alex.lminestom.data.config.LMinestomDefaultValues;
import me.alex.lminestom.start.LMinestom;
import net.minestom.server.MinecraftServer;
import org.slf4j.Logger;

public class LMinestomCloudNetImpl {

    private static final Logger logger = LMinestom.getMainLogger();
    private static final LMinestomConfig lMinestomConfig = LMinestom.getDefaultConfig();

    public static void initCloudNetImpl() {
        if (Boolean.getBoolean(lMinestomConfig.getConfigEntry(LMinestomDefaultValues.CloudnetImpl))) {
            logger.info("Trying to enabled CloudNet Implementation.");
            if (MinecraftServer.isStarted()) {
                logger.warn("You can only enable CloudNet Implementation before Server start.");
                return;
            }

            if (CloudNetDriver.getInstance() == null) {
                logger.error("There was no CloudNet found, disabling CloudNet Implementation.");
                return;
            }

            CloudNetDriver cloudNetDriver = CloudNetDriver.getInstance();
            String currentServer = cloudNetDriver.getComponentName();

            cloudNetDriver.getCloudServiceProvider().getCloudServiceByNameAsync(currentServer).onComplete(serviceInfoSnapshot -> {
                if (!MinecraftServer.isStarted()) {
                    int port = serviceInfoSnapshot.getConfiguration().getPort();
                    System.setProperty(LMinestomDefaultValues.SystemPort.getIdentifier(), port + "");
                    logger.warn("Enabled CloudNet Server Implementation. Overriding port to {}!", port);
                }
            });
        }
    }
}
