package me.alex.lminestom.data.config;

public enum LMinestomDefaultValues {

    ChunkViewDistance("minestom.chunk-view-distance", "8"),
    EntityViewDistance("minestom.entity-view-distance", "5"),
    TerminalEnabled("minestom.terminal.disabled", "false"),
    MaxTPS("minestom.tps", "20"),
    SystemPort("minestom.port", "25565"),
    SystemIP("minestom.ip", "0.0.0.0"),
    OptifineSupportEnabled("lminestom.optifinesupport.enabled", "true"),
    CustomDefaultWorldEnabled("lminestom.customdefaultworld", "false"),
    DefaultWorldFolderName("lminestom.customworldfoldername", "yourename"),
    IsOnlineModeEnabled("minestom.online-mode.enabled", "false"),
    ServerMOTD("lminestom.motd", "LMinestom!"),
    EnableExtensionManager("lminestom.extensionmanager.enabled", "true"),
    VelocityModeEnabled("lminestom.velocity.enabled", "false"),
    VelocityModeKey("lminestom.velocity.secret", "youresecretkey"),
    MaxOnlinePlayers("lminestom.server.maxplayers","50");


    private final String identifier;
    private final String defaultValue;

    LMinestomDefaultValues(String identifier, String defaultValue) {
        this.identifier = identifier;
        this.defaultValue = defaultValue;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
