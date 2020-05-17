package com.gtw.drools.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.drools.decisiontable.InputType;
import org.drools.decisiontable.SpreadsheetCompiler;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.event.rule.DebugRuleRuntimeEventListener;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.utils.KieHelper;

/**
 * 生成KieSession工具类
 * @author gtw
 */
@Slf4j
public class KieSessionUtils {
    /**
     * 规则文件后缀
     */
    public static final String SUFFIX_DRL = "drl";
    /**
     * CSV后缀
     */
    public static final String SUFFIX_CSV = "csv";

    /**
     * 扫描resources目录下的所有DRL文件生成KieSession
     * @return KieSession
     */
    public static KieSession buildDefaultKieSession() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        return kieContainer.newKieSession();
    }

    public static KieSession buildKieSessionFromFiles(String... filePaths) throws FileNotFoundException {
        List<String> rules = new ArrayList<>();
        for (String filePath : filePaths) {
            rules.add(getDrlString(filePath));
        }
        // TODO Model 模式了解，这里写死了
        return decodeToSession(Model.STREAM, rules);
    }

    /**
     * 根据XLS文件生成KieSession
     * @param filePath 决策表文件路径
     * @return KieSession
     */
    public static KieSession buildKieSessionFromFile(String filePath) throws FileNotFoundException {
        return buildKieSessionFromDrlString(getDrlString(filePath));
    }

    /**
     * 解析DRL字符串生成KieSession
     * @param drl DRL字符串
     * @return KieSession
     */
    public static KieSession buildKieSessionFromDrlString(String drl) {
        KieHelper kieHelper = new KieHelper();
        kieHelper.addContent(drl, ResourceType.DRL);
        hasErrorMessage(kieHelper.verify());
        KieBaseConfiguration config = kieHelper.ks.newKieBaseConfiguration();
        config.setOption(EventProcessingOption.STREAM);
        return kieHelper.build().newKieSession();
    }

    /**
     * 把字符串解析成KieSession
     * @param model
     * @param drls 规则文件字符串
     * @return  KieSession
     */
    public static KieSession decodeToSession(Model model, List<String> drls) {
        KieHelper kieHelper = new KieHelper();
        for (String drl : drls) {
            kieHelper.addContent(drl, ResourceType.DRL);
        }
        hasErrorMessage(kieHelper.verify());

        KieBaseConfiguration config = kieHelper.ks.newKieBaseConfiguration();
        if (model.getModel().equalsIgnoreCase(EventProcessingOption.STREAM.getMode())) {
            config.setOption(EventProcessingOption.STREAM);
        } else {
            config.setOption(EventProcessingOption.CLOUD);
        }
        KieBase kieBase = kieHelper.build(config);
        return kieBase.newKieSession();
    }

    ///**
    // * 扫描resources目录下指定位置的DRL文件生成有状态KieSession
    // * @param classPath resources 指定目录下的DRL文件路径
    // * @return KieSession
    // */
    //public static KieSession buildKieSessionFromClasspath(String classPath) {
    //    KieServices kieServices = KieServices.Factory.get();
    //    Resource resource = kieServices.getResources().newClassPathResource(classPath);
    //
    //    KieSession kieSession = getKieBase(resource).newKieSession();
    //    kieSession.addEventListener(new DebugRuleRuntimeEventListener());
    //    return kieSession;
    //}
    //
    ///**
    // * 扫描resources目录下指定位置的DRL文件生成无状态KieSession
    // * @param classPath resources下指定文件目录地址
    // * @return StatelessKieSession
    // */
    //public static StatelessKieSession buildStatelessKieSessionFromClasspath(String classPath) {
    //    KieServices kieServices = KieServices.Factory.get();
    //    Resource resource = kieServices.getResources().newClassPathResource(classPath);
    //    return getKieBase(resource).newStatelessKieSession();
    //}
    //
    //private static KieBase getKieBase(Resource resource) {
    //    KieServices kieServices = KieServices.Factory.get();
    //
    //    KieFileSystem kfs = kieServices.newKieFileSystem();
    //    resource.setResourceType(ResourceType.DRL);
    //    kfs.write(resource);
    //
    //    KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
    //    hasErrorMessage(kieBuilder.getResults());
    //
    //    KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
    //    return kieContainer.getKieBase();
    //}

    /**
     * 将文件中规则内容解析成字符串
     * @param filePath 文件路径
     * @return 规则内容字符串
     */
    private static String getDrlString(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        // 为DRL文件
        if (filePath.toLowerCase().endsWith(SUFFIX_DRL)) {
            return transDrlFile2String(file);
        }

        InputStream inputStream = new FileInputStream(file);
        SpreadsheetCompiler compiler = new SpreadsheetCompiler();
        InputType inputType = InputType.XLS;
        // 默认为xls文件，结尾为csv，则改为csv处理方式
        if (filePath.toLowerCase().endsWith(SUFFIX_CSV)) {
            inputType = InputType.CSV;
        }
        String drl = compiler.compile(inputStream, inputType);
        log.info("{} file rule string is : {}", filePath, drl);
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
                throw new IllegalStateException(file.getName() + " transDrlFile2String release fail ", e);
            }

        }
    }

    /**
     * 检查编译结果
     * @param results 编译结果
     */
    private static void hasErrorMessage(Results results) {
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

    enum Model {
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


    private KieSessionUtils() {
    }

}
