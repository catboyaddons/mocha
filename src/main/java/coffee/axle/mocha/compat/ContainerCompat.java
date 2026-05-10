// SPDX-FileCopyrightText: 2026 Axle Duggan (axlecoffee) <contact@axle.coffee>
//
// SPDX-License-Identifier: LGPL-3.0-only

package coffee.axle.mocha.compat;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.player.Player;
//? if <26 {
/*import net.minecraft.world.inventory.ClickType;
*///?} else {
import net.minecraft.world.inventory.ContainerInput;
//?}

public final class ContainerCompat {
    private ContainerCompat() {}

    public enum ClickKind {
        PICKUP, CLONE, QUICK_MOVE
    }

    public static void handleClick(MultiPlayerGameMode gameMode, int containerId, int slot, int button, ClickKind kind, Player player) {
        //? if <26 {
        /*ClickType mcKind = switch (kind) {
            case PICKUP -> ClickType.PICKUP;
            case CLONE -> ClickType.CLONE;
            case QUICK_MOVE -> ClickType.QUICK_MOVE;
        };
        gameMode.handleInventoryMouseClick(containerId, slot, button, mcKind, player);
        *///?} else {
        ContainerInput mcKind = switch (kind) {
            case PICKUP -> ContainerInput.PICKUP;
            case CLONE -> ContainerInput.CLONE;
            case QUICK_MOVE -> ContainerInput.QUICK_MOVE;
        };
        gameMode.handleContainerInput(containerId, slot, button, mcKind, player);
        //?}
    }
}
