package com.ordana.spelunkery.events;


import com.ordana.spelunkery.Spelunkery;
import net.mehvahdjukaar.moonlight.api.platform.network.Message;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;

public record ClientBoundSendKnockbackPacket(Vec3 knockback, int id) implements Message {

    public static final TypeAndCodec<RegistryFriendlyByteBuf, ClientBoundSendKnockbackPacket> CODEC = Message.makeType(
            Spelunkery.res("s2c_send_knockback"), ClientBoundSendKnockbackPacket::new);

    public ClientBoundSendKnockbackPacket(RegistryFriendlyByteBuf buf) {
        this(buf.readVec3(), buf.readVarInt());
    }

    @Override
    public void write(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        registryFriendlyByteBuf.writeVarInt(this.id);
        registryFriendlyByteBuf.writeVec3(this.knockback);
    }

    @Override
    public void handle(Context context) {
        // client world
        ClientReceivers.handleSendBombKnockbackPacket(this);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return CODEC.type();
    }
}