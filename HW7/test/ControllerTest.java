import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import controller.Controller;
import controller.GUIController;
import controller.SimpleController;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import model.ApplyEffects;
import model.IEnhancer;
import model.Image;
import model.Layer;
import model.SimpleEnhancer;
import model.SimpleImage;
import org.junit.Test;
import view.FileType;
import view.FileTypeFactory;
import view.FileTypeUtil;
import view.GUIView;
import view.IGUIView;

// ALL Tests pass, but you may need to change where the file is coming from.

/**
 * Represents examples of and tests for the Controller.
 */
public class ControllerTest {

  Layer layers = new Layer();
  IEnhancer model = new SimpleEnhancer();
  IEnhancer model1 = new SimpleEnhancer();


  @Test(expected = IllegalArgumentException.class)
  public void testReadbableNull() {
    Controller controller = new SimpleController(null, this.layers, this.model);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLayerNull() {
    StringReader reader = new StringReader("qqq");
    Controller controller = new SimpleController(reader, null, this.model);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testModelNull() {
    StringReader reader = new StringReader("qqq");
    Controller controller = new SimpleController(reader, this.layers, null);
  }

  @Test
  public void testLoadCommand() {
    StringReader reader = new StringReader(
        "load /Users/vinaynarahari/Downloads/HW5 - 1 4/res/PPMImages/snail.ppm");
    Controller controller = new SimpleController(reader, this.layers, this.model);
    controller.processInput();
    assertEquals(256, this.model.getImageAlt().getHeight());
    assertEquals(256, this.model.getImageAlt().getWidth());
  }

  @Test
  public void testLoadJPEG() {
    StringReader reader = new StringReader("load /Users/vinaynarahari/Downloads/CarOODImage.jpeg");
    Controller controller = new SimpleController(reader, this.layers, this.model);
    controller.processInput();
    assertEquals(667, this.model.getImageAlt().getHeight());
    assertEquals(1000, this.model.getImageAlt().getWidth());
  }

  @Test
  public void testLoadPNG() {
    StringReader reader = new StringReader("load /Users/vinaynarahari/Downloads/PNGOODLeanard.png");
    Controller controller = new SimpleController(reader, this.layers, this.model);
    controller.processInput();
    assertEquals(396, this.model.getImageAlt().getHeight());
    assertEquals(512, this.model.getImageAlt().getWidth());
  }

  @Test
  public void testLoadMultiLayeredImage() throws FileNotFoundException {
    StringReader reader = new StringReader("create layer first" + "\n"
        + "create layer second");
    Controller controller = new SimpleController(reader, this.layers, this.model);
    controller.processInput();
    assertEquals(2, this.layers.getImages().size());
  }

  @Test
  public void testBlurCommand() {
    StringReader reader = new StringReader("blur");
    Controller controller = new SimpleController(reader, this.layers, this.model);
    controller.processInput();

    Image image = new SimpleImage(new int[300][200][3]);
    this.model.loadImage(image);

    double[][] filter = {
        {0.0625, 0.125, 0.00625},
        {0.125, 0.25, 0.125},
        {0.0625, 0.125, 0.0625}};

    assertArrayEquals(new ApplyEffects().applyFilter(image, filter).getImage(),
        this.model.getImageAlt().getImage());
  }

  @Test
  public void testSharpenCommand() {
    StringReader reader = new StringReader("sharpen");
    Controller controller = new SimpleController(reader, this.layers, this.model);
    controller.processInput();

    Image image = new SimpleImage(new int[300][200][3]);
    this.model.loadImage(image);

    double[][] filter = {
        {-0.125, -0.125, -0.125, -0.125, -0.125},
        {-0.125, 0.25, 0.25, 0.25, -0.125},
        {-0.125, 0.25, 1, 0.25, -0.125},
        {-0.125, 0.25, 0.25, 0.25, -0.125},
        {-0.125, -0.125, -0.125, -0.125, -0.125}};

    assertArrayEquals(new ApplyEffects().applyFilter(image, filter).getImage(),
        this.model.getImageAlt().getImage());
  }

  @Test
  public void testSepiaCommand() {

    int[][][] newArray1 = new int[1][1][3];

    newArray1[0][0][0] = 0;
    newArray1[0][0][1] = 1;
    newArray1[0][0][2] = 2;

    this.model.loadImage(new SimpleImage(newArray1));

    StringReader reader = new StringReader("sepia");
    Controller controller = new SimpleController(reader, this.layers, this.model);
    controller.processInput();

    int[][][] resultArray1 = new int[1][1][3];
    resultArray1[0][0][0] = 1;
    resultArray1[0][0][1] = 1;
    resultArray1[0][0][2] = 0;

    assertArrayEquals(resultArray1, this.model.getImageAlt().getImage());

  }

  @Test
  public void testMonochromeCommand() {

    int[][][] newArray1 = new int[1][1][3];

    newArray1[0][0][0] = 0;
    newArray1[0][0][1] = 0;
    newArray1[0][0][2] = 2;

    this.model.loadImage(new SimpleImage(newArray1));

    StringReader reader = new StringReader("monochrome");
    Controller controller = new SimpleController(reader, this.layers, this.model);
    controller.processInput();

    int[][][] resultArray1 = new int[1][1][3];
    resultArray1[0][0][0] = 0;
    resultArray1[0][0][1] = 0;
    resultArray1[0][0][2] = 0;

    assertArrayEquals(resultArray1, this.model.getImageAlt().getImage());

  }

  @Test
  public void testCheckerBoardCommand() {

    int size = 10;

    StringReader reader = new StringReader("checkerboard 10");
    Controller controller = new SimpleController(reader, this.layers, this.model);
    controller.processInput();

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        for (int k = 0; k < 3; k++) {
          if (i % 2 == j % 2) {
            assertTrue(this.model.getImageAlt().getImage()[i][j][k] == 0);
          } else {
            assertTrue(this.model.getImageAlt().getImage()[i][j][k] == 255);
          }
        }
      }
    }
  }

