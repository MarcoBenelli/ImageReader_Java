package it.unifi;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SequentialImgReader {
    public void read(File[] imgNames) throws IOException {
        images = new BufferedImage[imgNames.length];
        for (int i = 0; i < imgNames.length; i++) {
            images[i] = ImageIO.read(imgNames[i]);
        }
    }

    public BufferedImage[] getImages() {
        return images;
    }

    private BufferedImage[] images;
}
