package fr.arinonia.bootstrap.ui.controls;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

public class ClickableLabel extends JLabel {
    private final Font normalFont;
    private final Font underlinedFont;
    private final Color normalColor;
    private final Color hoverColor;

    public ClickableLabel(String text, Font font, float fontSize, Color normalColor, Color hoverColor, String tooltip) {
        super(text);
        this.normalColor = normalColor;
        this.hoverColor = hoverColor;

        this.normalFont = (font != null) ? font.deriveFont(fontSize) : this.getFont().deriveFont(fontSize);

        final Map<TextAttribute, Object> underlineStyle = new HashMap<>();
        underlineStyle.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        this.underlinedFont = this.normalFont.deriveFont(underlineStyle);

        this.setFont(this.normalFont);
        this.setForeground(normalColor);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        if (tooltip != null && !tooltip.isEmpty()) {
            this.setToolTipText(tooltip);
        }

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(final MouseEvent e) {
                setFont(underlinedFont);
                setForeground(hoverColor);
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                setFont(normalFont);
                setForeground(normalColor);
            }
        });
    }

    public void addClickListener(final Runnable action) {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                action.run();
            }
        });
    }
}