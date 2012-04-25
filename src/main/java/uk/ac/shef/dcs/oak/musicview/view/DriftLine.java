package uk.ac.shef.dcs.oak.musicview.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

import uk.ac.shef.dcs.oak.musicview.Event;
import uk.ac.shef.dcs.oak.musicview.Model;

/**
 * The drift line shows the overall drift from the norm
 * 
 * @author sat
 * 
 */
public class DriftLine extends JPanel
{
   /** The voice displayed by this line */
   private double chosenVoice = -1;

   /** Basis model */
   private final Model model;

   /**
    * Constructor
    * 
    * @param mod
    *           The model to construct the line from
    * @param voice
    *           The voice to display
    */
   public DriftLine(final Model mod, final double voice)
   {
      model = mod;
      chosenVoice = voice;
   }

   @Override
   public final void paint(final Graphics g)
   {
      super.paint(g);
      double pixelsPerSecond = this.getWidth() / model.getTotalLength();

      int currY = this.getHeight() / 2;
      int currX = 0;

      Map<Double, Double> drifts = new TreeMap<Double, Double>();
      double maxDrift = 0;
      for (Event ev : model.getEvents())
         if (ev.getPitch() == chosenVoice)
         {
            double drift = ev.getOnset() - model.getScoreTime(ev);
            drifts.put(model.getScoreTime(ev), drift);
            maxDrift = Math.max(maxDrift, Math.abs(drift));
         }

      // Paint the line
      for (Entry<Double, Double> entry : drifts.entrySet())
      {
         int pixY = this.getHeight() / 2
               - (int) ((entry.getValue() / maxDrift) * this.getHeight() / 2);
         int pixX = (int) (pixelsPerSecond * entry.getKey());
         g.drawLine(currX, currY, pixX, pixY);
         currX = pixX;
         currY = pixY;
      }

      // Draw the baseline
      g.setColor(Color.lightGray);
      g.drawLine(0, this.getHeight() / 2, getWidth(), getHeight() / 2);
   }

   /**
    * Tester function
    * 
    * @param args
    *           cli
    * @throws Exception
    *            somethings gone wrong
    */
   public static void main(final String[] args) throws Exception
   {
      Model mod = Model.generateModel(new File("/Users/sat/data/renee/8LB.txt"), 1, 1);
      DriftLine line44 = new DriftLine(mod, 44);
      DriftLine line40 = new DriftLine(mod, 40);
      DriftLine line36 = new DriftLine(mod, 36);
      JFrame framer = new JFrame();
      framer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      framer.setSize(500, 500);
      framer.setLocationRelativeTo(null);
      framer.setLayout(new GridLayout(3, 1));
      framer.add(line44);
      framer.add(line40);
      framer.add(line36);
      framer.setVisible(true);
   }
}
