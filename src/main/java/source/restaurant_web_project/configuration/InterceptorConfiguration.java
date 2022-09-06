package source.restaurant_web_project.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import source.restaurant_web_project.intercaptors.DeleteCategoryInterceptor;
import source.restaurant_web_project.intercaptors.UserChangesInterceptor;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    private final UserChangesInterceptor userChangesInterceptor;
    private final DeleteCategoryInterceptor deleteCategoryInterceptor;

    public InterceptorConfiguration(UserChangesInterceptor userChangesInterceptor, DeleteCategoryInterceptor deleteCategoryInterceptor) {
        this.userChangesInterceptor = userChangesInterceptor;
        this.deleteCategoryInterceptor = deleteCategoryInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userChangesInterceptor).addPathPatterns("/owner/user-control/add-role/**","/owner/user-control/remove-role/**");
        registry.addInterceptor(deleteCategoryInterceptor).addPathPatterns("/owner/menu/category/delete/**");
    }
}
