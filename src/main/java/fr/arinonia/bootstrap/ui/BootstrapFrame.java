package fr.arinonia.bootstrap.ui;

import fr.arinonia.bootstrap.config.BootstrapConfig;
import fr.arinonia.bootstrap.utils.UiUtil;
import fr.arinonia.bootstrap.utils.WindowMover;

import javax.swing.*;
import java.awt.*;

public class BootstrapFrame extends JFrame {

    public BootstrapFrame() {
        this.setTitle(BootstrapConfig.getInstance().getAppName());
        this.setSize(BootstrapConfig.getInstance().getWindowWidth(), BootstrapConfig.getInstance().getWindowHeight());
        this.setUndecorated(true);
        this.setBackground(new Color(0, 0, 0));
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setAlwaysOnTop(true);
        this.setIconImage(UiUtil.getImage("/images/icon.png"));

        final WindowMover windowMover = new WindowMover(this);
        this.addMouseListener(windowMover);
        this.addMouseMotionListener(windowMover);
    }
}
