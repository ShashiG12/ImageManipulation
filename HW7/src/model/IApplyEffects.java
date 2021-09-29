package model;

/**
 * Interface for applying special effects to images. This interface hosts methods that specifically
 * deal with non-programmatic effects.
 */
public interface IApplyEffects {

  /**
   * Applies a given filter to an image.
   *
   * @param image  the image we want to apply a certain filter to
   * @param filter the filter we want to apply
   * @return the filtered image
   * @throws IllegalArgumentException if image is null, filter is null, the filter dimensions are
   *                                  even, and/or filter dimenstions are not equal
   */
  Image applyFilter(Image image, double[][] filter) throws IllegalArgumentException;


  /**
   * Applies a color transformation to a given image.
   *
   * @param image  the image to be modified
   * @param filter the filter to be applied
   * @return a transformed image
   * @throws IllegalArgumentException if image is null, filter is null, the filter dimensions are
   *                                  even, and/or filter dimenstions are * not equal
   */
  Image applyColorFilter(Image image, double[][] filter) throws IllegalArgumentException;

  /**
   * Creates a mosaic image based on the seed provided.
   *
   * @param image the image to be modified.
   * @param seed the degree of which the mosaic effect should be seen.
   * @return the completed Mosaic Image.
   */
  Image applyMosaic(Image image, int seed);

  /**
   * Creates a downsized image.
   *
   * @param image the image to be modified.
   * @return the completed Mosaic Image.
   */
  Image applyDownSize(Image image, int height, int width);

}
