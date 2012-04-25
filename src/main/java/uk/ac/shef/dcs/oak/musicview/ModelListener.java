package uk.ac.shef.dcs.oak.musicview;

/**
 * Interface for listening for changes to the model
 * 
 * @author sat
 * 
 */
public interface ModelListener
{
   /**
    * Alerts listener that a new model has been loaded
    * 
    * @param mod
    *           the new model
    */
   void newModelLoaded(Model mod);
}
