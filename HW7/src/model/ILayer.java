package model;

import java.util.SortedMap;

/**
 * This is the interface for a Layer. This interface will contain methods to access and manipulate
 * layers. Any future functionality related to layers of images will first be added here.
 */
public interface ILayer {

  /**
   * Adds a layer to the SortedMap.
   *
   * @param name  the name representation of the layer.
   * @param image the image the layer represents
   */
  void add(String name, Image image);

  /**
   * Removes a layer from the SortedMap.
   *
   * @param name the name of the layer wanted to be removed
   */
  void remove(String name);

  /**
   * Retrieves the Image corresponding to a specific layer.
   *
   * @param name the name of layer that the Image belongs to.
   * @return an Image
   */
  Image get(String name);

  /**
   * Gets the SortedMap containing the layers.
   *
   * @return the SortedMap.
   */
  SortedMap<String, Image> getImages();


}
