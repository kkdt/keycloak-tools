package kkdt.keycloak.desktop.config;

import kkdt.keycloak.desktop.UIFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import java.awt.EventQueue;

@Configuration
public class ApplicationConfiguration implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfiguration.class);

    @Autowired
    private UIFrame mainWindow;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Starting {}", mainWindow.getTitle());

        EventQueue.invokeLater(() -> {
            mainWindow.pack();
            mainWindow.setLocationRelativeTo(null);
            mainWindow.setVisible(true);
        });
    }
}
