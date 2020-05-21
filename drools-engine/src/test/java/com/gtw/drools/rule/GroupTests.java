package com.gtw.drools.rule;

import com.gtw.drools.model.LoverFact;
import com.gtw.drools.util.KieSessionUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.runtime.KieSession;

import java.io.FileNotFoundException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class GroupTests {
    private KieSession kieSession;

    @Before
    public void before() throws FileNotFoundException {
        String drlFilePath = this.getClass().getClassLoader().getResource("rules/group-rule.drl").getPath();
        kieSession = KieSessionUtil.buildKieSessionFromFiles(drlFilePath);
    }

    @Test
    public void test(){
        LoverFact loverFact = new LoverFact();
        loverFact.setName("AAA");

        kieSession.insert(loverFact);
        // 除了focus的agenda会执行，其他非agenda的规则也会执行
        kieSession.getAgenda().getAgendaGroup("aaa").setFocus();
        int fireCount = kieSession.fireAllRules();
        assertThat(3, equalTo(fireCount));


        // 相同Fact仅会触发规则一次，不会多次触发规则
        // 已执行过的规则不会再执行，只有新规则focus会被执行
        kieSession.getAgenda().getAgendaGroup("bbb").setFocus();
        fireCount = kieSession.fireAllRules();
        assertThat(1, equalTo(fireCount));
    }

    @After
    public void after() {
        kieSession.dispose();
    }
}
