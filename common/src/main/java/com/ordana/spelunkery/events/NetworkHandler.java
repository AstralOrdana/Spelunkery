package com.ordana.spelunkery.events;

import com.ordana.spelunkery.Spelunkery;
import net.mehvahdjukaar.moonlight.api.platform.network.ChannelHandler;
import net.mehvahdjukaar.moonlight.api.platform.network.NetworkDir;

public class NetworkHandler {

    public static final ChannelHandler CHANNEL = ChannelHandler.createChannel(Spelunkery.res("network"));

    public static void registerMessages() {
        CHANNEL.register(NetworkDir.PLAY_TO_CLIENT,
                ClientBoundSendKnockbackPacket.class, ClientBoundSendKnockbackPacket::new);

        CHANNEL.register(NetworkDir.PLAY_TO_CLIENT,
                ClientBoundParticlePacket.class, ClientBoundParticlePacket::new);
    }

}