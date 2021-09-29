package view;

/**
 * Factory class to create a specific file type class with its own implementation of read and export
 * based on the file type provided. Each supported file type will have its own class.
 */
public class FileTypeFactory {

  /**
   * Creates an instance of a certain filetype class.
   *
   * @param fileType different supported filetypes
   * @return returns an instance of the filetype class
   */
  public static FileTypeUtil create(FileType fileType) {
    if (fileType == FileType.PPM) {
      return new PPMFileType();
    } else if (fileType == FileType.POPULAR) {
      return new JPEGAndPNGFileType();
    } else {
      throw new IllegalArgumentException("Invalid filetype");
    }

  }

}
