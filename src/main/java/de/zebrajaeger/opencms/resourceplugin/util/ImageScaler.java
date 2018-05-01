package de.zebrajaeger.opencms.resourceplugin.util;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class ImageScaler {

    public BufferedImage scaleTo(BufferedImage src, int size) {
        BufferedImage target = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics g = target.getGraphics();

        // debug
//        BufferedImage target = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
//        g.setColor(Color.CYAN);
//        g.drawRect(0,0, target.getWidth()-1, target.getHeight()-1);

        double scaleX = (double) src.getWidth() / (double) size;
        double scaleY = (double) src.getHeight() / (double) size;
        double scale = Math.max(scaleX, scaleY);

        int targetW = (int) ((double) src.getWidth() / scale);
        int targetX1 = (size - targetW) / 2;
        int targetX2 = targetX1 + targetW;

        int targetH = (int) ((double) src.getHeight() / scale);
        int targetY1 = (size - targetH) / 2;
        int targetY2 = targetY1 + targetH;

        g.drawImage(src, targetX1, targetY1, targetX2, targetY2, 0, 0, src.getWidth() - 1, src.getHeight() - 1, null);
        return target;
    }
}
