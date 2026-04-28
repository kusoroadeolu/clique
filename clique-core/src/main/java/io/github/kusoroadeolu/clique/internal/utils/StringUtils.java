package io.github.kusoroadeolu.clique.internal.utils;


import io.github.kusoroadeolu.clique.internal.Cell;
import io.github.kusoroadeolu.clique.internal.RGBColor;
import io.github.kusoroadeolu.clique.internal.documentation.InternalApi;
import io.github.kusoroadeolu.clique.parser.MarkupParser;
import io.github.kusoroadeolu.clique.spi.AnsiCode;

import java.util.Objects;

import static io.github.kusoroadeolu.clique.internal.utils.Constants.*;
import static io.github.kusoroadeolu.clique.internal.utils.AnsiDetector.ansiEnabled;
import static io.github.kusoroadeolu.clique.style.StyleCode.RESET;

@InternalApi(since = "3.2.0")
public final class StringUtils {
    private StringUtils(){}

    public static void clearStringBuilder(StringBuilder sb) {
        sb.setLength(ZERO);
    }

    public static Cell parseToCell(String text, MarkupParser parser) {
        return new Cell(parser.getOriginalString(text), parser.parse(text));
    }

    public static String parse(String text, MarkupParser parser) {
        return parser.parse(text);
    }

    public static String stripAnsi(String styled) {
        int i = 0;
        boolean inEscape = false;
        var clean = new StringBuilder();
        while (i < styled.length()) {
            char c = styled.charAt(i);
            if (c == ESC && nextCharEquals(styled, i + 1, LBRACKET)) {
                inEscape = true;
            } else if (inEscape && c == ANSI_END) {
                inEscape = false;
            }else if (!inEscape){
                clean.append(c);
            }

            i++;
        }

        return clean.toString();
    }

    public static boolean nextCharEquals(String s, int pos, char ch){
        return pos < s.length() && s.charAt(pos) == ch;
    }


    public static AnsiCode hex(String hex){
        return hexBase(hex, false);
    }

    public static AnsiCode bgHex(String hex){
        return hexBase(hex, true);
    }

    public static AnsiCode hexBase(String hex, boolean background){
        if (hex.isBlank()) return () -> EMPTY;
        if (!hex.startsWith(HASH) || hex.length() != 7) throw new IllegalArgumentException("Invalid hex color: expected format '#RRGGBB' but got '" + hex + "'");
        int red = Integer.parseInt(hex.substring(1, 3), 16);
        int green = Integer.parseInt(hex.substring(3, 5), 16);
        int blue = Integer.parseInt(hex.substring(5, 7), 16);
        return new RGBColor(red, green, blue, background);
    }



    public static String formatAndReset(StringBuilder sb, String text, AnsiCode... ansiCodes) {
        style(text, sb, ansiCodes).append(RESET);
        String result = sb.toString();
        clearStringBuilder(sb);
        return result;
    }

    //A helper method to style text with the given codes
    public static StringBuilder style(String text, StringBuilder sb, AnsiCode... ansiCodes) {
        Objects.requireNonNull(text, "Text cannot be null");
        Objects.requireNonNull(ansiCodes, "Ansi codes cannot be null");

        //Check if ansi is enabled
        if (ansiEnabled()) {
            for (AnsiCode code : ansiCodes) {
                if (code != null) sb.append(code.ansiSequence());

            }
        }

        return sb.append(text);
    }
}
