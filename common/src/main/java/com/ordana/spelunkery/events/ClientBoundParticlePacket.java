package com.ordana.spelunkery.events;

import com.ordana.spelunkery.Spelunkery;
import net.mehvahdjukaar.moonlight.api.platform.network.Message;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;


public class ClientBoundParticlePacket implements Message {

    public static final TypeAndCodec<RegistryFriendlyByteBuf, ClientBoundParticlePacket> CODEC = Message.makeType(
            Spelunkery.res("s2c_send_particle"), ClientBoundParticlePacket::new);

    public final EventType id;
    @Nullable
    public final Vec3 pos;
    @Nullable
    public final Integer extraData;

    public ClientBoundParticlePacket(FriendlyByteBuf buffer) {
        this.id = buffer.readEnum(EventType.class);

        if (buffer.readBoolean()) {
            this.extraData = buffer.readInt();
        } else this.extraData = null;

        if (buffer.readBoolean()) {
            this.pos = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        } else this.pos = null;
    }

    public ClientBoundParticlePacket(BlockPos pos, EventType id) {
        this(Vec3.atCenterOf(pos), id);
    }

    public ClientBoundParticlePacket(Vec3 pos, EventType id) {
        this(pos, id, null);
    }

    public ClientBoundParticlePacket(Vec3 pos, EventType id, Integer extraData) {
        this.pos = pos;
        this.id = id;
        this.extraData = extraData;
    }

    public ClientBoundParticlePacket(Entity entity, EventType id) {
        this.extraData = entity.getId();
        this.id = id;
        this.pos = null;
    }


    @Override
    public void write(RegistryFriendlyByteBuf buffer) {
        buffer.writeEnum(this.id);
        if (extraData != null) {
            buffer.writeBoolean(true);
            buffer.writeInt(extraData);
        } else {
            buffer.writeBoolean(false);
        }
        if (pos != null) {
            buffer.writeBoolean(true);
            buffer.writeDouble(this.pos.x);
            buffer.writeDouble(this.pos.y);
            buffer.writeDouble(this.pos.z);
        } else {
            buffer.writeBoolean(false);
        }
    }

    @Override
    public void handle(Context context) {
        ClientReceivers.handleSpawnBlockParticlePacket(this);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return CODEC.type();
    }

    public enum EventType {
        SULFUR_VENT,
        SLUICE
    }

}