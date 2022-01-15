package me.alex.lminestom.data.extras;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import me.alex.lminestom.data.config.LMinestomDefaultValues;
import me.alex.lminestom.start.LMinestom;
import net.minestom.server.MinecraftServer;
import org.slf4j.Logger;

public class LMinestomCloudNetImpl {

    private static final Logger logger = LMinestom.getMainLogger();

    public static void initCloudNetImpl() {
        if (Boolean.getBoolean(LMinestomDefaultValues.CloudnetImpl.getIdentifier())) {
            logger.info("Trying to enabled CloudNet Implementation.");
            if (MinecraftServer.isStarted()) {
                logger.warn("You can only enable CloudNet Implementation before Server start.");
                return;
            }


            CloudNetDriver cloudNetDriver = CloudNetDriver.getInstance();
            if (cloudNetDriver == null) {
                logger.error("There was no CloudNet found, disabling CloudNet Implementation.");
                return;
            }

            String currentServer = cloudNetDriver.getComponentName();
            ServiceInfoSnapshot serviceInfoSnapshot = cloudNetDriver.getCloudServiceProvider().getCloudServiceByName(currentServer);

            if (!MinecraftServer.isStarted() && serviceInfoSnapshot.getConfiguration() != null) {
                int port = serviceInfoSnapshot.getConfiguration().getPort();
                System.setProperty(LMinestomDefaultValues.SystemPort.getIdentifier(), port + "");
                logger.warn("Enabled CloudNet Server Implementation. Overriding port to {}!", port);
            } else logger.warn("Something went wrong during CloudNet impl start up.");
        }
    }
}
