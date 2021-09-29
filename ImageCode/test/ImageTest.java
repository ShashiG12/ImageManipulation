import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import model.ApplyEffects;
import model.EffectType;
import model.IApplyEffects;
import model.IEnhancer;
import model.Image;
import model.SimpleEnhancer;
import model.SimpleImage;
import org.junit.Test;
import view.FileType;
import view.FileTypeFactory;
import view.FileTypeUtil;
import view.JPEGAndPNGFileType;
import view.PPMFileType;


/**
 * Class to test the functionality of different image modifications.
 */
public class ImageTest {

  FileTypeUtil imageUtil = FileTypeFactory.create(FileType.PPM);
  FileTypeUtil imageUtil1 = FileTypeFactory.create(FileType.POPULAR);
  IEnhancer enhancer = new SimpleEnhancer();
  IApplyEffects applyEffects = new ApplyEffects();

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidImage() {
    Image image = new SimpleImage(null);
    assertEquals(1024, image.getWidth());
    assertEquals(768, image.getHeight());
  }

  @Test
  public void testLoadImage() {
    Image image = new SimpleImage(
        imageUtil.read("/Users/vinaynarahari/Downloads/code (8)/Koala.ppm"));
    this.enhancer.loadImage(image);
    assertEquals(image, this.enhancer.getImageAlt());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidLoad() {
    Image image = null;
    this.enhancer.loadImage(null);
    assertEquals(1024, image.getWidth());
    assertEquals(768, image.getHeight());
  }

  @Test
  public void testRead() {
    Image image = new SimpleImage(
        imageUtil.read("/Users/vinaynarahari/Downloads/code (8)/Koala.ppm"));
    assertEquals(1024, image.getWidth());
    assertEquals(768, image.getHeight());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidRead() {
    Image image = new SimpleImage(
        imageUtil.read("gibberish.jpeg"));
    assertEquals(1024, image.getWidth());
    assertEquals(768, image.getHeight());
  }

  @Test
  public void testExport() {
    Image image = new SimpleImage(
        imageUtil.read("/Users/vinaynarahari/Downloads/code (8)/Koala.ppm"));
    this.enhancer.loadImage(image);

    assertEquals(1024, image.getWidth());
    assertEquals(768, image.getHeight());

    imageUtil.export(this.enhancer.getImageAlt(),
        "/Users/vinaynarahari/Downloads/code (8)/KoalaBlur.ppm");

    Image image1 = new SimpleImage(
        imageUtil.read("/Users/vinaynarahari/Downloads/code (8)/KoalaBlur.ppm"));
    this.enhancer.loadImage(image);

    assertEquals(1024, image1.getWidth());
    assertEquals(768, image1.getHeight());
    assertArrayEquals(image.getImage(), image1.getImage());
  }

  @Test
  public void testGetImageAlt() {
    Image image = new SimpleImage(new int[1][1][3]);
    assertArrayEquals(image.getImage(), this.enhancer.getImageAlt().getImage());
  }

  @Test
  public void testGetWidth() {
    Image image = new SimpleImage(new int[300][200][3]);
    assertEquals(200, image.getWidth());

    Image image1 = new SimpleImage(new int[10][20][3]);
    assertEquals(20, image1.getWidth());
  }

  @Test
  public void testGetHeight() {
    Image image = new SimpleImage(new int[300][200][3]);
    assertEquals(300, image.getHeight());

    Image image1 = new SimpleImage(new int[10][20][3]);
    assertEquals(10, image1.getHeight());
  }

  @Test
  public void testGetImage() {
    Image image = new SimpleImage(new int[300][200][3]);
    assertArrayEquals(new int[300][200][3], image.getImage());

    Image image1 = new SimpleImage(new int[3][2][3]);
    assertArrayEquals(new int[3][2][3], image1.getImage());
  }

  @Test
  public void testGetEffectBlur() {
    Image image = new SimpleImage(new int[300][200][3]);
    this.enhancer.loadImage(image);
    this.enhancer.getEffect(EffectType.BLUR);

    double[][] filter = {
        {0.0625, 0.125, 0.00625},
        {0.125, 0.25, 0.125},
        {0.0625, 0.125, 0.0625}};

    assertArrayEquals(new ApplyEffects().applyFilter(image, filter).getImage(),
        this.enhancer.getImageAlt().getImage());
  }

  @Test
  public void testGetEffectSharpen() {
    Image image = new SimpleImage(new int[300][200][3]);
    this.enhancer.loadImage(image);
    this.enhancer.getEffect(EffectType.SHARPEN);

    double[][] filter = {
        {-0.125, -0.125, -0.125, -0.125, -0.125},
        {-0.125, 0.25, 0.25, 0.25, -0.125},
        {-0.125, 0.25, 1, 0.25, -0.125},
        {-0.125, 0.25, 0.25, 0.25, -0.125},
        {-0.125, -0.125, -0.125, -0.125, -0.125}};

    assertArrayEquals(new ApplyEffects().applyFilter(image, filter).getImage(),
        this.enhancer.getImageAlt().getImage());
  }

  @Test
  public void testUndoBlur() {
    Image image = new SimpleImage(new int[300][200][3]);
    this.enhancer.loadImage(image);
    this.enhancer.getEffect(EffectType.BLUR);
    this.enhancer.undo();
    assertArrayEquals(image.getImage(), this.enhancer.getImageAlt().getImage());
  }

  @Test
  public void testUndoSharpen() {
    Image image = new SimpleImage(new int[300][200][3]);
    this.enhancer.loadImage(image);
    this.enhancer.getEffect(EffectType.SHARPEN);
    this.enhancer.undo();
    assertArrayEquals(image.getImage(), this.enhancer.getImageAlt().getImage());
  }


  @Test
  public void testApplyFilter() {
    // example array
    int[][][] newArray = new int[3][3][3];

    for (int i = 0; i < newArray.length; i++) {
      for (int j = 0; j < newArray[0].length; j++) {
        newArray[i][j][0] = 5;
        newArray[i][j][1] = 10;
        newArray[i][j][2] = 20;
      }
    }

    this.enhancer.loadImage(new SimpleImage(newArray));
    this.enhancer.getEffect(EffectType.BLUR);

    // correct array after filter math
    int[][][] resultArray = {
        {{1, 6, 16}, {1, 5, 13}, {0, 1, 4}},
        {{1, 5, 12}, {1, 4, 10}, {0, 1, 3}},
        {{0, 1, 3}, {0, 1, 3}, {0, 0, 1}}
    };

    assertArrayEquals(resultArray,
        this.enhancer.getImageAlt().getImage());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidApplyFilterNullCase() {

    double[][] filter = {
        {0.0625, 0.125, 0.00625},
        {0.125, 0.25, 0.125},
        {0.0625, 0.125, 0.0625}};

    this.applyEffects.applyFilter(null, filter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidApplyFilterFilterIncorrect() {

    Image image = new SimpleImage(new int[300][200][3]);

    double[][] filter = {
        {0.0625, 0.125, 0.00625},
        {0.125, 0.25, 0.125}};

    this.applyEffects.applyFilter(image, filter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidApplyColorFilterNullCase() {

    double[][] filter = {
        {0.0625, 0.125, 0.00625},
        {0.125, 0.25, 0.125},
        {0.0625, 0.125, 0.0625}};

    this.applyEffects.applyColorFilter(null, filter);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidApplyColorFilterFilterIncorrect() {

    Image image = new SimpleImage(new int[300][200][3]);

    double[][] filter = {
        {0.0625, 0.125, 0.00625},
        {0.125, 0.25, 0.125},
        {0.0625, 0.125, 0.0625},
        {0.0625, 0.125, 0.0625}};

    this.applyEffects.applyColorFilter(image, filter);
  }

  @Test
  public void testApplyColorFilter() {
    // example array
    int[][][] newArray1 = new int[1][1][3];

    newArray1[0][0][0] = 0;
    newArray1[0][0][1] = 1;
    newArray1[0][0][2] = 2;

    this.enhancer.loadImage(new SimpleImage(newArray1));
    this.enhancer.getEffect(EffectType.SEPIA);

    // correct array after filter math
    int[][][] resultArray1 = new int[1][1][3];
    resultArray1[0][0][0] = 1;
    resultArray1[0][0][1] = 1;
    resultArray1[0][0][2] = 0;

    assertArrayEquals(resultArray1,
        this.enhancer.getImageAlt().getImage());
  }

  @Test
  public void testCheckerBoard() {
    int size = 10;

    this.enhancer.checkerBoardImage(size);

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        for (int k = 0; k < 3; k++) {
          if (i % 2 == j % 2) {
            assertTrue(this.enhancer.getImageAlt().getImage()[i][j][k] == 0);
          } else {
            assertTrue(this.enhancer.getImageAlt().getImage()[i][j][k] == 255);
          }
        }
      }
    }
  }

  @Test
  public void testFactoryClass() {
    assertEquals(true, FileTypeFactory.create(FileType.PPM) instanceof PPMFileType);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFactoryNullCase() {
    assertEquals(true, FileTypeFactory.create(null) instanceof PPMFileType);
  }

  @Test
  public void testFactoryClassJPEG() {
    assertEquals(true, FileTypeFactory.create(FileType.POPULAR) instanceof
        JPEGAndPNGFileType);
  }

  @Test
  public void testFactoryClassPNG() {
    assertEquals(true, FileTypeFactory.create(FileType.POPULAR) instanceof
        JPEGAndPNGFileType);
  }

  @Test
  public void testReadPNG() {
    Image image = new SimpleImage(
        imageUtil1.read("/Users/vinaynarahari/Downloads/PNGOODLeanard.png"));
    assertEquals(512, image.getWidth());
    assertEquals(396, image.getHeight());
  }

  @Test
  public void testReadJPEG() {
    Image image = new SimpleImage(
        imageUtil1.read(""
            + "/Users/vinaynarahari/Downloads/HW6Final 5 3 3 2 2/pngSaveTest.jpeg"));
    assertEquals(512, image.getWidth());
    assertEquals(396, image.getHeight());
  }

  @Test
  public void testExportPNG() {
    Image image = new SimpleImage(
        imageUtil1.read(""
            + "/Users/vinaynarahari/Downloads/PNGOODLeanard.png"));

    imageUtil1.export(image, "varunballer");

    assertEquals(512, image.getWidth());
    assertEquals(396, image.getHeight());
  }

  @Test
  public void testExportJPEG() {
    Image image = new SimpleImage(
        imageUtil1.read(""
            + "/Users/vinaynarahari/Downloads/HW6Final 5 3 3 2 2/pngSaveTest.jpeg"));
    assertEquals(512, image.getWidth());
    assertEquals(396, image.getHeight());
  }

  @Test
  public void testDownSize() {
    Image hello = new SimpleImage(
        imageUtil1.read("res\\snail.png"));

    IEnhancer enhancer = new SimpleEnhancer();
    enhancer.loadImage(hello);
    enhancer.setHeight(50);
    enhancer.setWidth(150);
    enhancer.getEffect(EffectType.DOWNSIZE);
    assertEquals(50, enhancer.getImageAlt().getHeight());
    assertEquals(150, enhancer.getImageAlt().getWidth());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testHeightLessThanZero() {
    Image hello = new SimpleImage(
        imageUtil1.read("res\\snail.png"));

    IEnhancer enhancer = new SimpleEnhancer();
    enhancer.loadImage(hello);
    enhancer.setHeight(-1);
    enhancer.setWidth(150);
    enhancer.getEffect(EffectType.DOWNSIZE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testHeightBiggerThanImage() {
    Image hello = new SimpleImage(
        imageUtil1.read("res\\snail.png"));

    IEnhancer enhancer = new SimpleEnhancer();
    enhancer.loadImage(hello);
    enhancer.setHeight(5000);
    enhancer.setWidth(150);
    enhancer.getEffect(EffectType.DOWNSIZE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWidthLessThanZero() {
    Image hello = new SimpleImage(
        imageUtil1.read("res\\snail.png"));

    IEnhancer enhancer = new SimpleEnhancer();
    enhancer.loadImage(hello);
    enhancer.setHeight(0);
    enhancer.setWidth(-1);
    enhancer.getEffect(EffectType.DOWNSIZE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWidthBiggerThanImage() {
    Image hello = new SimpleImage(
        imageUtil1.read("res\\snail.png"));

    IEnhancer enhancer = new SimpleEnhancer();
    enhancer.loadImage(hello);
    enhancer.setHeight(100);
    enhancer.setWidth(50000);
    enhancer.getEffect(EffectType.DOWNSIZE);
  }

  @Test
  public void testMosaic() {
    IEnhancer enhancer = new SimpleEnhancer();
    enhancer.loadImage(new SimpleImage(new int[1][2][3]));

    enhancer.getImageAlt().getImage()[0][0][0] = 2;
    enhancer.getImageAlt().getImage()[0][0][1] = 5;
    enhancer.getImageAlt().getImage()[0][0][2] = 6;
    enhancer.getImageAlt().getImage()[0][1][0] = 7;
    enhancer.getImageAlt().getImage()[0][1][1] = 8;
    enhancer.getImageAlt().getImage()[0][1][2] = 3;

    enhancer.setSeed(1);
    enhancer.getEffect(EffectType.MOSAIC);


    assertEquals(4, enhancer.getImageAlt().getImage()[0][0][0]);
    assertEquals(6, enhancer.getImageAlt().getImage()[0][0][1]);
    assertEquals(4, enhancer.getImageAlt().getImage()[0][0][2]);
    assertEquals(4, enhancer.getImageAlt().getImage()[0][1][0]);
    assertEquals(6, enhancer.getImageAlt().getImage()[0][1][1]);
    assertEquals(4, enhancer.getImageAlt().getImage()[0][1][2]);

  }

  @Test(expected = IllegalArgumentException.class)
  public void testSeedLessThanOne() {
    IEnhancer enhancer = new SimpleEnhancer();
    enhancer.loadImage(new SimpleImage(new int[1][2][3]));
    enhancer.setSeed(-1);
    enhancer.getEffect(EffectType.MOSAIC);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSeedGreaterThanArea() {
    IEnhancer enhancer = new SimpleEnhancer();
    enhancer.loadImage(new SimpleImage(new int[1][100][3]));
    enhancer.setSeed(101);
    enhancer.getEffect(EffectType.MOSAIC);
  }

  @Test
  public void testSeedEqualToArea() {
    Image hello = new SimpleImage(
        imageUtil1.read("res\\snail.png"));

    IEnhancer enhancer = new SimpleEnhancer();
    enhancer.loadImage(hello);
    enhancer.setSeed(256 * 256);
    enhancer.getEffect(EffectType.MOSAIC);
    assertEquals(hello, enhancer.getImageAlt());
  }

  @Test
  public void testSetSeed() {
    Image hello = new SimpleImage(
        imageUtil1.read("res\\snail.png"));

    IEnhancer enhancer = new SimpleEnhancer();
    enhancer.loadImage(hello);
    enhancer.setSeed(256);
    enhancer.getEffect(EffectType.MOSAIC);
    assertEquals(256, enhancer.getSeed());
  }

  @Test
  public void testGetSeed() {
    Image hello = new SimpleImage(
        imageUtil1.read("res\\snail.png"));

    IEnhancer enhancer = new SimpleEnhancer();
    enhancer.loadImage(hello);
    enhancer.setSeed(256);
    enhancer.getEffect(EffectType.MOSAIC);
    assertEquals(256, enhancer.getSeed());
  }

  @Test
  public void testSetHeight() {
    Image hello = new SimpleImage(
        imageUtil1.read("res\\snail.png"));

    IEnhancer enhancer = new SimpleEnhancer();
    enhancer.loadImage(hello);
    enhancer.setHeight(100);
    enhancer.setWidth(100);
    enhancer.getEffect(EffectType.DOWNSIZE);
    assertEquals(100, enhancer.getImageAlt().getHeight());
  }

  @Test
  public void testGetEnhancerHeight() {
    Image hello = new SimpleImage(
        imageUtil1.read("res\\snail.png"));

    IEnhancer enhancer = new SimpleEnhancer();
    enhancer.loadImage(hello);
    enhancer.setHeight(100);
    enhancer.setWidth(100);
    enhancer.getEffect(EffectType.DOWNSIZE);
    assertEquals(100, enhancer.getImageAlt().getHeight());
  }

  @Test
  public void testSetWidth() {
    Image hello = new SimpleImage(
        imageUtil1.read("res\\snail.png"));

    IEnhancer enhancer = new SimpleEnhancer();
    enhancer.loadImage(hello);
    enhancer.setHeight(100);
    enhancer.setWidth(101);
    enhancer.getEffect(EffectType.DOWNSIZE);
    assertEquals(101, enhancer.getImageAlt().getWidth());
  }

  @Test
  public void testGetEnhancerWidth() {
    Image hello = new SimpleImage(
        imageUtil1.read("res\\snail.png"));

    IEnhancer enhancer = new SimpleEnhancer();
    enhancer.loadImage(hello);
    enhancer.setHeight(100);
    enhancer.setWidth(101);
    enhancer.getEffect(EffectType.DOWNSIZE);
    assertEquals(101, enhancer.getImageAlt().getWidth());
  }



  // The tests below were used to check if images were altered as expected.
  //  @Test
  //  public void sharpenTest() {
  //    Image hello = new SimpleImage(
  //        imageUtil.read("C:\\Users\\shash\\Downloads\\code (3)\\Koala.ppm"));
  //    IEnhancer enhancer = new SimpleEnhancer();
  //    enhancer.loadImage(hello);
  //    enhancer.getEffect(FilterType.SHARPEN);
  //    imageUtil.export(enhancer.getImage(), "C:\\Users\\shash\\Downloads\\KoalaSharpen.ppm");
  //  }
  //
  //  @Test
  //  public void monochromeTest() {
  //    Image hello = new SimpleImage(
  //        imageUtil.read("/Users/vinaynarahari/Downloads/HW5 - 1/res/blackbuck.ppm"));
  //    IEnhancer enhancer = new SimpleEnhancer();
  //    enhancer.loadImage(hello);
  //    enhancer.getEffect(FilterType.MONOCHROME);
  //    imageUtil.export(enhancer.getImageAlt(),
  //    "/Users/vinaynarahari/Downloads/HW5 - 1/res/blackbuck(greyscale).ppm");
  //  }
  //
  //  @Test
  //  public void sepiaTest() {
  //    Image hello = new SimpleImage(
  //        imageUtil.read("/Users/vinaynarahari/Downloads/HW5 - 1/res/blackbuck.ppm"));
  //    IEnhancer enhancer = new SimpleEnhancer();
  //    enhancer.loadImage(hello);
  //    enhancer.getEffect(FilterType.SEPIA);
  //    imageUtil.export(enhancer.getImageAlt(),
  //    "/Users/vinaynarahari/Downloads/HW5 - 1/res/blackbuck(sepia).ppm");
  //  }


}
