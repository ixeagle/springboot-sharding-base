package org.yaukie.api.util;

import com.google.common.base.CaseFormat;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.PropertyConfigurator;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaukie.api.dto.BaseData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: yuenbin
 * @Date :2020/3/13
 * @Time :20:32
 * @Motto: It is better to be clear than to be clever !
 * @Destrib: 通用代码机
 **/
public class GeneratorUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(GeneratorUtil.class);
    private static ThreadLocal<JDBCConnectionConfiguration> holder = new ThreadLocal<>();

    /**
     * 项目类路径
     */
    static final String CLASS_PATH = Thread.currentThread().getContextClassLoader().getResource("").getPath();

    static final String ABSOLUTE_PATH = CLASS_PATH.substring(CLASS_PATH.indexOf("/") + 1);

    /**
     * 项目在硬盘上的基础路径
     */
    private static final String PROJECT_PATH = System.getProperty("user.dir");

    /**
     * 目标模块路径
     */
    static final String MODULE_DIR = ABSOLUTE_PATH.substring(PROJECT_PATH.length(), ABSOLUTE_PATH.indexOf("/target"));


    /**
     * 模板位置
     */
    private static final String TEMPLATE_FILE_PATH = PROJECT_PATH + "/sharding-common/src/main/resources/template";
    /**
     * java文件路径
     */
    private static final String JAVA_PATH = MODULE_DIR + "/src/main/java/";
    /**
     * 资源文件路径
     */
    private static final String RESOURCES_PATH = MODULE_DIR + "/src/main/resources";


    private static final String DATE = new SimpleDateFormat("yyyy/MM/dd HH/mm/SS").format(new Date());


    static {
        PropertyConfigurator.configure(Thread.currentThread()
                .getContextClassLoader().getResource("log4j.properties"));
    }

    /**
     * 通过数据表名称生成代码，Model 名称通过解析数据表名称获得，下划线转大驼峰的形式。
     * 如输入表名称 "t_user_detail" 将生成 TUserDetail、TUserDetailMapper、TUserDetailService ...
     *
     * @param tableNames 数据表名称...
     */
   public   static void generator(String jdbcUrl, String jdbcUserName, String jdbcPass, String driverClassName, String basePackage, String tableKey,String... tableNames) {
        JDBCConnectionConfiguration jdbcConnectionConfiguration = holder.get();
        if (jdbcConnectionConfiguration == null) {
            jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
            jdbcConnectionConfiguration.setConnectionURL(jdbcUrl);
            jdbcConnectionConfiguration.setUserId(jdbcUserName);
            jdbcConnectionConfiguration.setPassword(jdbcPass);
            jdbcConnectionConfiguration.setDriverClass(driverClassName);
            holder.set(jdbcConnectionConfiguration);
        }
        for (String tableName : tableNames) {
            generator(basePackage, tableName, null,tableKey);
        }
    }

    /**
     * 通过数据表名称，和自定义的 Model 名称生成代码
     * 如输入表名称 "t_user_detail" 和自定义的 Model 名称 "User" 将生成 User、UserMapper、UserService ...
     *
     * @param tableName 数据表名称
     * @param modelName 自定义的 Model 名称
     */
   protected static void generator(String basePackage, String tableName, String modelName,String tableKey) {
        File file1 = new File(PROJECT_PATH + JAVA_PATH);
        File file2 = new File(PROJECT_PATH + RESOURCES_PATH);
        if (!file1.getAbsoluteFile().exists()) {
            file1.getAbsoluteFile().mkdirs();
        }
        if (!file2.getAbsoluteFile().exists()) {
            file2.getAbsoluteFile().mkdirs();
        }

        //(tableName, modelName);
        genController(basePackage, tableName, modelName);
        genService(basePackage, tableName, modelName);
        genModelAndMapper(basePackage, tableName, modelName,tableKey);
        genEntityVO(basePackage, tableName, modelName);
        genRequestVO(basePackage, tableName, modelName);
        genResponseVO(basePackage, tableName, modelName);
    }


   protected static void genModelAndMapper(String basePackage, String tableName, String modelName,String tableKey) {
        Context context = new Context(ModelType.FLAT);
        context.setId("Potato");
        context.setTargetRuntime("MyBatis3");
        context.addProperty(PropertyRegistry.CONTEXT_BEGINNING_DELIMITER, "`");
        context.addProperty(PropertyRegistry.CONTEXT_ENDING_DELIMITER, "`");
        context.setJdbcConnectionConfiguration(holder.get());

        PluginConfiguration pluginConfiguration = new PluginConfiguration();
        pluginConfiguration.setConfigurationType("org.mybatis.generator.plugins.EqualsHashCodePlugin");
         context.addPluginConfiguration(pluginConfiguration);

        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration();
        javaModelGeneratorConfiguration.setTargetProject(PROJECT_PATH + JAVA_PATH);
        javaModelGeneratorConfiguration.setTargetPackage(basePackage + ".model");
        context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);

        SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();
        sqlMapGeneratorConfiguration.setTargetProject(PROJECT_PATH + RESOURCES_PATH);
        sqlMapGeneratorConfiguration.setTargetPackage("mapper");
         context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);

        JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
        javaClientGeneratorConfiguration.setTargetProject(PROJECT_PATH + JAVA_PATH);
        javaClientGeneratorConfiguration.setTargetPackage(basePackage + ".dao.mapper");
        javaClientGeneratorConfiguration.setConfigurationType("XMLMAPPER");
        context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);

        TableConfiguration tableConfiguration = new TableConfiguration(context);
        tableConfiguration.setTableName(tableName);
        tableConfiguration.setDomainObjectName(modelName);
        tableConfiguration.setGeneratedKey(new GeneratedKey(tableKey, "Mysql",true,null));
         context.addTableConfiguration(tableConfiguration);

        List<String> warnings;
        MyBatisGenerator generator;
        try {
            Configuration config = new Configuration();
            config.addContext(context);
            config.validate();

            boolean overwrite = true;
            DefaultShellCallback callback = new DefaultShellCallback(overwrite);
            warnings = new ArrayList<String>();
            generator = new MyBatisGenerator(config, callback, warnings);
            generator.generate(null);
        } catch (Exception e) {
            throw new RuntimeException("生成Model和Mapper失败", e);
        }

        if (generator.getGeneratedJavaFiles().isEmpty() || generator.getGeneratedXmlFiles().isEmpty()) {
            throw new RuntimeException("生成Model和Mapper失败：" + warnings);
        }
        if (StringUtils.isEmpty(modelName)) {
            modelName = tableNameConvertUpperCamel(tableName);
        }
        LOGGER.info("======={}========" + modelName + ".java 生成成功");
        LOGGER.info("======={}========" + modelName + "Mapper.java 生成成功");
        LOGGER.info("======={}========" + modelName + "Mapper.xml 生成成功");
    }

   protected static void genService(String basePackage, String tableName, String modelName) {
        try {
            freemarker.template.Configuration cfg = getConfiguration();

            Map<String, Object> data = new HashMap<>();
            data.put("date", DATE);
            data.put("author", "yuenbin");
            String modelNameUpperCamel = StringUtils.isEmpty(modelName) ? tableNameConvertUpperCamel(tableName) : modelName;
            data.put("modelNameUpperCamel", modelNameUpperCamel);
            data.put("modelNameLowerCamel", tableNameConvertLowerCamel(tableName));
            data.put("basePackage", basePackage);

            File file = new File(PROJECT_PATH + JAVA_PATH + packageConvertPath(basePackage) + "/service/api/" + modelNameUpperCamel + "Service.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            LOGGER.info("======{}======" + "准备生成" + modelNameUpperCamel + "Service.java");
            cfg.getTemplate("template-service.ftl").process(data,
                    new FileWriter(file));
            LOGGER.info(modelNameUpperCamel + "Service.java 生成成功");

            File file1 = new File(PROJECT_PATH + JAVA_PATH + packageConvertPath(basePackage) + "/service/impl/" + modelNameUpperCamel + "ServiceImpl.java");
            if (!file1.getParentFile().exists()) {
                file1.getParentFile().mkdirs();
            }
            LOGGER.info("======{}======" + "准备生成" + modelNameUpperCamel + "ServiceImpl.java");
            cfg.getTemplate("template-serviceimpl.ftl").process(data,
                    new FileWriter(file1));
            LOGGER.info("======{}======" + modelNameUpperCamel + "ServiceImpl.java 生成成功");
        } catch (Exception e) {
            throw new RuntimeException("生成Service失败", e);
        }
    }

   protected static void genController(String basePackage, String tableName, String modelName) {

        try {
            freemarker.template.Configuration cfg = getConfiguration();
            //
            String[] split = tableName.split("_");
            String tableLastName = split[1];
            //
            String modelLastName = firstToCapital(tableLastName);
            Map<String, Object> data = new HashMap<>();
            data.put("date", DATE);
            data.put("author", "yuenbin");
            String modelNameUpperCamel = StringUtils.isEmpty(modelName) ? tableNameConvertUpperCamel(tableName) : modelName;
            data.put("baseRequestMapping", modelNameConvertMappingPath(modelNameUpperCamel));
            data.put("modelNameUpperCamel", modelNameUpperCamel);
            data.put("modelNameLowerCamel", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, modelNameUpperCamel));
            data.put("basePackage", basePackage);
            data.put("tableLastName", tableLastName);
            data.put("modelLastName", modelLastName);
            data.put("modelName", "controller");

            File file = new File(PROJECT_PATH + JAVA_PATH + packageConvertPath(basePackage) + "/controller/" + modelNameUpperCamel + "Controller.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            LOGGER.info("======{}======" + "准备生成" + modelNameUpperCamel + "Controller.java");
            cfg.getTemplate("template-controller.ftl").process(data, new FileWriter(file));
            LOGGER.info("======={}======" + modelNameUpperCamel + "Controller.java 生成成功");
        } catch (Exception e) {
            throw new RuntimeException("生成Controller失败", e);
        }

    }

   protected static void genRequestVO(String basePackage, String tableName, String modelName) {
        genVO(basePackage, tableName, modelName, packageConvertPath(basePackage) + "/vo/request/", "RequestVO");

    }

   protected static void genResponseVO(String basePackage, String tableName, String modelName) {
        genVO(basePackage, tableName, modelName, packageConvertPath(basePackage) + "/vo/response/", "ResponseVO");
    }

   protected static void genEntityVO(String basePackage, String tableName, String modelName) {
        genVO(basePackage, tableName, modelName, packageConvertPath(basePackage) + "/vo/entity/", "EntityVO");
    }

   protected static void genVO(String basePackage, String tableName, String modelName, String packagePath, String className) {
        try {
            freemarker.template.Configuration cfg = getConfiguration();
            //
            List<BaseData> baseDataList = DatabaseUtil.getBaseDataList(tableName);
            Map<String, Object> data = new HashMap<>();
            data.put("date", DATE);
            data.put("author", "yuenbin");
            String modelNameUpperCamel = StringUtils.isEmpty(modelName) ? tableNameConvertUpperCamel(tableName) : modelName;
            data.put("baseRequestMapping", modelNameConvertMappingPath(modelNameUpperCamel));
            data.put("modelNameUpperCamel", modelNameUpperCamel);
            data.put("modelNameLowerCamel", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, modelNameUpperCamel));
            data.put("basePackage", basePackage);
            data.put("baseDataList", baseDataList);

            File file = new File(PROJECT_PATH + JAVA_PATH + packagePath + modelNameUpperCamel + className + ".java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            LOGGER.info("======={}=======" + "准备生成" + modelNameUpperCamel + className + ".java");
            cfg.getTemplate("template-"+className + ".ftl").process(data, new FileWriter(file));

            LOGGER.info("======={}=======" + modelNameUpperCamel + className + ".java 生成成功");
        } catch (Exception e) {
            throw new RuntimeException(className, e);
        }

    }

    private static freemarker.template.Configuration getConfiguration() throws IOException {
        freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);
        cfg.setDirectoryForTemplateLoading(new File(TEMPLATE_FILE_PATH));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        return cfg;
    }

    private static String tableNameConvertLowerCamel(String tableName) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableName.toLowerCase());
    }

    private static String tableNameConvertUpperCamel(String tableName) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName.toLowerCase());

    }

    private static String tableNameConvertMappingPath(String tableName) {
        //兼容使用大写的表名
        tableName = tableName.toLowerCase();
        return "/op/" + (tableName.contains("_") ? tableName.replaceAll("_", "") +"/": tableName+"/");
    }

    private static String modelNameConvertMappingPath(String modelName) {
        String tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, modelName);
        return tableNameConvertMappingPath(tableName);
    }

    /**
     * 包名转路径
     *
     * @param packageName
     * @return
     */
    private static String packageConvertPath(String packageName) {
        return String.format("/%s/", packageName.contains(".") ? packageName.replaceAll("\\.", "/") : packageName);
    }

    //首字母大写
    private static String firstToCapital(String str) {
        char[] chars = str.toCharArray();
        if (chars[0] >= 'a' && chars[0] <= 'z') {
            chars[0] = (char) (chars[0] - 32);
        }
        return new String(chars);
    }


   protected static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",Locale.US);
        try {
            Date date = sdf.parse("Mon Mar 02 11:01:28 CST 2020" );
            SimpleDateFormat formDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            LOGGER.info(date+"");
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}