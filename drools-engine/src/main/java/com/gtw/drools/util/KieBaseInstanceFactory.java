package com.gtw.drools.util;

import com.gtw.drools.common.LRUCachePool;
import com.gtw.drools.util.KieSessionUtil.Model;
import org.apache.commons.lang3.StringUtils;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.io.ResourceType;
import org.kie.internal.utils.KieHelper;
import org.springframework.stereotype.Component;

/**
 * 构造KieBase的工厂
 * @author gtw
 */
@Component
public class KieBaseInstanceFactory {
    private static LRUCachePool<String, KieBase> CACHE_POOL = new LRUCachePool<>(512);

    /**
     * 从工厂获取KieBase实例
     * @param ruleKey 规则key
     * @param ruleContent 规则内容
     * @return
     */
    public KieBase getKieBaseInstance(String ruleKey, String ruleContent, Model model) {
        if(StringUtils.isBlank(ruleKey) || StringUtils.isBlank(ruleContent)){
            return null;
        }

        if(CACHE_POOL.get(ruleKey) == null) {
            KieHelper kieHelper = new KieHelper();
            kieHelper.addContent(ruleContent, ResourceType.DRL);
            KieSessionUtil.hasErrorMessage(kieHelper.verify());

            KieBaseConfiguration config = kieHelper.ks.newKieBaseConfiguration();
            if (model.getModel().equalsIgnoreCase(EventProcessingOption.STREAM.getMode())) {
                config.setOption(EventProcessingOption.STREAM);
            } else {
                config.setOption(EventProcessingOption.CLOUD);
            }
            KieBase kieBase = kieHelper.build(config);
            CACHE_POOL.put(ruleKey, kieBase);
        }
        return CACHE_POOL.get(ruleKey);
    }

    /**
     * 清除指定ruleKey对应的KieBase
     * @param ruleKey 规则的key
     */
    public void deleteKieBaseWithRuleKey(String ruleKey) {
        CACHE_POOL.remove(ruleKey);
    }

    /**
     * 清空所有缓存的KieBase
     */
    public void clearKieBase() {
        CACHE_POOL.clear();
    }

}