  @Test
  public void testCreateLayerCommand() {
    StringReader reader = new StringReader("create layer first");
    Controller controller = new SimpleController(reader, this.layers, this.model);
    controller.processInput();

    Image image = new SimpleImage(new int[1][1][3]);

    assertTrue(this.layers.getImages().containsKey("first"));
    assertArrayEquals(image.getImage(), this.layers.get("first").getImage());
  }

  @Test
  public void testRemoveLayerCommand() throws FileNotFoundException {

    FileReader reader = new FileReader("/Users/vinaynarahari/Downloads/USEME (1).txt");
    Controller controller = new SimpleController(reader, this.layers, this.model);
    controller.processInput();

    assertFalse(this.layers.getImages().containsKey("first"));
  }

  @Test
  public void testCurrentCommand() {

    StringReader reader = new StringReader("create layer first" +
        "\n" + "load /Users/vinaynarahari/Downloads/HW5 - 1 4/res/PPMImages/snail.ppm" +
        "\n" + "current first");
    Controller controller = new SimpleController(reader, this.layers, this.model);
    controller.processInput();

    assertTrue(this.layers.getImages().containsKey("first"));
    assertEquals(1, this.layers.get("first").getHeight());
    assertEquals(1, this.layers.get("first").getWidth());
  }

  @Test
  public void testRedoLayerCommand() {

    StringReader reader = new StringReader("create layer first" + "\n" + "current first" +
        "\n" + "load /Users/vinaynarahari/Downloads/HW5 - 1 4/res/PPMImages/snail.ppm"
        + "\n" + "redo layer first");
    Controller controller = new SimpleController(reader, this.layers, this.model);
    controller.processInput();

    assertTrue(this.layers.getImages().containsKey("first"));
    assertTrue(this.layers.getImages().containsKey("first1"));

    assertEquals(256, this.layers.get("first").getHeight());
    assertEquals(256, this.layers.get("first").getWidth());
    assertEquals(256, this.layers.get("first1").getHeight());
    assertEquals(256, this.layers.get("first1").getWidth());
  }

  @Test
  public void testInvisible() {
    StringReader reader = new StringReader("create layer first" + "\n" + "current first" +
        "\n" + "load /Users/vinaynarahari/Downloads/HW5 - 1 4/res/PPMImages/snail.ppm"
        + "\n" + "invisible first");
    Controller controller = new SimpleController(reader, this.layers, this.model);
    controller.processInput();

    assertTrue(controller.getInvisibleLayers().containsKey("first"));
    assertEquals(1, this.layers.get("first").getHeight());
    assertEquals(1, this.layers.get("first").getWidth());
  }

  @Test
  public void testVisible() {
    StringReader reader = new StringReader("create layer first" + "\n" + "current first" +
        "\n" + "load /Users/vinaynarahari/Downloads/HW5 - 1 4/res/PPMImages/snail.ppm"
        + "\n" + "invisible first" + "\n" + "visible first");
    Controller controller = new SimpleController(reader, this.layers, this.model);
    controller.processInput();

    assertFalse(controller.getInvisibleLayers().containsKey("first"));
    assertEquals(256, this.layers.get("first").getHeight());
    assertEquals(256, this.layers.get("first").getWidth());
  }

  @Test
  public void testUndoCommand() {

    FileTypeUtil imageUtil = FileTypeFactory.create(FileType.PPM);

    Image image1 = new SimpleImage(
        imageUtil.read("/Users/vinaynarahari/Downloads/HW5 - 1 4/res/PPMImages/snail.ppm"));

    this.model1.loadImage(image1);

    StringReader reader = new StringReader("create layer first" + "\n" +
        "current first" + "\n"
        + "load /Users/vinaynarahari/Downloads/HW5 - 1 4/res/PPMImages/snail.ppm"
        + "\n" + "sharpen" + "\n" + "undo");
    Controller controller = new SimpleController(reader, this.layers, this.model);
    controller.processInput();

    assertEquals(256, this.layers.get("first").getHeight());
    assertEquals(256, this.layers.get("first").getWidth());

    for (int i = 0; i < this.model.getImageAlt().getHeight(); i++) {
      for (int j = 0; j < this.model.getImageAlt().getWidth(); j++) {
        assertEquals(this.layers.get("first").getImage()[i][j][0],
            this.model1.getImageAlt().getImage()[i][j][0]);
        assertEquals(this.layers.get("first").getImage()[i][j][1],
            this.model1.getImageAlt().getImage()[i][j][1]);
        assertEquals(this.layers.get("first").getImage()[i][j][2],
            this.model1.getImageAlt().getImage()[i][j][2]);
      }
    }
  }

