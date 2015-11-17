package ru.mirea.oop.practice.coursej.s131250;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mirea.oop.practice.coursej.Configuration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;

class ImageBuilder {
    private static final Logger logger = LoggerFactory.getLogger(ImageBuilder.class);
    private static final int TEXT_FONT_SIZE = 14;
    private static final int TITLE_FONT_SIZE = 16;
    private static final String IMAGE_TITLE = "VK Bot /WolframAlpha/ results";
    private static final String IMAGE_FILE_TYPE = "gif";
    private static final String IMAGE_FILE_EXTENSION = ".gif";
    private String fileName;
    private static final AtomicLong counter = new AtomicLong(1);


    public ImageBuilder() {
        long counterValue = counter.getAndIncrement();
        BufferedImage img = new BufferedImage(80, 20, BufferedImage.TYPE_INT_ARGB);
        FontMetrics imgMetrics = getImageMetrics(img, true);

        BufferedImage result = new BufferedImage(
                imgMetrics.stringWidth(IMAGE_TITLE), imgMetrics.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d2 = result.createGraphics();
        g2d2.drawImage(img, 0, 0, null);
        g2d2.setPaint(Color.black);
        g2d2.setFont(new Font(Font.SERIF, Font.BOLD, TITLE_FONT_SIZE));
        g2d2.drawString(IMAGE_TITLE, 0, imgMetrics.getHeight());
        g2d2.dispose();
        try {
            ImageIO.write(result, IMAGE_FILE_TYPE, new File(Configuration.getFileName(counterValue + IMAGE_FILE_EXTENSION)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileName = counterValue + IMAGE_FILE_EXTENSION;
    }

    private FontMetrics getImageMetrics(BufferedImage img, boolean isTitle) {
        Graphics2D g2d = img.createGraphics();
        if (isTitle) {
            g2d.setFont(new Font(Font.SERIF, Font.BOLD, TITLE_FONT_SIZE));
        } else {
            g2d.setFont(new Font(Font.SERIF, Font.BOLD, TEXT_FONT_SIZE));
        }
        return g2d.getFontMetrics();
    }

    private void writeImageFile(BufferedImage result) throws IOException {
        long counterValue = counter.getAndIncrement();
        ImageIO.write(result, IMAGE_FILE_TYPE, new File(Configuration.getFileName(counterValue + IMAGE_FILE_EXTENSION)));
        File file = new File(Configuration.getFileName(fileName));
        if (!file.delete()) {
            logger.error("Ошибка удаления файла " + Configuration.getFileName(fileName));
        }
        fileName = counterValue + IMAGE_FILE_EXTENSION;
    }

    public void writeTextOnImage(String text) throws IOException {
        BufferedImage img = ImageIO.read(new File(Configuration.getFileName(fileName)));
        FontMetrics imgMetrics = getImageMetrics(img, false);

        BufferedImage result = new BufferedImage(
                img.getWidth() + imgMetrics.stringWidth(text), img.getHeight() + imgMetrics.getHeight() + 5, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d2 = result.createGraphics();
        g2d2.drawImage(img, 0, 0, null);
        g2d2.setPaint(Color.black);
        g2d2.setFont(new Font(Font.SERIF, Font.PLAIN, TEXT_FONT_SIZE));
        g2d2.drawString(text, 0, img.getHeight() + imgMetrics.getHeight());
        g2d2.dispose();
        writeImageFile(result);
    }


    public void pasteImageFromURL(String imageURL) throws Exception {
        BufferedImage img1 = ImageIO.read(new File(Configuration.getFileName(fileName)));
        BufferedImage img2 = ImageIO.read(new URL(imageURL));

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

        writeImageFile(im);
    }

    public String getFullFileName() {
        return Configuration.getFileName(fileName);
    }
}