package vcfs.views.components;

import javax.swing.*;
import java.awt.*;

/**
 * GradientPanel - Creates a modern gradient background with 3D effect.
 * Used for visual enhancement across all UI surfaces.
 */
public class GradientPanel extends JPanel {
    private Color startColor;
    private Color endColor;
    private boolean isVertical;
    
    public GradientPanel(Color startColor, Color endColor, boolean isVertical) {
        this.startColor = startColor;
        this.endColor = endColor;
        this.isVertical = isVertical;
        setOpaque(false);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        GradientPaint gradient;
        if (isVertical) {
            gradient = new GradientPaint(0, 0, startColor, 0, height, endColor, false);
        } else {
            gradient = new GradientPaint(0, 0, startColor, width, 0, endColor, false);
        }
        
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, width, height);
        
        // Add subtle shadow effect
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.setColor(new Color(0, 0, 0, 15));
        g2d.drawRect(0, 0, width - 1, height - 1);
    }
}
