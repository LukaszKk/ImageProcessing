package image.processing;

import java.awt.image.BufferedImage;

public class Reconstruction extends ImageTransform {
    /**
     * Reconstruction of the image using mask and pattern
     *
     * @param image  BufferedImage object (mask)
     * @param image2 BufferedImage object (pattern)
     * @return new BufferedImage object
     */
    public BufferedImage reconstruct(BufferedImage image, BufferedImage image2) {
        BufferedImage newImage;
        BufferedImage res;
        newImage = image;

        do {
            res = newImage;
            newImage = dilatation(newImage, 3);
            for (int i = 0; i < image.getWidth(); i++) {
                for (int j = 0; j < image.getHeight(); j++) {
                    if (newImage.getRGB(i, j) == white && image2.getRGB(i, j) == black)
                        newImage.setRGB(i, j, black);
                }
            }
        }
        while (!isEqual(res, newImage));

        return res;
    }

    /**
     * Compare two images
     *
     * @param image  BufferedImage object
     * @param image2 BufferedImage object
     * @return true if images are equal, otherwise false
     */
    private boolean isEqual(BufferedImage image, BufferedImage image2) {
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                if (image.getRGB(i, j) != image2.getRGB(i, j))
                    return false;
            }
        }

        return true;
    }
}
