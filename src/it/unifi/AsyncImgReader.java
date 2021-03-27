package it.unifi;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.*;

public class AsyncImgReader {
    public AsyncImgReader(int numThreads) {
        this.numThreads = numThreads;
    }

    public void read(File[] imgNames) {
        images = new Future[imgNames.length];
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        for (int i = 0; i < imgNames.length; i++) {
            images[i] = executor.submit(new ImgReaderThread(imgNames[i]));
        }
        executor.shutdown();
        System.out.println("read all images");
    }

    public Future<BufferedImage>[] getImages() {
        return images;
    }

    private final int numThreads;
    private Future<BufferedImage>[] images;

    static class ImgReaderThread implements Callable<BufferedImage> {
        public ImgReaderThread(File imgName) {
            this.imgName = imgName;
        }

        @Override
        public BufferedImage call() throws IOException {
            System.out.printf("read image %s\n", imgName);
            return ImageIO.read(imgName);
        }

        private final File imgName;
    }
}

