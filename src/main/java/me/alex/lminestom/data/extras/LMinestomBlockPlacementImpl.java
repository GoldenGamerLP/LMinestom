package me.alex.lminestom.data.extras;

import me.alex.lminestom.data.config.LMinestomConfig;
import me.alex.lminestom.data.config.LMinestomDefaultValues;
import me.alex.lminestom.start.LMinestom;
import net.minestom.server.extras.PlacementRules;
import org.slf4j.Logger;

public class LMinestomBlockPlacementImpl {

    private static final Logger logger = LMinestom.getMainLogger();
    private static final LMinestomConfig lMinestomConfig = LMinestom.getDefaultConfig();

    public static void initBlockPlacementRules() {
        if (Boolean.getBoolean(lMinestomConfig.getConfigEntry(LMinestomDefaultValues.BlockPlacementRuleEnabled))) {
            logger.info("Trying to enabled BlockPlacementRules.");
            PlacementRules.init();
            logger.info("Enabled BlockPlacementRules!");
        }
    }
}
