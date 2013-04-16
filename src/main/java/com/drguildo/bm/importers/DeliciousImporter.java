/*
 * This program is free software. It comes without any warranty, to the extent
 * permitted by applicable law. You can redistribute it and/or modify it under
 * the terms of the Do What The Fuck You Want To Public License, Version 2, as
 * published by Sam Hocevar. See http://sam.zoy.org/wtfpl/COPYING for more
 * details.
 */

package com.drguildo.bm.importers;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.drguildo.bm.Bookmarks;

public class DeliciousImporter {
  public Bookmarks convert(File file) throws IOException {
    Bookmarks bookmarks = new Bookmarks();

    Document document = Jsoup.parse(file, "UTF-8");
    Elements elements = document.select("a[href]").select("[tags]");

    for (Element element : elements) {
      String x = element.attr("href");
      String y = element.attr("tags");
      String z = element.attr("add_date");

      long add_date = Long.parseLong(z);
      // Delicious uses UNIX time, which is seconds since the epoch, but
      // Java uses milliseconds since the epoch.
      add_date *= 1000L;

      bookmarks.add(x, y + ", imported:delicious", new Date(add_date));
    }

    return bookmarks;
  }
}
