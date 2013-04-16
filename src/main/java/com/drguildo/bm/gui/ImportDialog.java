package com.drguildo.bm.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.drguildo.bm.Bookmarks;
import com.drguildo.bm.importers.DeliciousImporter;
import com.drguildo.bm.importers.TextImporter;

public class ImportDialog extends JDialog {
  private static final long serialVersionUID = 6653636972309796591L;
  private static final String[] importers = { "Delicious", "Text" };

  public ImportDialog(JFrame owner, boolean modal, final BookmarksModel model,
      final Bookmarks bookmarks) {
    super(owner, true);

    final JComboBox<String> importComboBox = new JComboBox<>(importers);
    final JTextField pathField = new JTextField();
    final JFileChooser fileChooser = new JFileChooser();
    final JButton browseButton = new JButton("Browse");
    final JButton openButton = new JButton("Open");
    final JTextField tagsField = new JTextField();

    browseButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int returnVal = fileChooser.showOpenDialog(browseButton);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
          pathField.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
      }
    });

    openButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        File file = fileChooser.getSelectedFile();

        if (file == null) {
          return;
        }

        switch (importComboBox.getSelectedIndex()) {
        case 0:
          // Delicious
          DeliciousImporter di = new DeliciousImporter();
          try {
            bookmarks.merge(di.convert(file));
            model.fireTableDataChanged();
          } catch (IOException ex) {
            ex.printStackTrace();
          }
          break;
        case 1:
          // Text
          TextImporter ti = new TextImporter();
          try {
            bookmarks.merge(ti.convert(file, tagsField.getText()));
            model.fireTableDataChanged();
          } catch (IOException ex) {
            ex.printStackTrace();
          }
          break;
        }
      }
    });

    setTitle("Import");
    setLayout(new MigLayout());
    setLocationRelativeTo(owner);

    add(importComboBox, "w 100%, span, wrap");

    add(pathField, "w 200px, split, span");
    add(browseButton, "right, wrap");

    add(new JLabel("Tags"), "split, span");
    add(tagsField, "w 100%, wrap");

    add(openButton, "right, skip");

    pack();
    setVisible(true);
  }
}
