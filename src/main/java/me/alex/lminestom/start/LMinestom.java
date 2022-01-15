package me.alex.lminestom.start;

import me.alex.lminestom.data.config.LMinestomConfig;
import me.alex.lminestom.data.config.LMinestomDefaultValues;
import me.alex.lminestom.data.defaultworld.LMinestomDefaultWorld;
import me.alex.lminestom.data.extras.*;
import me.alex.lminestom.data.extras.commands.LMinestomExtensionManager;
import me.alex.lminestom.data.extras.commands.LMinestomStopCommand;
import me.alex.lminestom.events.LMinestomEventDispatcher;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.InstanceContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;

public class LMinestom {

    private static final Logger lminestomLogger = LoggerFactory.getLogger("Minestom");
    private static LMinestomConfig lMinestomConfig;
    private static MinecraftServer minecraftServer;
    private static InstanceContainer defaultInstance;

    public static void main(String[] args) throws IOException {
        long ms = System.currentTimeMillis();
        lMinestomConfig = new LMinestomConfig(Paths.get("lminestom.properties").toAbsolutePath().toFile());

        minecraftServer = MinecraftServer.init();
        initalize();

        minecraftServer.start(lMinestomConfig.getConfigEntry(LMinestomDefaultValues.SystemIP), Integer.parseInt(lMinestomConfig.getConfigEntry(LMinestomDefaultValues.SystemPort)));
        lminestomLogger.info("Started server up in {} ms!", System.currentTimeMillis() - ms);
    }

    private static void initalize() {
        new LMinestomDefaultWorld().initDefaultContainer();
        new LMinestomEventDispatcher().registerListeners();

        LMinestomCloudNetImpl.initCloudNetImpl();
        LMinestomMojangAuthImpl.initMojangAuth();
        LMinestomVelocityImpl.initVelocitySupport();
        LMinestomOptifinSupportImpl.initOptifineSupport();
        LMinestomBlockPlacementImpl.initBlockPlacementRules();

        if (Boolean.getBoolean("lminestom.extensionmanager.enabled"))
            new LMinestomExtensionManager("extensionmanager", "exm");
        new LMinestomStopCommand("stop", "stopp", "halt");
    }

    public static InstanceContainer getDefaultInstance() {
        return defaultInstance;
    }

    public static void setDefaultInstance(InstanceContainer defaultInstance) {
        LMinestom.defaultInstance = defaultInstance;
    }

    public static Logger getMainLogger() {
        return lminestomLogger;
    }

    public static LMinestomConfig getDefaultConfig() {
        return lMinestomConfig;
    }
}
