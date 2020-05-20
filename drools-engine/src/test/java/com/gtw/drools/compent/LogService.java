package com.gtw.drools.compent;

import lombok.extern.slf4j.Slf4j;

/**
 * @author gtw
 */
@Slf4j
public class LogService {

    public static void writeInfoLog(String ruleName, String logContent) {
        log.info("{} is fire , and the result is : {}", ruleName, logContent);
    }
}
