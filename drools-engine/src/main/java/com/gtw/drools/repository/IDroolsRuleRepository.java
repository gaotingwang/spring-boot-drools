package com.gtw.drools.repository;

import java.util.List;

import com.gtw.drools.domain.Rule;

public interface IDroolsRuleRepository {

    /**
     * 保存规则
     * @param rule 规则
     */
    void save(Rule rule);

    /**
     * 根据规则场景和名称查找规则
     * @param business 规则场景
     * @param ruleName 规则名称
     * @return 规则名称对应的规则
     */
    Rule findRule(String business, String ruleName);

    /**
     * 业务场景下的规则
     * @param business 业务场景名称
     * @return 业务场景下的规则
     */
    List<Rule> findRuleByBusiness(String business);
}
