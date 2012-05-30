package uk.ac.shef.dcs.oak.musicview.view;

import uk.ac.shef.dcs.oak.musicview.Controller;
import uk.ac.shef.dcs.oak.musicview.Event;

/**
 * Summary drift line shows the drift for the whole thing
 * 
 * @author sat
 * 
 */
public class SummaryDriftLine extends DriftLine
{
   /**
    * Constructor
    * 
    * @param cont
    *           The controller used to manage the interface
    */
   public SummaryDriftLine(final Controller cont)
   {
      super(cont, -1);
   }

   @Override
   protected final boolean isValidEvent(final Event ev)
   {
      return true;
   }

}
