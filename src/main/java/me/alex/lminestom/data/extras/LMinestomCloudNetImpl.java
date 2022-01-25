package me.alex.lminestom.data.extras;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import me.alex.lminestom.data.config.LMinestomDefaultValues;
import me.alex.lminestom.start.LMinestom;
import net.minestom.server.MinecraftServer;
import org.slf4j.Logger;

public class LMinestomCloudNetImpl {

    private static final Logger logger = LMinestom.getMainLogger();
    private static volatile Boolean isEnabled = false;

    //CloudNet Implementation
    //Getting Port for this Service and overriding it to the port
    public static void initCloudNetImpl() {
        if (Boolean.getBoolean(LMinestomDefaultValues.CloudnetImpl.getIdentifier())) {
            logger.info("Trying to enabled CloudNet Implementation.");
            if (MinecraftServer.isStarted()) {
                logger.warn("You can only enable CloudNet Implementation before Server start.");
                return;
            }

            if (Boolean.getBoolean(LMinestomDefaultValues.OnlineMode.getIdentifier())) {
                logger.warn("You can only enable CloudNet Implementation if your in Offline Mode.");
                return;
            }


            CloudNetDriver cloudNetDriver = CloudNetDriver.getInstance();
            if (cloudNetDriver == null) {
                logger.warn("There was no CloudNet found, disabling CloudNet Implementation.");
                return;
            }

            String currentServer = cloudNetDriver.getComponentName();
            ServiceInfoSnapshot serviceInfoSnapshot = cloudNetDriver.getCloudServiceProvider().getCloudServiceByName(currentServer);

            if (!MinecraftServer.isStarted() && serviceInfoSnapshot.getConfiguration() != null) {
                //Getting Port -> Setting Port and Printing Message
                int port = serviceInfoSnapshot.getConfiguration().getPort();
                System.setProperty(LMinestomDefaultValues.SystemPort.getIdentifier(), port + "");
                logger.info("Enabled CloudNet Server Implementation. Overriding port to {}!", port);
            } else logger.warn("Something went wrong during CloudNet impl start up.");
        }
    }
}
