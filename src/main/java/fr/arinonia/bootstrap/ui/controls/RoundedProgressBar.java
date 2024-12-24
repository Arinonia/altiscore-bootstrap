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

        animationTimer = new Timer(50, e -> {
            animationValue += 0.1f;
            if (animationValue > 2 * Math.PI) {
                animationValue = 0;
            }
            repaint();
        });

        progressTimer = new Timer(16, e -> { // ~60 FPS
            if (Math.abs(currentValue - targetValue) > 0.01) {
                currentValue += (targetValue - currentValue) * 0.1;
                repaint();
            } else {
                currentValue = targetValue;
                progressTimer.stop();
            }
        });

        animationTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int width = getWidth();
        int height = getHeight();
        int progress = Util.crossMult((int) this.currentValue, this.maximum, width);

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

    private void drawShadow(Graphics2D g2, int width, int height) {
        for (int i = 0; i < 4; i++) {
            float alpha = 0.1f - (i * 0.02f);
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

    private void drawShineEffect(Graphics2D g2, int progress, int height) {
        float shineLoc = (float) (Math.sin(this.animationValue) * progress);
        GradientPaint shineGradient = new GradientPaint(
                shineLoc - 10, 0,
                new Color(255, 255, 255, 0),
                shineLoc, 0,
                new Color(255, 255, 255, 50),
                true
        );
        g2.setPaint(shineGradient);
        g2.fill(new RoundRectangle2D.Float(0, 0, progress, height, height, height));
    }

    public void setValue(int value) {
        this.targetValue = Math.min(Math.max(value, this.minimum), this.maximum);
        if (!this.progressTimer.isRunning()) {
            this.progressTimer.start();
        }
    }



    public void setMaximum(int maximum) {
        this.maximum = maximum;
        this.repaint();
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
        this.repaint();
    }


    @Override
    public void setBackground(Color background) {
        this.background = background;
        this.repaint();
    }

    @Override
    public void setForeground(Color foreground) {
        this.foreground = foreground;
        this.repaint();
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        this.repaint();
    }

    public void setShowGlowEffect(boolean showGlowEffect) {
        this.showGlowEffect = showGlowEffect;
        this.repaint();
    }

    public void setShowAnimation(boolean showAnimation) {
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