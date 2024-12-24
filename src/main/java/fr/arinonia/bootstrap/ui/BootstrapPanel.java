package fr.arinonia.bootstrap.ui;

import fr.arinonia.bootstrap.Bootstrap;
import fr.arinonia.bootstrap.config.BootstrapConfig;
import fr.arinonia.bootstrap.ui.controls.ClickableLabel;
import fr.arinonia.bootstrap.ui.controls.RoundedProgressBar;
import fr.arinonia.bootstrap.utils.UiUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class BootstrapPanel extends JPanel {
    private final JLabel statusUpdate = new JLabel("Connexion au serveur...");
    private final RoundedProgressBar roundedProgressBar = new RoundedProgressBar();

    //! consider using BootstrapConfig instead
    private static final Color WHITE = new Color(255, 255, 255);

    public BootstrapPanel() {
        super(null);
        this.setBackground(BootstrapConfig.getInstance().getBackgroundColor());

        final JLabel title = new JLabel(BootstrapConfig.getInstance().getAppName());
        title.setForeground(BootstrapConfig.getInstance().getTextColor());

        Font roboto = null;
        try {
            roboto = Font.createFont(Font.TRUETYPE_FONT, Bootstrap.class.getResourceAsStream("/fonts/roboto.ttf"));
        } catch (final FontFormatException | IOException e) {
            throw new RuntimeException("stop playing with launcher file's", e);
        }
        this.setCustomFont(title, roboto, 42.0F);
        title.setBounds(BootstrapConfig.getInstance().getWindowWidth() / 2  - getLabelWidth(title) / 2, 10, BootstrapConfig.getInstance().getWindowWidth(), 40);
        this.add(title);

        final JLabel close = new JLabel("x");
        this.setCustomFont(close, roboto, 30.0F);
        close.setBounds(510, 5, 20, 40);
        close.setForeground(WHITE);
        close.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                Bootstrap.getInstance().getApplicationService().exit();
            }

            @Override
            public void mouseEntered(final MouseEvent e) {
                close.setForeground(new Color(255, 0, 0));
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                close.setForeground(WHITE);
            }
        });
        this.add(close);

        final JLabel hide = new JLabel("-");
        this.setCustomFont(hide, roboto, 46.0F);
        hide.setBounds(484, 5, 20, 40);
        hide.setForeground(WHITE);
        hide.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                //FadeUtilityClass.fade(Bootstrap.getInstance().getBootstrapFrame(), false);
            }

            @Override
            public void mouseEntered(final MouseEvent e) {
                hide.setForeground(new Color(180, 180, 180));
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                hide.setForeground(WHITE);
            }
        });
        this.add(hide);

        final ImageIcon imageIcon = UiUtil.getImageIcon("/images/icon.png");

        final Image image = imageIcon.getImage();

        final JLabel gif = new JLabel();
        gif.setBounds(20, 50, 500, 500);
        gif.setIcon(new ImageIcon(image));
        this.add(gif);

        this.roundedProgressBar.setBounds(30, 550, 480, 20);
        this.roundedProgressBar.setBackground(new Color(48, 25, 88));
        this.roundedProgressBar.setForeground(new Color(149, 128, 255));
        this.roundedProgressBar.setMinimum(0);
        this.roundedProgressBar.setMaximum(100);
        this.roundedProgressBar.setValue(0);
        this.roundedProgressBar.setShowGlowEffect(true);
        this.roundedProgressBar.setShowAnimation(true);
        this.add(this.roundedProgressBar);

        final ClickableLabel connection_problem = new ClickableLabel(
                "Problème de connexion ?",
                roboto,
                22.0F,
                BootstrapConfig.getInstance().getTextColor(),
                BootstrapConfig.getInstance().getTextColor().brighter(),
                "Redirige vers le discord"
        );
        connection_problem.setBounds(50, 650, 260, 40);
        connection_problem.addClickListener(() -> {
            //Util.openBrowser(Constants.DISCORD_URL);
        });
        this.add(connection_problem);

        this.statusUpdate.setForeground(BootstrapConfig.getInstance().getTextColor());
        this.setCustomFont(this.statusUpdate, roboto, 20.0F);
        this.statusUpdate.setBounds(BootstrapConfig.getInstance().getWindowWidth() / 2 - getLabelWidth(this.statusUpdate) / 2, 570, 350, 40);
        this.add(this.statusUpdate);
    }

    public int getLabelWidth(final JLabel label) {
        return this.getFontMetrics(label.getFont()).stringWidth(label.getText());
    }
    public void setCustomFont(final JLabel label, final Font font, final float size) {
        label.setFont((font != null) ? font.deriveFont(size) : label.getFont().deriveFont(size));
    }

    /*public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    }*/

    public void updateStatusLabel(final String text) {
        this.statusUpdate.setText(text);
        this.statusUpdate.setBounds(BootstrapConfig.getInstance().getWindowWidth() / 2 - getLabelWidth(this.statusUpdate) / 2, 570, 450, 40);
    }

    public JLabel getStatusUpdate() {
        return this.statusUpdate;
    }

    public RoundedProgressBar getRoundedProgressBar() {
        return this.roundedProgressBar;
    }
}
