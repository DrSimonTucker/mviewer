package uk.ac.shef.dcs.oak.musicview.view;

import uk.ac.shef.dcs.oak.musicview.Controller;
import uk.ac.shef.dcs.oak.musicview.Event;

/**
 * Summary Action Line displays all the events
 * 
 * @author sat
 * 
 */
public class SummaryActionLine extends ActionLine
{
   /**
    * Constructor
    * 
    * @param cont
    *           The controller used to manage the interface
    */
   public SummaryActionLine(final Controller cont)
   {
      super(cont, -1);
   }

   @Override
   protected final boolean isValidEvent(final Event ev)
   {
      return true;
   }

}
