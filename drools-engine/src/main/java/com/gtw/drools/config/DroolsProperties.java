package com.gtw.drools.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author gtw
 */
@Data
@ConfigurationProperties(prefix = "spring.drools")
public class DroolsProperties {

    /**
     * Drools使用开关
     */
    private boolean enable = false;

    /**
     * 规则文件和决策表目录，多个目录使用逗号分割
     */
    private String path;

    /**
     * 轮询周期 - 单位：秒
     */
    private Long update;

    /**
     * 模式，stream 或 cloud
     */
    private String mode;

}
