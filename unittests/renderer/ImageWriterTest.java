package renderer;

import org.junit.jupiter.api.Test;
import java.awt.*;


/**
 * @author aviya and sima
 */
class ImageWriterTest {

    @Test
    void writeToImage() {
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