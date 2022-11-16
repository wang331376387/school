package com.zl.yxt.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.util.Calendar;

@Component
public class ApplicationContext implements ServletContextAware {

    private static final Logger log = LoggerFactory.getLogger(ApplicationContext.class);

    /**
     *
     * <p>初始化到Application作用域当中</p>
     * @param context
     * @see org.springframework.web.context.ServletContextAware#setServletContext(javax.servlet.ServletContext)
     */
    @Override
    public void setServletContext(ServletContext context) {
        String contextPath = context.getContextPath();

        context.setAttribute("ctx", contextPath);
    }

}
