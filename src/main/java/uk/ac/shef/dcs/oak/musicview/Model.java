package uk.ac.shef.dcs.oak.musicview;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import au.com.bytecode.opencsv.CSVReader;

/**
 * The model represents a set of events and associated metadata
 * 
 * @author sat
 * 
 */
public class Model
{
   /** The length of a bar in seconds */
   private final double barLength = 1.461650;

   /** The bottom pitch */
   private double bottomPitch = 0.0;

   /** The collection of events that this model represents */
   private final List<Event> events = new LinkedList<Event>();

   /** The lower bound to zoom to */
   private double lowerBound = 1;

   /** The selected subject */
   private Integer selectedSubject = -1;

   /** The selected trial */
   private Integer selectedTrial = -1;

   /** The list of all the subjects */
   private final Set<Integer> subjects = new TreeSet<Integer>();

   /** The list of all the trials */
   private final Set<Integer> trials = new TreeSet<Integer>();

   /** The upper bound to zoom to */
   private double upperBound = 16;

   /**
    * Gets all the available subjects for this file
    * 
    * @return A {@link Collection} of Integers representing the subjects
    */
   public final Collection<Integer> getAllSubjects()
   {
      return subjects;
   }

   /**
    * Gets all the available trials for this file and trial
    * 
    * @return A {@link Collection} of Integers representing the trials
    */
   public final Collection<Integer> getAllTrials()
   {
      return trials;
   }

   /**
    * Gets the times in seconds of each bar
    * 
    * @return A Collection of times for each bar
    */
   public final Collection<Double> getBarTimes()
   {
      Collection<Double> barTimes = new LinkedList<Double>();
      for (int i = (int) lowerBound; i <= upperBound; i++)
         barTimes.add(i * barLength);
      return barTimes;
   }

   /**
    * Gets the set of events that the model represents
    * 
    * @return {@link Collection} of events
    */
   public final Collection<Event> getEvents()
   {
      return events;
   }

   /**
    * Gets the maximum velocity of all the notes
    * 
    * @param voice
    *           the voice to get the velocity for
    * @return The maximum velocity as a double
    */
   public final double getMaxVelocity(final double voice)
   {
      double maxVel = 0;
      for (Event ev : getEvents())
         if (ev.getPitch() == voice)
            maxVel = Math.max(ev.getVelocity(), maxVel);
      return maxVel;
   }

   /**
    * Gets the minimum velocity of all the notes
    * 
    * @param voice
    *           the voice to get the velocity for
    * @return The minimum velocity as a double
    */
   public final double getMinVelocity(final double voice)
   {
      double minVel = Double.MAX_VALUE;
      for (Event ev : getEvents())
         if (ev.getPitch() == voice)
            minVel = Math.min(ev.getVelocity(), minVel);
      return minVel;
   }

   /**
    * Gets the number of bars in the recording
    * 
    * @return THe number of bars (double but whole number)
    */
   public final double getNumberOfBars()
   {
      return upperBound - lowerBound;
   }

   /**
    * Gets the time offset for the given zoom level
    * 
    * @return The time offset for the given zoom level as a double
    */
   public final double getOffset()
   {
      return lowerBound * barLength;
   }

   /**
    * Gets the pitch as a percentage of the range
    * 
    * @param ev
    *           THe event to get the pitch perc for
    * @return The pitch as a percentage of the range ([0,1])
    */
   public final double getPitchPerc(final Event ev)
   {
      double pitchRange = getPitchRange();
      return (ev.getPitch() - bottomPitch) / pitchRange;
   }

   /**
    * Gets the pitch range of all the events
    * 
    * @return double pitch range (top - bottom pitch)
    */
   public final double getPitchRange()
   {
      double topPitch = events.get(0).getPitch();
      bottomPitch = events.get(0).getPitch();

      for (int i = 1; i < events.size(); i++)
      {
         topPitch = Math.max(topPitch, events.get(i).getPitch());
         bottomPitch = Math.min(bottomPitch, events.get(i).getPitch());
      }

      return topPitch - bottomPitch;
   }

   /**
    * Gets the score time of the event
    * 
    * @param ev
    *           The event to get time for
    * @return The time in seconds that this event should have occured
    */
   public final double getScoreTime(final Event ev)
   {
      return ev.getBar() * barLength;
   }

