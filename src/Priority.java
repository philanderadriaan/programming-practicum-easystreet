/*
 * 
 * 
 * TCSS 305 - Autumn 2011
 * 
 * Assignment 2 - Easy Street
 * 
 * 17 October 2011,
 */
/**
 * Due to the inconsistent nature of how the vehicles choose directions, such
 * that decisions on direction does not take precedence over terrain data and
 * vice versa, it is near impossible to sort out the decision making process by
 * nested loop. If statements might yield redundant exit points, and eliminating
 * the exit points seems very complicated and attempts at doing it had become a
 * big pile of mess.
 * 
 * The purpose of this class is to give Direction and Terrain equal
 * opportunities to affect the decision making of chooseDirection(). The vehicle
 * will look at a certain direction and look for a desired terrain at its
 * destination. The decision will be based on the combinations of direction and
 * terrain, which will be placed as priorities, and it is up to the vehicles to
 * decide which combinations has a higher priority than others.
 * 
 * @author 
 * @version 65
 */
public class Priority
{
  /**
   * The intended direction.
   */
  private final Direction my_direction;
  /**
   * The desired terrain of the destination.
   */
  private final Terrain my_terrain;

  /**
   * Constructs a priority, pointing at a direction and looks for the desired
   * terrain.
   * 
   * @param the_direction The direction that is being observed.
   * @param the_terrain The desired terrain.
   */
  public Priority(final Direction the_direction, final Terrain the_terrain)
  {
    /*
     * Sets the intended direction.
     */
    my_direction = the_direction;
    /*
     * Checks if the terrain the vehicle is light.
     */
    if (the_terrain == Terrain.LIGHT)
    {
      /*
       * The terrain is light. The vehicle is "dumbed down" so that it is unable
       * to differentiate between light and street.
       */
      my_terrain = Terrain.STREET;
    }
    else
    {
      /*
       * Terrain is not light. Leave it as it is.
       */
      my_terrain = the_terrain;
    }
  }

  /**
   * What is the intended direction?
   * 
   * @return The intended direction.
   */
  public Direction getDirection()
  {
    return my_direction;
  }

  /**
   * What is the desired terrain?
   * 
   * @return The desired terrain..
   */
  public Terrain getTerrain()
  {
    return my_terrain;
  }

  @Override
  public boolean equals(final Object the_other)
  {
    /*
     * False till proven true.
     */
    boolean equals = false;
    /*
     * Checks if the classes are the same.
     */
    if (the_other != null && the_other.getClass() == getClass())
    {
      /*
       * Initializes the other object as the same class.
       */
      final Priority other_priority = (Priority) the_other;
      /*
       * True if the direction and terrains are the same.
       */
      equals =
          other_priority.getDirection() == my_direction &&
              other_priority.getTerrain() == my_terrain;
    }
    /*
     * Returns whether the two priorities are equal or not.
     */
    return equals;
  }

  @Override
  public int hashCode()
  {
    /*
     * Returns the hashCode() of the toString() method.
     */
    return toString().hashCode();
  }
}
