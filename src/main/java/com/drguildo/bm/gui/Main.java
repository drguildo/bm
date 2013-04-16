/*
 * This program is free software. It comes without any warranty, to the extent
 * permitted by applicable law. You can redistribute it and/or modify it under
 * the terms of the Do What The Fuck You Want To Public License, Version 2, as
 * published by Sam Hocevar. See http://sam.zoy.org/wtfpl/COPYING for more
 * details.
 */

package com.drguildo.bm.gui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.regex.PatternSyntaxException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

import net.miginfocom.swing.MigLayout;

import com.drguildo.bm.Bookmarks;
import com.drguildo.bm.BookmarksManager;
import com.drguildo.bm.Settings;

class Main {
  private static final JFrame frame = new JFrame();

  private static Bookmarks bookmarks;
  private static BookmarksModel model;
  private static JTable table;
  private static TableRowSorter<BookmarksModel> sorter;

  private static void createAndShowGUI() {
    model = new BookmarksModel();
    table = new JTable(model);
    sorter = new TableRowSorter<>(model);

    final JTextField urlField = new JTextField();
    final TagTextField tagsField = new TagTextField();
    final JTextField searchField = new JTextField();
    final JPopupMenu bookmarkPopup = new JPopupMenu();

    JPanel mainPanel = new JPanel(new MigLayout());

    JButton addButton = new JButton("Add");

    JMenuItem openMenuItem = new JMenuItem("Open");
    JMenuItem copyUrlMenuItem = new JMenuItem("Copy");
    JMenuItem deleteMenuItem = new JMenuItem("Delete");

    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    JMenuItem importMenuItem = fileMenu.add(new JMenuItem("Import"));
    fileMenu.addSeparator();
    JMenuItem exitMenuItem = fileMenu.add(new JMenuItem("Exit"));
    JMenu toolsMenu = new JMenu("Tools");
    JMenuItem renameMenuItem = toolsMenu.add(new JMenuItem("Rename Tag"));

    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    int windowWidth = dim.width / 2;
    int windowHeight = (int) (dim.height / 1.2);

    frame.setTitle("Bookmark Manager XP 2000 Professional Edition");
    frame.setPreferredSize(new Dimension(windowWidth, windowHeight));
    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        saveAndExit();
      }
    });

    addButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        newBookmark(urlField, tagsField);
      }
    });

    KeyAdapter keyAdapter = new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          newBookmark(urlField, tagsField);
        }
      }
    };
    urlField.addKeyListener(keyAdapter);
    tagsField.addKeyListener(keyAdapter);

    // Calling this twice seems to be the only way to get the date column
    // ordered descending.
    sorter.toggleSortOrder(2);
    sorter.toggleSortOrder(2);

    table.getColumnModel().getColumn(0).setPreferredWidth(windowWidth - 400);
    table.setRowSorter(sorter);
    table.addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
        case KeyEvent.VK_DELETE: {
          deleteSelected();
          break;
        }
        }
      }
    });
    table.addMouseListener(new MouseAdapter() {
      private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
          bookmarkPopup.show(e.getComponent(), e.getX(), e.getY());
        }
      }

      @Override
      public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
      }
    });
    table.getColumnModel().getColumn(1).setCellEditor(new TagCellEditor());

    searchField.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void changedUpdate(DocumentEvent e) {
      }

      @Override
      public void insertUpdate(DocumentEvent e) {
        newFilter();
      }

      public void newFilter() {
        RowFilter<BookmarksModel, Object> rowFilter;

        try {
          rowFilter = RowFilter.regexFilter(searchField.getText(), 1);
          sorter.setRowFilter(rowFilter);
        } catch (PatternSyntaxException e) {
          return;
        }
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        newFilter();
      }
    });

    openMenuItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (table.getSelectedRowCount() > Settings.getMaxOpen()) {
          JOptionPane.showMessageDialog(frame,
              "You're trying to open too many URLs at once!", "Error",
              JOptionPane.ERROR_MESSAGE);
          return;
        }

        for (String url : getSelectedURLs()) {
          openURL(url);
        }
      }
    });
    copyUrlMenuItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String url : getSelectedURLs()) {
          stringBuilder.append(url);
        }

        StringSelection selection = new StringSelection(stringBuilder
            .toString());
        frame.getToolkit().getSystemClipboard()
            .setContents(selection, selection);
      }
    });
    deleteMenuItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        deleteSelected();
      }
    });

    bookmarkPopup.add(openMenuItem);
    bookmarkPopup.add(copyUrlMenuItem);
    bookmarkPopup.add(deleteMenuItem);

    importMenuItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        new ImportDialog(frame, true, model, bookmarks);
      }
    });
    exitMenuItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        saveAndExit();
      }
    });
    renameMenuItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        new RenameTagDialog(frame, true, model, bookmarks);
      }
    });

    menuBar.add(fileMenu);
    menuBar.add(toolsMenu);

    mainPanel.add(new JLabel("URL"));
    mainPanel.add(urlField, "w 100%, span, wrap");

    mainPanel.add(new JLabel("Tags"));
    mainPanel.add(tagsField, "w 100%");
    mainPanel.add(addButton, "wrap");

    mainPanel.add(new JScrollPane(table), "h 100%, w 100%, span, wrap");

    mainPanel.add(new JLabel("Filter"));
    mainPanel.add(searchField, "w 100%, span");

    frame.add(menuBar, BorderLayout.NORTH);
    frame.add(mainPanel, BorderLayout.CENTER);

    frame.pack();
    frame.setVisible(true);
  }

  private static void deleteSelected() {
    for (String url : getSelectedURLs()) {
      System.out.println("Deleting: " + url);
      bookmarks.remove(url);
    }
    model.fireTableDataChanged();
  }

  private static ArrayList<String> getSelectedURLs() {
    ArrayList<String> urls = new ArrayList<>();

    for (int i : table.getSelectedRows()) {
      urls.add(bookmarks.get(table.convertRowIndexToModel(i)).getUrl());
    }

    return urls;
  }

  private static void newBookmark(JTextField urlField, JTextField tagsField) {
    bookmarks.add(urlField.getText(), tagsField.getText());

    urlField.setText("");
    tagsField.setText("");

    model.fireTableDataChanged();
  }

  private static void openURL(String url) {
    if (!Desktop.isDesktopSupported()) {
      System.err.println("Desktop is not supported (fatal)");
      return;
    }

    Desktop desktop = java.awt.Desktop.getDesktop();

    if (!desktop.isSupported(Desktop.Action.BROWSE)) {
      System.err.println("Desktop doesn't support the browse action (fatal)");
      return;
    }

    try {
      URI uri = new URI(url);
      desktop.browse(uri);
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  private static void saveAndExit() {
    try {
      BookmarksManager.save(bookmarks);
      System.exit(0);
    } catch (IOException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(frame, "Unable to save bookmarks.",
          "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  public static void main(String[] args) {
    try {
      bookmarks = BookmarksManager.load();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        try {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (UnsupportedLookAndFeelException e) {
        }
        createAndShowGUI();
      }
    });
  }
}
