package ru.mirea.oop.practice.coursej.s131250;

import ru.mirea.oop.practice.coursej.Configuration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;


public class ImageBuilder {


    public static String textWrite(String im1, String text) {
        Random rnd = new Random(System.currentTimeMillis());
        int number = rnd.nextInt(Integer.MAX_VALUE);
        try {
            BufferedImage img1 = ImageIO.read(new File(im1));
            Graphics2D g2d = img1.createGraphics();
            g2d.setFont(new Font("Serif", Font.BOLD, 14));
            FontMetrics fm = g2d.getFontMetrics();
            int x = fm.stringWidth(text);
            int y = fm.getHeight();

            BufferedImage result = new BufferedImage(
                    img1.getWidth() + x, img1.getHeight() + y + 5, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d2 = result.createGraphics();
            g2d2.drawImage(img1, 0, 0, null);
            g2d2.setPaint(Color.black);
            g2d2.setFont(new Font("Serif", Font.PLAIN, 14));
            g2d2.drawString(text, 0, img1.getHeight() + y);
            g2d2.dispose();
            ImageIO.write(result, "gif", new File(Configuration.getFileName(number + ".gif")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file = new File(im1);
        if (!file.delete()) {
            System.out.println("File delete error");
        }

        return Configuration.getFileName(number + ".gif");
    }


    public static String textWrite(String text) {
        Random rnd = new Random(System.currentTimeMillis());
        int number = rnd.nextInt(Integer.MAX_VALUE);
        try {
            BufferedImage img = new BufferedImage(80, 20, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = img.createGraphics();
            g2d.setPaint(Color.red);
            g2d.setFont(new Font("Serif", Font.BOLD, 16));
            FontMetrics fm = g2d.getFontMetrics();
            int x = fm.stringWidth(text);
            int y = fm.getHeight();

            BufferedImage result = new BufferedImage(
                    x, y, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d2 = result.createGraphics();
            g2d2.drawImage(img, 0, 0, null);
            g2d2.setPaint(Color.black);
            g2d2.setFont(new Font("Serif", Font.BOLD, 16));
            g2d2.drawString(text, 0, y);
            g2d2.dispose();
            ImageIO.write(result, "gif", new File(Configuration.getFileName(number + ".gif")));
        } catch (IOException e) {
            e.printStackTrace();
        }


        return Configuration.getFileName(number + ".gif");
    }

    public static String combImages(String im1, String im2) {
        Random rnd = new Random(System.currentTimeMillis());
        int number = rnd.nextInt(Integer.MAX_VALUE);
        try {
            BufferedImage img1 = ImageIO.read(new File(im1));
            BufferedImage img2 = ImageIO.read(new File(im2));

            int h, w;
            h = img1.getHeight() + img2.getHeight();
            if (img1.getWidth() > img2.getWidth()) {
                w = img1.getWidth();
            } else {
                w = img2.getWidth();
            }

            BufferedImage im = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            im.getGraphics().drawImage(img1, 0, 0, null);
            im.getGraphics().drawImage(img2, 0, img1.getHeight(), null);

            ImageIO.write(im, "gif", new File(Configuration.getFileName(number + ".gif")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File(im1);
        if (!file.delete()) {
            System.out.println("File delete error");
        }

        File file2 = new File(im2);
        if (!file2.delete()) {
            System.out.println("File delete error");
        }

        return Configuration.getFileName(number + ".gif");
    }


    public static String combImagesfromURL(String im1, String im2) {
        Random rnd = new Random(System.currentTimeMillis());
        int number = rnd.nextInt(Integer.MAX_VALUE);
        try {
            BufferedImage img1 = ImageIO.read(new File(im1));
            BufferedImage img2 = ImageIO.read(new URL(im2));

            int h, w;
            h = img1.getHeight() + img2.getHeight();
            if (img1.getWidth() > img2.getWidth()) {
                w = img1.getWidth();
            } else {
                w = img2.getWidth();
            }

            BufferedImage im = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            im.getGraphics().drawImage(img1, 0, 0, null);
            im.getGraphics().drawImage(img2, 0, img1.getHeight(), null);

            ImageIO.write(im, "gif", new File(Configuration.getFileName(number + ".gif")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File(im1);
        if (!file.delete()) {
            System.out.println("File delete error");
        }

        return Configuration.getFileName(number + ".gif");
    }

}
