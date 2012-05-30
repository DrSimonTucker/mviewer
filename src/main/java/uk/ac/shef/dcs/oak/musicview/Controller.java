package uk.ac.shef.dcs.oak.musicview;

import java.util.Collection;

import javax.swing.JFrame;

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
      ModelFrame framer = new ModelFrame(this);

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
