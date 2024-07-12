/*
 * TCSS 305 - Autumn 2011
 * Homework 2: Easy Street 
 * Author: Daniel M. Zimmerman (Formatting and Comments) and Marty Stepp
 */

/**
 * Runs the Easy Street program.
 * 
 * @author Daniel M. Zimmerman
 * @author Marty Stepp
 * @version Autumn 2011
 */

public final class EasyStreetMain
{
  /** 
   * Private constructor to prevent construction of instances.
   */
  private EasyStreetMain()
  {
    // do nothing
  }
  
  /** 
   * Constructs the main GUI window frame. 
   * 
   * @param the_args Command line arguments (ignored).
   */
  public static void main(final String... the_args)
  {
    // initialize GUI frame
    final EasyStreetFrame gui = new EasyStreetFrame();
    gui.setVisible(true);
  }
}

// end of class EasyStreetMain
