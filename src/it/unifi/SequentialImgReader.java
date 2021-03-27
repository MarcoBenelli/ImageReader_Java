package it.unifi;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SequentialImgReader implements ImgReader {
    @Override
    public void read(BufferedImage[] images, File[] imgNames) throws IOException {
        for (int i = 0; i < imgNames.length; i++) {
            images[i] = ImageIO.read(imgNames[i]);
        }
    }
}
