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
        ImgReaderThread[] threads = new ImgReaderThread[numThreads];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new ImgReaderThread(images, imgNames, numThreads, i);
            threads[i].start();
        }

        for (ImgReaderThread thread : threads)
            thread.join();
        //System.out.println("read all images");
    }

    public BufferedImage[] getImages() {
        return images;
    }

    private final int numThreads;
    private BufferedImage[] images;

    static class ImgReaderThread extends Thread {
        public ImgReaderThread(BufferedImage[] images, File[] imgNames, int numThreads, int threadIndex) {
            this.images = images;
            this.imgNames = imgNames;
            this.numThreads = numThreads;
            this.threadIndex = threadIndex;
        }

        @Override
        public void run() {
            for (int i = threadIndex; i < images.length; i += numThreads) {
                //System.out.printf("Thread %d reads image %d\n", threadIndex, i);
                try {
                    images[i] = ImageIO.read(imgNames[i]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private final BufferedImage[] images;
        private final File[] imgNames;
        private final int numThreads;
        private final int threadIndex;
    }
}
