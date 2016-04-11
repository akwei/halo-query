package halo.query.dal;

import org.springframework.beans.factory.InitializingBean;

import java.util.List;

public class DALConnectionListenerFactory implements InitializingBean {

    private static DALConnectionListenerFactory instance;

    private List<DALConnectionListener> dalConnectionListeners;

    public static DALConnectionListenerFactory getInstance() {
        return instance;
    }

    public void setDalConnectionListeners(List<DALConnectionListener> dalConnectionListeners) {
        this.dalConnectionListeners = dalConnectionListeners;
    }

    public List<DALConnectionListener> getDalConnectionListeners() {
        return dalConnectionListeners;
    }

    public static boolean hasListener() {
        if (instance == null) {
            return false;
        }
        if (instance.dalConnectionListeners == null || instance.dalConnectionListeners.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        instance = this;
    }
}
