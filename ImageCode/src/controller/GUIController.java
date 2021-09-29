package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.SortedMap;
import model.ApplyEffects;
import model.EffectType;
import model.IEnhancer;
import model.Image;
import model.Layer;
import model.SimpleImage;
import view.FileType;
import view.FileTypeFactory;
import view.IGUIView;

/**
 * This class implements the functionality promised in the Controller interface. It ensures that the
 * user inputs an appropriate command and if so, runs the command to produce the desired final
 * output. This controller is specifically designed to work with our GUI view implementation.
 * Some changes were made to certain methods and processInput() is not used.
 *
 */
public class GUIController implements Controller {

  private IEnhancer model;

  private String name;

  private String imageInput;

  private Layer layers;

  private FileType fileType;

  private HashMap<String, Image> invisImages;

  private String size;

  private IGUIView view;

  private String seed;

  private String height;

  private String width;

  /**
   * Constructs a GUI controller.
   * @param view the view which provides user input
   * @param layers layers to add add images to
   * @param model the model to actually modify images
   */
  public GUIController(IGUIView view, Layer layers, IEnhancer model) {
    this.view = view;
    this.model = model;
    this.name = "";
    this.layers = layers;
    this.invisImages = new HashMap<>();
    this.size = "";
    this.imageInput = "";
    this.seed = "";
    this.height = "";
    this.width = "";
  }


  @Override
  public void acceptCommand(String s) {
    try {
      //performs the same function as processInput() but takes in a string instead
      processInput2(s);
    } catch (Exception e) {
      view.displayError(e.toString());
    }
    this.view.renderView(model.getImageAlt());
    view.update();
  }

  @Override
  public void processInput() {
    // does nothing in this version of the controller.
  }


  //processes input string by string
  private void processInput2(String userInput) {

    if (userInput.contains("create layer")) {
      this.name = userInput.substring(13);

    } else if (userInput.contains("load")) {
      this.imageInput = userInput.substring(5);

      if (userInput.contains("ppm")) {
        this.fileType = FileType.PPM;
      } else {
        this.fileType = FileType.POPULAR;
      }

    } else if (userInput.contains("current")) {
      this.name = userInput.substring(8);

    } else if (userInput.contains("remove layer")) {
      this.name = userInput.substring(13);
    } else if (userInput.contains("redo layer")) {
      this.name = userInput.substring(11);
    } else if (userInput.contains("in")) {
      this.name = userInput.substring(10);
    } else if (userInput.contains("save")) {
      this.imageInput = userInput.substring(5);

      if (userInput.contains("ppm")) {
        this.fileType = FileType.PPM;
      } else {
        this.fileType = FileType.POPULAR;
      }
    } else if (userInput.contains("checkerboard")) {
      this.size = userInput.substring(13);
    } else if (userInput.contains("visible")) {
      this.name = userInput.substring(8);
    } else if (userInput.contains("mosaic")) {
      this.seed = userInput.substring(7);
    } else if (userInput.contains("downsize")) {
      String s = userInput.substring(9);
      String[] split = s.split("\\s+");
      this.height = split[0];
      this.width = split[1];
    }

    // The Runnable acts like a function object, mapping each String command to the run
    // implementation in the appropriate class.
    HashMap<String, Runnable> operations = new HashMap<String, Runnable>();

    operations.put("load " + imageInput, new Load());
    operations.put("blur", new Blur());
    operations.put("sharpen", new Sharpen());
    operations.put("monochrome", new Monochrome());
    operations.put("sepia", new Sepia());
    operations.put("create layer " + name, new CreateLayer());
    operations.put("remove layer " + name, new RemoveLayer());
    operations.put("current " + name, new SetLayer());
    operations.put("redo layer " + name, new Redo());
    operations.put("save " + imageInput, new Save());
    operations.put("invisible " + name, new Invisible());
    operations.put("checkerboard " + size, new Checkerboard());
    operations.put("visible " + name, new Visible());
    operations.put("undo", new Undo());
    operations.put("mosaic " + seed, new Mosaic());
    operations.put("downsize " + height + " " + width, new DownSize());

    if (operations.containsKey(userInput)) {
      operations.get(userInput).run();
      if (!((userInput.contains("remove")) || (userInput.contains("visible")))) {
        layers.add(name, model.getImageAlt());
      }
    } else {
      throw new IllegalArgumentException("Either your input is not supported or"
          + " images are not"
          + " the same size");
    }
  }


  @Override
  public HashMap<String, Image> getInvisibleLayers() {
    return this.invisImages;
  }


  // takes care of loading images
  private class Load implements Runnable {

    @Override
    public void run() {
      int[][][] image1 = FileTypeFactory.create(fileType).read(imageInput);
      model.loadImage(new SimpleImage(image1));
    }
  }

  // takes care of blurring images
  private class Blur implements Runnable {

    @Override
    public void run() {
      model.getEffect(EffectType.BLUR);
    }
  }

  // takes care of sharpening images
  private class Sharpen implements Runnable {

    @Override
    public void run() {
      model.getEffect(EffectType.SHARPEN);
    }
  }

  // takes care of applying a monochrome color filter to images
  private class Monochrome implements Runnable {

    @Override
    public void run() {
      model.getEffect(EffectType.MONOCHROME);
    }
  }

