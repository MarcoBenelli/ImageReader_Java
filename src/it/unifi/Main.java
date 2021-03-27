package it.unifi;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * This class demonstrates how to load an Image from an external file
 */
public class Main {

    public static void main(String[] args) {
        File inputDir = new File("input_images");

        File[] imgNames = inputDir.listFiles();
        assert imgNames != null;

        BufferedImage[] images = new BufferedImage[imgNames.length];

        SequentialImgReader sir = new SequentialImgReader();
        try {
            sir.read(images, imgNames);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < images.length; i++) {
            try {
                ImageIO.write(images[i], "jpg", new File("output_images/" + imgNames[i].getName()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}