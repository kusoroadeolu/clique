package io.github.kusoroadeolu.clique.components;

import io.github.kusoroadeolu.clique.internal.documentation.Stable;

import java.util.Collection;


/**
 * @since 4.0.0
 * */
@Stable(since = "4.0.0")
public interface PendingTable {
    Table headers(String... headers);

    Table headers(Collection<String> headers);
}
