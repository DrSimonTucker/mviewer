package uk.ac.shef.dcs.oak.musicview.view;

import java.awt.Color;
import java.awt.Graphics;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import uk.ac.shef.dcs.oak.musicview.Controller;
import uk.ac.shef.dcs.oak.musicview.Model;
import uk.ac.shef.dcs.oak.musicview.ModelListener;

/**
 * The action line shows the score as a series of actions
 * 
 * @author sat
 * 
 */
public class BarDriftLine extends JPanel implements ModelListener
{
   /** The left margin in pixels */
   public static final int LEFT_MARGIN = 20;

   private static final int MARGIN = 10;

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
   public BarDriftLine(final Controller cont)
   {
      cont.addListener(this);
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
         List<Double> barTimes = model.getBarTimes();
         List<Double> playBarTimes = model.getPlayBarTimes();
         System.out.println("HERE = " + playBarTimes);
         System.out.println("ALSO = " + barTimes);

         List<Double> barLengths = new LinkedList<Double>();
         double maxBarTime = 0;
         double minBarTime = Double.MAX_VALUE;
         for (int i = 0; i < barTimes.size() - 1; i++)
         {
            barLengths.add(barTimes.get(i + 1) - barTimes.get(i));
            maxBarTime = Math.max(maxBarTime, Math.abs(barTimes.get(i + 1) - barTimes.get(i)));
            maxBarTime = Math.max(maxBarTime,
                  Math.abs(playBarTimes.get(i + 1) - playBarTimes.get(i)));
            minBarTime = Math.min(minBarTime, Math.abs(barTimes.get(i + 1) - barTimes.get(i)));
            minBarTime = Math.min(minBarTime,
                  Math.abs(playBarTimes.get(i + 1) - playBarTimes.get(i)));
         }

         // Draw in the gray middle line
         g.setColor(Color.lightGray);
         g.drawLine(0, this.getHeight() / 2, getWidth(), getHeight() / 2);

         // Draw the bar lines if needed
         g.setColor(Color.black);
         for (int i = 0; i < barTimes.size() - 1; i++)
         {
            double perc = 1 - ((barTimes.get(i + 1) - barTimes.get(i)) - minBarTime)
                  / (maxBarTime - minBarTime);
            int height = (int) (perc * ((this.getHeight() - MARGIN)));

            int pixPosLeft = LEFT_MARGIN
                  + (int) ((barTimes.get(i) - model.getOffset()) * pixelPerSecond);
            int pixPosRight = LEFT_MARGIN
                  + (int) ((barTimes.get(i + 1) - model.getOffset()) * pixelPerSecond);
            g.drawLine(pixPosLeft, height, pixPosRight, height);
         }

         // Draw the bar lines if needed
         g.setColor(Color.red);
         for (int i = 0; i < playBarTimes.size() - 1; i++)
         {
            double perc = 1 - ((playBarTimes.get(i + 1) - playBarTimes.get(i)) - minBarTime)
                  / (maxBarTime - minBarTime);
            int height = (int) (perc * ((this.getHeight() - MARGIN)));
            int pixPosLeft = LEFT_MARGIN
                  + (int) ((barTimes.get(i) - model.getOffset()) * pixelPerSecond);
            int pixPosRight = LEFT_MARGIN
                  + (int) ((barTimes.get(i + 1) - model.getOffset()) * pixelPerSecond);
            g.drawLine(pixPosLeft, height, pixPosRight, height);
         }

         // Add the max drift in seconds to the top left of the display
         g.setColor(Color.red);
         NumberFormat nf = NumberFormat.getInstance();
         nf.setMaximumFractionDigits(3);
         g.drawString(nf.format((maxBarTime - minBarTime)) + "s", 0, 10);

      }
   }
}
