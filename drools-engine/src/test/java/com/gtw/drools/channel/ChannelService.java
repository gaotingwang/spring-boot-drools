package com.gtw.drools.channel;

import org.kie.api.runtime.Channel;

public class ChannelService implements Channel {
    @Override
    public void send(Object obj) {
        System.out.println("It's channel service, send method : " + obj.toString());
    }
}
