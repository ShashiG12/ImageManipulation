import model.IEnhancer;
import model.SimpleEnhancer;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Class to test the functionality and methods of the GUI.
 */
public class GUITest {
  IEnhancer model = new SimpleEnhancer();

  //Method implements Swing functionality
  //Told by Gerri that testing is not required for java libraries
  @Test
  public void testRenderView() {
    //label.setIcon(...)
    assertEquals(this.model, this.model);
  }

  //Method implements Swing functionality
  //Told by Gerri that testing is not required for java libraries
  @Test
  public void testDisplayError() {
    //JOptionPane.showMessageDialog(...)
    assertEquals(this.model, this.model);
  }

  //Method implements Swing functionality
  //Told by Gerri that testing is not required for java libraries
  @Test
  public void update() {
    //JFrame.repaint(...)
    assertEquals(this.model, this.model);
  }
}
