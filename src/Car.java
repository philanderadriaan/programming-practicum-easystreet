/*
 * 
 * 
 * TCSS 305 - Autumn 2011
 * 
 * Assignment 2 - Easy Street
 * 
 * 17 October 2011,
 */
import java.util.Map;

/**
 * This class represents a car in easytreet.
 * 
 * @author 
 * @version 65
 */
public class Car extends Vehicle
{
  /**
   * The time the car will stay dead after being ran over.
   */
  private static final int DEATH_TIME = 10;

  /**
   * Constructs a car and put it in easystreet.
   * 
   * @param the_x Starting x position.
   * @param the_y Starting y position.
   * @param the_direction Starting direction.
   * @param the_terrain Starting terrain.
   */
  public Car(final int the_x, final int the_y, final Direction the_direction,
             final Terrain the_terrain)
  {
    /*
     * Cars stop at red light, ignores yellow and green.
     */
    super(the_x, the_y, the_direction, the_terrain, DEATH_TIME, Light.YELLOW);
  }

  @Override
  public Direction chooseDirection(final Map<Direction, Terrain> the_neighbors,
                                   final Light the_light)
  {
    /*
     * Cars first check their front for street, then check the left for street,
     * then check the right for street, then reverse as a last resort if there
     * is street behind them.
     */
    final Priority[] priorities = {
      new Priority(getDirection(), Terrain.STREET),
      new Priority(getDirection().left(), Terrain.STREET),
      new Priority(getDirection().right(), Terrain.STREET),
      new Priority(getDirection().reverse(), Terrain.STREET)};
    /*
     * Choose the path by its highest priority. The priority is determined by
     * the order in the array.
     */
    return chooseDirection(the_neighbors, priorities, PriorityChoice.HIGHEST);
  }
}
