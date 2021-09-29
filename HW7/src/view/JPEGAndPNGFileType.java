package view;

import static javax.imageio.ImageIO.write;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import model.Image;

/**
 * Class to handle the importing and exporting of JPEG and PNG file types.
 */
public class JPEGAndPNGFileType implements FileTypeUtil {

  BufferedImage image;

  @Override
  public int[][][] read(String filename) {

    try {
      this.image = ImageIO.read(new FileInputStream(filename));
      int[][][] imageResult = new int[this.image.getHeight()][this.image.getWidth()][3];

      for (int i = 0; i < image.getHeight(); i++) {
        for (int j = 0; j < image.getWidth(); j++) {
          int rgb = image.getRGB(j, i);
          imageResult[i][j][0] = new Color(rgb).getRed();
          imageResult[i][j][1] = new Color(rgb).getGreen();
          imageResult[i][j][2] = new Color(rgb).getBlue();
        }
      }

      return imageResult;
    } catch (IOException e) {
      e.printStackTrace();
    }

    return new int[0][0][0];
  }

  @Override
  public void export(Image image, String filename) {

    int height = image.getHeight();
    int width = image.getWidth();

    BufferedImage exportImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int r = image.getImage()[i][j][0];
        int g = image.getImage()[i][j][1];
        int b = image.getImage()[i][j][2];
        int newValue = (r << 16) + (g << 8) + b;
        exportImage.setRGB(j, i, newValue);
      }
    }

    String extension = filename.substring(filename.indexOf(".") + 1);

    try {
      write(exportImage, extension, new File(filename));
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
