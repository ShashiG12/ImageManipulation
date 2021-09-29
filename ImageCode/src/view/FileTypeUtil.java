package view;

import model.Image;

/**
 * Interface detailing the necessary methods for a filetypeutil object. Any new file type will
 * require a way to read and export images. Any new file types we may need to extend support to will
 * implement this interface.
 */
public interface FileTypeUtil {

  /**
   * Read an image file in the PPM format and print the colors.
   *
   * @param filename the path of the file.
   * @return int[][][] representing the image
   */
  int[][][] read(String filename);

  /**
   * Read an SimpleImage and export it to a PPM file.
   *
   * @param image    the image to be exported
   * @param filename the name of the destination file
   */
  void export(Image image, String filename);
}
