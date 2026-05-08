// SPDX-FileCopyrightText: 2026 Axle Duggan (axlecoffee) <contact@axle.coffee>
//
// SPDX-License-Identifier: LGPL-3.0-only

package coffee.axle.mocha;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mocha implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("mocha");

    @Override
    public void onInitialize() {
        LOGGER.info("[Mocha] one venti minty mocha - starbucks 2024");
    }
}
