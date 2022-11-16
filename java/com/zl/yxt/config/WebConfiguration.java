package com.zl.yxt.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.zl.yxt.interceptor.TokenInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Spring MVC 配置
 * implements WebMvcConfigurer
 */
@Configuration
//public class WebConfiguration implements WebMvcConfigurer  {
public class WebConfiguration extends WebMvcConfigurationSupport  {

    private final Logger logger = LoggerFactory.getLogger(WebConfiguration.class);

    @Autowired
    private TokenInterceptor tokenInterceptor;

    //构造方法
    public WebConfiguration(TokenInterceptor tokenInterceptor) {
        this.tokenInterceptor = tokenInterceptor;
    }

    //解决跨域问题
    @Override
    public void addCorsMappings(CorsRegistry registry) {
      /*addMapping：允许所以映射
      allowedHeaders：允许所以请求头
      allowedMethods:允许所以请求方式，get、post、put、delete
      allowedOrigins：允许所以域名访问
      allowCredentials：允许携带cookie参数*/
        registry.addMapping("/**").allowedHeaders("*")
                .allowedMethods("*")
                .allowedOrigins("*")
                .allowCredentials(true);
    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer){
        configurer.setTaskExecutor(new ConcurrentTaskExecutor(Executors.newFixedThreadPool(3)));
        configurer.setDefaultTimeout(30000);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludePath = new ArrayList<>();
        //排除拦截，除了注册登录(此时还没token)，其他都拦截
        excludePath.add("/*/register/**");  //注册
        excludePath.add("/*/login");     //登录
        excludePath.add("/admin");
        excludePath.add("/admin/");  //后台登录首页
        excludePath.add("/**/*.js");  //静态资源
        excludePath.add("/**/favicon.ico");  //静态资源
        excludePath.add("/**/*.css");  //静态资源
        excludePath.add("/**/*.jpg");  //静态资源
        excludePath.add("/**/*.gif");  //静态资源
        excludePath.add("/**/*.png");  //静态资源
        excludePath.add("/websocket/**");
        /*swagger*/
        excludePath.add("doc.html");
        excludePath.add("/swagger-resources/**");
        excludePath.add("/webjars/**");
        excludePath.add("/v2/**");
        excludePath.add("/swagger-ui.html/**");

        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePath);
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/public/");
        super.addResourceHandlers(registry);
    }

    //使用阿里 FastJson 作为JSON MessageConverter
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        supportedMediaTypes.add(MediaType.APPLICATION_PDF);
        supportedMediaTypes.add(MediaType.APPLICATION_RSS_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XML);
        supportedMediaTypes.add(MediaType.IMAGE_GIF);
        supportedMediaTypes.add(MediaType.IMAGE_JPEG);
        supportedMediaTypes.add(MediaType.IMAGE_PNG);
        supportedMediaTypes.add(MediaType.TEXT_EVENT_STREAM);
        supportedMediaTypes.add(MediaType.TEXT_HTML);
        supportedMediaTypes.add(MediaType.TEXT_MARKDOWN);
        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
        supportedMediaTypes.add(MediaType.TEXT_XML);
        converter.setSupportedMediaTypes(supportedMediaTypes);
        converter.setDefaultCharset(Charset.forName("UTF-8"));
        FastJsonConfig config = new FastJsonConfig();
        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        config.setSerializerFeatures(SerializerFeature.WriteDateUseDateFormat);//格式化时间
        converter.setFastJsonConfig(config);
        converters.add(converter);
    }

}
