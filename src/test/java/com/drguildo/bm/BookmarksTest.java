/*
 * This program is free software. It comes without any warranty, to the extent
 * permitted by applicable law. You can redistribute it and/or modify it under
 * the terms of the Do What The Fuck You Want To Public License, Version 2, as
 * published by Sam Hocevar. See http://sam.zoy.org/wtfpl/COPYING for more
 * details.
 */

package com.drguildo.bm;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class BookmarksTest {
  Bookmarks bookmarks;
  String[] urls = { "http://www.example.net/", "http://www.google.com/" };
  String emptyTags = "";

  @Before
  public void setUp() throws Exception {
    bookmarks = new Bookmarks();
  }

  @Test
  public void testAdd() throws Exception {
    bookmarks.add(urls[0], emptyTags);
    assertTrue(bookmarks.count() == 1);

    bookmarks.add(urls[1], emptyTags);
    assertTrue(bookmarks.count() == 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddInvalid() throws Exception {
    bookmarks.add("", emptyTags);
    assertTrue(bookmarks.count() == 0);
  }

  @Test
  public void testAddDuplicate() throws Exception {
    bookmarks.add(urls[0], emptyTags);
    bookmarks.add(urls[0], emptyTags);
    assertTrue(bookmarks.count() == 1);
  }

  @Test
  public void testGet() throws Exception {
    bookmarks.add(urls[0], emptyTags);

    Bookmark b = bookmarks.get(0);
    assertTrue(b.getUrl().equals(urls[0]));
    assertTrue(b.getTags().isEmpty());

    String testTags = "test1,test2, test3";

    bookmarks.add(urls[1], testTags);

    b = bookmarks.get(1);
    assertTrue(b.getUrl().equals(urls[1]));

    Tags tags = b.getTags();
    assertTrue(tags.contains("test1"));
    assertTrue(tags.contains("test2"));
    assertTrue(tags.contains("test3"));
    assertFalse(tags.contains("test4"));
  }

  @Test
  public void testRemove() throws Exception {
    bookmarks.add(urls[0], emptyTags);
    assertTrue(bookmarks.count() == 1);

    bookmarks.remove(urls[0]);
    assertTrue(bookmarks.count() == 0);
  }

  @Test
  public void testFind() throws Exception {
    assertTrue(bookmarks.find(urls[0]) == -1);

    bookmarks.add(urls[0], emptyTags);
    assertTrue(bookmarks.find(urls[0]) == 0);
    assertTrue(bookmarks.find(urls[1]) == -1);

    bookmarks.add(urls[1], emptyTags);
    assertTrue(bookmarks.find(urls[1]) == 1);

    bookmarks.remove(urls[0]);
    assertTrue(bookmarks.find(urls[0]) == -1);
    assertTrue(bookmarks.find(urls[1]) == 0);

    bookmarks.remove(urls[1]);
    assertTrue(bookmarks.find(urls[1]) == -1);
  }

  @Test
  public void testAddWithoutProtocol() throws Exception {
    String testUrl = "www.example.net";

    bookmarks.add(testUrl, emptyTags);
    assertTrue(bookmarks.get(0).getUrl().equals("http://" + testUrl));
  }

  @Test
  public void testAddTags() throws Exception {
    bookmarks.add(urls[0], ",");
    assertTrue(bookmarks.get(0).getTags().isEmpty());
    bookmarks.remove(urls[0]);

    bookmarks.add(urls[0], "test,");
    assertTrue(bookmarks.get(0).getTags().size() == 1);
    bookmarks.remove(urls[0]);

    bookmarks.add(urls[0], ",test");
    assertTrue(bookmarks.get(0).getTags().size() == 1);
    bookmarks.remove(urls[0]);
  }

  @Test
  public void testRenameTag() throws Exception {
    bookmarks.add(urls[0], "foo,bar");
    bookmarks.add(urls[1], "foo,baz");
    bookmarks.renameTag("foo", "test");
    assertTrue(!bookmarks.get(0).getTags().contains("foo"));
    assertTrue(!bookmarks.get(1).getTags().contains("foo"));
    assertTrue(bookmarks.get(0).getTags().contains("test"));
    assertTrue(bookmarks.get(0).getTags().contains("bar"));
    assertTrue(bookmarks.get(1).getTags().contains("test"));
    assertTrue(bookmarks.get(1).getTags().contains("baz"));
    assertTrue(bookmarks.get(0).getTags().size() == 2);
    assertTrue(bookmarks.get(1).getTags().size() == 2);
  }
}
