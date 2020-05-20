package com.gtw.drools.rule;

import java.io.FileNotFoundException;

import com.gtw.drools.model.LoverFact;
import com.gtw.drools.util.KieSessionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class FunctionTests {

    private KieSession kieSession;

    @Before
    public void before() throws FileNotFoundException {
        String drlFilePath = this.getClass().getClassLoader().getResource("rules/function-rule.drl").getPath();
        kieSession = KieSessionUtils.buildKieSessionFromFiles(drlFilePath);
    }

    @Test
    public void test() {
        LoverFact loverFact1 = new LoverFact();
        loverFact1.setName("张三");

        kieSession.insert(loverFact1);

        int fireCount = kieSession.fireAllRules();
        assertThat(1, equalTo(fireCount));
    }

    @After
    public void after() {
        kieSession.dispose();
    }
}
