package com.gtw.drools.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.drools.decisiontable.InputType;
import org.drools.decisiontable.SpreadsheetCompiler;
import org.drools.decisiontable.parser.DefaultRuleSheetListener;
import org.drools.decisiontable.parser.RuleSheetListener;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.utils.KieHelper;

/**
 * KieSession 获取工具类
 * @author gtw
 */
@Slf4j
public class KieSessionUtil {
    /**
     * 规则文件DRL后缀
     */
    public static final String SUFFIX_DRL = "drl";
    /**
     * 规则文件CSV后缀
     */
    public static final String SUFFIX_CSV = "csv";
    /**
     * 规则文件EXCEL后缀
     */
    public static final String SUFFIX_XLS = "xls";
    /**
     * 规则文件EXCEL后缀
     */
    public static final String SUFFIX_XLSX = "xlsx";

    /**
     * 扫描resources目录下的所有DRL文件生成KieSession
     * @return KieSession
     */
    public static KieSession buildDefaultKieSession() {
        KieServices kieServices = KieServices.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        return kieContainer.newKieSession();
    }

    /**
     * 从规则文件（DRL,EXCEL,CSV）生成KieSession
     * @param filePath 规则文件的绝对路径
     * @return KieSession
     */
    public static KieSession buildKieSessionFromFile(String filePath) throws FileNotFoundException {
        String ruleKey = "rule_" + filePath;
        String rule = getRuleFromFile(filePath);
        return buildKieSessionFromRuleString(ruleKey, rule);
    }

    /**
     * 从规则文件（DRL,EXCEL,CSV）生成StatelessKieSession
     * @param filePath 规则文件的绝对路径
     * @return StatelessKieSession
     */
    public static StatelessKieSession buildStatelessKieSessionFromFile(String filePath) throws FileNotFoundException {
        String ruleKey = "rule_" + filePath;
        String rule = getRuleFromFile(filePath);
        return buildStatelessKieSessionFromRuleString(ruleKey, rule);
    }

    /**
     * 根据规则字符串生成KieSession
     * @param ruleKey 规则的key值
     * @param ruleContent 规则内容字符串
     * @return KieSession
     */
    public static KieSession buildKieSessionFromRuleString(String ruleKey, String ruleContent) {
        // TODO Model 模式了解，这里写死了
        KieBase kieBase = decodeToKieBase(ruleContent, Model.STREAM);
        return kieBase.newKieSession();
    }

    /**
     * 根据规则字符串生成StatelessKieSession
     * @param ruleKey 规则的key值
     * @param ruleContent 规则内容字符串
     * @return StatelessKieSession
     */
    public static StatelessKieSession buildStatelessKieSessionFromRuleString(String ruleKey, String ruleContent) {
        // TODO Model 模式了解，这里写死了
        KieBase kieBase = decodeToKieBase(ruleContent, Model.STREAM);
        return kieBase.newStatelessKieSession();
    }

    /**
     * 解析多个规则文件生成规则字符串集合
     * @param filePath 规则文件路径
     * @return 规则字符串集合
     */
    public static String getRuleFromFile(String filePath) throws FileNotFoundException {
        return getRuleString(filePath);
    }

    /**
     * 从文件流内容中解析出规则字符串
     * @param inputStream 决策表文件流
     * @param inputType CSV / XLS
     * @param listener 决策表监听对象
     * @return 决策表中对应的规则字符串
     */
    public static String getRuleString(InputStream inputStream, InputType inputType, RuleSheetListener listener) {
        SpreadsheetCompiler compiler = new SpreadsheetCompiler();
        return compiler.compile(inputStream, inputType, listener);
    }

    /**
     * 根据规则字符串生成KieBase
     * @param ruleContent 规则内容字符串
     * @param model
     * @return KieBase
     */
    private static KieBase decodeToKieBase(String ruleContent, Model model) {
        KieHelper kieHelper = new KieHelper();
        kieHelper.addContent(ruleContent, ResourceType.DRL);
        hasErrorMessage(kieHelper.verify());

        KieBaseConfiguration config = kieHelper.ks.newKieBaseConfiguration();
        if (model.getModel().equalsIgnoreCase(EventProcessingOption.STREAM.getMode())) {
            config.setOption(EventProcessingOption.STREAM);
        } else {
            config.setOption(EventProcessingOption.CLOUD);
        }
        return kieHelper.build(config);
    }

    /**
     * 将文件中规则内容解析成字符串
     * @param filePath 文件路径
     * @return 规则内容字符串
     */
    private static String getRuleString(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        // 为DRL文件
        if (filePath.toLowerCase().endsWith(SUFFIX_DRL)) {
            return transDrlFile2String(file);
        }

        InputType inputType = null;
        // 默认为xls文件，结尾为csv，则改为csv处理方式
        if (filePath.toLowerCase().endsWith(SUFFIX_CSV)) {
            inputType = InputType.CSV;
        }
        // 默认为xls文件，结尾为csv，则改为csv处理方式
        if (filePath.toLowerCase().endsWith(SUFFIX_XLS) || filePath.toLowerCase().endsWith(SUFFIX_XLSX)) {
            inputType = InputType.XLS;
        }
        if (null == inputType) {
            throw new IllegalArgumentException(file.getName() + "文件格式不正确");
        }

        InputStream inputStream = new FileInputStream(file);
        String drl = getRuleString(inputStream, inputType, new DefaultRuleSheetListener());
        log.info("{} file rule string is : {}", file.getName(), drl);
        return drl;
    }

    /**
     * 将DRL文件解析成字符串
     * @param file DRL文件
     */
    private static String transDrlFile2String(File file) {
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, length);
            }
            return bos.toString();
        } catch (IOException e) {
            throw new IllegalStateException(file.getName() + " transDrlFile2String fail ", e);
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 检查编译结果
     * @param results 编译结果
     */
    public static void hasErrorMessage(Results results) {
        if (results.hasMessages(Message.Level.WARNING, Message.Level.ERROR)) {
            List<Message> messages = results.getMessages(Message.Level.WARNING, Message.Level.ERROR);
            for (Message message : messages) {
                if (Message.Level.ERROR == message.getLevel()) {
                    log.error("build Kie Warning: {}", message.getText());
                    throw new IllegalStateException("Build Kie errors were found, Check the logs.");
                } else {
                    log.warn("build Kie Warning: {}", message.getText());
                }
            }
        }
    }

    public enum Model {
        CLOUD("cloud"),
        STREAM("stream");

        private String model;

        public String getModel() {
            return model;
        }

        Model(String model) {
            this.model = model;
        }
    }


    private KieSessionUtil() {
    }

}
