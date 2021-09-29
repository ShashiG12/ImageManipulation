package view;

import controller.Controller;
import controller.GUIController;
import controller.SimpleController;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import model.IEnhancer;
import model.Image;
import model.Layer;

/**
 * This class represents the GUIView. The GUIView is different from the view required in the
 * previous assignment in that it communicates with our new controller called the GUIController.
 * The GUIView is a simple, easy to use way of rendering modifications to individual images and
 * layers.
 */

public class GUIView extends JFrame implements IGUIView {

  private IEnhancer model;
  private final JFileChooser fc = new JFileChooser();
  private final int BUTTON_WIDTH = 150;
  private final int BUTTON_HEIGHT = 50;
  private final int MAIN_BUTTON_Y = 600;
  private Controller controller;
  private JLabel imageLabel;
  private Layer layers = new Layer();


  /**
   * Constructs the GUI view.
   * @param model the model to be used inside of the GUIController so that the view can communicate
   *        user commands to the controller for execution.
   */
  public GUIView(IEnhancer model) {
    super("Image Manipulation Program");
    super.setSize(715, 765);
    super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    super.getContentPane().setLayout(null);
    this.model = model;
    initializeButtons();
    super.setVisible(true);
    this.controller = new GUIController(this, this.layers, model);

    this.imageLabel = new JLabel(new ImageIcon());
    this.imageLabel.setHorizontalAlignment(JLabel.CENTER);
    this.imageLabel.setVisible(true);
    //this.imageLabel.setBounds(0, 0, 300, 300);
    //this.imageLabel.setPreferredSize(new Dimension(200, 200));
    this.getContentPane().add(imageLabel, BorderLayout.CENTER);

    JScrollPane scrollPane = new JScrollPane();

    scrollPane = new JScrollPane(this.imageLabel);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setBounds(0, 0, 700, 700);
    scrollPane.setVisible(true);
    getContentPane().add(scrollPane);
  }

