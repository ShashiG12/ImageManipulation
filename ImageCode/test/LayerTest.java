import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.HashMap;
import model.Image;
import model.Layer;
import model.SimpleImage;
import org.junit.Test;

/**
 * Represents examples of and tests for Layers.
 */
public class LayerTest {

  Layer layers = new Layer();
  StringReader reader = new StringReader("qqqq");
  Image image = new SimpleImage(new int[1][1][3]);


  @Test
  public void testAdd() {
    layers.add("first", this.image);
    assertTrue(layers.getImages().containsKey("first"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddNull() {
    layers.add(null, this.image);
    assertTrue(layers.getImages().containsKey("first"));
  }

  @Test
  public void testRemove() {
    layers.add("first", this.image);
    assertTrue(layers.getImages().containsKey("first"));
    layers.remove("first");
    assertFalse(layers.getImages().containsKey("first"));

  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveNull() {
    layers.add("first", this.image);
    assertTrue(layers.getImages().containsKey("first"));
    layers.remove(null);
  }

  @Test
  public void testGet() {
    layers.add("first", this.image);
    assertArrayEquals(new int[1][1][3], this.layers.get("first").getImage());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetNull() {
    layers.add("first", this.image);
    assertArrayEquals(new int[1][1][3], this.layers.get(null).getImage());
  }

  @Test
  public void testGetImages() {
    HashMap<String, Image> images = new HashMap<>();
    images.put("first", this.image);
    layers.add("first", this.image);
    assertEquals(images, layers.getImages());
  }
}
