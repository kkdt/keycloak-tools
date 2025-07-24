package kkdt.keycloak.desktop;

import kkdt.keycloak.desktop.controller.LoginController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.function.Consumer;

/**
 * User and authentication information panel.
 */
public class UILoginPanel extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(UILoginPanel.class);

    private JTextField username = new JTextField(30);
    private JTextField name = new JTextField(30);
    private JTextField email = new JTextField(30);
    private JTextField subject = new JTextField(30);
    private JTextArea token = new JTextArea(25, 30);
    private JTextField issueTime = new JTextField(30);
    private JTextField expireTime = new JTextField(30);
    private JTextField issuer = new JTextField(30);
    private JTextField clientId = new JTextField(30);
    private JTextField source = new JTextField(30);
    private JButton clientButton = new JButton("Client ID");
    private JButton passwordButton = new JButton("Password");

    public UILoginPanel() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(250, 250));

        username.setEditable(false);
        name.setEditable(false);
        email.setEditable(false);
        subject.setEditable(false);
        token.setEditable(false);
        token.setLineWrap(true);
        issueTime.setEditable(false);
        expireTime.setEditable(false);
        issuer.setEditable(false);
        clientId.setEditable(false);
        source.setEditable(false);

        clientButton.setSize(new Dimension(175, 30));
        clientButton.setToolTipText("Login using the 'client_credentials' grant type (service to service)");
        passwordButton.setSize(new Dimension(175, 30));
        passwordButton.setToolTipText("Login using the 'password' grant type (user login)");

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controls.add(passwordButton);
        controls.add(clientButton);

        JPanel userInfo = userInfoPanel();
        JPanel auth = authenticationPanel();

        add(userInfo, BorderLayout.NORTH);
        add(auth, BorderLayout.CENTER);
        add(controls, BorderLayout.SOUTH);
    }

    public UILoginPanel withLoginController(LoginController actionListener) {
        if(actionListener != null) {
            this.passwordButton.addActionListener(actionListener);
            this.clientButton.addActionListener(actionListener);
            actionListener.setAuthenticatedUser(authenticatedUser());
        }
        return this;
    }

    public Consumer<UserInfo> authenticatedUser() {
        return data -> {
            EventQueue.invokeLater(() -> {
                String _token = token.getText();
                logger.info("Current token {} new token",
                    _token.equals(data.getToken()) ? "is the same as" : "is different than the");

                username.setText(data.getUsername());
                email.setText(data.getEmail());
                name.setText(data.getName());
                subject.setText(data.getSubject());
                token.setText(data.getToken());
                issueTime.setText(data.getIssuedAt().toString());
                expireTime.setText(data.getExpiresAt().toString());
                issuer.setText(data.getIssuer().toString());
                clientId.setText(data.getAuthorizedParty());
                source.setText(data.getSource());
            });
        };
    }

    private JPanel authenticationPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Authentication"));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel("Token", SwingConstants.LEFT), c);
        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(token, c);

        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel("Issued", SwingConstants.LEFT), c);
        c.gridx = 1;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(issueTime, c);

        c.gridx = 0;
        c.gridy = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel("Expire", SwingConstants.LEFT), c);
        c.gridx = 1;
        c.gridy = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(expireTime, c);

        c.gridx = 0;
        c.gridy = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel("Issuer", SwingConstants.LEFT), c);
        c.gridx = 1;
        c.gridy = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(issuer, c);

        c.gridx = 0;
        c.gridy = 4;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel("Source", SwingConstants.LEFT), c);
        c.gridx = 1;
        c.gridy = 4;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(source, c);

        c.gridx = 0;
        c.gridy = 5;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel("Client ID", SwingConstants.LEFT), c);
        c.gridx = 1;
        c.gridy = 5;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(clientId, c);

        return panel;
    }

    private JPanel userInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel("Username", SwingConstants.LEFT), c);
        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(username, c);

        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel("Name", SwingConstants.LEFT), c);
        c.gridx = 1;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(name, c);

        c.gridx = 0;
        c.gridy = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel("Email", SwingConstants.LEFT), c);
        c.gridx = 1;
        c.gridy = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(email, c);

        c.gridx = 0;
        c.gridy = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel("Subject", SwingConstants.LEFT), c);
        c.gridx = 1;
        c.gridy = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(subject, c);

        return panel;
    }
}
