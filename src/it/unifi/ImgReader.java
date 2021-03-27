package it.unifi;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public interface ImgReader {
    void read(BufferedImage[] images, File[] imgNames) throws IOException;
}
