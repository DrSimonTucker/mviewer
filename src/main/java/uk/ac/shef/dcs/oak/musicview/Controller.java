package uk.ac.shef.dcs.oak.musicview;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import uk.ac.shef.dcs.oak.musicview.view.ActionLine;
import uk.ac.shef.dcs.oak.musicview.view.SelectionPanel;

/**
 * Controls the whole shebang
 * 
 * @author sat
 * 
 */
public class Controller
{
   /** Model listeners */
   private final Collection<ModelListener> listeners = new LinkedList<ModelListener>();

   /** The model used in the displays */
   private Model mod;

   /**
    * Adds a listener for models
    * 
    * @param listener
    *           The listener to add
    */
   public final void addListener(final ModelListener listener)
   {
      listeners.add(listener);
   }

   /**
    * Runs the controller
    */
   public final void run()
   {
      JFrame framer = new JFrame();
      SelectionPanel panel = new SelectionPanel(this);

      JPanel infoPanel = new JPanel(new GridLayout(3, 1));
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
      mod = model;
      updateNewModel();
   }

   /**
    * Updates the listeners that we have a new model
    */
   private void updateNewModel()
   {
      for (ModelListener listener : listeners)
         listener.newModelLoaded(mod);
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
