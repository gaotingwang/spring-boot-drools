package com.gtw.drools.rule;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import com.gtw.drools.model.LoverFact;
import com.gtw.drools.model.ScoreInfo;
import com.gtw.drools.util.KieSessionUtil;
import org.junit.Test;
import org.kie.api.runtime.KieSession;

public class BuildKieSessionTests {

    @Test
    public void test_load_all_rules(){
        // 扫描resources目录下的所有DRL文件生成KieSession
        // rule01.drl和rule03.drl都会被扫出来
        KieSession kieSession = KieSessionUtil.buildDefaultKieSession();
        kieSession.insert(100d);
        kieSession.fireAllRules();
    }

    @Test
    public void test_read_xls() throws FileNotFoundException {
        Map<String, String> amountMap = new HashMap<>();
        ScoreInfo info = new ScoreInfo();
        info.setCount(10);

        String xlsFilePath = this.getClass().getClassLoader().getResource("./xls/score_sign.xls").getPath();
        KieSession kieSession = KieSessionUtil.buildKieSessionFromFile(xlsFilePath);
        kieSession.getAgenda().getAgendaGroup("score_sign").setFocus();
        kieSession.insert(info);
        kieSession.setGlobal("amountMap", amountMap);
        kieSession.fireAllRules();

        System.out.println("评估规则ok");
        System.out.println("获得积分奖励:" + amountMap.get("score"));
        System.out.println("获得美金奖励:" + amountMap.get("coupon"));
    }

    @Test
    public void test_read_drl() throws FileNotFoundException {
        String xlsFilePath = this.getClass().getClassLoader().getResource("drools/rule02.drl").getPath();
        KieSession kieSession = KieSessionUtil.buildKieSessionFromFile(xlsFilePath);
        LoverFact loverFact = new LoverFact();
        loverFact.setName("aaa");
        kieSession.insert(loverFact);
        kieSession.fireAllRules();
    }

    @Test
    public void test_read_files() throws FileNotFoundException {
        Map<String, String> amountMap = new HashMap<>();
        ScoreInfo info = new ScoreInfo();
        info.setCount(10);
        LoverFact loverFact = new LoverFact();
        loverFact.setName("aaa");

        String xlsFilePath = this.getClass().getClassLoader().getResource("./xls/score_sign.xls").getPath();
        //String drlFilePath = this.getClass().getClassLoader().getResource("drools/rule02.drl").getPath();
        KieSession kieSession = KieSessionUtil.buildKieSessionFromFile(xlsFilePath);
        kieSession.getAgenda().getAgendaGroup("score_sign").setFocus();
        kieSession.insert(info);
        kieSession.insert(loverFact);
        kieSession.setGlobal("amountMap", amountMap);
        kieSession.fireAllRules();

        System.out.println("评估规则ok");
        System.out.println("获得积分奖励:" + amountMap.get("score"));
        System.out.println("获得美金奖励:" + amountMap.get("coupon"));
    }

    //@Test
    //public void test03() {
    //    KieSession kieSession = KieSessionUtils.buildKieSessionFromClasspath("./drools/rule02.drl");
    //    LoverFact loverFact = new LoverFact();
    //    loverFact.setName("aaa");
    //    kieSession.insert(loverFact);
    //    kieSession.fireAllRules();
    //}

}
