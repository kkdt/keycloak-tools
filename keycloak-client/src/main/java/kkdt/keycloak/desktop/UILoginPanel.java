package kkdt.keycloak.desktop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Username / password panel with controls to login and logout.
 */
public class UILoginPanel extends JPanel {
    private static final Logger log = LoggerFactory.getLogger(UILoginPanel.class);
    private JTextField username;
    private JTextField password;
    private JButton loginButton;
    private JButton logoutButton;


    public UILoginPanel() {
        setLayout(new GridBagLayout());
        setMinimumSize(new Dimension(250, 250));

        username = new JTextField("Username", 30);
        password = new JTextField("Password", 30);
        loginButton = new JButton("Login");
        loginButton.setSize(new Dimension(150, 30));
        logoutButton = new JButton("Logout");
        logoutButton.setSize(new Dimension(150, 30));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controls.add(logoutButton);
        controls.add(loginButton);

        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.NONE;
        add(new JLabel("Username"), c);
        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(username, c);

        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.NONE;
        add(new JLabel("Password"), c);
        c.gridx = 1;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(password, c);

        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(controls, c);
    }
}
