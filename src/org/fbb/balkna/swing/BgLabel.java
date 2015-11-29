package org.fbb.balkna.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.JLabel;
import org.fbb.balkna.awt.utils.ImgUtils;
import org.fbb.balkna.model.Model;

/**
 *
 * @author jvanek
 */
class BgLabel extends JLabel {

    private List<BufferedImage> imgs;
    private int selcted;

    private final int b = 2;
    private boolean cross;

    private final Thread swapper = new Thread() {

        @Override
        public void run() {
            while (true) {
                try {
                    if (Model.getModel().getImagesOnTimerSpeed() > 0 && imgs != null && imgs.size() > 1) {
                        incrementSelected();
                    }
                    Thread.sleep(Math.max(1000, Model.getModel().getImagesOnTimerSpeed() * 1000));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    };

    public BgLabel() {
        super();
        swapper.setDaemon(true);
        swapper.start();
    }

    @Override
    public synchronized void paint(Graphics g) {
        if (imgs != null) {
            BufferedImage img = imgs.get(selcted);
            if (img != null && getWidth() > 2 * b && getHeight() > 2 * b) {
                g.drawImage(ImgUtils.squeeze(img, getWidth() - 2 * b, getHeight() - 2 * b), b, b, getWidth() - 2 * b, getHeight() - 2 * b, null);
            }
        }
        if (cross) {
            float alpha = 1f;
            if (g instanceof Graphics2D) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setStroke(new BasicStroke(5));
                alpha = 0.3f; //wide and transaprent
            } //else thin andnot transaprent. not tested..
            Color color = new Color(1, 0, 0, alpha); //Red 
            g.setColor(color);
            g.drawLine(0, 0, getWidth(), getHeight());
            g.drawLine(getWidth(), 0, 0, getHeight());
        }
        super.paint(g);
    }

    private synchronized void incrementSelected() {
        selcted++;
        if (selcted >= imgs.size()) {
            //yes 1, 0th is always the summary. not wonted in auto iteration?
            selcted = 1;
        }
        repaint();
    }

    public void setSrcs(List<BufferedImage> src) {
        this.imgs = src;
        repaint();
    }

    public List<BufferedImage> getImgs() {
        return imgs;
    }

    public void setSelcted(int selcted) {
        this.selcted = selcted;
    }

    void cross(boolean b) {
        this.cross = b;
    }

}
