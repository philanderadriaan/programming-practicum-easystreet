/*
 * TCSS 305 - Autumn 2011
 * Homework 2: Easy Street 
 * Author: Daniel M. Zimmerman (Formatting and Comments) and Marty Stepp
 */

/**
 * An enumeration (and associated functionality) for types of terrain that
 * may appear on the map.
 * 
 * @author Daniel M. Zimmerman
 * @author Marty Stepp
 * @version Autumn 2011
 */

public enum Terrain
{
  /**
   * Grass.
   */
  GRASS('-'), 
  
  /**
   * Street.
   */
  STREET('|'), 
  
  /**
   * Light.
   */
  LIGHT('+'), 
  
  /**
   * Wall.
   */
  WALL('X'), 
  
  /**
   * Trail.
   */
  TRAIL('.');

  /**
   * The character corresponding to a particular value of the enumeration.
   */
  private char my_letter;

  // Constructor
  
  /**
   * Constructs a new Terrain with the specified letter.
   * 
   * @param the_letter The letter.
   */
  private Terrain(final char the_letter)
  {
    my_letter = the_letter;
  }

  // Instance Methods
  
  /**
   * Returns the Terrain represented by the given letter.
   * 
   * @param the_letter The letter. 
   * @return the Terrain represented by the given letter,
   * or GRASS if no Terrain is represented by the given letter.
   */
  public static Terrain valueOf(final char the_letter)
  {
    Terrain result = GRASS;
    
    for (Terrain terrain : Terrain.values())
    {
      if (terrain.my_letter == the_letter)
      {
        result = terrain;
        break;
      }
    }
    
    return result;
  }

  /** 
   * Returns a String representation of this Terrain, such as "WALL (X)".
   * 
   * @return a String representation of this Terrain.
   */
  public String toString()
  {
    return super.toString() + " (" + my_letter + ")";
  }
}

// end of class Terrain
