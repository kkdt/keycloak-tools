package kkdt.keycloak.desktop.controller;

import kkdt.keycloak.desktop.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Supplier;

public class LoginController implements ActionListener, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private ApplicationContext applicationContext;
    private Supplier<UserInfo> userInfo;

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        UserInfo userInputs = userInfo.get();
        switch(actionEvent.getActionCommand()) {
            case "Login":
                logger.info("Logging into keycloak, Username: {}", userInputs.getUsername());
                userInputs.disable();
                break;
            case "Logout":
                logger.info("Logging out from keycloak, Username: {}", userInputs.getUsername());
                userInputs.enable();
                break;
        }
    }

    public void setUserInfo(Supplier<UserInfo> userInfo) {
        this.userInfo = userInfo;
    }

    public UserInfo userInfo() {
        return userInfo.get();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        logger.info("Setting application context, Application Name: {}, Class: {}",
            applicationContext.getApplicationName(),
            applicationContext.getClass().getName());
        this.applicationContext = applicationContext;
    }
}
