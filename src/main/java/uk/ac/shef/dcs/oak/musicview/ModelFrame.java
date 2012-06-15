package uk.ac.shef.dcs.oak.musicview;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import uk.ac.shef.dcs.oak.musicview.view.BarDriftLine;
import uk.ac.shef.dcs.oak.musicview.view.MetronomicActionLine;
import uk.ac.shef.dcs.oak.musicview.view.SelectionPanel;

public class ModelFrame extends JFrame implements ModelListener
{
   List<JComponent> addedComps = new LinkedList<JComponent>();

   Controller cont = null;
   GridBagLayout gbl = new GridBagLayout();

   JPanel mainPanel = new JPanel();

   Model mod = null;

   SelectionPanel pan;

   public ModelFrame(Controller contr)
   {
      mainPanel.setLayout(gbl);
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

      SwingUtilities.invokeLater(new Runnable()
      {
         @Override
         public void run()
         {
            refreshGUI();
            validate();
         }
      });
   }

   private void refreshGUI()
   {
      // Remove all the components and then destroy them
      for (JComponent comp : addedComps)
         mainPanel.remove(comp);

      addedComps.clear();

      // Construct a simple voice display
      JLabel sumLineLabel = new JLabel("S");
      gbl.setConstraints(sumLineLabel, new GridBagConstraints(0, 0, 1, 1, 0, 0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      mainPanel.add(sumLineLabel);
      addedComps.add(sumLineLabel);

      BarDriftLine sdl = new BarDriftLine(cont);
      gbl.setConstraints(sdl, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.CENTER,
            GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      mainPanel.add(sdl);
      addedComps.add(sdl);

      JLabel sumActionLabel = new JLabel("S");
      gbl.setConstraints(sumActionLabel, new GridBagConstraints(0, 1, 1, 1, 0, 0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      mainPanel.add(sumActionLabel);
      addedComps.add(sumActionLabel);

      int counter = 2;
      // System.out.println("VOICES = " + mod.getVoices());
      // System.exit(1);
      for (Double voice : mod.getVoices())
      {
         JLabel ovalLabel = new JLabel("" + voice);
         gbl.setConstraints(ovalLabel, new GridBagConstraints(0, counter, 1, 1, 0, 0,
               GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
         mainPanel.add(ovalLabel);
         addedComps.add(ovalLabel);

         MetronomicActionLine al = new MetronomicActionLine(cont, voice);
         gbl.setConstraints(al, new GridBagConstraints(1, counter, 1, 1, 1, 1,
               GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
         mainPanel.add(al);
         addedComps.add(al);

         counter++;
      }

      /*
       * if (mod.getNumberOfVoices() < 4) for (Double voice : mod.getVoices()) {
       * JLabel ovalLabel = new JLabel("" + voice);
       * gbl.setConstraints(ovalLabel, new GridBagConstraints(0, counter, 1, 1,
       * 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5,
       * 5, 5, 5), 0, 0)); mainPanel.add(ovalLabel); addedComps.add(ovalLabel);
       * 
       * DriftLine al = new DriftLine(cont, voice); gbl.setConstraints(al, new
       * GridBagConstraints(1, counter, 1, 1, 1, 1, GridBagConstraints.CENTER,
       * GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
       * mainPanel.add(al); addedComps.add(ovalLabel);
       * 
       * counter++; }
       */

      this.setExtendedState(JFrame.MAXIMIZED_BOTH);
   }
}