  private void initializeButtons() {

    // Layer Menu
    JButton createLayer = new JButton("Create layer");
    createLayer.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String name = JOptionPane.showInputDialog("Enter layer name:");
        controller.acceptCommand("create layer " + name);
      }
    });
    createLayer.setBounds(10, MAIN_BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
    super.getContentPane().add(createLayer);




    JButton getCurrentLayer = new JButton("Set current layer");
    getCurrentLayer.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {


        Object[] possibilites = new Object[layers.getImages().size()];


        int count = 0;
        for (String string : layers.getImages().keySet()) {
          possibilites[count] = string;
          count++;
        }

        String name = (String) JOptionPane.showInputDialog(getCurrentLayer, "Enter layer name:",
            "Get current layer", JOptionPane.PLAIN_MESSAGE, new ImageIcon(), possibilites, null);
        if ((name != null) && (name.length() > 0)) {
          controller.acceptCommand("current " + name);
        }
      }
    });
    getCurrentLayer.setBounds(200, MAIN_BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
    super.getContentPane().add(getCurrentLayer);










    JButton removeLayerButton = new JButton("Remove layer");
    removeLayerButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String name = JOptionPane.showInputDialog("Enter layer name:");
        controller.acceptCommand("remove layer " + name);
      }
    });
    removeLayerButton.setBounds(200, MAIN_BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
    super.getContentPane().add(removeLayerButton);

    JButton redoLayerButton  = new JButton("Redo layer");
    redoLayerButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String name = JOptionPane.showInputDialog("Enter layer name:");
        controller.acceptCommand("redo layer " + name);
      }
    });
    redoLayerButton.setBounds(200, MAIN_BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
    super.getContentPane().add(redoLayerButton);

    JButton invisibleLayerButton  = new JButton("Make layer invisible");
    invisibleLayerButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String name = JOptionPane.showInputDialog("Enter layer name:");
        controller.acceptCommand("invisible " + name);
      }
    });
    invisibleLayerButton.setBounds(200, MAIN_BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
    super.getContentPane().add(invisibleLayerButton);


    JButton visibleLayerButton  = new JButton("Make layer visible");
    visibleLayerButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String name = JOptionPane.showInputDialog("Enter layer name:");
        controller.acceptCommand("visible " + name);
      }
    });
    visibleLayerButton.setBounds(200, MAIN_BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
    super.getContentPane().add(visibleLayerButton);







    // File Menu
    JButton importButton = new JButton("Load a file");
    importButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int returnVal = fc.showOpenDialog(importButton);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
          File file = fc.getSelectedFile();

          controller.acceptCommand("load " + file.getAbsolutePath());
        }
      }
    });
    importButton.setBounds(600, MAIN_BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
    super.getContentPane().add(importButton);

    //undo last action
    JButton undoButton = new JButton("Undo previous action");
    undoButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.acceptCommand("undo");
      }
    });
    undoButton.setBounds(400, MAIN_BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
    super.getContentPane().add(undoButton);

    JButton readBatch = new JButton("Read a batch file");
    readBatch.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int returnVal = fc.showOpenDialog(importButton);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
          File file = fc.getSelectedFile();
          Readable readable = null;
          try {
            readable = new FileReader(file);
          } catch (FileNotFoundException fileNotFoundException) {
            displayError(fileNotFoundException.toString());
          }
          Controller simpleController = new SimpleController(readable, layers, model);
          simpleController.processInput();
        }
      }
    });
    readBatch.setBounds(600, MAIN_BUTTON_Y + 200, BUTTON_WIDTH, BUTTON_HEIGHT);
    super.getContentPane().add(readBatch);


    JButton saveButton = new JButton("Save a file");
    saveButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int returnVal = fc.showSaveDialog(saveButton);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
          File file = fc.getSelectedFile();
          controller.acceptCommand("save " + file.getAbsolutePath());
        }
      }
    });
    saveButton.setBounds(600, MAIN_BUTTON_Y + 200, BUTTON_WIDTH, BUTTON_HEIGHT);
    super.getContentPane().add(saveButton);







    // Filter Menu
    JButton blurButton = new JButton("Blur");
    blurButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.acceptCommand("blur");
      }
    });
    blurButton.setBounds(10, MAIN_BUTTON_Y + 100, BUTTON_WIDTH, BUTTON_HEIGHT);
    super.getContentPane().add(blurButton);




    JButton sharpenButton = new JButton("Sharpen");
    sharpenButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.acceptCommand("sharpen");
      }
    });
    sharpenButton.setBounds(10, MAIN_BUTTON_Y + 200, BUTTON_WIDTH, BUTTON_HEIGHT);
    super.getContentPane().add(sharpenButton);




    JButton monochromeButton = new JButton("Monochrome");
    monochromeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.acceptCommand("monochrome");
      }
    });
    monochromeButton.setBounds(200, MAIN_BUTTON_Y + 100, BUTTON_WIDTH, BUTTON_HEIGHT);
    super.getContentPane().add(monochromeButton);

    //sepia
    JButton sepiaButton = new JButton("Sepia");
    sepiaButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.acceptCommand("sepia");
      }
    });
    sepiaButton.setBounds(400, MAIN_BUTTON_Y + 100, BUTTON_WIDTH, BUTTON_HEIGHT);
    super.getContentPane().add(sepiaButton);


    JButton downSizeButton = new JButton("Down Size");
    downSizeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String height = JOptionPane.showInputDialog("Enter height:");
        String width = JOptionPane.showInputDialog("Enter width:");
        controller.acceptCommand("downsize " + height + " " + width);
      }
    });
    sepiaButton.setBounds(400, MAIN_BUTTON_Y + 100, BUTTON_WIDTH, BUTTON_HEIGHT);
    super.getContentPane().add(downSizeButton);


    JButton mosaicButton = new JButton("Mosaic");
    mosaicButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String seed = JOptionPane.showInputDialog("Enter # of seeds:");
        controller.acceptCommand("mosaic " + seed);
      }
    });
    mosaicButton.setBounds(400, MAIN_BUTTON_Y + 100, BUTTON_WIDTH, BUTTON_HEIGHT);
    super.getContentPane().add(mosaicButton);



    // Programmatic Menu
    JButton checkerboard = new JButton("Checkerboard");
    checkerboard.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String size = JOptionPane.showInputDialog("Enter checkerboard size:");
        controller.acceptCommand("checkerboard " + size);
      }
    });
    checkerboard.setBounds(400, MAIN_BUTTON_Y + 100, BUTTON_WIDTH, BUTTON_HEIGHT);
    super.getContentPane().add(checkerboard);




    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    JMenu filterMenu = new JMenu("Filter");
    JMenu programmaticMenu = new JMenu("Programmatic");
    JMenu layerMenu = new JMenu("Layer");


    layerMenu.add(createLayer);
    layerMenu.add(getCurrentLayer);
    layerMenu.add(removeLayerButton);
    layerMenu.add(redoLayerButton);
    layerMenu.add(invisibleLayerButton);
    layerMenu.add(visibleLayerButton);

    fileMenu.add(importButton);
    fileMenu.add(saveButton);
    fileMenu.add(readBatch);
    fileMenu.add(undoButton);



    filterMenu.add(blurButton);
    filterMenu.add(sharpenButton);
    filterMenu.add(sepiaButton);
    filterMenu.add(monochromeButton);
    filterMenu.add(downSizeButton);
    filterMenu.add(mosaicButton);

    programmaticMenu.add(checkerboard);

    menuBar.add(fileMenu);
    menuBar.add(filterMenu);
    menuBar.add(programmaticMenu);
    menuBar.add(layerMenu);

    this.setJMenuBar(menuBar);

  }



  @Override
  public void renderView(Image image) {
    this.imageLabel.setIcon(new ImageIcon(this.imageToBuffered(image)));
  }

  //converts a Image to a buffered image
  private BufferedImage imageToBuffered(Image image) {
    BufferedImage image1 = new BufferedImage(
        image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

    for (int i = 0; i < image.getImage().length; i++) {
      for (int j = 0; j < image.getImage()[0].length; j++) {
        int r = image.getImage()[i][j][0];
        int g = image.getImage()[i][j][1];
        int b = image.getImage()[i][j][2];
        int color = (r << 16) + (g << 8) + b;
        image1.setRGB(j, i, color);
      }
    }
    return image1;
  }

  @Override
  public void displayError(String error) {
    JOptionPane.showMessageDialog(
        this, error, "Error", JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void update() {
    this.repaint();
  }
}

