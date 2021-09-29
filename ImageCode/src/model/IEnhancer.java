package model;

/**
 * This interface represents the special effects that can be applied to an image. Some of the
 * special effects include: blurring, sharpening, monochrome, and checkerboard. Any future special
 * effect implementations will be first added to this interface if need be. Else they will be added
 * as an enum and the getEffect will process it.
 */

public interface IEnhancer {

  /**
   * This makes the image appear like a checkerboard.
   */
  void checkerBoardImage(int size);


  /**
   * This method loads a given image into the model.
   */
  void loadImage(Image image);

  /**
   * Returns the corresponding image.
   *
   * @return this local image
   */
  Image getImageAlt();

  /**
   * This method removes a special effect the user performed and does not want anymore.
   */
  void undo();

  /**
   * Executes the appropriate filter modification.
   *
   * @param effectType the given filter to be applied
   */
  void getEffect(EffectType effectType);


  /**
   * Sets the mosaic filter to operate on the desired seed.
   * @param seed the seed value.
   */
  void setSeed(int seed);

  /**
   * Sets the desired height to be downsized to.
   * @param height the desired height.
   */
  void setHeight(int height);

  /**
   * Sets the desired width to be downsized to.
   * @param width the desired width.
   */
  void setWidth(int width);

  /**
   * Gets the seed with which to apply the mosaic filter.
   */
  int getSeed();




}
