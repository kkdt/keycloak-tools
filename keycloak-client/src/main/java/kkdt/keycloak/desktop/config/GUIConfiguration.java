package kkdt.keycloak.desktop.config;

import kkdt.keycloak.desktop.UIFrame;
import kkdt.keycloak.desktop.UILoginPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.swing.JFrame;

@Configuration
public class GUIConfiguration {

    @Value("${app.title}")
    private String title;

    @Bean
    public UILoginPanel loginPanel() {
        UILoginPanel panel = new UILoginPanel();
        return panel;
    }

    @Bean
    public UIFrame mainWindow(@Autowired UILoginPanel loginPanel) {
        UIFrame frame = new UIFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContents(loginPanel);
        frame.setResizable(false);
        return frame;
    }

}
