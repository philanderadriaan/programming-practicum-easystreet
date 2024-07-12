/*
 * TCSS 305 - Autumn 2011
 * Homework 2: Easy Street 
 * Author: Daniel M. Zimmerman and Marty Stepp
 */

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The graphical user interface for the EasyStreet program.
 * 
 * @author Marty Stepp
 * @author Daniel M. Zimmerman
 * @version Autumn 2011
 */

public class EasyStreetFrame extends JFrame implements ActionListener
{
  // Static Fields
  
  /**
   * The serial version UID.
   */
  private static final long serialVersionUID = 0;

  /**
   * The window title.
   */
  private static final String TITLE = "Easy Street";
  
  /**
   * The filename of the city map. 
   */
  private static final String CITY_FILE = "city_map.txt";
  
  /**
   * The Start command.
   */
  private static final String START_COMMAND = "Start";
  
  /**
   * The Stop command.
   */
  private static final String STOP_COMMAND = "Stop";
  
  /**
   * The Step command.
   */
  private static final String STEP_COMMAND = "Step";
  
  /**
   * The Reset command.
   */
  private static final String RESET_COMMAND = "Reset";
  
  /**
   * The initial frames per second at which the simulation will run.
   */
  private static final int INITIAL_FRAMES_PER_SECOND = 10;
  
  /**
   * The maximum frames per second at which the simulation will run.
   */
  private static final int MAX_FRAMES_PER_SECOND = 60;
  
  /**
   * The size in pixels of a side of one "square" on the grid.
   */
  private static final int SQUARE_SIZE = 40;
  
  /**
   * The size in pixels of the directional markers drawn on vehicles in debug mode.
   */
  private static final int MARKER_SIZE = 10;

  /**
   * The offset in pixels of the debug messages drawn for each square.
   */
  private static final int DEBUG_OFFSET = 10;

  /**
   * The number of clock ticks between light changes.
   */
  private static final int LIGHT_CHANGE_TICKS = 20;

  /**
   * The numerator for delay calculations.
   */
  private static final int MY_DELAY_NUMERATOR = 1000; 
  
  /**
   * The minor tick spacing for the FPS slider.
   */
  private static final int MINOR_TICK_SPACING = 1;
  
  /**
   * The major tick spacing for the FPS slider.
   */
  private static final int MAJOR_TICK_SPACING = 10;
  
