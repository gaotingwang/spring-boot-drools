package com.gtw.drools.rule;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.gtw.drools.model.Coupon;
import com.gtw.drools.model.Customer;
import com.gtw.drools.model.Item;
import com.gtw.drools.model.LoverFact;
import com.gtw.drools.model.Order;
import com.gtw.drools.model.OrderLine;
import com.gtw.drools.model.types.IsGoldCustomer;
import com.gtw.drools.model.types.IsLowRangeItem;
import com.gtw.drools.util.KieSessionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.ObjectFilter;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class QueryTests {

    private KieSession kieSession;

    @Before
    public void before() throws FileNotFoundException {
        String drlFilePath = this.getClass().getClassLoader().getResource("rules/query-rule.drl").getPath();
        kieSession = KieSessionUtils.buildKieSessionFromFiles(drlFilePath);
    }

    @Test
    public void test() {
        LoverFact loverFact1 = new LoverFact();
        loverFact1.setAge(20);
        LoverFact loverFact2 = new LoverFact();
        loverFact2.setAge(19);

        // 通过insert放入working memory中
        kieSession.insert(loverFact1);
        kieSession.insert(loverFact2);

        // 查询working memory中符合约束条件的Fact对象
        QueryResults queryResults = kieSession.getQueryResults("query-by-name", 20);
        assertThat(1, equalTo(queryResults.size()));
        System.out.println("query the fact size is : " + queryResults.size());

        for (QueryResultsRow row : queryResults) {
            LoverFact fact = (LoverFact)row.get("$lover");
            System.out.println("the fact age is : " + fact.getAge());
        }

        int fireCount = kieSession.fireAllRules();
        assertThat(2, equalTo(fireCount));
    }

    @After
    public void after() {
        kieSession.dispose();
    }
}
