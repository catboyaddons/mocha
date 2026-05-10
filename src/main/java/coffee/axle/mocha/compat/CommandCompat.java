// SPDX-FileCopyrightText: 2026 Axle Duggan (axlecoffee) <contact@axle.coffee>
//
// SPDX-License-Identifier: LGPL-3.0-only

package coffee.axle.mocha.compat;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
//? if <26 {
/*import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
*///?} else {
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
//?}
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public final class CommandCompat {
    private CommandCompat() {}

    public static LiteralArgumentBuilder<FabricClientCommandSource> literal(String name) {
        //? if <26 {
        /*return ClientCommandManager.literal(name);
        *///?} else {
        return ClientCommands.literal(name);
        //?}
    }

    public static <T> RequiredArgumentBuilder<FabricClientCommandSource, T> argument(String name, ArgumentType<T> type) {
        //? if <26 {
        /*return ClientCommandManager.argument(name, type);
        *///?} else {
        return ClientCommands.argument(name, type);
        //?}
    }
}