   /**
    * Gets the chosen subject
    * 
    * @return chosen subject
    */
   public final Integer getSelectedSubject()
   {
      return selectedSubject;
   }

   /**
    * Gets the chosen trial
    * 
    * @return chosen trial
    */
   public final Integer getSelectedTrial()
   {
      return selectedTrial;
   }

   /**
    * Computes the total length of the piece
    * 
    * @return double length of the piece in seconds - guaranteed to be higher
    *         that the number of notes
    */
   public final double getTotalLength()
   {
      return getNumberOfBars() * barLength;
   }

   /**
    * Gets the velocity as a normalised percentage
    * 
    * @param ev
    *           The event to get percentage for
    * @return The velocity as a double in the range [0,1]
    */
   public final double getVelocityPerc(final Event ev)
   {

      return (ev.getVelocity() - getMinVelocity(ev.getPitch()))
            / (getMaxVelocity(ev.getPitch()) - getMinVelocity(ev.getPitch()));
   }

   /**
    * Zooms in the model to a given range
    * 
    * @param lower
    *           THe lower range to zoom to
    * @param upper
    *           THe upper range to zoom to
    */
   public final void zoom(final double lower, final double upper)
   {
      lowerBound = lower;
      upperBound = upper;
   }

   /**
    * Generates a model given a file and other parameters
    * 
    * @param f
    *           File to read
    * @param subject
    *           THe subject to consider
    * @param trial
    *           The trial to consider
    * @param lower
    *           the Lower bound of zoom
    * @param upper
    *           the Upper bound of zoom
    * @return A valid model for this subject and trial
    * @throws IOException
    *            if something goes wrong reading the file
    */
   public static final Model generateModel(final File f, final int subject, final int trial,
         final double lower, final double upper) throws IOException
   {
      Model mod = new Model();
      mod.selectedSubject = subject;
      mod.selectedTrial = trial;
      mod.lowerBound = lower - 1;
      mod.upperBound = upper + 1;

      CSVReader reader = new CSVReader(new FileReader(f));
      String[] headers = reader.readNext();
      int onsetPos, scoreTime, velocity, voice, subj, tri;
      onsetPos = getHeader(headers, "Onset");
      scoreTime = getHeader(headers, "ScoreTime");
      velocity = getHeader(headers, "Velocity");
      voice = getHeader(headers, "Voice");
      subj = getHeader(headers, "Subject");
      tri = getHeader(headers, "Trial");

      // Two types of data
      if (subj == -1)
         for (String[] nextLine = reader.readNext(); nextLine != null; nextLine = reader.readNext())
         {
            double tScoreTime = Double.parseDouble(nextLine[scoreTime]);
            if (tScoreTime >= lower && tScoreTime <= upper)
            {
               Event ev = new Event(Double.parseDouble(nextLine[velocity]),
                     Double.parseDouble(nextLine[onsetPos]), Double.parseDouble(nextLine[voice]),
                     Double.parseDouble(nextLine[scoreTime]));
               mod.events.add(ev);
            }
         }
      else
         for (String[] nextLine = reader.readNext(); nextLine != null; nextLine = reader.readNext())
         {
            if (Integer.parseInt(nextLine[subj]) == subject)
            {
               if (Integer.parseInt(nextLine[tri]) == trial)
               {
                  double tScoreTime = Double.parseDouble(nextLine[scoreTime]);
                  if (tScoreTime >= lower && tScoreTime <= upper)
                  {
                     Event ev = new Event(Double.parseDouble(nextLine[velocity]),
                           Double.parseDouble(nextLine[onsetPos]),
                           Double.parseDouble(nextLine[voice]),
                           Double.parseDouble(nextLine[scoreTime]));
                     mod.events.add(ev);
                  }
               }

               mod.trials.add(Integer.parseInt(nextLine[tri]));
            }
            mod.subjects.add(Integer.parseInt(nextLine[subj]));

         }
      return mod;
   }

   /**
    * Helper function to get the index of a string in the array
    * 
    * @param list
    *           THe list of headers
    * @param key
    *           THe key to search for
    * @return The index of the key within the list
    */
   private static int getHeader(final String[] list, final String key)
   {
      for (int i = 0; i < list.length; i++)
         if (list[i].equals(key))
            return i;
      return -1;
   }
}
