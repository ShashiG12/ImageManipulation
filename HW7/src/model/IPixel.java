package model;

/**
 * This interface contains methods that get a Pixel's properties. Any and all Pixel functionality
 * will first be added to this interface.
 */
public interface IPixel {

  /**
   * Gets the x-coordinate position of a Pixel.
   * @return an int representing the x-coordinate
   */
  int getXCord();

  /**
   * Gets the y-coordinate position of a Pixel.
   * @return an int representing the y-coordinate
   */
  int getYCord();

  /**
   * Provides a new color to the Pixel based on the surrounding Pixels.
   * @param rgb the channel values for a Pixel.
   */
  void assignColor(int[] rgb);

  /**
   * The average color of a pixel cluster.
   * @return the average color value.
   */
  int[] getAverageColor();

}
