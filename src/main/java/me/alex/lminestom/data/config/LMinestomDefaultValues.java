package me.alex.lminestom.data.config;

public enum LMinestomDefaultValues {

    SystemPort("port", "25565"),
    SystemIP("ip-adress", "0.0.0.0"),
    MaxTPS("tps", "20"),
    MaxOnlinePlayers("max-players", "50"),
    ChunkViewDistance("view-distance", "8"),
    EntityViewDistance("entity-view-distance", "5"),
    OptifineSupportEnabled("optifine-enabled", "true"),
    BlockPlacementRuleEnabled("blockplacement-rules-enabled", "true"),
    TerminalEnabled("terminal-enabled", "false"),
    OnlineMode("online-mode", "true"),
    CloudnetImpl("cloudnet-impl-enabled", "false"),
    ExtensionManager("extension-manager-enabled", "true"),
    VelocityModeEnabled("velocity.enabled", "true"),
    VelocitySecretKey("velocity.secret", "yourKey"),
    ServerMotd("motd", "This is L-Minestom!"),
    CustomDefaultWorld("custom-default-world.enabled", "false"),
    CustomDefaultWorldFolder("custom-default-world.folder", "yourFolder");


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
