package fr.arinonia.bootstrap.utils;

import fr.arinonia.bootstrap.Bootstrap;
import fr.arinonia.bootstrap.logger.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class UiUtil {

    public static BufferedImage getImage(final String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(Objects.requireNonNull(Bootstrap.class.getResourceAsStream(path)));
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static ImageIcon getImageIcon(final String path) {
        final URL url = Bootstrap.class.getResource(path);
        if (url == null) {
            Logger.error("Unable to load resource " + path);
            return null;
        }
        return new ImageIcon(url);
    }

    public static ImageIcon getImageIconScaled(final String path, final int width, final int height) {
        final URL url = Bootstrap.class.getResource(path);
        if (url == null) {
            Logger.error("Unable to load resource " + path);
            return null;
        }
        final ImageIcon icon = new ImageIcon(url);
        final Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
        return new ImageIcon(scaled);
    }
}
