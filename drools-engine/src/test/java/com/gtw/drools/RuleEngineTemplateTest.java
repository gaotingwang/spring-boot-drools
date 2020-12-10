package com.gtw.drools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import com.gtw.drools.domain.DecisionTable;
import com.gtw.drools.jpa.impl.DroolsRuleRepositoryJPA;
import com.gtw.drools.model.FeedBackInfo;
import com.gtw.drools.util.KieBaseInstanceFactory;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

class RuleEngineTemplateTest {

    @Test
    void executeWithFacts() {
        RuleEngineTemplate template = new RuleEngineTemplate(new DroolsRuleRepositoryJPA(), new KieBaseInstanceFactory());

        FeedBackInfo feedBackInfo = new FeedBackInfo();
        feedBackInfo.setResponseLevel("VIP");
        feedBackInfo.setModifyPri("S");
        feedBackInfo.setRepeatModifyFlag("1");
        template.executeWithFacts("test", "test", "其他方式", null, feedBackInfo);
        assertThat(feedBackInfo.getFetchPriority(), equalTo(999));
    }

    @Test
    void loadDecisionTable() throws FileNotFoundException {
        RuleEngineTemplate template = new RuleEngineTemplate(new DroolsRuleRepositoryJPA(), new KieBaseInstanceFactory());

        String xlsFilePath = this.getClass().getClassLoader().getResource("./xls/fetch.xls").getPath();
        File file = new File(xlsFilePath);
        InputStream inputStream = new FileInputStream(file);
        DecisionTable table = template.loadDecisionTable(inputStream);
        assertThat(table.getAgendaGroups().toString(), equalTo("[\"其他方式\"]"));
        System.out.println(table);
    }

    @Test
    void testDate() {
        Date date = new Date();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        Date startDate = Date.from(localDateTime.with(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(localDateTime.with(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
        System.out.println(startDate);
        System.out.println(endDate);
    }
}