/*
 * This program is free software. It comes without any warranty, to the extent
 * permitted by applicable law. You can redistribute it and/or modify it under
 * the terms of the Do What The Fuck You Want To Public License, Version 2, as
 * published by Sam Hocevar. See http://sam.zoy.org/wtfpl/COPYING for more
 * details.
 */

package com.drguildo.bm.gui;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import com.drguildo.bm.Bookmarks;
import com.drguildo.bm.BookmarksManager;

public class TagTextField extends JTextField implements DocumentListener {
  private static final long serialVersionUID = 3694714505248505597L;

  private final Bookmarks bookmarks;

  public TagTextField() {
    super();

    bookmarks = BookmarksManager.getBookmarks();

    getDocument().addDocumentListener(this);

    // setToolTipText("Tags are separated by a comma (e.g. tag1, tag two, tag3).");
  }

  @Override
  public void changedUpdate(DocumentEvent e) {
  }

  @Override
  public void insertUpdate(DocumentEvent e) {
    cursorTag(e);
  }

  @Override
  public void removeUpdate(DocumentEvent e) {
    cursorTag(e);
  }

  public void cursorTag(DocumentEvent e) {
    if (e.getLength() != 1) {
      return;
    }

    Document document = e.getDocument();

    String text = null;
    try {
      text = document.getText(0, document.getLength());
    } catch (BadLocationException e1) {
      return;
    }

    int offset = e.getOffset();
    int start = -1;
    int end = -1; // XXX: Is this needed?

    // Check whether the cursor is located in or on the boundaries of a
    // word.
    for (int i = offset; i >= 0 && i < text.length(); i--) {
      if (isValidTagCharacter(text.charAt(i))) {
        start = i;
      } else {
        break;
      }
    }

    for (int i = offset; i >= 0 && i < text.length(); i++) {
      if (isValidTagCharacter(text.charAt(i))) {
        end = i;
      } else {
        break;
      }
    }

    // The cursor is located within or on the boundaries of a word.
    if ((start != -1) && (end != -1)) {
      String prefix = text.substring(start, offset + 1);
      SwingUtilities.invokeLater(new CompletionTask(document, prefix,
          offset + 1));
    }
  }

  private boolean isValidTagCharacter(char c) {
    return !(Character.isWhitespace(c) || c == ',');
  }

  private class CompletionTask implements Runnable {
    private final Document document;
    private final String prefix;
    private final int offset;

    CompletionTask(Document d, String p, int o) {
      document = d;
      prefix = p;
      offset = o;
    }

    @Override
    public void run() {
      ArrayList<String> tags = bookmarks.getTags();

      // TODO: Think about upper and lower-case.
      int n = Collections.binarySearch(tags, prefix);
      if (n < 0 && (Math.abs(n) <= tags.size())) {
        String match = tags.get(Math.abs(n) - 1);
        if (match.startsWith(prefix)) {
          String completion = match.substring(prefix.length());
          try {
            document.insertString(offset, completion, null);
            setCaretPosition(offset + completion.length());
            moveCaretPosition(offset);
          } catch (BadLocationException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }
}
