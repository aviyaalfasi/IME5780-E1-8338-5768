package renderer;

import org.junit.jupiter.api.Test;
import java.awt.*;

/**
 * @author aviya and sima
 */

class ImageWriterTest {

    @Test
    void writeToImage() {
        /**
         * Produce a scene with view plane size 1000*1600, resolution 500*800 and render it into a jpeg image with a 10*16 grid.
         */
        String image_name = "MyFirstImageTest";
        int width = 1000;
        int height = 1600;
        int nx = 500;
        int ny = 800;
        ImageWriter imageWriter = new ImageWriter(image_name, width, height, nx, ny);
        for (int col = 0; col < ny; col++)
            for (int row = 0; row < nx; row++)
                if (col % 10 == 0 || row % 10 == 0)
                    imageWriter.writePixel(row, col, Color.YELLOW);

        imageWriter.writeToImage();
    }
}