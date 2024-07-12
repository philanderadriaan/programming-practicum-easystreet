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
 * This class represents a human in easystreet.
 * 
 * @author 
 * @version 65
 */
public class Human extends Vehicle
{
  /**
   * The amount of time the human stay dead after being ran over.
   */
  private static final int DEATH_TIME = 50;

  /**
   * Constructs a human and put it in easystreet.
   * 
   * @param the_x Starting x position.
   * @param the_y Starting y position.
   * @param the_direction Starting direction.
   * @param the_terrain Starting terrain.
   */
  public Human(final int the_x, final int the_y, final Direction the_direction,
               final Terrain the_terrain)
  {
    /*
     * Humans does not care about traffic light.
     */
    super(the_x, the_y, the_direction, the_terrain, DEATH_TIME, Light.RED);
  }

  @Override
  public boolean canPass(final Terrain the_terrain, final Light the_light)
  {
    boolean can_pass;
    /*
     * Check if humans are on the street or light.
     */
    if (getInitialTerrain() == Terrain.STREET || getInitialTerrain() == Terrain.LIGHT)
    {
      /*
       * Humans on the street or light will have the same characteristics as other vehicles.
       */
      can_pass = super.canPass(the_terrain, the_light);
    }
    else
    {
      /*
       * Humans on any other terrain will only walk on the same terrain.
       */
      can_pass = the_terrain == getInitialTerrain();
    }
    /*
     * Determines if humans can pass through the terrain or not.
     */
    return can_pass;
  }

  @Override
  public Direction chooseDirection(final Map<Direction, Terrain> the_neighbors,
                                   final Light the_light)
  {
    /*
     * Humans will look at all direction and choose their direction randomly.
     */
    final Priority[] priorities = {
      new Priority(Direction.NORTH, getInitialTerrain()),
      new Priority(Direction.SOUTH, getInitialTerrain()),
      new Priority(Direction.EAST, getInitialTerrain()),
      new Priority(Direction.WEST, getInitialTerrain())};
    /*
     * Choose the available paths randomly.
     */
    return chooseDirection(the_neighbors, priorities, PriorityChoice.RANDOM);
  }
}
