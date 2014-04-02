package net.mormolhs.facebook.fbanalytics.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by toikonomakos on 3/16/14.
 */
public class JBackgroundPanel extends JPanel {
    private ImageIcon img;

    public JBackgroundPanel() {

    }

    public void setImg(ImageIcon img) {
        this.img = img;
        setPreferredSize(new Dimension(img.getIconWidth(), img.getIconHeight()));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // paint the background image and scale it to fill the entire space
        g.drawImage(img.getImage(), 0, 0, getWidth(), getHeight(), this);
    }


}
