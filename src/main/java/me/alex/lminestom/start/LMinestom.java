package me.alex.lminestom.start;

import me.alex.lminestom.data.config.LMinestomConfig;
import me.alex.lminestom.data.defaultworld.LMinestomDefaultWorld;
import me.alex.lminestom.data.extras.commands.LMinestomExtensionManager;
import me.alex.lminestom.data.extras.LMinestomMojangAuthImpl;
import me.alex.lminestom.data.extras.LMinestomOptifinSupportImpl;
import me.alex.lminestom.data.extras.LMinestomVelocityImpl;
import me.alex.lminestom.data.extras.commands.LMinestomStopCommand;
import me.alex.lminestom.events.LMinestomEventDispatcher;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.InstanceContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;

public class LMinestom {

    private static LMinestomConfig lMinestomConfig;
    private static MinecraftServer minecraftServer;
    private static InstanceContainer defaultInstance;
    private static final Logger lminestomLogger = LoggerFactory.getLogger("Minestom");

    public static void main(String[] args) throws IOException {
        lMinestomConfig = new LMinestomConfig(Paths.get("lminestom.properties").toAbsolutePath().toFile());

        minecraftServer = MinecraftServer.init();
        new LMinestomDefaultWorld().initDefaultContainer();
        new LMinestomEventDispatcher().registerListeners();

        LMinestomMojangAuthImpl.initMojangAuth();
        LMinestomVelocityImpl.initVelocitySupport();
        LMinestomOptifinSupportImpl.initOptifineSupport();

        if (Boolean.getBoolean("lminestom.extensionmanager.enabled"))
            new LMinestomExtensionManager("extensionmanager", "exm");
        new LMinestomStopCommand("stop","stopp","halt");

        minecraftServer.start(System.getProperty("minestom.ip", "0.0.0.0"), Integer.getInteger("minestom.port", 25565));
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
}
