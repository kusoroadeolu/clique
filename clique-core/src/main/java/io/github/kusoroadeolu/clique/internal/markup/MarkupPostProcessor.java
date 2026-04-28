package io.github.kusoroadeolu.clique.internal.markup;

import io.github.kusoroadeolu.clique.internal.documentation.InternalApi;

import static io.github.kusoroadeolu.clique.internal.utils.Constants.LBRACKET;

@InternalApi(since = "3.2.0")
public class MarkupPostProcessor {
    private MarkupPostProcessor(){}

    private static final String ESCAPED_BRACKET = "\\[";

    public static String postProcess(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.replace(ESCAPED_BRACKET, String.valueOf(LBRACKET));
    }

}