package model;

/**
 * This interface represents an Image. The components of the an Image are the following: Height,
 * Width, RGB values. The structure of an image follows the format of [Height][Width][RGB values] -
 * constructed as a triple nested array.
 */
public interface Image {


  /**
   * Retrieves the width of the image.
   *
   * @return an integer representing the width.
   */
  int getWidth();


  /**
   * Retrieves the height of the image.
   *
   * @return an integer representing the height.
   */
  int getHeight();


  /**
   * Retrieves the image.
   *
   * @return a triple nested array representation of the image. We choose a triple nested array to
   *         represent an image because any image comprises of a height, width,
   *         and each pixel within the
   *         image has 3 RGB values. Since the height, width,
   *         and RGB values are fixed beforehand, we felt
   *         that an array would be the most efficient representation.
   */
  int[][][] getImage();
}