  /**
   * The stroke used for painting.
   */
  private static final BasicStroke STROKE = 
    new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 
                    2, new float[] {2, 2, 2, 2}, 0);
  
  // Instance Fields
  
  /**
   * The slider for "frames per second".
   */
  private JSlider my_slider;
  
  /**
   * The panel we render graphics on.
   */
  private EasyStreetPanel my_panel;

  /**
   * A timer used to update the state of the simulation.
   */
  private final Timer my_timer;

  /**
   * A list of vehicles in the simulation.
   */
  private final List<Movable> my_vehicles;

  /**
   * The terrain grid for the simulation.
   */
  private Terrain[][] my_grid;

  /**
   * The traffic lights in the simulation.
   */
  private Light my_lights;

  /**
   * The delay between updates, based on the frames per second setting.
   */
  private int my_delay;

  /**
   * The current timestep of the simulation.
   */
  private long my_timestep;

  /**
   * A flag indicating whether or not we are running in debug mode.
   */
  private boolean my_debug_flag;

  // Constructor
  
  /**
   * Constructs a new EasyStreetFrame, using the files in the current working
   * directory.
   */
  public EasyStreetFrame()
  {
    super(TITLE);
    // initialize instance variables
    my_delay = MY_DELAY_NUMERATOR / INITIAL_FRAMES_PER_SECOND;
    my_timer = new Timer(my_delay, this);
    my_vehicles = new ArrayList<Movable>();
    my_lights = Light.GREEN;
    my_debug_flag = false;
    initGUI();
  }

  // Instance Methods
  
  /**
   * A notification method called in response to action events in this
   * window. 
   * 
   * @param the_event The action event that triggered the call.
   */
  public void actionPerformed(final ActionEvent the_event)
  {
    // event came from the timer 
    
    advanceAnimation();
    my_panel.repaint();
  }

  /**
   * Sets up the GUI.
   */
  private void initGUI()
  {
    try
    {
      readCity(CITY_FILE);
    } 
    catch (final IOException ioe) 
    {
      JOptionPane.showMessageDialog(this,
          "Could not read city map file " + CITY_FILE + ":\n\n" + 
          ioe.getMessage(), "I/O Error", JOptionPane.ERROR_MESSAGE);
    }

    // set up graphical components
    my_panel = new EasyStreetPanel();

    my_slider = new JSlider(JSlider.HORIZONTAL, 0, MAX_FRAMES_PER_SECOND,
        INITIAL_FRAMES_PER_SECOND);
    my_slider.setMajorTickSpacing(MAJOR_TICK_SPACING);
    my_slider.setMinorTickSpacing(MINOR_TICK_SPACING);
    my_slider.setPaintLabels(true);
    my_slider.setPaintTicks(true);
    my_slider.addChangeListener(new ChangeListener()
    {
      public void stateChanged(final ChangeEvent the_event)
      {
        final int value = my_slider.getValue();
        if (value > 0)
        {
          my_delay = MY_DELAY_NUMERATOR / value;
          my_timer.setDelay(my_delay);
        }
      }
    });

    final JCheckBox box = new JCheckBox("Debug Mode");
    box.addActionListener(new ActionListener() 
    {
      public void actionPerformed(final ActionEvent the_event)
      {
        my_debug_flag = box.isSelected();
      }
    });

    // layout
    final Container north = new JPanel(new FlowLayout());
    addButtons(north);

    final Container south = new JPanel(new FlowLayout());
    south.add(new JLabel("FPS: "));
    south.add(my_slider);
    south.add(box);

    final Container cp = new JPanel(new BorderLayout());
    cp.add(my_panel, BorderLayout.CENTER);
    cp.add(north, BorderLayout.NORTH);
    cp.add(south, BorderLayout.SOUTH);

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);
    add(cp);
    pack();
  }
  
  /**
   * Adds the control buttons to the specified container.
   * 
   * @param the_container The container.
   */
  private void addButtons(final Container the_container) 
  {
    final JButton start = new JButton(START_COMMAND);
    start.addActionListener(new ActionListener() 
    {
      public void actionPerformed(final ActionEvent the_event)
      {
        my_timer.start();
      }
    });
    
    final JButton stop = new JButton(STOP_COMMAND);
    stop.addActionListener(new ActionListener()
    {
      public void actionPerformed(final ActionEvent the_event)
      {
        my_timer.stop();
      }
    });
    
    final JButton step = new JButton(STEP_COMMAND);
    step.addActionListener(new ActionListener()
    {
      public void actionPerformed(final ActionEvent the_event)
      {
        advanceAnimation();
        repaint();
      }
    });
    
    final JButton reset = new JButton(RESET_COMMAND);
    reset.addActionListener(new ActionListener()
    {
      public void actionPerformed(final ActionEvent the_event)
      {
        reset();
        repaint();
      }
    });
     
    the_container.add(start);
    the_container.add(stop);
    the_container.add(step);
    the_container.add(reset);
  }
  
  /**
   * Generates a read-only neighbors map for the specified vehicle.
   * 
   * @param the_mover The vehicle.
   * @return The neighbors map.
   */
  private Map<Direction, Terrain> generateNeighbors(final Movable the_mover)
  {
    final int x = the_mover.x();
    final int y = the_mover.y();
    final Map<Direction, Terrain> result = new HashMap<Direction, Terrain>();
    
    for (Direction dir : Direction.values())
    {
      if (isValidIndex(y + dir.dy(), x + dir.dx()))
      {
        result.put(dir, my_grid[y + dir.dy()][x + dir.dx()]);   
      }
    }
    return Collections.unmodifiableMap(result);
  }
  
  /**
   * Advances the simulation by one frame of animation, moving each vehicle
   * once and checking collisions.
   */
  private void advanceAnimation()
  {
    for (Movable mover : my_vehicles)
    {
      // move the vehicle
      moveVehicle(mover);

      // look for collisions
      for (Movable other : my_vehicles)
      {
        handleCollision(mover, other);
      }
    }

    my_timestep++;
    if (my_timestep % LIGHT_CHANGE_TICKS == 0)
    {
      my_lights = my_lights.advance();
    }
  }
  
  /**
   * Moves the specified vehicle.
   * 
   * @param the_vehicle The vehicle to move.
   */
  private void moveVehicle(final Movable the_vehicle) 
  {
    final Map<Direction, Terrain> neighbors = 
      generateNeighbors(the_vehicle);

    if (the_vehicle.isAlive())
    {
      final Direction new_direction = 
        the_vehicle.chooseDirection(neighbors, my_lights);
      the_vehicle.setDirection(new_direction);
      
      // move one square in current direction, if it's okay to do so
      if (the_vehicle.canPass(neighbors.get(new_direction), my_lights))
      {
        the_vehicle.setDirection(new_direction);
        the_vehicle.setX(the_vehicle.x() + new_direction.dx());
        the_vehicle.setY(the_vehicle.y() + new_direction.dy());
      }
    }
    else 
    {
      // become one move closer to revival
      the_vehicle.poke();
    }
  }
  
  /**
   * Handles a collision between the two specified vehicles.
   * 
   * @param the_first The first vehicle.
   * @param the_second The other vehicle.
   */
  private void handleCollision(final Movable the_first, 
                               final Movable the_second)
  {
    if (!the_first.equals(the_second) && 
        the_first.x() == the_second.x() && 
        the_first.y() == the_second.y())
    {
      // tell both vehicles they have collided
      the_first.collide(the_second);
      the_second.collide(the_first);
    }
  }
  
  /** 
   * Tests whether the square at the given x/y position exists on the map. 
   * 
   * @param the_y The y position.
   * @param the_x The x position.
   * @return true if the position exists on the map, false otherwise.
   */
  private boolean isValidIndex(final int the_y, final int the_x)
  {
    return 0 <= the_y && the_y < my_grid.length && 0 <= the_x && the_x < my_grid[the_y].length;
  }

  /**
   * Tests whether the square at the given x/y position exists on the
   * map and is a street.
   * 
   * @param the_y The y position.
   * @param the_x The x position.
   * @return true if the position exists on the map, false otherwise.
   */
  private boolean isValidStreet(final int the_y, final int the_x)
  {
    return isValidIndex(the_y, the_x) && 
           my_grid[the_y][the_x] == Terrain.STREET;
  }

  /**
   * Reads the map from the specified file in the current working directory.
   * 
   * @param the_filename The filename.
   * @throws IOException if there is a problem reading the map. 
   */
  private void readCity(final String the_filename) throws IOException
  {
    Scanner input;
    
    try
    {
      input = new Scanner(new File(the_filename));
    }
    catch (final IOException e)
    {
      input = new Scanner(getClass().getResourceAsStream(the_filename));
    }

    // First, we read the map description
    
    readGrid(input);
    
    // Then, we read where the initial vehicles are

    readVehicles(input);
  }

  /**
   * Reads the grid portion of the map file.
   * 
   * @param the_input The input scanner.
   */
  private void readGrid(final Scanner the_input)
  {
    final int num_lines = the_input.nextInt();
    final int num_letters = the_input.nextInt();
    the_input.nextLine();
    my_grid = new Terrain[num_lines][num_letters];
    for (int line_count = 0; line_count < num_lines; line_count++)
    {
      final String line = the_input.nextLine();
      for (int letter_count = 0; letter_count < num_letters; letter_count++)
      {
        my_grid[line_count][letter_count] = 
          Terrain.valueOf(line.charAt(letter_count));
      }
    }
  }
  
  /**
   * Reads the vehicle portion of the map file.
   * 
   * @param the_input The input scanner.
   */
  private void readVehicles(final Scanner the_input)
  {
    final int num_vehicles = the_input.nextInt();
    the_input.nextLine();
    for (int line_count = 0; line_count < num_vehicles; line_count++)
    {
      final char vehicle_type = the_input.next().charAt(0);
      final int vehicle_x = the_input.nextInt();
      final int vehicle_y = the_input.nextInt();
      final char vehicle_direction = the_input.next().charAt(0);
      final char vehicle_terrain = the_input.next().charAt(0);
      
      switch (vehicle_type) 
      {
        case 'B': // Bicycle
          my_vehicles.add(new Bicycle(vehicle_x, vehicle_y, 
                                      Direction.valueOf(vehicle_direction),
                                      Terrain.valueOf(vehicle_terrain)));
          break;
          
        case 'C': // Car
          my_vehicles.add(new Car(vehicle_x, vehicle_y, 
                                  Direction.valueOf(vehicle_direction),
                                  Terrain.valueOf(vehicle_terrain)));
          break;
        
        case 'H': // Human
          my_vehicles.add(new Human(vehicle_x, vehicle_y, 
                                    Direction.valueOf(vehicle_direction),
                                    Terrain.valueOf(vehicle_terrain)));
          break;

        case 'T': // Truck
          my_vehicles.add(new Truck(vehicle_x, vehicle_y, 
                                    Direction.valueOf(vehicle_direction),
                                    Terrain.valueOf(vehicle_terrain)));
          break;
        
        default:
          // this should never happen
          assert false;
      }
      the_input.nextLine();
    }
  }
  
  /**
   * Resets all the vehicles to their initial locations, resets the tick
   * counter, and stops the simulation.
   */
  private void reset()
  {
    my_timer.stop();
    my_timestep = 0;
    my_lights = Light.GREEN;

    for (Movable mov : my_vehicles)
    {
      mov.reset();
    }
  }

  /**
   * Draws the debug information for a single movable.
   * 
   * @param the_graphics The graphic context.
   * @param the_mover The movable being drawn.
   */
  private void drawDebugInfo(final Graphics2D the_graphics, 
                             final Movable the_mover)
  {
    if (my_debug_flag)
    {
      int x = the_mover.x() * SQUARE_SIZE;
      int y = the_mover.y() * SQUARE_SIZE;
      
      // draw numbers on each vehicle
      the_graphics.setColor(Color.WHITE);
      the_graphics.drawString(the_mover.toString(), x, y + SQUARE_SIZE - 1);
      the_graphics.setColor(Color.BLACK);
      the_graphics.drawString(the_mover.toString(), x + 1, y + SQUARE_SIZE);
  
      // draw arrow on vehicle for its direction
      final Direction dir = the_mover.getDirection();
      int dx = (SQUARE_SIZE - MARKER_SIZE) / 2;
      int dy = dx;
      
      switch (dir)
      {
        case WEST:
          dx = 0;
          break;
          
        case EAST:
          dx = SQUARE_SIZE - MARKER_SIZE;
          break;
          
        case NORTH:
          dy = 0;
          break;
        
        case SOUTH:
          dy = SQUARE_SIZE - MARKER_SIZE;
          break;
          
        default:
      }
      
      x = x + dx;
      y = y + dy;
  
      the_graphics.setColor(Color.YELLOW);
      the_graphics.fillOval(x, y, MARKER_SIZE, MARKER_SIZE);
    }
  }
  
  /**
   * Draws the city map with the specified Graphics2D object.
   * 
   * @param the_graphics The Graphics2D object.
   */
  private void drawMap(final Graphics2D the_graphics)
  {
    for (int y = 0; y < my_grid.length; y++)
    {
      final int topy = y * SQUARE_SIZE;
      
      for (int x = 0; x < my_grid[y].length; x++)
      {
        final int leftx = x * SQUARE_SIZE;
        
        switch (my_grid[y][x])
        {
          case STREET:
            the_graphics.setPaint(Color.LIGHT_GRAY);
            the_graphics.fillRect(leftx, topy, SQUARE_SIZE, SQUARE_SIZE);
            drawStreetLines(the_graphics, x, y);
            break;
            
          case WALL:
            the_graphics.setPaint(Color.BLACK);
            the_graphics.fillRect(leftx, topy, SQUARE_SIZE, SQUARE_SIZE);
            break;
            
          case TRAIL:
            the_graphics.setPaint(Color.YELLOW.darker().darker());
            the_graphics.fillRect(leftx, topy, SQUARE_SIZE, SQUARE_SIZE);
            break;
            
          case LIGHT:
            // draw a circle of appropriate color
            the_graphics.setPaint(Color.LIGHT_GRAY);
            the_graphics.fillRect(leftx, topy, SQUARE_SIZE, SQUARE_SIZE);
            setLightPaint(the_graphics);
            the_graphics.fillOval(leftx, topy, SQUARE_SIZE, SQUARE_SIZE);
            break;
            
          default:
        }
        
        drawDebugInfo(the_graphics, x, y);
      }
    }
  }

  /**
   * Sets the paint color appropriately for the current lights.
   * 
   * @param the_graphics The Graphics2D object on which to set the paint.
   */
  private void setLightPaint(final Graphics2D the_graphics)
  {
    the_graphics.setPaint(Color.GREEN.darker());
    switch (my_lights)
    {
      case RED:
        the_graphics.setPaint(Color.RED.darker());
        break;
        
      case YELLOW:
        the_graphics.setPaint(Color.YELLOW);
        break; 
        
      default: // GREEN, but we already set the color green above.
    }
  }
  
  /**
   * Draws debugging information, if necessary.
   * 
   * @param the_graphics The Graphics context to use for drawing.
   * @param the_x The x-coordinate of the street.
   * @param the_y The y-coordinate of the street.
   */
  private void drawDebugInfo(final Graphics2D the_graphics,
                             final int the_x, final int the_y)
  {
    if (my_debug_flag)
    {
      // draw numbers for the row and column
      final Paint old_paint = the_graphics.getPaint();
      the_graphics.setPaint(Color.BLACK);

      final int leftx = the_x * SQUARE_SIZE;
      final int topy = the_y * SQUARE_SIZE;
      the_graphics.drawString("(" + the_x + ", " + the_y + ")", leftx, 
                              topy + DEBUG_OFFSET);
      the_graphics.setPaint(old_paint);
    }
  }
  
  /**
   * Draws dotted lines on streets.
   * 
   * @param the_graphics The Graphics context to use for drawing.
   * @param the_x The x-coordinate of the street.
   * @param the_y The y-coordinate of the street.
   */
  private void drawStreetLines(final Graphics2D the_graphics,
                               final int the_x, final int the_y)
  {
    final Paint old_paint = the_graphics.getPaint();
    the_graphics.setPaint(Color.YELLOW);
    
    final int leftx = the_x * SQUARE_SIZE;
    final int topy = the_y * SQUARE_SIZE;
    final int centerx = leftx + SQUARE_SIZE / 2;
    final int centery = topy + SQUARE_SIZE / 2;
    final int rightx = leftx + SQUARE_SIZE;
    final int bottomy = topy + SQUARE_SIZE;
    
    if (isValidStreet(the_y - 1, the_x))
    {
      the_graphics.drawLine(centerx, centery, centerx, topy); // above
    }
    if (isValidStreet(the_y + 1, the_x))
    {
      the_graphics.drawLine(centerx, centery, centerx, bottomy); // below
    }
    if (isValidStreet(the_y, the_x - 1))
    {
      the_graphics.drawLine(centerx, centery, leftx, centery); // left
    }
    if (isValidStreet(the_y, the_x + 1))
    {
      the_graphics.drawLine(centerx, centery, rightx, centery); // right
    } 
    
    the_graphics.setPaint(old_paint);
  }

  /**
   * A drawing panel for the map.
   */
  private class EasyStreetPanel extends JPanel
  {
    // Instance Fields
    
    /**
     * The UID of this class (to avoid warnings).
     */
    private static final long serialVersionUID = 4269666L;

    /**
     * The font used by this panel.
     */
    private final Font my_font = new Font("SansSerif", Font.BOLD, 9);

    // Constructor
    
    /** 
     * Constructs a new panel. 
     */
    public EasyStreetPanel()
    {
      super();
      setPreferredSize(new Dimension(my_grid[0].length * SQUARE_SIZE,
          my_grid.length * SQUARE_SIZE));
      setBackground(Color.GREEN);
      setFont(my_font);
    }

    // Instance Methods
    
    /** 
     * Paints this panel on the screen with the specified Graphics object.
     * 
     * @param the_graphics The Graphics object.
     */
    public void paintComponent(final Graphics the_graphics)
    {
      super.paintComponent(the_graphics);
      final Graphics2D g2 = (Graphics2D) the_graphics;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
          RenderingHints.VALUE_ANTIALIAS_ON);

      g2.setStroke(STROKE);

      // draw city map

      drawMap(g2);
      
      // draw vehicles
      for (Movable mover : my_vehicles)
      {
        final String image_filename = mover.getImageFileName();
        final Image img = loadImage(image_filename);
        g2.drawImage(img, mover.x() * SQUARE_SIZE, mover.y() * SQUARE_SIZE, 
                     SQUARE_SIZE, SQUARE_SIZE, this);

        drawDebugInfo(g2, mover);
      }
      
      drawPanelDebugInfo(g2);
    }
    
    /**
     * Paints the debug information for the panel, if debugging
     * is turned on.
     * 
     * @param the_g2d The Graphics2D object to paint to.
     */
    private void drawPanelDebugInfo(final Graphics2D the_g2d)
    {
      if (my_debug_flag)
      {
        the_g2d.setColor(Color.WHITE);
        the_g2d.drawString("Update #" + my_timestep, DEBUG_OFFSET / 2, 
                      my_font.getSize() + DEBUG_OFFSET / 2);
      }
    }
    
    /**
     * Loads the image with the specified filename.
     * 
     * @param the_filename The file name.
     * @return The image.
     */
    private Image loadImage(final String the_filename)
    {
      ImageIcon icon = new ImageIcon(the_filename);
      
      if (icon.getImageLoadStatus() != MediaTracker.COMPLETE)
      {
        icon = new ImageIcon(getClass().getResource(the_filename));
      }
      
      return icon.getImage();
    }
  }
}

// end of class EasyStreetFrame
