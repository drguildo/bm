/*
 * This program is free software. It comes without any warranty, to the extent
 * permitted by applicable law. You can redistribute it and/or modify it under
 * the terms of the Do What The Fuck You Want To Public License, Version 2, as
 * published by Sam Hocevar. See http://sam.zoy.org/wtfpl/COPYING for more
 * details.
 */

package com.drguildo.bm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class BookmarksManager {
  private static Bookmarks bookmarks = new Bookmarks();

  private static File bookmarkFile = Settings.getBookmarkFile();
  private static Path bookmarkPath = Settings.getBookmarkPath();

  private static final boolean DEBUG = false;

  public static Bookmarks load() throws IOException {
    StringBuilder stringBuilder = new StringBuilder();

    if (!Files.exists(bookmarkPath)) {
      return bookmarks;
    }

    try {
      BufferedReader bufferedReader = new BufferedReader(new FileReader(
          bookmarkFile));
      String string = bufferedReader.readLine();

      while (string != null) {
        stringBuilder.append(string);
        string = bufferedReader.readLine();
      }

      bufferedReader.close();
    } catch (IOException e) {
      e.printStackTrace();
      throw new IOException(e);
    }

    if (stringBuilder.length() == 0) {
      return bookmarks;
    }

    String json = stringBuilder.toString();

    JsonParser parser = new JsonParser();
    JsonArray array = parser.parse(json).getAsJsonArray();

    for (JsonElement element : array) {
      JsonObject jsonObject = element.getAsJsonObject();

      URL url = new URL(jsonObject.get("url").getAsString());

      Tags tags = new Tags();
      for (JsonElement tagElement : jsonObject.get("tags").getAsJsonArray()) {
        tags.add(tagElement.getAsString());
      }

      Date date = new Date(jsonObject.get("added").getAsLong());

      bookmarks.add(new Bookmark(url, tags, date));
    }

    return bookmarks;
  }

  public static void save() throws IOException {
    save(bookmarks);
  }

  public static void save(Bookmarks bookmarks) throws IOException {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(Date.class, new DateSerializer());
    gsonBuilder.registerTypeAdapter(Tags.class, new TagsSerializer());

    if (DEBUG) {
      Gson gson = gsonBuilder.setPrettyPrinting().create();
      System.out.println(gson.toJson(bookmarks.toArrayList()));
      return;
    }

    if (bookmarkFile.exists()) {
      Files.move(bookmarkFile.toPath(), new File(bookmarkFile.getPath()
          + ".bak").toPath(), StandardCopyOption.REPLACE_EXISTING);
    } else {
      if (!bookmarkFile.createNewFile()) {
        throw new IOException("Failed to create bookmark file.");
      }
    }

    FileWriter writer = new FileWriter(bookmarkFile);
    writer.write(gsonBuilder.create().toJson(bookmarks.toArrayList()));
    writer.flush();
    writer.close();
  }

  public static Bookmarks getBookmarks() {
    return bookmarks;
  }
}
