/*
 * This program is free software. It comes without any warranty, to the extent
 * permitted by applicable law. You can redistribute it and/or modify it under
 * the terms of the Do What The Fuck You Want To Public License, Version 2, as
 * published by Sam Hocevar. See http://sam.zoy.org/wtfpl/COPYING for more
 * details.
 */

package com.drguildo.bm.importers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.drguildo.bm.Bookmarks;

public class TextImporter {
  public Bookmarks convert(File file, String tags) throws IOException {
    BufferedReader bf = new BufferedReader(new FileReader(file));
    Bookmarks b = new Bookmarks();

    String url;
    while (bf.ready()) {
      url = bf.readLine();
      if (url != null) {
        b.add(url, tags + ", imported:text");
      }
    }

    bf.close();

    return b;
  }
}
