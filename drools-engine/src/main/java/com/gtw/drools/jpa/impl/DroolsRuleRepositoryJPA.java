package com.gtw.drools.jpa.impl;

import java.util.ArrayList;
import java.util.List;

import com.gtw.drools.domain.Rule;
import com.gtw.drools.repository.IDroolsRuleRepository;
import org.springframework.stereotype.Repository;

/**
 * @author gtw
 */
@Repository
public class DroolsRuleRepositoryJPA implements IDroolsRuleRepository {

    @Override
    public void save(Rule rule) {
        // todo 需要做rule的持久化
        System.out.println("需要做rule的持久化");
    }

    @Override
    public Rule findRule(String business, String ruleName) {
        // TODO 从缓存中和持久化中加载
        // todo 返回 rule
        return new Rule(business, ruleName, "package com.group.feedback.fetch;\n"
            + "//generated from Decision Table\n"
            + "import com.gtw.drools.model.FeedBackInfo;\n"
            + "global org.slf4j.Logger log;\n"
            + "// rule values at B12, header at B7\n"
            + "rule \"fetch-rule_12\"\n"
            + "\tsalience 65535\n"
            + "\tagenda-group \"其他方式\"\n"
            + "\twhen\n"
            + "\t\t$feedback : FeedBackInfo(responseLevel == \"VIP\", modifyPri == \"S\", repeatModifyFlag == \"1\")\n"
            + "\tthen\n"
            + "\t\t$feedback.setFetchPriority(999);\n"
            + "delete($feedback);\n"
            + "end\n"
            + "\n"
            + "// rule values at B13, header at B7\n"
            + "rule \"fetch-rule_13\"\n"
            + "\tsalience 65534\n"
            + "\tagenda-group \"其他方式\"\n"
            + "\twhen\n"
            + "\t\t$feedback : FeedBackInfo(responseLevel == \"S\", repeatModifyFlag == \"0\")\n"
            + "\tthen\n"
            + "\t\t$feedback.setFetchPriority(998);\n"
            + "delete($feedback);\n"
            + "end\n"
            + "\n"
            + "// rule values at B14, header at B7\n"
            + "rule \"fetch-rule_14\"\n"
            + "\tsalience 65533\n"
            + "\tagenda-group \"其他方式\"\n"
            + "\twhen\n"
            + "\t\t$feedback : FeedBackInfo(responseLevel == \"A\", modifyPri == \"A\")\n"
            + "\tthen\n"
            + "\t\t$feedback.setFetchPriority(997);\n"
            + "delete($feedback);\n"
            + "end\n"
            + "\n"
            + "// rule values at B15, header at B7\n"
            + "rule \"fetch-rule_15\"\n"
            + "\tsalience 65532\n"
            + "\tagenda-group \"其他方式\"\n"
            + "\twhen\n"
            + "\t\t$feedback : FeedBackInfo(responseLevel == \"A\", repeatModifyFlag == \"1\")\n"
            + "\tthen\n"
            + "\t\t$feedback.setFetchPriority(996);\n"
            + "delete($feedback);\n"
            + "end\n");
    }

    @Override
    public List<Rule> findRuleByBusiness(String business) {
       return new ArrayList<>();
    }
}
