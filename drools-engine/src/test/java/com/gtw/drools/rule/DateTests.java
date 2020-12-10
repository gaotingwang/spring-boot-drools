package com.gtw.drools.rule;

import com.gtw.drools.util.KieSessionUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.runtime.KieSession;

import java.io.FileNotFoundException;

public class DateTests {
    private KieSession kieSession;

    @Before
    public void before() throws FileNotFoundException {
        String drlFilePath = this.getClass().getClassLoader().getResource("rules/date-rule.drl").getPath();

        // 必须在获取Session前设置时间格式
        System.setProperty("drools.dateformat", "yyyy-MM-dd");
        kieSession = KieSessionUtil.buildKieSessionFromFile(drlFilePath);
    }

    @Test
    public void test(){
        kieSession.fireAllRules();
    }

    @After
    public void after() {
        kieSession.dispose();
    }
}
