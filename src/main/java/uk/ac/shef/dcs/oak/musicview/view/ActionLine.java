package uk.ac.shef.dcs.oak.musicview.view;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import uk.ac.shef.dcs.oak.musicview.Controller;
import uk.ac.shef.dcs.oak.musicview.Event;
import uk.ac.shef.dcs.oak.musicview.Model;
import uk.ac.shef.dcs.oak.musicview.ModelListener;

/**
 * The action line shows the score as a series of actions
 * 
 * @author sat
 * 
 */
public class ActionLine extends JPanel implements ModelListener
{
   /** Flag for painting the bar lines */
   private static final boolean BAR_LINES = true;

   private static final int GIVEN_CIRCLE_SIZE = 8;

   /** The left margin in pixels */
   public static final int LEFT_MARGIN = 20;

   private static final int MARGIN = 10;

   /** Size of the circle */
   private static final int MAX_CIRCLE_SIZE = 40;

   /** Size of the circle */
   private static final int MIN_CIRCLE_SIZE = 10;

   /** How much to offset the predicted and actual hit marks */
   private static final int OFFSET = 0;

   /** The voice we're considering */
   private final double chosenVoice;

   double id = Math.random();

   /** The list of events that should be plotted */
   private Model model;

   /**
    * Constructor
    * 
    * @param cont
    *           The main controller
    * @param voice
    *           The voice that this line will display
    */
   public ActionLine(final Controller cont, final double voice)
   {
      cont.addListener(this);
      chosenVoice = voice;
   }

   /**
    * Determines if the given event is valid or not
    * 
    * @param ev
    *           The event to test
    * @return boolean whether the event is valid
    */
   protected boolean isValidEvent(final Event ev)
   {
      return ev.getPitch() == chosenVoice;
   }

   @Override
   public final void newModelLoaded(final Model mod)
   {
      // System.out.println("Action loaded new model: " + this.getSize());
      model = mod;
      if (this.getSize().getHeight() > 0)
         SwingUtilities.invokeLater(new Runnable()
         {
            @Override
            public void run()
            {
               repaint();
            }
         });
   }

   @Override
   public final void paint(final Graphics g)
   {
      // System.out.println("repainting " + this.getSize());

      super.paint(g);

      if (model != null)
      {
         // Do some housekeeping
         double pixelPerSecond = (this.getWidth() - LEFT_MARGIN * 2) / model.getTotalLength();

         // Plot the events
         for (Event ev : model.getEvents())
            if (isValidEvent(ev))
            {
               double percVelocity = model.getVelocityPerc(ev);
               System.out.println("PERC = " + percVelocity);
               int velCircleSize = GIVEN_CIRCLE_SIZE;
               int circleSize = (int) (percVelocity * (MAX_CIRCLE_SIZE - MIN_CIRCLE_SIZE))
                     + MIN_CIRCLE_SIZE;
               int targetCircleSize = (int) (model.getTargetVelocityPerc(ev) * (MAX_CIRCLE_SIZE - MIN_CIRCLE_SIZE))
                     + MIN_CIRCLE_SIZE;

               // Draw the Desired onset in black
               // Draw the Desired onset in black
               g.setColor(Color.darkGray);
               int actPixCent = LEFT_MARGIN
                     + (int) ((model.getMetronomicScoreTime(ev) - model.getOffset()) * pixelPerSecond);
               int actYPixCent = this.getHeight() / 2 + OFFSET;
               g.drawOval(actPixCent - velCircleSize / 2, actYPixCent - targetCircleSize / 2,
                     velCircleSize, targetCircleSize);

               // Draw a circle at the relevant point and the relevant pitch
               g.setColor(Color.RED);
               int xPixCent = LEFT_MARGIN
                     + (int) (((model.getMetronomicScoreTime(ev) + (model.getMetroNormPerfTime(ev) - model
                           .getMetroNormTargTime(ev))) - model.getOffset()) * pixelPerSecond);
               int yPixCent = this.getHeight() / 2;
               g.drawOval(xPixCent - velCircleSize / 2, yPixCent - circleSize / 2, velCircleSize,
                     circleSize);

            }

         // Draw the bar lines if needed
         if (BAR_LINES)
         {
            g.setColor(Color.lightGray);
            for (double barTime : model.getMetronomicBarTimes())
            {
               int pixPos = LEFT_MARGIN + (int) ((barTime - model.getOffset()) * pixelPerSecond);
               g.drawLine(pixPos, 0, pixPos, this.getHeight());
            }
         }
      }
   }
}
