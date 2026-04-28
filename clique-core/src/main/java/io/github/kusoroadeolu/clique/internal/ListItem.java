package io.github.kusoroadeolu.clique.internal;

import io.github.kusoroadeolu.clique.components.ItemList;
import io.github.kusoroadeolu.clique.internal.documentation.InternalApi;

import java.util.Objects;

@InternalApi(since = "4.0.2")
public record ListItem(
        String symbol,
        String content,
        ItemList sublist
) {
    public ListItem{
        Objects.requireNonNull(symbol, "Symbol cannot be null");
        Objects.requireNonNull(content, "Content cannot be null");
    }
}
