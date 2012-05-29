package uk.ac.shef.dcs.oak.musicview;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JPanel;

import uk.ac.shef.dcs.oak.musicview.view.ActionLine;
import uk.ac.shef.dcs.oak.musicview.view.DriftLine;
import uk.ac.shef.dcs.oak.musicview.view.SelectionPanel;
import uk.ac.shef.dcs.oak.musicview.view.SummaryActionLine;
import uk.ac.shef.dcs.oak.musicview.view.SummaryDriftLine;

/**
 * Controls the whole shebang
 * 
 * @author sat
 * 
 */
public class Controller
{

   /** The model used in the displays */
   private Model mod;

   /**
    * Constructor
    */
   public Controller()
   {
      // Set up a default model
      mod = new Model();
   }

   /**
    * Adds a listener for models
    * 
    * @param listener
    *           The listener to add
    */
   public final void addListener(final ModelListener listener)
   {
      mod.addListener(listener);
   }

   /**
    * Runs the controller
    */
   public final void run()
   {
      JFrame framer = new JFrame();
      SelectionPanel panel = new SelectionPanel(this);

      JPanel infoPanel = new JPanel(new GridLayout(8, 1));
      infoPanel.add(new SummaryActionLine(this));
      infoPanel.add(new SummaryDriftLine(this));

      infoPanel.add(new DriftLine(this, 44));
      infoPanel.add(new DriftLine(this, 40));
      infoPanel.add(new DriftLine(this, 36));

      infoPanel.add(new ActionLine(this, 44));
      infoPanel.add(new ActionLine(this, 40));
      infoPanel.add(new ActionLine(this, 36));

      framer.add(panel, BorderLayout.SOUTH);
      framer.add(infoPanel, BorderLayout.CENTER);
      framer.setSize(500, 500);
      framer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      framer.setLocationRelativeTo(null);

      framer.setVisible(true);
   }

   /**
    * Sets the model to be used
    * 
    * @param model
    *           The model to be used in the display
    */
   public final void setModel(final Model model)
   {
      Collection<ModelListener> listeners = mod.getListeners();
      mod = model;
      for (ModelListener listen : listeners)
         mod.addListener(listen);
      mod.forceUpdate();
   }

   /**
    * Entry point
    * 
    * @param args
    *           CLI
    */
   public static void main(final String[] args)
   {
      Controller cont = new Controller();
      cont.run();
   }
}
