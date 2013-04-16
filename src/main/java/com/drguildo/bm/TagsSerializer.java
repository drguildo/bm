/*
 * This program is free software. It comes without any warranty, to the extent
 * permitted by applicable law. You can redistribute it and/or modify it under
 * the terms of the Do What The Fuck You Want To Public License, Version 2, as
 * published by Sam Hocevar. See http://sam.zoy.org/wtfpl/COPYING for more
 * details.
 */

package com.drguildo.bm;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class TagsSerializer implements JsonSerializer<Tags> {
  @Override
  public JsonElement serialize(Tags src, Type typeOfSrc,
      JsonSerializationContext context) {
    JsonArray fuckThisShit = new JsonArray();
    for (String tag : src.toList()) {
      fuckThisShit.add(new JsonPrimitive(tag));
    }
    return fuckThisShit;
  }
}
