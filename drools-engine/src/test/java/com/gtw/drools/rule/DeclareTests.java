package com.gtw.drools.rule;

import java.io.FileNotFoundException;

import com.gtw.drools.util.KieSessionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.definition.type.FactType;
import org.kie.api.runtime.KieSession;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class DeclareTests {
    private KieSession kieSession;

    @Before
    public void before() throws FileNotFoundException {
        String drlFilePath = this.getClass().getClassLoader().getResource("rules/declare-rule.drl").getPath();
        kieSession = KieSessionUtils.buildKieSessionFromFiles(drlFilePath);
    }

    @Test
    public void test_use_declare() throws Exception {
        KieBase kieBase = kieSession.getKieBase();
        // 使用规则文件中declare的对象
        FactType factType = kieBase.getFactType("rules", "Country");
        Object country = factType.newInstance();
        factType.set(country, "name", "阿根廷");

        kieSession.insert(country);
        int count = kieSession.fireAllRules();
        assertThat(4, equalTo(count));
    }

    @After
    public void after() {
        kieSession.dispose();
    }
}
