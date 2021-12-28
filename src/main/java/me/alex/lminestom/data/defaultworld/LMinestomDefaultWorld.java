package me.alex.lminestom.data.defaultworld;

import me.alex.lminestom.data.chunkgenerator.DefaultVoidGenerator;
import me.alex.lminestom.start.LMinestom;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.InstanceContainer;
import org.slf4j.Logger;

import java.nio.file.Files;
import java.nio.file.Paths;

public class LMinestomDefaultWorld {

    private final Logger logger = LMinestom.getMainLogger();
    private volatile boolean init = false;
    private InstanceContainer instanceContainer;

    public LMinestomDefaultWorld() {
        if (init) {
            logger.warn("You can only init the Default Class one time");
            return;
        }
        init = true;
    }

    public void initDefaultContainer() {
        instanceContainer = MinecraftServer.getInstanceManager().createInstanceContainer();

        if (Boolean.getBoolean("lminestom.customdefaultworld")) {
            logger.info("Enabled custom default world.");

            String defaultWorldName = System.getProperty("lminestom.customworldfoldername", "default");
            if (Files.exists(Paths.get(defaultWorldName))) {
                logger.info("Enabling AnvilLoader!");
                instanceContainer.setChunkLoader(new AnvilLoader(Paths.get(defaultWorldName)));
            } else {
                logger.info("Enabled custom default world but found no world, using default world generator.");
                instanceContainer.setChunkGenerator(new DefaultVoidGenerator());
            }
        } else {
            logger.info("Enabling default Chunk Generator");
            instanceContainer.setChunkGenerator(new DefaultVoidGenerator());
        }

        /*if(Boolean.getBoolean("lminestom.lobby.mode") && Boolean.getBoolean("lminestom.customdefaultworld")) {
            logger.info("Enabling Lobby Mode (Custom World Loading) (Experimental, you at own risk!)");
            int radius = Integer.getInteger("lminestom.lobby.loadradius",0);
            if(radius != 0) {
                instanceContainer.enableAutoChunkLoad(false);
                logger.info("Loading Lobby world in radius of ({})",radius);

                for(int x = -radius; x <= radius; x++) {
                    for(int z = -radius; z <= radius; z++) {
                        logger.info("Loading Chunk: X: {}, Z: {}",x,z);
                        instanceContainer.loadChunk(x,z);
                    }
                }
                instanceContainer.getWorldBorder().setCenter(0,0);
                instanceContainer.getWorldBorder().setDiameter(radius * 2 * 16 / 2.2);
            } else {
                logger.info("You cannot use a smaller load radius value than 1!");
                instanceContainer.enableAutoChunkLoad(true);
            }
        } else {
            logger.info("Enabled Auto Chunk Loading");
            instanceContainer.enableAutoChunkLoad(true);
        }*/
        LMinestom.setDefaultInstance(instanceContainer);
    }
}
