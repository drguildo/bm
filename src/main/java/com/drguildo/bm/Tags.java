/*
 * This program is free software. It comes without any warranty, to the extent
 * permitted by applicable law. You can redistribute it and/or modify it under
 * the terms of the Do What The Fuck You Want To Public License, Version 2, as
 * published by Sam Hocevar. See http://sam.zoy.org/wtfpl/COPYING for more
 * details.
 */

package com.drguildo.bm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

public class Tags {
  public TreeSet<String> tags;

  public Tags() {
    tags = new TreeSet<>();
  }

  public Tags(String t) {
    this();

    if (t.length() > 0) {
      for (String tag : Arrays.asList(t.split(","))) {
        add(tag);
      }
    }
  }

  public void add(String t) {
    if (!t.trim().isEmpty() && !t.contains(",")) {
      tags.add(t.trim());
    }
  }

  public void add(Tags t) {
    for (String tag : t.toList()) {
      tags.add(tag);
    }
  }

  public void rename(String oldTag, String newTag) {
    if (tags.contains(oldTag)) {
      tags.remove(oldTag);
      tags.add(newTag);
    }
  }

  public void remove(String t) {
    tags.remove(t);
  }

  public boolean contains(String t) {
    return tags.contains(t);
  }

  public int size() {
    return tags.size();
  }

  public ArrayList<String> toList() {
    return new ArrayList<String>(tags);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();

    for (String tag : tags) {
      builder.append(tag);
      if (!tag.equals(tags.last())) {
        builder.append(", ");
      }
    }

    return builder.toString();
  }

  public boolean isEmpty() {
    return tags.isEmpty();
  }
}
