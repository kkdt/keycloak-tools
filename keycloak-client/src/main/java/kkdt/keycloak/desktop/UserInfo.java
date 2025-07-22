package kkdt.keycloak.desktop;

import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.EventQueue;

public class UserInfo {
    private JTextField username;
    private JPasswordField password;

    public UserInfo with(JTextField username, JPasswordField password) {
        this.username = username;
        this.password = password;
        return this;
    }

    public String getUsername() {
        return username.getText().trim();
    }

    public char[] getPassword() {
        return password.getPassword();
    }

    public void disable() {
        this.username.setEditable(false);
        this.username.setEnabled(false);
        this.password.setEditable(false);
        this.password.setEnabled(false);
    }

    public void enable() {
        this.username.setEditable(true);
        this.username.setEnabled(true);
        this.password.setEditable(true);
        this.password.setEnabled(true);
        EventQueue.invokeLater(() -> {
            this.username.setText("");
            this.password.setText("");
        });
    }
}
