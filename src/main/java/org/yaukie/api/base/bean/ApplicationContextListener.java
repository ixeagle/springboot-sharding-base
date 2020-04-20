package org.yaukie.api.base.bean;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Map;
/**
 *  @Author: yuenbin
 *  @Date :2020/3/20
 * @Time :10:51
 * @Motto: It is better to be clear than to be clever !
 * @Destrib:
**/
@Component
 public class ApplicationContextListener implements ApplicationListener<ContextRefreshedEvent> {

    private static Logger logger = LoggerFactory.getLogger(ApplicationContextListener.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        // root application context
        if(null == contextRefreshedEvent.getApplicationContext().getParent()) {
            logger.info(">>>>> spring初始化完毕 <<<<<");
            // spring初始化完毕后，通过反射调用所有使用Service注解的initMapper方法
            Map<String, Object> baseServices = contextRefreshedEvent.getApplicationContext().getBeansWithAnnotation(Service.class);
            for(Object service : baseServices.values()) {
                logger.info(">>>>> {}.initMapper()", service.getClass().getName());
                try {
                    Method initMapper = service.getClass().getMethod("initMapper");
                    initMapper.invoke(service);
                } catch (Exception e) {
                    logger.error("初始化Service的initMapper方法异常", e);
                    e.printStackTrace();
                }
            }

        }
    }

}
