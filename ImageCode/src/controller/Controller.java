package controller;

import java.util.HashMap;
import model.Image;

/**
 * This interface hosts methods related to the controller functionality.
 * In general, it hosts methods that are responsible for processing and appropriately reacting to
 * the user's command.
 */
public interface Controller {

  /**
   * Takes in user input and parses it. After parsing, it determines which method to call based on
   * the desired command.
   */

  void processInput();

  /**
   * Retreives the HashMap storing invisible layers.
   *
   * @return a HashMap.
   */
  HashMap<String, Image> getInvisibleLayers();


  /**
   * Accepts the desired command as a string and passes it on to the controller.
   * @param s the command as a string.
   */
  void acceptCommand(String s);

}
