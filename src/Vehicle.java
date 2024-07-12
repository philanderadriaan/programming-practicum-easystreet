/*
 * 
 * 
 * TCSS 305 - Autumn 2011
 * 
 * Assignment 2 - Easy Street
 * 
 * 17 October 2011,
 */
import java.util.Arrays;
import java.util.Map;

/**
 * This class represents a vehicle in a general sense, which contains
 * characteristics that all vehicles have.
 * 
 * @author 
 * @version 65
 */
abstract class Vehicle implements Movable
{
  /**
   * Starting x.
   */
  private final int my_initial_x;
  /**
   * Starting y.
   */
  private final int my_initial_y;
  /**
   * Starting direction.
   */
  private final Direction my_initial_direction;
  /**
   * Starting terrain.
   */
  private final Terrain my_initial_terrain;
  /**
   * Current x.
   */
  private int my_current_x;
  /**
   * Current y.
   */
  private int my_current_y;
  /**
   * Current direction.
   */
  private Direction my_current_direction;
  /**
   * Time left for the vehicle to stay dead.
   */
  private int my_death_count;
  /**
   * Time it takes for vehicle to stay dead after being ran over.
   */
  private final int my_death_time;
  /**
   * The light that the vehicle will ignore.
   */
  private final Light my_ignore_light;

  /**
   * Constructs a vehicle and put it in easystreet.
   * 
   * @param the_x Starting x.
   * @param the_y Starting y.
   * @param the_direction Starting direction.
   * @param the_terrain Starting terrain.
   * @param the_death_time Time it takes for vehicle to stay dead.
   * @param the_ignore_light The light that the vehicle ignores.
   */
  protected Vehicle(final int the_x, final int the_y, final Direction the_direction,
                    final Terrain the_terrain, final int the_death_time,
                    final Light the_ignore_light)
  {
    /*
     * Initializes initial positions.
     */
    my_initial_x = the_x;
    my_initial_y = the_y;
    my_initial_direction = the_direction;
    my_initial_terrain = the_terrain;
    /*
     * Sets the current position to initial positions.
     */
    privateReset();
    /*
     * Initializes death time.
     */
    my_death_time = the_death_time;
    /*
     * Initializes ignore light.
     */
    my_ignore_light = the_ignore_light;
  }

  @Override
  public boolean canPass(final Terrain the_terrain, final Light the_light)
  {
    /*
     * Initializes an array of lights in order.
     */
    final Light[] light_array = {Light.RED, Light.YELLOW, Light.GREEN};
    /*
     * A vehicle in the streets can move all around the streets and pass through
     * the light that it ignores, but the light that it does not ignore will
     * block it.
     */
    return the_terrain == Terrain.STREET ||
           the_terrain == Terrain.LIGHT &&
           Arrays.asList(light_array).indexOf(the_light) >=
           Arrays.asList(light_array).indexOf(my_ignore_light);
  }

  /**
   * Determines the direction the vehicles will choose.
   * 
   * @param the_neighbors Terrain neighboring the vehicle.
   * @param the_priorities An array of priorities to choose the direction.
   * @param the_choice Method of choosing the priorities.
   * @return Determines where the vehicle will go.
   */
  protected Direction chooseDirection(final Map<Direction, Terrain> the_neighbors,
                                      final Priority[] the_priorities,
                                      final PriorityChoice the_choice)
  {
    /*
     * Directions starts out random.
     */
    Direction direction = Direction.random();
    /*
     * Checks for existence of path in the array of priorities.
     */
    if (pathExist(the_neighbors, the_priorities))
    {
      /*
       * Path exists. Choose the path based on the method to choose priority.
       */
      if (the_choice == PriorityChoice.HIGHEST)
      {
        /*
         * Direction determined by the highest priority, which is the one that
         * is placed earliest in the array.
         */
        direction = the_priorities[getIndex(the_neighbors, the_priorities)].getDirection();
      }
      else
      {
        /*
         * Keeps producing random directions until the direction is equals to
         * with a path in the array of priority.
         */
        while (!Arrays.asList(the_priorities).contains(new Priority(direction, the_neighbors.
                                                                    get(direction))))
        {
          direction = Direction.random();
        }
      }
    }
    return direction;
  }

  @Override
  public void collide(final Movable the_other)
  {
    /*
     * Checks if the vehicle is still alive, and compare their death time. A
     * vehicle with lower death time will run over a vehicle with a much higher
     * death time.
     */
    if (the_other.getDeathTime() < getDeathTime() && isAlive())
    {
      /*
       * Sets the countdown to the death time.
       */
      my_death_count = getDeathTime();
    }
  }

  @Override
  public String getImageFileName()
  {
    /*
     * For building strings.
     */
    final StringBuilder builder = new StringBuilder();
    /*
     * File name starts with a lower case version of class name.
     */
    builder.append(getClass().getName().toLowerCase());
    /*
     * Checks if the vehicle is alive or not.
     */
    if (!isAlive())
    {
      /*
       * The vehicle is dead. Add additional strings to get the dead vehicle
       * image.
       */
      builder.append("_dead");
    }
    /*
     * Image is in gif format.
     */
    builder.append(".gif");
    /*
     * Returns the string representation of the builder.
     */
    return builder.toString();
  }

