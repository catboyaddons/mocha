// SPDX-License-Identifier: AGPL-3.0-or-later
// SPDX-FileCopyrightText: Axle Duggan <contact@axle.coffee>

package coffee.axle.examplemod

import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object ExampleMod : ModInitializer {

    const val ID = "example_mod"
    val LOGGER = LoggerFactory.getLogger(ID)

    override fun onInitialize() {
        LOGGER.info("ExampleMod initialized")
    }
}
