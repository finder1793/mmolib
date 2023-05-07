package io.lumine.mythic.lib.util;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

public interface Jsonable {

    @NotNull
    public JsonElement toJson();
}
