package uk.ac.shef.dcs.oak.musicview.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.ac.shef.dcs.oak.musicview.Controller;
import uk.ac.shef.dcs.oak.musicview.Model;
import uk.ac.shef.dcs.oak.musicview.ModelListener;

import com.jidesoft.swing.RangeSlider;

/**
 * The selection panel controls the loading and unloading of the model (and the
 * zooming)
 * 
 * @author sat
 * 
 */
public class SelectionPanel extends JPanel implements ModelListener
{
   /** Flag to prevent overlap when loading new models */
   private boolean loading = false;

   /** The controller that the selection panel links to */
   private final Controller mainController;

   /** The file that has been selected by the user */
   private File selectedFile;

   RangeSlider slider;

   /** The combo box model for subjects */
   private final DefaultComboBoxModel subjectBoxModel = new DefaultComboBoxModel();

   /** The combo box model for trials */
   private final DefaultComboBoxModel trialBoxModel = new DefaultComboBoxModel();

   /**
    * Constructor
    * 
    * @param cont
    *           The Controller to be used
    */
   public SelectionPanel(final Controller cont)
   {
      mainController = cont;
      cont.addListener(this);
      initGUI();
   }

   /**
    * Prepares the display
    */
   private void initGUI()
   {
      JButton loadButton = new JButton("Load");
      add(loadButton);
      loadButton.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(final ActionEvent e)
         {
            loadNewModel(true);
         }
      });

      JLabel subjectLabel = new JLabel("Subject: ");
      add(subjectLabel);
      JComboBox subjectCombo = new JComboBox(subjectBoxModel);
      add(subjectCombo);
      subjectCombo.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(final ActionEvent e)
         {
            loadNewModel(false);
         }
      });

      JLabel trialLabel = new JLabel("Trial: ");
      add(trialLabel);
      JComboBox trialCombo = new JComboBox(trialBoxModel);
      add(trialCombo);
      trialCombo.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(final ActionEvent e)
         {
            loadNewModel(false);
         }
      });

      // Add in the range slider
      JLabel zoomLabel = new JLabel("Zoom: ");
      add(zoomLabel);
      slider = new RangeSlider(1, 15);
      slider.setMajorTickSpacing(1);
      slider.setPaintTicks(true);
      slider.setLowValue(1);
      slider.setHighValue(15);
      add(slider);
      slider.addChangeListener(new ChangeListener()
      {
         @Override
         public void stateChanged(final ChangeEvent arg0)
         {
            loadNewModel(false);
         }
      });
   }

   /**
    * Loads a new model for use
    * 
    * @param chooseFile
    *           flag to show whether a new file is needed
    */
   private void loadNewModel(final boolean chooseFile)
   {
      if (!loading)
      {
         if (chooseFile)
         {
            JFileChooser chooser = new JFileChooser();
            chooser.showOpenDialog(this);
            selectedFile = chooser.getSelectedFile();
         }

         try
         {
            Object subj = subjectBoxModel.getSelectedItem();
            Integer subjNumber = (Integer) subj;
            Object trial = trialBoxModel.getSelectedItem();
            Integer trialNumber = (Integer) trial;
            if (trialNumber == null)
               trialNumber = 1;
            if (subjNumber == null)
               subjNumber = 1;
            mainController.setModel(Model.generateModel(selectedFile, subjNumber, trialNumber,
                  slider.getLowValue(), slider.getHighValue() + 1));
         }
         catch (IOException e)
         {
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
         }
      }
   }

   @Override
   public final void newModelLoaded(final Model mod)
   {
      loading = true;
      subjectBoxModel.removeAllElements();
      for (Integer subject : mod.getAllSubjects())
         subjectBoxModel.addElement(subject);
      subjectBoxModel.setSelectedItem(mod.getSelectedSubject());

      trialBoxModel.removeAllElements();
      for (Integer trial : mod.getAllTrials())
         trialBoxModel.addElement(trial);
      trialBoxModel.setSelectedItem(mod.getSelectedTrial());
      loading = false;
   }
}