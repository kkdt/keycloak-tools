package kkdt.keycloak.desktop;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * The main GUI window.
 */
public class UIFrame extends JFrame {
    private JMenuItem exit;
    private JMenuItem about;

    /**
     * Expected <code>BorderLayout</code> window.
     *
     * @param title
     */
    public UIFrame(String title) {
        super(title);
        setJMenuBar(initMenu());
        setLayout(new BorderLayout(10,10));
    }

    public void setContents(JPanel contents) {
        getContentPane().add(contents, BorderLayout.NORTH);
    }

    /**
     * Exposes a footer area.
     *
     * @param footer
     */
    public void setFooter(Component footer) {
        getContentPane().add(footer, BorderLayout.SOUTH);
    }

    private JMenuBar initMenu() {
        ActionListener actionListener = actionListener();

        exit = new JMenuItem("Exit");
        exit.setMnemonic(KeyEvent.VK_E);
        exit.setToolTipText("Exit application");
        exit.addActionListener(actionListener);

        about = new JMenuItem("About");
        about.setMnemonic(KeyEvent.VK_A);
        about.setToolTipText("About");
        about.addActionListener(actionListener);

        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);
        file.add(exit);

        JMenu help = new JMenu("Help");
        help.setMnemonic(KeyEvent.VK_H);
        help.add(about);

        JMenuBar menubar = new JMenuBar();
        menubar.add(file);
        menubar.add(help);

        return menubar;
    }

    private ActionListener actionListener() {
        return actionEvent -> {
            switch(actionEvent.getActionCommand()) {
                case "Exit":
                    System.exit(0);
                    break;
                case "About":
                    handleAbout();
                    break;
            }
        };
    }

    private void handleAbout() {
        Package p = KeycloakDesktopClientApplication.class.getPackage();
        String version = p.getImplementationVersion();
        String spec = p.getSpecificationVersion();
        String vendor = p.getImplementationVendor();
        StringBuilder buffer = new StringBuilder(p.getImplementationTitle() != null ? p.getImplementationTitle() : "Keycloak Desktop Client");
        buffer.append("\nVersion: " + version);
        buffer.append("\nSpecification: " + p.getSpecificationTitle() + " " + spec);
        buffer.append("\nAuthor: " + vendor);
        JOptionPane.showMessageDialog(null,
            buffer.toString(),
            "About",
            JOptionPane.INFORMATION_MESSAGE);
    }
}