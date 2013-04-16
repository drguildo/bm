/*
 * This program is free software. It comes without any warranty, to the extent
 * permitted by applicable law. You can redistribute it and/or modify it under
 * the terms of the Do What The Fuck You Want To Public License, Version 2, as
 * published by Sam Hocevar. See http://sam.zoy.org/wtfpl/COPYING for more
 * details.
 */

package com.drguildo.bm;

import java.io.File;
import java.nio.file.Path;

public class Settings {
  private static File bookmarkFile = new File(System.getProperty("user.home")
      + File.separatorChar + "bm.json");

  // The maximum number of bookmarks to open at once.
  private static int maxOpen = 4;

  public static int getMaxOpen() {
    return maxOpen;
  }

  public static void setMaxOpen(int newMaxOpen) {
    maxOpen = newMaxOpen;
  }

  public static File getBookmarkFile() {
    return bookmarkFile;
  }

  public static Path getBookmarkPath() {
    return bookmarkFile.toPath();
  }
}
