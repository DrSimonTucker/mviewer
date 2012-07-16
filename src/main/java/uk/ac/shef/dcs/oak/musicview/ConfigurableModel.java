package uk.ac.shef.dcs.oak.musicview;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Model which can be configured
 * 
 * @author sat
 * 
 */
public class ConfigurableModel
{
   List<Event> notes;
   List<ConfigurableModelListener> listeners = new LinkedList<ConfigurableModelListener>();

   public void addListener(ConfigurableModelListener listener)
   {
      listeners.add(listener);
   }

   public List<ConfigurableModelListener> getListeners()
   {
      return listeners;
   }

   int barStart, barEnd;

   enum BarConfig
   {
      METRONOMIC, PLAYER_TIME, TARGET_TIME
   };

   private BarConfig bConfig = BarConfig.METRONOMIC;

   private List<Double> barTimes;

   public void setBarConfig(BarConfig config)
   {
      bConfig = config;

      barTimes.clear();
   }

   public double getTotalLength()
   {
      List<Double> barTimes = getBarTimes();
      return barTimes.get(barTimes.size() - 1) - barTimes.get(0);
   }

   public double getOffset()
   {
      return getBarTimes().get(0);
   }

   public List<Double> getBarTimes()
   {
      if (barTimes.size() == 0)
         if (bConfig == BarConfig.METRONOMIC)
         {
            // Compute the metronomic bar time and populate the list
            double firstBar = notes.get(0).getBar();
            double firstBarTime = notes.get(0).getOnset();
            double lastBar = notes.get(notes.size() - 1).getBar();
            double lastBarTime = notes.get(notes.size() - 1).getBar();

            double barLength = (lastBarTime - firstBarTime) / (lastBar - firstBar);
            for (int i = barStart; i <= barEnd; i++)
               barTimes.add(i * barLength);
         }
         else if (bConfig == BarConfig.PLAYER_TIME)
            for (int i = barStart; i <= barEnd; i++)
            {
               Map<Double, List<Double>> timeMap = new TreeMap<Double, List<Double>>();
               double firstBar = Double.MAX_VALUE;
               double lastBar = 0;
               for (Event note : notes)
                  if (note.getBar() >= i && note.getBar() <= (i + 1))
                  {
                     if (!timeMap.containsKey(note.getBar()))
                        timeMap.put(note.getBar(), new LinkedList<Double>());
                     timeMap.get(note.getBar()).add(note.getOnset());

                     firstBar = Math.min(firstBar, note.getBar());
                     lastBar = Math.max(lastBar, note.getBar());
                  }

               if (Math.abs(firstBar - Math.round(firstBar)) < 0.01)
                  barTimes.add(computeMean(timeMap.get(firstBar)));
               else
               {
                  double diff = computeMean(timeMap.get(lastBar))
                        - computeMean(timeMap.get(firstBar));
                  barTimes.add(diff / (lastBar - firstBar) * (lastBar - Math.floor(firstBar)));
               }

            }
         else if (bConfig == BarConfig.TARGET_TIME)
            for (int i = barStart; i <= barEnd; i++)
            {
               Map<Double, List<Double>> timeMap = new TreeMap<Double, List<Double>>();
               double firstBar = Double.MAX_VALUE;
               double lastBar = 0;
               for (Event note : notes)
                  if (note.getBar() >= i && note.getBar() <= (i + 1))
                  {
                     if (!timeMap.containsKey(note.getBar()))
                        timeMap.put(note.getBar(), new LinkedList<Double>());
                     timeMap.get(note.getBar()).add(note.getTargetOnset());

                     firstBar = Math.min(firstBar, note.getBar());
                     lastBar = Math.max(lastBar, note.getBar());
                  }

               if (Math.abs(firstBar - Math.round(firstBar)) < 0.01)
                  barTimes.add(computeMean(timeMap.get(firstBar)));
               else
               {
                  double diff = computeMean(timeMap.get(lastBar))
                        - computeMean(timeMap.get(firstBar));
                  barTimes.add(diff / (lastBar - firstBar) * (lastBar - Math.floor(firstBar)));
               }

            }

      return barTimes;
   }

   private double computeMean(List<Double> values)
   {
      double sum = 0;
      for (Double val : values)
         sum += val;
      return sum / values.size();
   }
}
