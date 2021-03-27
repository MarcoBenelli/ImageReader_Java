package it.unifi;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Main {

    public static void main(String[] args) {
        File inputDir = new File("input_images");

        File[] imgNames = inputDir.listFiles();
        assert imgNames != null;

        AsyncImgReader ir = new AsyncImgReader(8);
        ir.read(imgNames);
        Future<BufferedImage>[] images = ir.getImages();

        /*
        for (int i = 0; i < images.length; i++) {
            while (!images[i].isDone())
                ;
            System.out.printf("image %d is done\n", i);
        }
        */

        for (int i = 0; i < images.length; i++) {
            try {
                ImageIO.write(images[i].get(), "jpg", new File("output_images/" + imgNames[i].getName()));
                System.out.printf("gotten image %d\n", i);
            } catch (IOException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}