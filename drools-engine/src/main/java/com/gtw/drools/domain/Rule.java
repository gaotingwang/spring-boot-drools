package com.gtw.drools.domain;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * 规则对象
 * @author gtw
 */
@Data
@NoArgsConstructor
public class Rule {
    /**
     * 唯一标识
     */
    private Long id;
    /**
     * 规则所属业务
     */
    private String business;
    /**
     * 规则名称
     */
    private String name;
    /**
     * 规则内容
     */
    private String content;
    /**
     * 规则生成时间
     */
    private Date insertTime;

    public Rule(String business, String name, String content) {
        this.business = business;
        this.name = name;
        this.content = content;
    }

    /**
     * 校验规则内容是否合法
     * @return 规则是否合法
     */
    public boolean verify() {
        if (StringUtils.isBlank(this.content)) {
            return false;
        }
        // TODO 完善对规则内容的校验
        return true;
    }
}
