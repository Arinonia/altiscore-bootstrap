package fr.arinonia.bootstrap.ui;

import fr.arinonia.bootstrap.config.BootstrapConfig;
import fr.arinonia.bootstrap.utils.UiUtil;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorDialog extends JDialog {

    public ErrorDialog(final Window parent, final String title, final String message, final Throwable error) {
        super(parent, title, ModalityType.APPLICATION_MODAL);

        this.setSize(500, 300);
        this.setLocationRelativeTo(parent);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setIconImage(UiUtil.getImage("/images/icon.png"));

        final JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BootstrapConfig.getInstance().getBackgroundColor());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        final JLabel messageLabel = new JLabel(message);
        messageLabel.setForeground(BootstrapConfig.getInstance().getTextColor());
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(messageLabel, BorderLayout.NORTH);

        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        error.printStackTrace(pw);

        final JTextArea errorArea = new JTextArea(sw.toString());
        errorArea.setEditable(false);
        errorArea.setBackground(BootstrapConfig.getInstance().getProgressBarBackground());//! consider renaming this
        errorArea.setForeground(BootstrapConfig.getInstance().getTextColor());

        final JScrollPane scrollPane = new JScrollPane(errorArea);
        scrollPane.setPreferredSize(new Dimension(450, 200));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        final JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dispose());

        final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BootstrapConfig.getInstance().getBackgroundColor());
        buttonPanel.add(okButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        this.setContentPane(mainPanel);
    }

    public static void showError(final Window parent, final String title, final String message, final Throwable error) {
        SwingUtilities.invokeLater(() -> {
            final ErrorDialog dialog = new ErrorDialog(parent, title, message, error);
            dialog.setVisible(true);
        });
    }
}