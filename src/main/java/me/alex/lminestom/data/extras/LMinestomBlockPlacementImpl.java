package me.alex.lminestom.data.extras;

import me.alex.lminestom.data.config.LMinestomDefaultValues;
import me.alex.lminestom.start.LMinestom;
import net.minestom.server.extras.PlacementRules;
import org.slf4j.Logger;

public class LMinestomBlockPlacementImpl {

    private static final Logger logger = LMinestom.getMainLogger();

    public static void initBlockPlacementRules() {
        if (Boolean.getBoolean(LMinestomDefaultValues.BlockPlacementRuleEnabled.getIdentifier())) {
            logger.info("Trying to enabled BlockPlacementRules.");
            PlacementRules.init();
            logger.info("Enabled BlockPlacementRules!");
        }
    }
}
