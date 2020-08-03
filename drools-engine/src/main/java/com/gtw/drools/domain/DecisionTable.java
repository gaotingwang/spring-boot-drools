package com.gtw.drools.domain;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 决策表
 * @author gtw
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DecisionTable {
    /**
     * 决策表解析后的规则内容
     */
    String ruleContent;

    /**
     * 规则中的AgendaGroup集合
     */
    Set<String> agendaGroups;
}
