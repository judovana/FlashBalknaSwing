package org.fbb.balkna.awt.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.fbb.balkna.model.graphs.AbstractGraph;
import org.fbb.balkna.model.graphs.DayStatst;
import org.fbb.balkna.model.graphs.DayTimes;

/**
 *
 * @author jvanek
 */
public class SwingGraph extends AbstractGraph {

    public static BufferedImage generatePassFailGraph(Map<Date, DayStatst> data) {
        List<DayStatst> list = getDataAsList(data);
        int max = getMaxRecord(list);
        int h = (max + 1) * 25 + new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR).createGraphics().getFontMetrics().getHeight();
        List<Class> lClases = getClasses(list);
        int w = (list.size() + 1) * lClases.size() * 3 * CWIDTH;
        return generatePassFailGraph(w, h, data);
        //return generatePassFailGraph(300, 300, data);
    }

    static int img = 1;

    private static BufferedImage generatePassFailGraph(int w, int h, Map<Date, DayStatst> data) {
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
//        if (data.isEmpty()) {
//            return bi;
//        }
        List<DayStatst> list = getDataAsList(data);
        //FIXME fill voids 28 31 -> 28 29 30 31 by 0,0,0 on all CLASSES and recomp xrate
        //Collections.reverse(list);
        int max = getMaxRecord(list);
        List<Class> lClases = getClasses(list);

        int yrate = h / (max + 1);
        int xrate = w / (list.size() + 1);

        Graphics2D g2 = bi.createGraphics();
        int x = (2 * xrate) / 2; // +1 up and /2 there, small bordering
        int y = h - (yrate / 2);

        for (int xx = 1; xx <= max; xx++) {
            g2.setColor(Color.GRAY);
            g2.drawLine(0, xx * yrate + 1 - yrate / 2, w, xx * yrate + 1 - yrate / 2);
            g2.setColor(Color.black);
            g2.drawString((max - xx + 1) + "x", 1, xx * yrate);
        }
        for (DayStatst day : list) {
            g2.setColor(Color.black);
            g2.drawString(day.getAdaptedDateTime(), x, y + g2.getFontMetrics().getHeight());
            int i = -((lClases.size() * 3) / 2) * CWIDTH;
            for (Class cl : lClases) {
                g2.setColor(Color.black);
                drawWide(g2, x + i, y, (day.getStarts(cl) * yrate));
                i += CWIDTH;
                g2.setColor(Color.green);
                drawWide(g2, x + i, y, (day.getPasses(cl) * yrate));
                i += CWIDTH;
                g2.setColor(Color.red);
                drawWide(g2, x + i, y, (day.getFails(cl) * yrate));
                i += CWIDTH;
            }
            g2.setColor(Color.GRAY);
            g2.drawLine(x + i - 1, 0, x + i - 1, h);
            x += xrate;
        }
        try {
            img++;
            ImageIO.write(bi, "png", new File("/home/jvanek/Desktop/a" + img + ".png"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bi;
    }

    private static void drawWide(Graphics2D g2, int x1, int y1, int h) {
        drawWide(g2, x1, y1, CWIDTH, h);
    }

    private static void drawWide(Graphics2D g2, int x1, int y1, int w, int h) {
        if (h == 0) {
            h = 5;
        } else if (h < 0) {
//yah, lower then zero keep 0 = fakes
            h = 1;
        }
        g2.fillRect(x1, y1 - h, w, h);
    }

    public static BufferedImage generateTimeGraph(Map<Date, DayTimes> data, int columns) {
        List<DayTimes> list = getTimeDataAsList(data);
        int max = getMaxTimeInTimes(list);
        int maxc = getMaxCyclesuseInTimes(list);
        //lets asusme those are always smaller for now ^
        int h = (max + 1) * 25 + new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR).createGraphics().getFontMetrics().getHeight();
        int w = (list.size() + 1) * columns * 1/*one column per record*/ * CWIDTH;
        return generateTimelGraph(w, h, data, columns);
    }

    private static BufferedImage generateTimelGraph(int w, int h, Map<Date, DayTimes> data, int columns) {
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
//        if (data.isEmpty()) {
//            return bi;
//        }
        List<DayTimes> list = getTimeDataAsList(data);
        //FIXME fill voids 28 31 -> 28 29 30 31 by 0,0,0 on all CLASSES and recomp xrate
        //Collections.reverse(list);
        int max = getMaxTimeInTimes(list);//minutes!
        int maxc = getMaxCyclesuseInTimes(list);;
        //List<Class> lClases = getClasses(list);
        // really always three? maybe really base on 

        int yrate = h / (max + 1);
        int xrate = w / (list.size() + 1);

        Graphics2D g2 = bi.createGraphics();
        int x = (3 * xrate) / 2; // +1 up and /2 there, small bordering
        int y = h - (yrate / 2);

        for (int xx = 1; xx <= max; xx++) {
            g2.setColor(Color.GRAY);
            g2.drawLine(0, xx * yrate + 1 - yrate / 2, w, xx * yrate + 1 - yrate / 2);
            g2.setColor(Color.black);
            g2.drawString((max - xx + 1) + "x", 1, xx * yrate);
        }
        for (DayTimes day : list) {
            g2.setColor(Color.black);
            g2.drawString(day.getAdaptedDateTime(), x, y + g2.getFontMetrics().getHeight());
            int i = -((columns * 1) / 2) * CWIDTH;
            if (day.isNothing()) {
            } else {
                if (day.getCyclesOperationms() >= 0) {
                    g2.setColor(Color.yellow);
                    drawWide(g2, x + i, y, (day.getCyclesOperationms() * yrate));
                    i += CWIDTH;
                }
                if (day.getTrainTime() >= 0) {
                    g2.setColor(Color.green);
                    drawWide(g2, x + i, y, (day.getTrainTimeMinutes() * yrate));
                    i += CWIDTH;
                }
                if (day.getExercisesTimes() >= 0) {
                    g2.setColor(Color.black);
                    drawWide(g2, x + i, y, (day.getExercisesTimesMinutes() * yrate));
                    i += CWIDTH;
                }
            }
            g2.setColor(Color.GRAY);
            g2.drawLine(x + i - 1, 0, x + i - 1, h);
            x += xrate;
        }
        try {
            img++;
            ImageIO.write(bi, "png", new File("/home/jvanek/Desktop/a" + img + ".png"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bi;
    }

}