  // takes care of applying a sepia color filter to images
  private class Sepia implements Runnable {

    @Override
    public void run() {
      model.getEffect(EffectType.SEPIA);
    }
  }

  // creates a layer
  private class CreateLayer implements Runnable {

    @Override
    public void run() {

      layers.add(name, new SimpleImage(new int[1][1][3]));
    }
  }

  // sets the image of a layer
  private class SetLayer implements Runnable {

    @Override
    public void run() {
      model.loadImage(layers.get(name));
    }
  }

  // removes a specific layer
  private class RemoveLayer implements Runnable {

    @Override
    public void run() {
      layers.remove(name);
    }
  }

  // redoes a specific layer
  private class Redo implements Runnable {

    @Override
    public void run() {
      layers.add(name + "1", layers.get(name));
    }
  }

  // creates an invisible layer
  private class Invisible implements Runnable {

    @Override
    public void run() {
      invisImages.put(name, layers.get(name));
      layers.add(name, new SimpleImage(new int[1][1][3]));
    }
  }

  // undoes a special effect
  private class Undo implements Runnable {

    @Override
    public void run() {
      model.undo();
    }
  }

  // makes an invisible layer visible again
  private class Visible implements Runnable {

    @Override
    public void run() {
      if (invisImages.containsKey(name)) {
        layers.add(name, invisImages.remove(name));
      }
    }
  }

  // creates a checkerboard image
  private class Checkerboard implements Runnable {

    @Override
    public void run() {
      int checkerSize = Integer.valueOf(size);
      model.checkerBoardImage(checkerSize);
    }
  }

  // creates a mosaic image
  private class Mosaic implements Runnable {

    @Override
    public void run() {
      int seedValue = Integer.valueOf(seed);
      model.setSeed(seedValue);
      model.getEffect(EffectType.MOSAIC);
    }
  }

  // creates a mosaic image
  private class DownSize implements Runnable {

    @Override
    public void run() {
      int heightValue = Integer.valueOf(height);
      int widthValue = Integer.valueOf(width);
      model.setHeight(heightValue);
      model.setWidth(widthValue);
      model.getEffect(EffectType.DOWNSIZE);

      for (String key : layers.getImages().keySet()) {
        for (Image image : layers.getImages().values()) {
          layers.getImages().replace(key,
              new ApplyEffects().applyDownSize(image, heightValue, widthValue));
        }
      }
    }
  }

  // saves the image
  private class Save implements Runnable {

    @Override
    public void run() {

      // removes any empty string SortedMap entries
      for (String input : layers.getImages().keySet()) {
        if (input.equals("")) {
          layers.remove(input);
        }
      }

      String[] input = imageInput.split("\\.");

      if (layers.getImages().isEmpty()) {
        FileTypeFactory.create(fileType).export(model.getImageAlt(), imageInput);
      } else if (layers.getImages().size() == 1) {
        FileTypeFactory.create(fileType).export(layers.get(name), imageInput);
      } else {

        String firstImage = layers.getImages().firstKey();
        Image firstImage1 = layers.get(firstImage);
        int height = firstImage1.getHeight();
        int width = firstImage1.getWidth();

        boolean sameSize = true;
        for (Image image : layers.getImages().values()) {
          boolean sameHeight = image.getHeight() == height;
          boolean otherHeight = image.getHeight() == 1;
          boolean sameWidth = image.getWidth() == width;
          boolean otherWidth = image.getWidth() == 1;

          sameSize = sameSize && (sameHeight || otherHeight) && (sameWidth || otherWidth);
        }

        if (sameSize) {

          int index = imageInput.lastIndexOf('\\');
          String str = imageInput.substring(index, imageInput.length());
          String filePath = imageInput.substring(0, index);
          String[] folderName = str.split("\\.");
          int index1 = filePath.lastIndexOf('\\');
          String[] str1 = str.split("\\.");

          File hello = new File(filePath + "\\" + folderName[0]);
          hello.mkdirs();

          String key = layers.getImages().lastKey();
          SortedMap<String, Image> images2 = layers.getImages();
          if (layers.get(key).equals(new SimpleImage(new int[1][1][3]))) {
            images2.remove(key);
            String key1 = images2.lastKey();
            FileTypeFactory.create(fileType).export(
                images2.get(key1),
                filePath + "\\" + folderName[0] + "\\" + "TopMost" + "." + input[1]);
          } else {
            FileTypeFactory.create(fileType).export(
                layers.get(name),
                filePath + "\\" + folderName[0] + "\\" + "TopMost" + "." + input[1]);
          }

          int count = 0;
          for (Image image : layers.getImages().values()) {
            FileTypeFactory.create(fileType).export(
                image,
                filePath + "\\" + folderName[0] + "\\" + str1[0] + count + "." + str1[1]);
            count++;
          }

          try {
            FileOutputStream fs = new FileOutputStream(filePath + "\\" + folderName[0] + "\\"
                + "Locations.txt");

            for (String name : layers.getImages().keySet()) {
              fs.write((name + ", " + imageInput + "\n").getBytes());
            }

          } catch (FileNotFoundException e) {
            e.printStackTrace();
          } catch (IOException e) {
            e.printStackTrace();
          }

        } else {
          view.displayError("Layer images are not the same size");
        }
      }
    }
  }
}



