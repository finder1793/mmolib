package io.lumine.mythic.lib.util;

import org.jetbrains.annotations.NotNull;

public interface PreloadedObject {

    @NotNull
    PostLoadAction getPostLoadAction();
}
