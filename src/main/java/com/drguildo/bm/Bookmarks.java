/*
 * This program is free software. It comes without any warranty, to the extent
 * permitted by applicable law. You can redistribute it and/or modify it under
 * the terms of the Do What The Fuck You Want To Public License, Version 2, as
 * published by Sam Hocevar. See http://sam.zoy.org/wtfpl/COPYING for more
 * details.
 */

package com.drguildo.bm;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.TreeSet;

public class Bookmarks {
  private final ArrayList<Bookmark> bookmarks;

  public Bookmarks() {
    bookmarks = new ArrayList<>();
  }

  public void add(Bookmark bookmark) {
    bookmarks.add(bookmark);
  }

  public void add(String url) {
    add(url, "");
  }

  public void add(String u, String t) {
    add(u, t, new Date());
  }

  public void add(String u, String t, Date a) {
    if (u.isEmpty()) {
      throw new IllegalArgumentException("Invalid URL.");
    }

    if (!u.contains("://")) {
      u = "http://" + u;
    }

    URL url;

    try {
      url = new URL(u);
    } catch (MalformedURLException e) {
      return;
    }

    Tags tags = new Tags();
    if (t.length() > 0) {
      for (String tag : Arrays.asList(t.split(","))) {
        tags.add(tag.trim());
      }
    }

    int index = find(u);
    if (index == -1) {
      bookmarks.add(new Bookmark(url, tags, a));
    } else {
      // The bookmark already exists.
      Bookmark b = get(index);
      b.getTags().add(tags);
    }
  }

  public void merge(Bookmarks b) {
    for (int i = 0; i < b.count(); i++) {
      add(b.get(i));
    }
  }

  public Bookmark get(int index) {
    return bookmarks.get(index);
  }

  public void remove(String url) {
    bookmarks.remove(find(url));
  }

  public int count() {
    return bookmarks.size();
  }

  public int find(String u) {
    for (Bookmark bookmark : bookmarks) {
      if (bookmark.getUrl().equals(u)) {
        return bookmarks.indexOf(bookmark);
      }
    }

    return -1;
  }

  public void renameTag(String oldTag, String newTag) {
    for (Bookmark bookmark : bookmarks) {
      Tags tags = bookmark.getTags();

      if (tags.contains(oldTag)) {
        tags.remove(oldTag);
        tags.add(newTag);
      }
    }
  }

  // TODO: Not be slow and ugly as fuck.
  public ArrayList<String> getTags() {
    TreeSet<String> tags = new TreeSet<>();

    for (Bookmark bookmark : bookmarks) {
      tags.addAll(bookmark.getTags().toList());
    }

    return new ArrayList<String>(tags);
  }

  public ArrayList<Bookmark> toArrayList() {
    return bookmarks;
  }

  @Override
  public String toString() {
    return bookmarks.toString();
  }
}
