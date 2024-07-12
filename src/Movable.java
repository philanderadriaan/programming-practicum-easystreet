/*
 * TCSS 305 - Autumn 2011
 * Homework 2: Easy Street 
 * Author: Daniel M. Zimmerman (Formatting and Comments) and Marty Stepp
 */

import java.util.Map;

/**
 * An interface for objects that can move in four directions.
 * 
 * @author Daniel M. Zimmerman
 * @author Marty Stepp
 * @version Autumn 2011
 */

public interface Movable
{
  /**
   * Returns whether or not this object may move onto the given type of
   * terrain, when the street lights are the given color.
   * 
   * @param the_terrain The terrain.
   * @param the_light The light color.
   * @return whether or not this object may move onto the given type of terrain
   * when the street lights are the given color. 
   */
  boolean canPass(Terrain the_terrain, Light the_light);

  /**
   * Returns the direction this object would like to move, based on the
   * given map of the terrain of its neighbors and the given status of the
   * street lights.
   * 
   * @param the_neighbors The map of neighboring terrain.
   * @param the_light The light status.
   * @return the direction this object would like to move.
   */
  Direction chooseDirection(Map<Direction, Terrain> the_neighbors, 
                            Light the_light);

  /**
   * Called when this Movable object collides with the given other movable
   * object.
   * 
   * @param the_other The other object.
   */
  void collide(Movable the_other);

  /**
   * Returns the number of updates between this vehicle's death and when it
   * should be revived.
   * 
   * @return the number of updates.
   */
  int getDeathTime();

  /**
   * Returns the file name of the image for this Movable object, such as
   * "car.gif".
   * 
   * @return the file name.
   */
  String getImageFileName();

  /** 
   * Returns this Movable object's direction. 
   * 
   * @return the direction.
   */
  Direction getDirection();

  /** 
   * Returns this Movable object's x-coordinate. 
   * 
   * @return the x-coordinate.
   */
  int x();

  /**
   * Returns this Movable object's y-coordinate. 
   * 
   * @return the y-coordinate.
   */
  int y();

  /**
   * Returns whether this Movable object is alive and should move on the
   * map.
   * 
   * @return true if the object is alive, false otherwise.
   */
  boolean isAlive();

  /**
   * Called by the UI to notify a dead vehicle that 1 movement round has
   * passed, so that it will become 1 move closer to revival.
   */
  void poke();

  /** 
   * Moves this vehicle back to its original position. 
   */
  void reset();

  /** 
   * Sets this object's facing direction to the given value. 
   * 
   * @param the_dir The new direction.
   */
  void setDirection(Direction the_dir);

  /** 
   * Sets this object's x-coordinate to the given value. 
   *
   * @param the_x The new x-coordinate.
   */
  void setX(int the_x);

  /** 
   * Sets this object's y-coordinate to the given value.
   * 
   * @param the_y The new y-coordinate.
   */
  void setY(int the_y);
}

// end of interface Movable
