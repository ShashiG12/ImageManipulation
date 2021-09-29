package view;

import model.Image;

/**
 * Interface defining required methods for a GUI view.
 * Any future functionality related to the GUI will be first be added here.
 */
public interface IGUIView {

  /**
   * Allows for the GUI to showcase a particular image to the user.
   * @param image the image to be displayed.
   */
  void renderView(Image image);

  /**
   * Displays any internal error to the user.
   * @param error the error to be displayed.
   */
  void displayError(String error);

  /**
   * Refreshes the view.
   */
  void update();
}

