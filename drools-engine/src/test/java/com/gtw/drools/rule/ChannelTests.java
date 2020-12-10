package com.gtw.drools.rule;

import java.io.FileNotFoundException;

import com.gtw.drools.channel.ChannelService;
import com.gtw.drools.model.LoverFact;
import com.gtw.drools.util.KieSessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.runtime.KieSession;

@Slf4j
public class ChannelTests {
    private KieSession kieSession;

    @Before
    public void before() throws FileNotFoundException {
        String drlFilePath = this.getClass().getClassLoader().getResource("rules/channel-rule.drl").getPath();
        kieSession = KieSessionUtil.buildKieSessionFromFile(drlFilePath);
    }

    @Test
    public void test1(){
        LoverFact loverFact = new LoverFact();
        loverFact.setName("AAA");

        // 注册channel
        kieSession.registerChannel("channelService", new ChannelService());
        kieSession.insert(loverFact);
        kieSession.fireAllRules();
    }

    @After
    public void after() {
        kieSession.dispose();
    }
}
