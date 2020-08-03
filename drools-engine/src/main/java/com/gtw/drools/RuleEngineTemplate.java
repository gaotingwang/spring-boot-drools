package com.gtw.drools;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.gtw.drools.domain.DecisionTable;
import com.gtw.drools.domain.Rule;
import com.gtw.drools.excepiton.RuleException;
import com.gtw.drools.repository.IDroolsRuleRepository;
import com.gtw.drools.util.KieSessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.drools.decisiontable.InputType;
import org.drools.decisiontable.parser.DefaultRuleSheetListener;
import org.drools.decisiontable.parser.RuleSheetListener;
import org.drools.template.model.Package;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 规则操作
 * @author gtw
 */
@Component
@Slf4j
public class RuleEngineTemplate {

    private final IDroolsRuleRepository iDroolsRuleRepository;

    public RuleEngineTemplate(IDroolsRuleRepository iDroolsRuleRepository) {
        this.iDroolsRuleRepository = iDroolsRuleRepository;
    }

    // TODO @PostConstruct规则预加载

    /**
     * 规则持久化
     * @param rule 规则
     */
    @Transactional(rollbackFor = Exception.class)
    public void writeRule(Rule rule) {
        if(rule.verify()) {
            iDroolsRuleRepository.save(rule);
        } else {
            throw new RuleException(rule.getName() + "：规则内容不正确，无法存储！");
        }
    }

    /**
     * 根据规则场景和名称加载规则
     * @param business 规则场景
     * @param ruleName 规则名称
     */
    public Rule loadRule(String business, String ruleName) {
        if(StringUtils.isBlank(business) || StringUtils.isBlank(ruleName)) {
            throw new RuleException("规则场景或名称为空");
        }
        // TODO 从缓存中加载
        return iDroolsRuleRepository.findRule(business, ruleName);
    }

    /**
     * 对对象执行指定的规则
     * @param business 规则场景
     * @param ruleName 要执行的规则名称
     * @param agendaGroup 规则中的事项组
     * @param facts 要执行规则的对象集合
     * @param globals 全局变量
     */
    public void executeWithFacts(String business, String ruleName, String agendaGroup, Map<String, Object> globals, Object... facts) {
        Rule rule = this.loadRule(business, ruleName);
        if(rule == null) {
            throw new RuleException(ruleName + "规则不存在");
        }

        KieSession kieSession = KieSessionUtil.buildKieSessionFromRuleString(Collections.singletonList(rule.getContent()));
        try {
            // 默认注入log功能
            kieSession.setGlobal("log", log);
            if (globals != null) {
                globals.keySet().forEach(key -> kieSession.setGlobal(key, globals.get(key)));
            }

            for(Object obj : facts) {
                kieSession.insert(obj);
            }

            if(StringUtils.isNotBlank(agendaGroup)) {
                kieSession.getAgenda().getAgendaGroup(agendaGroup).setFocus();
            }
            kieSession.fireAllRules();
        } finally {
            kieSession.dispose();
        }
    }

    /**
     * 从决策表文件流加载决策表内容
     * @param inputStream 决策表文件流
     * @return 决策表内容
     */
    public DecisionTable loadDecisionTable(InputStream inputStream) {
        RuleSheetListener listener = new DefaultRuleSheetListener();
        String ruleContent = KieSessionUtil.getRuleString(inputStream, InputType.XLS, listener);

        Set<String> agendaGroups = new HashSet<>();
        Package rulePackage = listener.getRuleSet();
        for (org.drools.template.model.Rule rule : rulePackage.getRules()) {
            String agendaGroup = rule.getAttribute("agenda-group");
            agendaGroups.add(agendaGroup);
        }

        return new DecisionTable(ruleContent, agendaGroups);
    }

}
