package model;

import java.util.ArrayList;

/**
 * This class implements the functionality outlined in the IEnhancer interface. This class contains
 * implementation for all methods dealing with image modification. Any other image modifying
 * functionality stems from this class.
 */

public class SimpleEnhancer implements IEnhancer {

  private Image image;

  private ArrayList<Image> undo;

  private int seed;

  private int height;

  private int width;


  private final double[][] blurArray = {
      {0.0625, 0.125, 0.00625},
      {0.125, 0.25, 0.125},
      {0.0625, 0.125, 0.0625}};

  private final double[][] sharpenArray = {
      {-0.125, -0.125, -0.125, -0.125, -0.125},
      {-0.125, 0.25, 0.25, 0.25, -0.125},
      {-0.125, 0.25, 1, 0.25, -0.125},
      {-0.125, 0.25, 0.25, 0.25, -0.125},
      {-0.125, -0.125, -0.125, -0.125, -0.125}};

  private final double[][] monoChromeArray = {
      {0.2126, 0.7152, 0.072},
      {0.2126, 0.7152, 0.072},
      {0.2126, 0.7152, 0.072}};

  private final double[][] sepiaArray = {
      {0.393, 0.769, 0.189},
      {0.349, 0.686, 0.168},
      {0.272, 0.534, 0.131}};

  private final int[][] rainbowArray =
      {{255, 0, 0},
          {255, 127, 0},
          {255, 255, 0},
          {0, 255, 0},
          {0, 0, 255},
          {75, 0, 130},
          {148, 0, 211}};


  /**
   * Constructs the SimpleEnhancer.
   */
  public SimpleEnhancer() {
    this.image = new SimpleImage(new int[1][1][3]);
    this.undo = new ArrayList<Image>();
  }

  // applies the appropriate effect based on the given filterType
  @Override
  public void getEffect(EffectType effectType) {
    switch (effectType) {
      case BLUR:
        this.filter(blurArray);
        break;
      case SHARPEN:
        this.filter(sharpenArray);
        break;
      case MONOCHROME:
        this.filter2(monoChromeArray);
        break;
      case SEPIA:
        this.filter2(sepiaArray);
        break;
      case MOSAIC:
        this.filter3(this.seed);
        break;
      case DOWNSIZE:
        this.filter4(this.height, this.width);
        break;
      default:
        throw new IllegalArgumentException("Not a valid filter");
    }
  }

  @Override
  public void checkerBoardImage(int size) {

    int[][][] checkerBoard = new int[size][size][3];

    for (int i = 0; i < checkerBoard.length; i++) {
      for (int j = 0; j < checkerBoard[0].length; j++) {
        if (i % 2 == j % 2) {
          checkerBoard[i][j][0] = 0;
          checkerBoard[i][j][1] = 0;
          checkerBoard[i][j][2] = 0;
        } else {
          checkerBoard[i][j][0] = 255;
          checkerBoard[i][j][1] = 255;
          checkerBoard[i][j][2] = 255;
        }
      }
    }

    this.image = new SimpleImage(checkerBoard);
  }

  @Override
  public void loadImage(Image image) {
    if (image == null) {
      throw new IllegalArgumentException("Image cannot be null");
    }
    this.undo.add(image);
    this.image = image;
  }


  // takes in a given filter and applies it to an image - used for blur/sharpen
  private void filter(double[][] filter) {
    this.image = new ApplyEffects().applyFilter(this.image, filter);
  }

  // takes in a given filter and applies it to an image - used for sepia/monochrome
  private void filter2(double[][] filter) {
    this.image = new ApplyEffects().applyColorFilter(this.image, filter);
  }

  // used to make a mosaic image
  private void filter3(int seed) {
    this.image = new ApplyEffects().applyMosaic(this.image, seed);
  }

  // downsizes an image
  private void filter4(int height, int width) {
    this.image = new ApplyEffects().applyDownSize(this.image, height, width);
  }


  @Override
  // this method actually returns the Image rather than the triple nested array that getImage
  // returns
  public Image getImageAlt() {
    return this.image;
  }


  @Override
  public void undo() {
    this.image = this.undo.get(this.undo.size() - 1);
  }


  @Override
  public void setSeed(int seed) {
    this.seed =  seed;
  }

  @Override
  public int getSeed() {
    return this.seed;
  }

  @Override
  public void setHeight(int height) {
    this.height =  height;
  }

  @Override
  public void setWidth(int width) {
    this.width =  width;
  }

}

