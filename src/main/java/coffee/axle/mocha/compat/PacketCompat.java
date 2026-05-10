package coffee.axle.mocha.compat;

import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class PacketCompat {

    public static ServerboundInteractPacket interactAt(Entity entity, boolean sneaking, InteractionHand hand, Vec3 hitVec) {
        //? if <26 {
        /*return ServerboundInteractPacket.createInteractionPacket(entity, sneaking, hand, hitVec);*/
        //?} else {
        return new ServerboundInteractPacket(entity.getId(), hand, hitVec, sneaking);
        //?}
    }

    public static ServerboundInteractPacket interact(Entity entity, boolean sneaking, InteractionHand hand) {
        //? if <26 {
        /*return ServerboundInteractPacket.createInteractionPacket(entity, sneaking, hand);*/
        //?} else {
        return new ServerboundInteractPacket(entity.getId(), hand, Vec3.ZERO, sneaking);
        //?}
    }
}
