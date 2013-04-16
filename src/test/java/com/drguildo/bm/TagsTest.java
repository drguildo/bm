/*
 * This program is free software. It comes without any warranty, to the extent
 * permitted by applicable law. You can redistribute it and/or modify it under
 * the terms of the Do What The Fuck You Want To Public License, Version 2, as
 * published by Sam Hocevar. See http://sam.zoy.org/wtfpl/COPYING for more
 * details.
 */

package com.drguildo.bm;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TagsTest {
  @Test
  public void testTags() throws Exception {
    Tags tags;

    tags = new Tags("foo, bar, baz");
    assertTrue(tags.size() == 3);
    assertTrue(tags.contains("foo"));
    assertTrue(tags.contains("bar"));
    assertTrue(tags.contains("baz"));

    tags = new Tags();

    tags.add("foo");
    assertTrue(tags.contains("foo"));
    assertTrue(tags.size() == 1);

    tags.add("foo");
    assertTrue(tags.contains("foo"));
    assertTrue(tags.size() == 1);

    assertTrue(!tags.contains("bar"));
    assertTrue(tags.size() == 1);

    tags.add("bar");
    assertTrue(tags.contains("bar"));
    assertTrue(tags.size() == 2);

    tags.remove("foo");
    assertTrue(!tags.contains("foo"));
    assertTrue(tags.size() == 1);
  }

  @Test
  public void testEmpty() throws Exception {
    Tags tags;

    tags = new Tags(",");
    assertTrue(tags.size() == 0);

    tags = new Tags(", ");
    assertTrue(tags.size() == 0);

    tags = new Tags(" ,");
    assertTrue(tags.size() == 0);

    tags = new Tags();

    tags.add("");
    tags.add(",");
    tags.add(", ");
    tags.add(" ,");
    assertTrue(!tags.contains(""));
    assertTrue(tags.size() == 0);
  }

  @Test
  public void testRename() throws Exception {
    Tags tags = new Tags();

    tags.add("foo");
    tags.add("bar");
    tags.rename("bar", "baz");
    assertTrue(tags.contains("foo"));
    assertTrue(!tags.contains("bar"));
    assertTrue(tags.contains("baz"));
    assertTrue(tags.size() == 2);

    tags = new Tags();
    tags.rename("foo", "bar");
    assertTrue(!tags.contains("bar"));
    assertTrue(tags.size() == 0);
  }
}
