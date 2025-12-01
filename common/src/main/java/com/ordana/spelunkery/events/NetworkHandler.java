package com.ordana.spelunkery.events;

import net.mehvahdjukaar.moonlight.api.platform.network.NetworkHelper;

public class NetworkHandler {

    public static void registerMessages() {
        NetworkHelper.addNetworkRegistration(registerMessagesEvent -> {
             registerMessagesEvent.registerClientBound(ClientBoundParticlePacket.CODEC);
            registerMessagesEvent.registerClientBound(ClientBoundSendKnockbackPacket.CODEC);
        }, 1);
    }

}