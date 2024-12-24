package fr.arinonia.bootstrap.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WindowMover extends MouseAdapter {

    private final JFrame frame;
    private Point point;

    public WindowMover(final JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        if (this.point != null) {
            final Point dragged_point = MouseInfo.getPointerInfo().getLocation();
            this.frame.setLocation(new Point((int)dragged_point.getX() - (int)this.point.getX(), (int)dragged_point.getY() - (int)this.point.getY()));
        }
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        this.point = e.getPoint();
    }
}
