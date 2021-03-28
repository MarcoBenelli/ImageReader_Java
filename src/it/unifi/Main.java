package it.unifi;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        File inputDir = new File("input_images/");
        File outputDir = new File("output_images/");

        File[] outputFiles = outputDir.listFiles();
        assert outputFiles != null;

        for (File f : outputFiles)
            f.delete();
        File[] imgNames = inputDir.listFiles();
        assert imgNames != null;


        SyncImgReader ir = new SyncImgReader(8);
        ir.read(imgNames);
        BufferedImage[] images = ir.getImages();

        /*
        for (int i = 0; i < images.length; i++) {
            while (!images[i].isDone())
                ;
            System.out.printf("image %d is done\n", i);
        }
        */

        for (int i = 0; i < images.length; i++) {
            try {
                ImageIO.write(images[i], "jpg", new File("output_images/" + imgNames[i].getName()));
                System.out.printf("saved image %d\n", i);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}