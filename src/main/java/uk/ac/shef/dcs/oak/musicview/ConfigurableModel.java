package uk.ac.shef.dcs.oak.musicview;

import java.util.List;

/**
 * Model which can be configured
 * 
 * @author sat
 * 
 */
public class ConfigurableModel
{
   List<Event> notes;

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
            for(int i = barStart ; i <= barEnd; i++)
            {
               double startTime = 0, endTime = 0;
               double startBar = 0, endBar = 0;
               for(Event ev : notes)
                  if (ev.getBar() >= barStart && ev.getBar() <= barEnd)
                     if (ev.getBar() < startBar)
            }

      return barTimes;
   }
}
