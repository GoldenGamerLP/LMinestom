package me.alex.lminestom.data.defaultworld;

import me.alex.lminestom.data.defaultworld.chunkgenerator.vanilla.VanillaLikeGenerator;
import me.alex.lminestom.data.config.LMinestomConfig;
import me.alex.lminestom.data.config.LMinestomDefaultValues;
import me.alex.lminestom.data.defaultworld.chunkgenerator.voidgenerator.DefaultVoidGenerator;
import me.alex.lminestom.start.LMinestom;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.InstanceContainer;
import org.slf4j.Logger;

import java.nio.file.Files;
import java.nio.file.Paths;

public class LMinestomDefaultWorld {

    private final Logger logger = LMinestom.getMainLogger();
    private final LMinestomConfig lMinestomConfig = LMinestom.getDefaultConfig();
    private volatile boolean init = false;

    public LMinestomDefaultWorld() {
        if (init) {
            logger.warn("You can only init the Default Class once!");
            return;
        }
        init = true;
    }

    public void initDefaultContainer() {
        if(Boolean.getBoolean(LMinestomDefaultValues.EnableDefaultInstance.getIdentifier())) {
            InstanceContainer container = MinecraftServer.getInstanceManager().createInstanceContainer();

            logger.info("Trying to enable Default Instance.");

            String chunkGenerator = lMinestomConfig.getConfigEntry(LMinestomDefaultValues.DefaultInstanceChunkGenerator);
            switch (chunkGenerator) {
                case "void":
                    container.setChunkGenerator(new DefaultVoidGenerator());
                    logger.info("Using Void ChunkGenerator for Default instance.");
                    break;
                case "vanilla":
                    container.setChunkGenerator(new VanillaLikeGenerator());
                    logger.info("Using Vanilla Like ChunkGenerator for Default instance.");
                    logger.info("VanillaLikeGenerator is heavy and not optimized yet!");
                    break;
                case "own":
                    String world = lMinestomConfig.getConfigEntry(LMinestomDefaultValues.DefaultInstanceFolder);
                    container.setChunkLoader(new AnvilLoader(Paths.get(world)));
                    logger.info("Enabled AnvilChunLoader with world {}",world);
                default:
                    logger.warn("{} is not a valid chunkgenerator!",chunkGenerator);
                    container.setChunkGenerator(new DefaultVoidGenerator());
                    break;
            }

            container.enableAutoChunkLoad(true);
            LMinestom.setDefaultInstance(container);

        }
    }
}