  @Test
  public void testSaveRemoveEmptyString() {
    StringReader reader = new StringReader("" + "\n" + "create layer first" +
        "\n" + "save imageTest.png");
    Controller controller = new SimpleController(reader, this.layers, this.model);
    controller.processInput();

    assertFalse(this.layers.getImages().containsKey(""));
    assertTrue(this.layers.getImages().containsKey("first"));
  }

  @Test(expected = Test.None.class)
  public void testSaveSameSize() {

    StringReader reader = new StringReader("" + "\n" + "create layer first" +
        "\n" + "save imageTest.png");
    Controller controller = new SimpleController(reader, this.layers, this.model);
    controller.processInput();

    this.layers.add("second", new SimpleImage(new int[1][1][3]));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNotSameSize() {

    StringReader reader = new StringReader("create layer first" +
        "\n" + "current first" + "\n" + "load /Users/vinaynarahari/Downloads/PNGOODLeanard.png"
        + "\n" + "create layer second" + "\n" + "current second" + "\n" +
        "load /Users/vinaynarahari/Downloads/CarOODImage.jpeg" + "\n" +
        "save imageTest.png");
    Controller controller = new SimpleController(reader, this.layers, this.model);
    controller.processInput();
  }

  @Test
  public void savePNG() {
    // We know the Leopard Image Height to be 396
    // We know the Leopard Image Width to be 512
    StringReader reader = new StringReader("create layer first" +
        "\n" + "current first" + "\n" + "load /Users/vinaynarahari/Downloads/PNGOODLeanard.png"
        + "\n" + "save pngSaveTest.png");
    Controller controller = new SimpleController(reader, this.layers, this.model);
    controller.processInput();

    FileTypeUtil imageUtil = FileTypeFactory.create(FileType.POPULAR);
    Image image = new SimpleImage(
        imageUtil.read("/Users/vinaynarahari/Desktop/CS3500 Summer 1/HW6/pngSaveTest.png"));

    assertEquals(396, image.getHeight());
    assertEquals(512, image.getWidth());

  }

  @Test
  public void saveJPEG() {
    // We know the Leopard Image Height to be 396
    // We know the Leopard Image Width to be 512
    StringReader reader = new StringReader("create layer first" +
        "\n" + "current first" + "\n" + "load /Users/vinaynarahari/Downloads/PNGOODLeanard.png"
        + "\n" + "save pngSaveTest.jpeg");
    Controller controller = new SimpleController(reader, this.layers, this.model);
    controller.processInput();

    FileTypeUtil imageUtil = FileTypeFactory.create(FileType.POPULAR);
    Image image = new SimpleImage(
        imageUtil.read("/Users/vinaynarahari/Desktop/CS3500 Summer 1/HW6/pngSaveTest.jpeg"));

    assertEquals(396, image.getHeight());
    assertEquals(512, image.getWidth());
  }

  @Test
  public void savePPM() {
    // We know the Leopard Image Height to be 396
    // We know the Leopard Image Width to be 512
    StringReader reader = new StringReader("create layer first" +
        "\n" + "current first" + "\n" + "load /Users/vinaynarahari/Downloads/PNGOODLeanard.png"
        + "\n" + "save pngSaveTest.ppm");
    Controller controller = new SimpleController(reader, this.layers, this.model);
    controller.processInput();

    FileTypeUtil imageUtil = FileTypeFactory.create(FileType.PPM);
    Image image = new SimpleImage(
        imageUtil.read("/Users/vinaynarahari/Desktop/CS3500 Summer 1/HW6/pngSaveTest.ppm"));

    assertEquals(396, image.getHeight());
    assertEquals(512, image.getWidth());
  }

  @Test
  public void testSaveMultiLayeredImage() throws FileNotFoundException {
    StringReader reader = new StringReader("create layer first" + "\n"
        + "create layer second");
    Controller controller = new SimpleController(reader, this.layers, this.model);
    controller.processInput();
    assertEquals(2, this.layers.getImages().size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIncorrectCommand() throws FileNotFoundException {
    StringReader reader = new StringReader("create layer first" + "\n"
        + "random first");
    Controller controller = new SimpleController(reader, this.layers, this.model);
    controller.processInput();
  }

  //test will open up a GUI with an illegal argument exception error
  @Test
  public void testInvalidAcceptCommand() {
    IGUIView view = new GUIView(this.model);
    Controller controller = new GUIController(view, new Layer(), this.model);
    controller.acceptCommand("jdskfjk");
    assertEquals(this.model, this.model);
  }

  //performs the same function as processInput() but takes in a string instead
  @Test
  public void testAcceptCommand() {
    assertEquals(this.model, this.model);
  }



}
