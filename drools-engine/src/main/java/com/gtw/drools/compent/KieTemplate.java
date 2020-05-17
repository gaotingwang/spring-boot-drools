package com.gtw.drools.compent;

import java.io.FileNotFoundException;

import com.gtw.drools.util.KieSessionUtils;
import org.kie.api.runtime.KieSession;

/**
 * @author gtw
 */
public class KieTemplate {

    public void dealWithFileRules(Object obj, String... filePaths) throws FileNotFoundException {
        KieSession kieSession = KieSessionUtils.buildKieSessionFromFiles(filePaths);
        kieSession.insert(obj);
        kieSession.fireAllRules();
    }
}
