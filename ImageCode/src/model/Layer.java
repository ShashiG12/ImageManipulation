package model;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * This class represents a Layer. The Layer class contains one SortedMap that maps the properties of
 * a layer - which are a name and an Image. The methods here are used to access the contents of the
 * map in varied ways.
 */
public class Layer implements ILayer {

  private SortedMap<String, Image> images;


  /**
   * Constructs a Layer.
   */
  public Layer() {
    this.images = new TreeMap<>() {
    };
  }

  @Override
  public void add(String name, Image image) {
    if (name == null || image == null) {
      throw new IllegalArgumentException("Inputs cannot be null");
    }

    this.images.put(name, image);
  }

  @Override
  public void remove(String name) {
    if (name == null) {
      throw new IllegalArgumentException("Inputs cannot be null");
    }
    this.images.remove(name);
  }

  @Override
  public Image get(String name) {
    if (name == null) {
      throw new IllegalArgumentException("Inputs cannot be null");
    }

    return this.images.get(name);
  }

  @Override
  public SortedMap<String, Image> getImages() {
    return this.images;
  }

}
