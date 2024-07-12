/*
 * TCSS 305 - Autumn 2011
 * Homework 2: Easy Street 
 * Author: Daniel M. Zimmerman (Formatting and Comments) and Marty Stepp
 */

/**
 * An enumeration of traffic light statuses.
 *
 * @author Daniel M. Zimmerman
 * @author Marty Stepp
 * @version Autumn 2011
 */

public enum Light 
{ 
  /**
   * Green means go.
   */
  GREEN, 
  
  /**
   * Yellow means caution.
   */
  YELLOW, 
  
  /**
   * Red means stop.
   */
  RED; 
  
  /**
   * Returns the next light in sequence after this one. The
   * sequence is GREEN, YELLOW, RED.
   * 
   * @return the next light in sequence. 
   */
  public Light advance()
  {
    Light result = GREEN;

    switch (this)
    {
      case GREEN:
        result = YELLOW;
        break;
        
      case YELLOW:
        result = RED;
        break;
        
      default: // RED, but we already initialized to GREEN
    }
    
    return result; 
  }
}
