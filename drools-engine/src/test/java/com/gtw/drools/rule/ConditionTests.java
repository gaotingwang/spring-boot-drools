package com.gtw.drools.rule;

import java.io.FileNotFoundException;

import com.gtw.drools.model.LoverFact;
import com.gtw.drools.util.KieSessionUtil;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.runtime.KieSession;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ConditionTests {
    private KieSession kieSession;

    @Before
    public void before() throws FileNotFoundException {
        String drlFilePath = this.getClass().getClassLoader().getResource("rules/condition-rule.drl").getPath();
        kieSession = KieSessionUtil.buildKieSessionFromFile(drlFilePath);
    }

    @Test
    public void test1(){
        LoverFact loverFact = new LoverFact();
        loverFact.setAge(19);
        loverFact.setName("李四");

        kieSession.insert(loverFact);
        int fireCount = kieSession.fireAllRules();
        assertThat(3, equalTo(fireCount));
    }

    @Test
    public void test2(){
        LoverFact loverFact = new LoverFact();
        loverFact.setAge(20);
        loverFact.setName("李四");

        kieSession.insert(loverFact);
        int fireCount = kieSession.fireAllRules();
        assertThat(2, equalTo(fireCount));
    }

}
