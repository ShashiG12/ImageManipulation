package model;

/**
 * This class implements the functionality outlined in the Image interface.This class is responsible
 * for hosting getters that give access to essential properties of an image.
 */
public class SimpleImage implements Image {

  // INVARIANT: Height is always positive
  private final int height;
  // INVARIANT: Width is always positive
  private final int width;

  private int[][][] image;


  /**
   * Constructs the SimpleImage.
   *
   * @param image used to derive its properties.
   */
  public SimpleImage(int[][][] image) {
    if (image == null) {
      throw new IllegalArgumentException("Image cannot be null");
    }
    this.height = image.length;
    this.width = image[0].length;
    this.image = image;
  }


  @Override
  public int getWidth() {
    return this.width;
  }

  @Override
  public int getHeight() {
    return this.height;
  }

  @Override
  public int[][][] getImage() {
    return this.image;
  }
}
