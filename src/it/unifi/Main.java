package it.unifi;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) throws Exception {
        File inputDir = new File("input_images/");
        int numTests = 16;
        int maxNumThreads = Runtime.getRuntime().availableProcessors();

        /*
        File outputDir = new File("output_images/");
        File[] outputFiles = outputDir.listFiles();
        assert outputFiles != null;
        for (File f : outputFiles)
            f.delete();
        */

        File[] imgNames = inputDir.listFiles();
        assert imgNames != null;

        // Test sequential version
        System.out.println("Sequential version");
        long time = 0;
        for (int i = 0; i < numTests; i++) {
            time -= System.currentTimeMillis();
            SequentialImgReader imgReader = new SequentialImgReader();
            imgReader.read(imgNames);
            BufferedImage[] images = imgReader.getImages();
            time += System.currentTimeMillis();
        }
        time /= numTests;
        System.out.println(time);

        // Test synchronous version
        System.out.println("Synchronous parallel version");
        for (int numThreads = 1; numThreads <= maxNumThreads; numThreads++) {
            time = 0;
            for (int i = 0; i < numTests; i++) {
                time -= System.currentTimeMillis();
                SyncImgReader syncImgReader = new SyncImgReader(numThreads);
                syncImgReader.read(imgNames);
                BufferedImage[] images = syncImgReader.getImages();
                time += System.currentTimeMillis();
            }
            time /= numTests;
            System.out.println("    " + numThreads + ": " + time);
        }

        // Test asynchronous version
        System.out.println("Asynchronous parallel version");
        for (int numThreads = 1; numThreads <= maxNumThreads; numThreads++) {
            time = 0;
            for (int i = 0; i < numTests; i++) {
                time -= System.currentTimeMillis();
                AsyncImgReader asyncImgReader = new AsyncImgReader(numThreads);
                asyncImgReader.read(imgNames);
                Future<BufferedImage>[] images = asyncImgReader.getImages();
                for (Future<BufferedImage> image : images) {
                    image.get();
                }
                time += System.currentTimeMillis();
            }
            time /= numTests;
            System.out.println("    " + numThreads + ": " + time);
        }

        //ImageIO.write(images[i], "jpg", new File("output_images/" + imgNames[i].getName()));
    }
}