package fr.arinonia.bootstrap.ui.controls;

import fr.arinonia.bootstrap.utils.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedProgressBar extends JComponent {
    private int maximum = 100;
    private int minimum = 0;
    private int targetValue = 0;
    private double currentValue = 0;
    private Color background;
    private Color foreground;
    private Color borderColor;
    private boolean showGlowEffect = true;
    private boolean showAnimation = true;
    private float animationValue = 0f;
    private final Timer animationTimer;
    private Timer progressTimer;

    public RoundedProgressBar() {
        this.setBackground(new Color(0, 0, 0, 0));
        this.background = new Color(35, 35, 35);
        this.foreground = new Color(100, 100, 100);
        this.borderColor = new Color(60, 60, 60);

        this.animationTimer = new Timer(50, e -> {
            this.animationValue += 0.1f;
            if (this.animationValue > 2 * Math.PI) {
                this.animationValue = 0;
            }
            repaint();
        });

        this.progressTimer = new Timer(16, e -> { // ~60 FPS
            if (Math.abs(this.currentValue - this.targetValue) > 0.01) {
                this.currentValue += (this.targetValue - this.currentValue) * 0.1;
                repaint();
            } else {
                this.currentValue = this.targetValue;
                this.progressTimer.stop();
            }
        });

        this.animationTimer.start();
    }

    @Override
    protected void paintComponent(final Graphics g) {
        final Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        final int width = getWidth();
        final int height = getHeight();
        final int progress = Util.crossMult((int) this.currentValue, this.maximum, width);

        if (this.showGlowEffect) {
            drawShadow(g2, width, height);
        }

        g2.setColor(this.background);
        g2.fill(new RoundRectangle2D.Float(0, 0, width, height, height, height));

        if (progress > 0) {
            GradientPaint gradient = new GradientPaint(
                    0, 0,
                    this.foreground,
                    0, height,
                    this.foreground.darker()
            );
            g2.setPaint(gradient);
            g2.fill(new RoundRectangle2D.Float(0, 0, progress, height, height, height));

            if (this.showAnimation) {
                drawShineEffect(g2, progress, height);
            }
        }

        g2.setColor(this.borderColor);
        g2.setStroke(new BasicStroke(1f));
        g2.draw(new RoundRectangle2D.Float(0, 0, width - 1, height - 1, height, height));

        g2.dispose();
    }

    private void drawShadow(final Graphics2D g2, final int width, final int height) {
        for (int i = 0; i < 4; i++) {
            final float alpha = 0.1f - (i * 0.02f);
            g2.setColor(new Color(0f, 0f, 0f, alpha));
            g2.fill(new RoundRectangle2D.Float(
                    -i, -i,
                    width + (2 * i),
                    height + (2 * i),
                    height + (2 * i),
                    height + (2 * i)
            ));
        }
    }

    private void drawShineEffect(final Graphics2D g2, final int progress, final int height) {
        final float shineLoc = (float) (Math.sin(this.animationValue) * progress);
        final GradientPaint shineGradient = new GradientPaint(
                shineLoc - 10, 0,
                new Color(255, 255, 255, 0),
                shineLoc, 0,
                new Color(255, 255, 255, 50),
                true
        );
        g2.setPaint(shineGradient);
        g2.fill(new RoundRectangle2D.Float(0, 0, progress, height, height, height));
    }

    public void setValue(final int value) {
        this.targetValue = Math.min(Math.max(value, this.minimum), this.maximum);
        if (!this.progressTimer.isRunning()) {
            this.progressTimer.start();
        }
    }



    public void setMaximum(final int maximum) {
        this.maximum = maximum;
        this.repaint();
    }

    public void setMinimum(final int minimum) {
        this.minimum = minimum;
        this.repaint();
    }


    @Override
    public void setBackground(final Color background) {
        this.background = background;
        this.repaint();
    }

    @Override
    public void setForeground(final Color foreground) {
        this.foreground = foreground;
        this.repaint();
    }

    public void setBorderColor(final Color borderColor) {
        this.borderColor = borderColor;
        this.repaint();
    }

    public void setShowGlowEffect(final boolean showGlowEffect) {
        this.showGlowEffect = showGlowEffect;
        this.repaint();
    }

    public void setShowAnimation(final boolean showAnimation) {
        this.showAnimation = showAnimation;
        if (showAnimation) {
            this.animationTimer.start();
        } else {
            this.animationTimer.stop();
        }
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        if (this.animationTimer != null) {
            this.animationTimer.stop();
        }
        if (this.progressTimer != null) {
            this.progressTimer.stop();
        }
    }

    public int getValue() {
        return (int)this.currentValue;
    }
}