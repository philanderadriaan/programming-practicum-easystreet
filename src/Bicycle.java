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
 * This class represents the bicycle in easystreet.
 * 
 * @author 
 * @version 65
 */
public class Bicycle extends Vehicle
{
  /**
   * The time bicycles stay dead after getting ran over.
   */
  private static final int DEATH_TIME = 20;

  /**
   * Constructs a bicycle and put it in easystreet.
   * 
   * @param the_x Starting x position.
   * @param the_y Starting y position.
   * @param the_direction Starting direction.
   * @param the_terrain Starting terrain.
   */
  public Bicycle(final int the_x, final int the_y, final Direction the_direction,
                 final Terrain the_terrain)
  {
    /*
     * Bicycles stops on yellow/red lights, and ignore green lights.
     */
    super(the_x, the_y, the_direction, the_terrain, DEATH_TIME, Light.GREEN);
  }

  @Override
  public boolean canPass(final Terrain the_terrain, final Light the_light)
  {
    /*
     * Able to travel through the streets like other vehicles as well as being
     * able to travel through trails.
     */
    return super.canPass(the_terrain, the_light) || the_terrain == Terrain.TRAIL;
  }

  @Override
  public Direction chooseDirection(final Map<Direction, Terrain> the_neighbors,
                                   final Light the_light)
  {
    /*
     * The bicycle will first check the front, left, and right for trails. If
     * there is not any trails at those directions, it will check for streets on
     * front, left, and right. If there is none of them either, then they will
     * reverse if there is trail or streets behind.
     */
    final Priority[] priorities = {
      new Priority(getDirection(), Terrain.TRAIL),
      new Priority(getDirection().left(), Terrain.TRAIL),
      new Priority(getDirection().right(), Terrain.TRAIL),
      new Priority(getDirection(), Terrain.STREET),
      new Priority(getDirection().left(), Terrain.STREET),
      new Priority(getDirection().right(), Terrain.STREET),
      new Priority(getDirection().reverse(), Terrain.TRAIL),
      new Priority(getDirection().reverse(), Terrain.STREET)};
    /*
     * Bicycles choose their directions based on the available terrains in the
     * highest priority. The priority is determined by its order in the array.
     */
    return chooseDirection(the_neighbors, priorities, PriorityChoice.HIGHEST);
  }
}
