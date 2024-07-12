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
 * This class represents a truck in easystreet.
 * 
 * @author 
 * @version 65
 */
public class Truck extends Vehicle
{
  /**
   * Constructs a truck and put it in easystreet.
   * 
   * @param the_x Starting x position.
   * @param the_y Starting y position.
   * @param the_direction Starting direction.
   * @param the_terrain Starting terrain.
   */
  public Truck(final int the_x, final int the_y, final Direction the_direction,
               final Terrain the_terrain)
  {
    /*
     * A truck is invulnerable, so the death time is zero. Trucks will also run
     * red lights.
     */
    super(the_x, the_y, the_direction, the_terrain, 0, Light.RED);
  }

  @Override
  public Direction chooseDirection(final Map<Direction, Terrain> the_neighbors,
                                   final Light the_light)
  {
    Direction direction;
    /*
     * The truck randomly selects front, left, and right directions if there is
     * street in at least one of the directions.
     */
    final Priority[] random_priorities = {
      new Priority(getDirection(), Terrain.STREET),
      new Priority(getDirection().left(), Terrain.STREET),
      new Priority(getDirection().right(), Terrain.STREET)};
    /*
     * If there is no street at front, left, and right, the trucks will reverse
     * if there is street behind them.
     */
    final Priority[] last_resort_priorities = {
      new Priority(getDirection().reverse(), Terrain.STREET)};
    /*
     * Check the first array of terrains for street.
     */
    if (pathExist(the_neighbors, random_priorities))
    {
      /*
       * There exists at least one street in the array. Randomly select one of
       * them.
       */
      direction = chooseDirection(the_neighbors, random_priorities, PriorityChoice.RANDOM);
    }
    else
    {
      /*
       * There is no path available. Try the second array of paths.
       */
      direction =
          chooseDirection(the_neighbors, last_resort_priorities, PriorityChoice.HIGHEST);
    }
    /*
     * Determines the direction the truck will go.
     */
    return direction;
  }
}