  @Override
  public int getDeathTime()
  {
    /*
     * How long will it stay dead?
     */
    return my_death_time;
  }

  @Override
  public Direction getDirection()
  {
    /*
     * What is the current direction?
     */
    return my_current_direction;
  }

  @Override
  public int x()
  {
    /*
     * What is the current x coordinate?
     */
    return my_current_x;
  }

  @Override
  public int y()
  {
    /*
     * What is the current y coordinate?
     */
    return my_current_y;
  }

  @Override
  public boolean isAlive()
  {
    /*
     * A vehicle is alive if the death counter is less than or equals to zero.
     */
    return my_death_count <= 0;
  }

  @Override
  public void poke()
  {
    /*
     * Death counter goes down by 1.
     */
    my_death_count--;
  }

  @Override
  public void reset()
  {
    /*
     * Resets vehicle to original position.
     */
    privateReset();
  }

  @Override
  public void setDirection(final Direction the_direction)
  {
    /*
     * Sets the direction to a certain direction.
     */
    privateSetDirection(the_direction);
  }

  @Override
  public void setX(final int the_x)
  {
    /*
     * Set the x coordinate to a certain place.
     */
    privateSetX(the_x);
  }

  @Override
  public void setY(final int the_y)
  {
    /*
     * Set the y coordinate to a certain place.
     */
    privateSetY(the_y);
  }

  /**
   * What is the initial terrain? Only child class can get the answer.
   * 
   * @return The initial terrain.
   */
  protected Terrain getInitialTerrain()
  {
    return my_initial_terrain;
  }

  /**
   * Is this vehicle just been revived?
   * 
   * @return Whether the vehicle had been revived or not.
   */
  private boolean isJustRevived()
  {
    /*
     * Death counter of 0 means its just been revived.
     */
    final boolean is_revived = my_death_count == 0;
    /*
     * Checks for revive status.
     */
    if (is_revived)
    {
      /*
       * Vehicle had been revived! Vehicle is free to do anything it pleases.
       */
      my_death_count--;
    }
    return is_revived;
  }

  /**
   * Gets the index of the highest priority in an array of priorities.
   * 
   * @param the_neighbors Terrains neighboring the vehicle.
   * @param the_priorities The array of priorities.
   * @return The index of highest priority in the array.
   */
  private int getIndex(final Map<Direction, Terrain> the_neighbors,
                       final Priority[] the_priorities)
  {
    /*
     * Initial index out of bounds.
     */
    int index = -1;
    /*
     * Loop through the array.
     */
    for (int i = 0; i < the_priorities.length; i++)
    {
      /*
       * Creates a potentially matching priority based on data of the neighbors
       * and the direction from the priority.
       */
      final Priority priority =
          new Priority(the_priorities[i].getDirection(), the_neighbors.get(the_priorities[i].
                                                                           getDirection()));
      /*
       * Checks for matching terrain.
       */
      if (priority.getTerrain() == the_priorities[i].getTerrain())
      {
        /*
         * Terrain matches! Record the index and break off the loop.
         */
        index = i;
        break;
      }

    }
    return index;
  }

  /**
   * Checks if there exists a path where the vehicle can go through based on the
   * array of priorities.
   * 
   * @param the_neighbors Neighboring terrains.
   * @param the_priorities Array of priorities.
   * @return Whether the path exists.
   */
  protected boolean pathExist(final Map<Direction, Terrain> the_neighbors,
                              final Priority[] the_priorities)
  {
    /*
     * Vehicles must be alive, must not be just revived, and there exists an
     * index of the highest priority in the array that is not out of bounds.
     */
    return isAlive() && !isJustRevived() && getIndex(the_neighbors, the_priorities) >= 0 &&
           getIndex(the_neighbors, the_priorities) < the_priorities.length;
  }

  /**
   * Private version of reset(), so that both constructors and public methods
   * can use without redundancies.
   */
  private void privateReset()
  {
    /*
     * Set the x, y, and direction to their initial positions.
     */
    privateSetX(my_initial_x);
    privateSetY(my_initial_y);
    privateSetDirection(my_initial_direction);
    /*
     * Death count set to -1 to signify that the vehicle is alive and is not
     * just revived.
     */
    my_death_count = -1;
  }

  /**
   * Private version of setX(), so that both constructors and public methods can
   * use without redundancies.
   * 
   * @param the_x Intended x position.
   */
  private void privateSetX(final int the_x)
  {
    /*
     * Set current x to intended x.
     */
    my_current_x = the_x;
  }

  /**
   * Private version of setY(), so that both constructors and public methods can
   * use without redundancies.
   * 
   * @param the_y Intended y position.
   */
  private void privateSetY(final int the_y)
  {
    /*
     * Set current y to intended y.
     */
    my_current_y = the_y;
  }

  /**
   * Private version of setDirection(), so that both constructors and public
   * methods can use without redundancies.
   * 
   * @param the_direction Intended direction.
   */
  private void privateSetDirection(final Direction the_direction)
  {
    /*
     * Set current direction to intended direction.
     */
    my_current_direction = the_direction;
  }
}
