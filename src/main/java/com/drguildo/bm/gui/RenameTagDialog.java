/*
 * This program is free software. It comes without any warranty, to the extent
 * permitted by applicable law. You can redistribute it and/or modify it under
 * the terms of the Do What The Fuck You Want To Public License, Version 2, as
 * published by Sam Hocevar. See http://sam.zoy.org/wtfpl/COPYING for more
 * details.
 */

package com.drguildo.bm.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.drguildo.bm.Bookmarks;

public class RenameTagDialog extends JDialog {
  private static final long serialVersionUID = -2554026212633140139L;

  public RenameTagDialog(JFrame owner, boolean modal,
      final BookmarksModel model, final Bookmarks bookmarks) {
    super(owner, true);

    Collection<String> tags = bookmarks.getTags();

    final JComboBox<String> comboBox = new JComboBox<>();
    final JTextField newTagField = new JTextField();

    JButton renameButton = new JButton("Rename");
    renameButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        bookmarks.renameTag((String) comboBox.getSelectedItem(),
            newTagField.getText());
        model.fireTableDataChanged();
      }
    });
    ;

    for (String tag : tags) {
      comboBox.addItem(tag);
    }

    setTitle("Rename Tag");
    setLayout(new MigLayout());
    setLocationRelativeTo(owner);

    add(new JLabel("Original"), "right");
    add(comboBox, "wrap");
    add(new JLabel("New"), "right");
    add(newTagField, "w 100%, wrap");
    add(renameButton, "skip, right");

    pack();
    setVisible(true);
  }
}
