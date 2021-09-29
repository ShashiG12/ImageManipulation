package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import model.Image;


/**
 * This class contains utility methods to read a PPM image from file and simply print its contents.
 * Feel free to change this method as required.
 */
public class PPMFileType implements FileTypeUtil {

  @Override
  public int[][][] read(String filename) throws IllegalArgumentException {
    Scanner sc;

    try {
      sc = new Scanner(new FileInputStream(filename));
    } catch (FileNotFoundException e) {
      System.out.println("File " + filename + " not found!");
      throw new IllegalArgumentException();
    }
    StringBuilder builder = new StringBuilder();
    // read the file line by line, and populate a string. This will throw away any comment lines
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.charAt(0) != '#') {
        builder.append(s + System.lineSeparator());
      }
    }

    // now set up the scanner to read from the string we just built
    sc = new Scanner(builder.toString());

    String token;

    token = sc.next();
    if (!token.equals("P3")) {
      System.out.println("Invalid PPM file: plain RAW file should begin with P3");
    }
    int width = sc.nextInt();
    //System.out.println("Width of image: " + width);
    int height = sc.nextInt();
    //System.out.println("Height of image: " + height);
    int maxValue = sc.nextInt();
    //System.out.println("Maximum value of a color in this file (usually 256): " + maxValue);

    int[][][] result = new int[height][width][3];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int r = sc.nextInt();
        int g = sc.nextInt();
        int b = sc.nextInt();
        result[i][j][0] = r;
        result[i][j][1] = g;
        result[i][j][2] = b;
        //System.out.println("Color of pixel (" + j + "," + i + "): " + r + "," + g + "," + b);
      }
    }
    return result;
  }

  @Override
  public void export(Image image, String filename) {
    try {

      FileOutputStream fs = new FileOutputStream(filename);

      // finds maximum color in file
      int max = 0;
      for (int i = 0; i < image.getHeight(); i++) {
        for (int j = 0; j < image.getWidth(); j++) {
          for (int k = 0; k < 3; k++) {
            if (image.getImage()[i][j][k] > max) {
              max = image.getImage()[i][j][k];
            }
          }
        }
      }

      // writes image specifications to file
      byte[] whitespace = (" ").getBytes();
      byte[] doubleWhitespace = ("  ").getBytes();
      byte[] newline = ("\n").getBytes();

      fs.write(("P3").getBytes());
      fs.write(newline);
      fs.write(String.valueOf(image.getWidth()).getBytes());
      fs.write(whitespace);
      fs.write(String.valueOf(image.getHeight()).getBytes());
      fs.write(newline);
      fs.write(String.valueOf(max).getBytes());
      fs.write(newline);

      // writes pixels to file
      for (int i = 0; i < image.getHeight(); i++) {
        for (int j = 0; j < image.getWidth(); j++) {
          for (int k = 0; k < 3; k++) {
            fs.write(String.valueOf(image.getImage()[i][j][k]).getBytes(StandardCharsets.UTF_8));
            fs.write(newline);
          }
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}

