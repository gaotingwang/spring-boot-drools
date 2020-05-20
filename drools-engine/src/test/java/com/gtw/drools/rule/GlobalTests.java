package com.gtw.drools.rule;

import java.io.FileNotFoundException;

import com.gtw.drools.compent.LogService;
import com.gtw.drools.model.LoverFact;
import com.gtw.drools.util.KieSessionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.runtime.KieSession;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class GlobalTests {
    private KieSession kieSession;

    @Before
    public void before() throws FileNotFoundException {
        String drlFilePath = this.getClass().getClassLoader().getResource("rules/global-rule.drl").getPath();
        kieSession = KieSessionUtils.buildKieSessionFromFiles(drlFilePath);
    }

    @Test
    public void test1(){
        LoverFact loverFact = new LoverFact();
        loverFact.setName("AAA");

        kieSession.insert(loverFact);
        kieSession.fireAllRules();
    }

    @Test
    public void test2(){
        LoverFact loverFact = new LoverFact();
        loverFact.setName("BBB");

        LogService logService = new LogService();

        // 服务化Global使用方式
        kieSession.setGlobal("logService", logService);
        kieSession.insert(loverFact);
        kieSession.fireAllRules();
    }

    @After
    public void after() {
        kieSession.dispose();
    }
}
