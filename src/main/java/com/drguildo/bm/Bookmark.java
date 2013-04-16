/*
 * This program is free software. It comes without any warranty, to the extent
 * permitted by applicable law. You can redistribute it and/or modify it under
 * the terms of the Do What The Fuck You Want To Public License, Version 2, as
 * published by Sam Hocevar. See http://sam.zoy.org/wtfpl/COPYING for more
 * details.
 */

package com.drguildo.bm;

import java.net.URL;
import java.util.Date;

public final class Bookmark {
  private URL url;
  private Tags tags;
  private Date added;

  public Bookmark(URL u, Tags t, Date a) {
    setUrl(u);
    setTags(t);
    added = a;
  }

  public String getUrl() {
    return url.toString();
  }

  public Tags getTags() {
    return tags;
  }

  public Date getAdded() {
    return added;
  }

  public void setUrl(URL newUrl) {
    url = newUrl;
  }

  public void setTags(Tags newTags) {
    tags = newTags;
  }

  public void setTags(String newTags) {
    setTags(new Tags(newTags));
  }

  public void setAdded(Date newAdded) {
    added = newAdded;
  }

  @Override
  public String toString() {
    return "Bookmark{" + "url=" + url + ", tags=" + tags + ", added=" + added
        + '}';
  }
}
