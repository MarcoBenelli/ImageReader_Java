package it.unifi;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.Future;
import java.util.Arrays;

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

        File[] imgNamesSubset = Arrays.copyOfRange(imgNames, 0, (int) Math.sqrt(imgNames.length * maxNumThreads));

        // Test sequential version
        long time = 0;
        for (int i = 0; i < numTests; i++) {
            time -= System.currentTimeMillis();
            SequentialImgReader imgReader = new SequentialImgReader();
            imgReader.read(imgNamesSubset);
            BufferedImage[] images = imgReader.getImages();
            time += System.currentTimeMillis();
        }
        time /= numTests;
        System.out.println("Elapsed time for sequential implementation with " + imgNamesSubset.length + " images: " + time + " ms");

        // Test synchronous version
        for (int numThreads = 1; numThreads <= maxNumThreads; numThreads++) {
            time = 0;
            for (int i = 0; i < numTests; i++) {
                time -= System.currentTimeMillis();
                SyncImgReader syncImgReader = new SyncImgReader(numThreads);
                syncImgReader.read(imgNamesSubset);
                BufferedImage[] images = syncImgReader.getImages();
                time += System.currentTimeMillis();
            }
            time /= numTests;
            System.out.println("Elapsed time for parallel synchronous implementation with " + numThreads + " threads and " + imgNamesSubset.length + " images: " + time + " ms");
        }

        // Test asynchronous version
        for (int numThreads = 1; numThreads <= maxNumThreads; numThreads++) {
            time = 0;
            for (int i = 0; i < numTests; i++) {
                time -= System.currentTimeMillis();
                AsyncImgReader asyncImgReader = new AsyncImgReader(numThreads);
                asyncImgReader.read(imgNamesSubset);
                Future<BufferedImage>[] images = asyncImgReader.getImages();
                for (Future<BufferedImage> image : images) {
                    image.get();
                }
                time += System.currentTimeMillis();
            }
            time /= numTests;
            System.out.println("Elapsed time for parallel asynchronous implementation with " + numThreads + " threads and " + imgNamesSubset.length + " images: " + time + " ms");
        }


        for (int numImgs = maxNumThreads; numImgs <= imgNames.length; numImgs *= 2) {
            time = 0;
            for (int i = 0; i < numTests; i++) {
                File[] imgNamesTest = Arrays.copyOfRange(imgNames, 0, numImgs);
                time -= System.currentTimeMillis();
                SequentialImgReader imgReader = new SequentialImgReader();
                imgReader.read(imgNamesTest);
                BufferedImage[] images = imgReader.getImages();
                time += System.currentTimeMillis();
            }
            time /= numTests;
            System.out.println("Elapsed time for sequential implementation with " + numImgs + " images: " + time + " ms");
        }

        for (int numImgs = maxNumThreads; numImgs <= imgNames.length; numImgs *= 2) {
            time = 0;
            for (int i = 0; i < numTests; i++) {
                File[] imgNamesTest = Arrays.copyOfRange(imgNames, 0, numImgs);
                time -= System.currentTimeMillis();
                SyncImgReader imgReader = new SyncImgReader(maxNumThreads);
                imgReader.read(imgNamesTest);
                BufferedImage[] images = imgReader.getImages();
                time += System.currentTimeMillis();
            }
            time /= numTests;
            System.out.println("Elapsed time for parallel synchronous implementation with " + maxNumThreads + " threads and " + numImgs + " images: " + time + " ms");
        }

        for (int numImgs = maxNumThreads; numImgs <= imgNames.length; numImgs *= 2) {
            time = 0;
            for (int i = 0; i < numTests; i++) {
                File[] imgNamesTest = Arrays.copyOfRange(imgNames, 0, numImgs);
                time -= System.currentTimeMillis();
                AsyncImgReader asyncImgReader = new AsyncImgReader(maxNumThreads);
                asyncImgReader.read(imgNamesTest);
                Future<BufferedImage>[] images = asyncImgReader.getImages();
                for (Future<BufferedImage> image : images) {
                    image.get();
                }
                time += System.currentTimeMillis();
            }
            time /= numTests;
            System.out.println("Elapsed time for parallel asynchronous implementation with " + maxNumThreads + " threads and " + numImgs + " images: " + time + " ms");
        }
        //ImageIO.write(images[i], "jpg", new File("output_images/" + imgNames[i].getName()));
    }
}