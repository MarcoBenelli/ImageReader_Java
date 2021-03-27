package it.unifi;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SyncImgReader {
    public SyncImgReader(int numThreads) {
        this.numThreads = numThreads;
    }

    public void read(File[] imgNames) throws InterruptedException {
        images = new BufferedImage[imgNames.length];
        ImgReaderThread[] threads = new ImgReaderThread[images.length];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new ImgReaderThread(imgNames[i]);
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
            images[i] = threads[i].getImage();
        }
        System.out.println("read all images");
    }

    public BufferedImage[] getImages() {
        return images;
    }

    private final int numThreads;
    private BufferedImage[] images;

    static class ImgReaderThread extends Thread {
        public ImgReaderThread(File imgName) {
            this.imgName = imgName;
        }

        @Override
        public void run() {
            System.out.printf("read image %s\n", imgName);
            try {
                image = ImageIO.read(imgName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public BufferedImage getImage() {
            return image;
        }

        private final File imgName;
        private BufferedImage image;
    }
}
