package com.drguildo.bm.gui;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

import com.drguildo.bm.Bookmark;
import com.drguildo.bm.Bookmarks;
import com.drguildo.bm.BookmarksManager;

public class TagCellEditor extends AbstractCellEditor implements
    TableCellEditor, CellEditorListener {
  private static final long serialVersionUID = 4622190600706665718L;

  private static final Bookmarks bookmarks = BookmarksManager.getBookmarks();
  private static final TagTextField tagTextField = new TagTextField();

  private Bookmark bookmark;

  public TagCellEditor() {
    addCellEditorListener(this);

    tagTextField.setBorder(BorderFactory.createEmptyBorder());
  }

  @Override
  public Object getCellEditorValue() {
    return tagTextField.getText();
  }

  @Override
  public Component getTableCellEditorComponent(JTable table, Object value,
      boolean isSelected, int row, int column) {
    bookmark = bookmarks.get(table.convertRowIndexToModel(row));

    tagTextField.setText(bookmark.getTags().toString());

    return tagTextField;
  }

  @Override
  public void editingCanceled(ChangeEvent e) {
  }

  @Override
  public void editingStopped(ChangeEvent e) {
    bookmark.setTags(tagTextField.getText());
  }
}
