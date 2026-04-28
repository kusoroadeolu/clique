package io.github.kusoroadeolu.clique.internal.markup;


import io.github.kusoroadeolu.clique.internal.documentation.InternalApi;
import io.github.kusoroadeolu.clique.spi.AnsiCode;
import io.github.kusoroadeolu.clique.style.StyleBuilder;

import java.util.List;

import static io.github.kusoroadeolu.clique.internal.utils.Constants.ZERO;

@InternalApi(since = "3.2.0")
public final class StyleResolver {

    private StyleResolver(){}

    //Restyle the extracted string with the given colors
    public static String resolve(List<ParseToken> tokens, String string, boolean autoReset) {
        final StyleBuilder sb = new StyleBuilder();
        String val;
        final int size = tokens.size();

        if (tokens.isEmpty()) {
            return string;
        }

        //Check if the styling starts from the beginning of the string
        if (tokens.getFirst().start() != 0) {
            sb.append(string.substring(ZERO, tokens.getFirst().start()));
        }

         for (int i = 0; i < size; i++) {
                final ParseToken curr = tokens.get(i);
                final ParseToken next = i != (size - 1) ? tokens.get(i + 1) :
                        new ParseToken(string.length(), ZERO, null); //if we're at the end of the loop, we apply the current style to the rem of the string

                final AnsiCode[] codes = curr.styles().toArray(AnsiCode[]::new);
                final int start = curr.end() + 1;
                final int end = next.start();
                val = string.substring(start, end);

                if (autoReset) sb.appendAndReset(val, codes);
                else sb.append(val, codes);
            }

        return sb.toString();
    }
}
