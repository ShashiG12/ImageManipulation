package model;

import java.util.ArrayList;

/**
 * This class represents a Pixel.
 * A Pixel has two key properties: an x-coordinate and a y-coordinate. The remaining methods
 * are used to assist the process of creating mosaic images.
 */
public class Pixel implements IPixel {

  private int x;
  private int y;
  private ArrayList<int[]> colors = new ArrayList<>();

  /**
   * Constructs a new pixel.
   * @param x the x value
   * @param y the y value
   */
  public Pixel(int  x, int y) {
    this.x = x;
    this.y = y;
  }


  @Override
  public int getXCord() {
    return this.x;
  }

  @Override
  public int getYCord() {
    return this.y;
  }

  @Override
  public void assignColor(int[] rgb) {
    this.colors.add(rgb);
  }

  @Override
  public int[] getAverageColor() {

    int[] avgColor = {0,0,0};
    for (int[] color : this.colors) {
      avgColor[0] += color[0];
      avgColor[1] += color[1];
      avgColor[2] += color[2];
    }

    double colorSize = this.colors.size();

    avgColor[0] /= colorSize;
    avgColor[1] /= colorSize;
    avgColor[2] /= colorSize;

    return avgColor;
  }
}
