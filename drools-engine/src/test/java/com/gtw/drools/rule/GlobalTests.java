package com.gtw.drools.rule;

import java.io.FileNotFoundException;

import com.gtw.drools.model.LoverFact;
import com.gtw.drools.util.KieSessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.runtime.KieSession;

@Slf4j
public class GlobalTests {
    private KieSession kieSession;

    @Before
    public void before() throws FileNotFoundException {
        String drlFilePath = this.getClass().getClassLoader().getResource("rules/global-rule.drl").getPath();
        kieSession = KieSessionUtil.buildKieSessionFromFiles(drlFilePath);
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

        // 服务化Global使用方式,例如引入日志框架
        kieSession.setGlobal("log", log);
        kieSession.insert(loverFact);
        kieSession.fireAllRules();
    }

    @After
    public void after() {
        kieSession.dispose();
    }
}
