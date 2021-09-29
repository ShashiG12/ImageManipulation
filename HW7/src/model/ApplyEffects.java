package model;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * This class contains implementations for 2 types of image modifications. The 2 types are: Filters
 * and Color Transformations The implementation of any new types of image modifications will be
 * placed here.
 */
public class ApplyEffects implements IApplyEffects {

  @Override
  public Image applyFilter(Image image, double[][] filter) throws IllegalArgumentException {

    this.checkInputs(image, filter);

    int[][][] image1 = new int[image.getHeight()][image.getWidth()][3];
    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        image1[i][j] = this.newPixel(this.getKernel(i, j, image.getImage(), filter.length), filter);
      }

    }
    return new SimpleImage(this.clamp(image1));
  }


  // retrieves each Kernel based on the given image.
  private int[][][] getKernel(int row, int column, int[][][] image2, int filterLength) {

    int[][][] kernel = new int[filterLength][filterLength][3];

    for (int i = 0; i < filterLength; i++) {
      for (int j = 0; j < filterLength; j++) {

        int a = i + row;
        int b = j + column;
        boolean checkDimensions = a > image2.length - 1 || b > image2[0].length - 1;

        if (checkDimensions) {
          kernel[i][j][0] = 0;
          kernel[i][j][1] = 0;
          kernel[i][j][2] = 0;
        } else {
          kernel[i][j][0] = image2[i + (row)][j + (column)][0];
          kernel[i][j][1] = image2[i + (row)][j + (column)][1];
          kernel[i][j][2] = image2[i + (row)][j + (column)][2];
        }
      }
    }
    return kernel;
  }


  // changes each specific Pixel value to match that of the filter's specifications
  private int[] newPixel(int[][][] kernel, double[][] filter) {
    int[] pixelValue = new int[3];
    for (int i = 0; i < kernel.length; i++) {
      for (int j = 0; j < kernel.length; j++) {
        pixelValue[0] += kernel[i][j][0] * filter[i][j];
        pixelValue[1] += kernel[i][j][1] * filter[i][j];
        pixelValue[2] += kernel[i][j][2] * filter[i][j];
      }

    }
    return pixelValue;
  }


  @Override
  public Image applyColorFilter(Image image, double[][] filter) throws IllegalArgumentException {

    this.checkInputs(image, filter);

    int[][][] image1 = new int[image.getHeight()][image.getWidth()][3];
    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        for (int k = 0; k < 3; k++) {
          image1[i][j][k] = (int) applyColorHelper(image.getImage()[i][j], filter[k]);
        }
      }
    }
    return new SimpleImage(clamp(image1));
  }

  @Override
  public Image applyMosaic(Image image, int seed) {

    int[][][] imageCopy = image.getImage();
    int height = image.getHeight();
    int width = image.getWidth();
    boolean isSeedNotValid = (seed > height * width) || (seed < 1);
    boolean isSeedImage = (seed == height * width);
    if (isSeedNotValid) {
      throw new IllegalArgumentException("Seed value is not valid");
    } else if (isSeedImage) {
      return image;

    } else {
      int[][][] mosaicImage = new int[height][width][3];
      ArrayList<Pixel> pixels = this.getNeighborPixels(image, seed);
      for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
          ArrayList<Double> distances = new ArrayList<>();
          for (int k = 0; k < seed; k++) {
            double x = Math.pow(j - pixels.get(k).getXCord(), 2);
            double y = Math.pow(i - pixels.get(k).getYCord(), 2);
            double finalResult = Math.sqrt(x + y);
            distances.add(finalResult);
          }

          int minIndex = distances.indexOf(Collections.min(distances));
          pixels.get(minIndex).assignColor(imageCopy[i][j]);
        }
      }

      for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
          ArrayList<Double> distances1 = new ArrayList<>();
          for (int k = 0; k < seed; k++) {
            double x = Math.pow(j - pixels.get(k).getXCord(), 2);
            double y = Math.pow(i - pixels.get(k).getYCord(), 2);
            double finalResult = Math.sqrt(x + y);
            distances1.add(finalResult);
          }

          int minIndex = distances1.indexOf(Collections.min(distances1));
          mosaicImage[i][j] = pixels.get(minIndex).getAverageColor();
        }
      }

      return new SimpleImage(mosaicImage);
    }
  }


  // Used in selecting seeds for applyMosaic method. Gets random pixels.
  private ArrayList<Pixel> getNeighborPixels(Image image, int seed) {

    ArrayList<Pixel> pixels = new ArrayList<>();

    Random rand = new Random();

    int x = rand.nextInt(image.getWidth());
    int y = rand.nextInt(image.getHeight());
    Pixel newPixel = new Pixel(x, y);
    pixels.add(newPixel);

    while (pixels.size() != seed) {
      x = rand.nextInt(image.getWidth());
      y = rand.nextInt(image.getHeight());
      newPixel = new Pixel(x, y);

      for (Pixel p : pixels) {
        boolean equalX = newPixel.getXCord() == p.getXCord();
        boolean equalY = newPixel.getYCord() == p.getYCord();

        if (!(equalX && equalY)) {
          pixels.add(newPixel);
          break;
        }
      }
    }
    return pixels;

  }


  @Override
  public Image applyDownSize(Image image, int height, int width) {

    if (height < 0 || height > image.getHeight()
        || width < 0 || width > image.getWidth()) {
      throw new IllegalArgumentException("Downsizing dimensions not valid");
    }

    int[][][] rgb = image.getImage();

    BufferedImage output = new BufferedImage(rgb[0].length, rgb.length, BufferedImage.TYPE_INT_RGB);
    for (int i = 0; i < rgb.length; i++) {
      for (int j = 0; j < rgb[0].length; j++) {
        int r = rgb[i][j][0];
        int g = rgb[i][j][1];
        int b = rgb[i][j][2];
        int color = (r << 16) + (g << 8) + b;
        output.setRGB(j, i, color);
      }
    }

    BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics2D = newImage.createGraphics();
    graphics2D.drawImage(output, 0, 0, width, height, null);
    graphics2D.dispose();

    int[][][] imageResult = new int[newImage.getHeight()][newImage.getWidth()][3];

    for (int i = 0; i < newImage.getHeight(); i++) {
      for (int j = 0; j < newImage.getWidth(); j++) {
        int rgbValue = newImage.getRGB(j, i);
        imageResult[i][j][0] = new Color(rgbValue).getRed();
        imageResult[i][j][1] = new Color(rgbValue).getGreen();
        imageResult[i][j][2] = new Color(rgbValue).getBlue();
      }
    }
    return new SimpleImage(imageResult);
  }

  // helper to apply color filter to individual pixels
  private double applyColorHelper(int[] pixel, double[] filter) {
    double newPixel = 0;
    for (int i = 0; i < filter.length; i++) {
      newPixel += pixel[i] * filter[i];
    }
    return newPixel;
  }

  // Ensures that each RGB value is between 0 and 255.
  // If less than 0, sets value to 0, else if greater than 255 sets value to 255
  private int[][][] clamp(int[][][] image) {
    for (int i = 0; i < image.length; i++) {
      for (int j = 0; j < image[0].length; j++) {
        for (int k = 0; k < 3; k++) {
          if (image[i][j][k] < 0) {
            image[i][j][k] = 0;
          }
          if (image[i][j][k] > 255) {
            image[i][j][k] = 255;
          }
        }
      }
    }
    return image;
  }

  // checks to make sure neither of the inputs are null or invalid
  private void checkInputs(Image image, double[][] filter) {
    boolean isLengthOdd = (filter.length % 2 == 0);
    boolean isSameDimension = (filter.length == filter[0].length);

    if (image == null || isLengthOdd || !isSameDimension) {
      throw new IllegalArgumentException("Invalid inputs");
    }
  }


}
