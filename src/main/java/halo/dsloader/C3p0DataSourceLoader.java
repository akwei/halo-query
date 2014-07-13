package halo.dsloader;

import org.springframework.beans.factory.InitializingBean;

import java.util.ResourceBundle;

/**
 * Created by akwei on 7/11/14.
 */
public class C3p0DataSourceLoader implements InitializingBean {
    private ResourceBundle rb = ResourceBundle.getBundle("c3p0");

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
