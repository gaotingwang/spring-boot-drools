package com.gtw.drools.rule;

import com.gtw.drools.model.LoverFact;
import com.gtw.drools.util.KieSessionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.runtime.KieSession;

import java.io.FileNotFoundException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class NoLoopTests {
    private KieSession kieSession;

    @Before
    public void before() throws FileNotFoundException {
        String drlFilePath = this.getClass().getClassLoader().getResource("rules/no-loop.drl").getPath();
        kieSession = KieSessionUtils.buildKieSessionFromFiles(drlFilePath);
    }

    @Test
    public void test(){
        LoverFact loverFact = new LoverFact();
        loverFact.setName("张三");

        kieSession.insert(loverFact);
        int count = kieSession.fireAllRules();
        assertThat(1, equalTo(count));
    }

    @After
    public void after() {
        kieSession.dispose();
    }
}
