package uk.ac.shef.dcs.oak.musicview;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import uk.ac.shef.dcs.oak.musicview.view.ActionLine;
import uk.ac.shef.dcs.oak.musicview.view.DriftLine;
import uk.ac.shef.dcs.oak.musicview.view.SelectionPanel;
import uk.ac.shef.dcs.oak.musicview.view.SummaryActionLine;
import uk.ac.shef.dcs.oak.musicview.view.SummaryDriftLine;

public class ModelFrame extends JFrame implements ModelListener
{
   Controller cont = null;

   JPanel mainPanel = new JPanel();
   Model mod = null;

   SelectionPanel pan;

   public ModelFrame(Controller contr)
   {
      pan = new SelectionPanel(contr);
      cont = contr;
      cont.addListener(this);

      initGUI();
   }

   public void initGUI()
   {
      add(mainPanel, BorderLayout.CENTER);
      add(pan, BorderLayout.SOUTH);
      pack();
   }

   @Override
   public void newModelLoaded(Model mod)
   {
      this.mod = mod;
      refreshGUI();
   }

   private void refreshGUI()
   {
      mainPanel.removeAll();

      GridBagLayout gbl = new GridBagLayout();
      mainPanel.setLayout(gbl);

      // Construct a simple voice display
      JLabel sumLineLabel = new JLabel("S");
      gbl.setConstraints(sumLineLabel, new GridBagConstraints(0, 0, 1, 1, 0, 0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      mainPanel.add(sumLineLabel);
      SummaryDriftLine sdl = new SummaryDriftLine(cont);
      gbl.setConstraints(sdl, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.CENTER,
            GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      mainPanel.add(sdl);

      JLabel sumActionLabel = new JLabel("S");
      gbl.setConstraints(sumActionLabel, new GridBagConstraints(0, 1, 1, 1, 0, 0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      mainPanel.add(sumActionLabel);
      SummaryActionLine sal = new SummaryActionLine(cont);
      gbl.setConstraints(sal, new GridBagConstraints(1, 1, 1, 1, 1, 1, GridBagConstraints.CENTER,
            GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      mainPanel.add(sal);

      int counter = 2;
      for (Double voice : mod.getVoices())
      {
         JLabel ovalLabel = new JLabel("" + voice);
         gbl.setConstraints(ovalLabel, new GridBagConstraints(0, counter, 1, 1, 0, 0,
               GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
         mainPanel.add(ovalLabel);
         ActionLine al = new ActionLine(cont, voice);
         gbl.setConstraints(al, new GridBagConstraints(1, counter, 1, 1, 1, 1,
               GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
         mainPanel.add(al);

         counter++;
      }

      if (mod.getNumberOfVoices() < 4)
         for (Double voice : mod.getVoices())
         {
            JLabel ovalLabel = new JLabel("" + voice);
            gbl.setConstraints(ovalLabel, new GridBagConstraints(0, counter, 1, 1, 0, 0,
                  GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
            mainPanel.add(ovalLabel);
            DriftLine al = new DriftLine(cont, voice);
            gbl.setConstraints(al, new GridBagConstraints(1, counter, 1, 1, 1, 1,
                  GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
            mainPanel.add(al);

            counter++;
         }

      this.setExtendedState(JFrame.MAXIMIZED_BOTH);
   }
}
