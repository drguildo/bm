/*
 * This program is free software. It comes without any warranty, to the extent
 * permitted by applicable law. You can redistribute it and/or modify it under
 * the terms of the Do What The Fuck You Want To Public License, Version 2, as
 * published by Sam Hocevar. See http://sam.zoy.org/wtfpl/COPYING for more
 * details.
 */

package com.drguildo.bm.gui;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;

import javax.swing.table.AbstractTableModel;

import com.drguildo.bm.Bookmark;
import com.drguildo.bm.Bookmarks;
import com.drguildo.bm.BookmarksManager;

class BookmarksModel extends AbstractTableModel {
  private static final long serialVersionUID = 1617225865339666522L;

  private final String[] columnNames = { "URL", "Tags", "Date Added" };
  private final Bookmarks bookmarks;
  private final SimpleDateFormat dateFormat = new SimpleDateFormat(
      "yyyy/MM/dd G HH:mm a");

  public BookmarksModel() {
    bookmarks = BookmarksManager.getBookmarks();
  }

  @Override
  public int getRowCount() {
    return bookmarks.count();
  }

  @Override
  public int getColumnCount() {
    return columnNames.length;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    Bookmark b = bookmarks.get(rowIndex);

    switch (columnIndex) {
    case 0:
      return b.getUrl();
    case 1: {
      return b.getTags();
    }
    case 2:
      return dateFormat.format(b.getAdded());
    default:
      throw new IndexOutOfBoundsException("Invalid column.");
    }
  }

  @Override
  public String getColumnName(int columnIndex) {
    return columnNames[columnIndex];
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return columnIndex < 2;
  }

  @Override
  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    Bookmark b = bookmarks.get(rowIndex);

    switch (columnIndex) {
    case 0: {
      try {
        URL url = new URL(aValue.toString());
        // XXX: Check the new URL is different?
        b.setUrl(url);
      } catch (MalformedURLException e) {
        return;
      }
      break;
    }
    case 1: {
      // XXX: Check the new tags are different?
      b.setTags(aValue.toString());
      break;
    }
    default: {
      break;
    }
    }

    fireTableDataChanged();
  }
}
