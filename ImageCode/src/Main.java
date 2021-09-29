import controller.Controller;
import controller.SimpleController;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import model.IEnhancer;
import model.Layer;
import model.SimpleEnhancer;
import view.GUIView;
import view.IGUIView;

/**
 * Main class to run program functionality.
 */
public class Main {

  /**
   * Method used to run program.
   * @param args arguments for main method
   */
  public static void main(String[] args) throws FileNotFoundException {

    IEnhancer model = new SimpleEnhancer();

    switch (args[0]) {
      case "-interactive":
        IGUIView view = new GUIView(model);
        break;
      case "-text":
        Controller controller = new SimpleController(new InputStreamReader(System.in), new Layer(),
            model);
        controller.processInput();
        break;
      case "-script":
        Reader inputFile = new FileReader(args[1]);
        Controller controller1 = new SimpleController(inputFile, new Layer(), model);
        try {
          controller1.processInput();
        }
        catch (Exception e) {
          System.out.println("Invalid file");
        }
        break;
      default:
        System.out.println("Not a valid input");
        break;
    }
  }


}
