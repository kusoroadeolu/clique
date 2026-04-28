package io.github.kusoroadeolu.clique.internal.utils;


import io.github.kusoroadeolu.clique.configuration.TextAlign;
import io.github.kusoroadeolu.clique.internal.BorderChars;
import io.github.kusoroadeolu.clique.internal.BoxWrapper;
import io.github.kusoroadeolu.clique.internal.Cell;
import io.github.kusoroadeolu.clique.internal.documentation.InternalApi;
import io.github.kusoroadeolu.clique.spi.AnsiCode;
import io.github.kusoroadeolu.clique.style.StyleCode;

import java.util.List;

import static io.github.kusoroadeolu.clique.internal.utils.Constants.BLANK;
import static io.github.kusoroadeolu.clique.internal.utils.Constants.NEWLINE;
import static io.github.kusoroadeolu.clique.internal.utils.StringUtils.clearStringBuilder;
import static io.github.kusoroadeolu.clique.internal.utils.StringUtils.formatAndReset;

@InternalApi(since = "3.2.0")
public class BoxUtils {

    private BoxUtils(){}

    private static final String RESET = StyleCode.RESET.ansiSequence();

    public static void alignText(StringBuilder sb, int idx, TextAlign textAlign, String spaces, List<Cell> wordWrap, String vLine, int padding) {
        final Cell cell = wordWrap.get(idx);
        final String ss = cell.styledText();
        final String fixed = BLANK.repeat(padding); //The padding in string


        final int fillSpace = spaces.length() - cell.width() - (padding * 2);
        switch (textAlign) {
            case TOP_LEFT, CENTER_LEFT, BOTTOM_LEFT ->
                sb.append(vLine)
                        .append(fixed)
                        .append(ss)
                        .append(RESET)
                        .append(BLANK.repeat(Math.max(0, fillSpace)))
                        .append(fixed)
                        .append(vLine)
                        .append(NEWLINE);

            case TOP_RIGHT, CENTER_RIGHT, BOTTOM_RIGHT ->
                sb.append(vLine)
                        .append(fixed)
                        .append(BLANK.repeat(Math.max(0, fillSpace)))
                        .append(ss)
                        .append(RESET)
                        .append(fixed)
                        .append(vLine)
                        .append(NEWLINE);

            case TOP_CENTER, CENTER, BOTTOM_CENTER -> {
                final int leftFill = fillSpace / 2;
                final int rightFill = fillSpace - leftFill;
                sb.append(vLine)
                        .append(fixed)
                        .append(BLANK.repeat(Math.max(0, leftFill)))
                        .append(ss).append(RESET)
                        .append(BLANK.repeat(Math.max(0, rightFill)))
                        .append(fixed)
                        .append(vLine).append(NEWLINE);
            }
        }
    }


    public static void drawBox(StringBuilder sb, BoxWrapper boxWrapper, TextAlign textAlign) {
        final int width = boxWrapper.width();
        final int padding = boxWrapper.configuration().getPadding();
        final String spaces = BLANK.repeat(width);
        final String hLines = sb.repeat(boxWrapper.hLine(), width).toString();
        clearStringBuilder(sb);
        sb.append(boxWrapper.tLeft()).append(hLines).append(boxWrapper.tRight()).append(NEWLINE);

        int startLine = 0;
        final int textHeight = boxWrapper.wordWrap().size(); // Text height
        final int availableLines = boxWrapper.height();

        if (textAlign == TextAlign.CENTER || textAlign == TextAlign.CENTER_LEFT || textAlign == TextAlign.CENTER_RIGHT) {
            startLine = (availableLines - textHeight) / 2; //Adding one to this like the others makes the vertical alignment(the top half, hog 1 more space than needed, starving the bottom half)
        } else if (textAlign == TextAlign.BOTTOM_LEFT || textAlign == TextAlign.BOTTOM_CENTER || textAlign == TextAlign.BOTTOM_RIGHT) {
            startLine = (availableLines - textHeight);
        }

        for (int i = 0; i < boxWrapper.height(); i++) {
            if (i >= startLine && i < (startLine + textHeight)) {
                int textIndex = i - startLine;
                alignText(sb, textIndex, textAlign, spaces, boxWrapper.wordWrap(), boxWrapper.vLine(), padding);
            } else {
                sb.append(boxWrapper.vLine()).append(spaces).append(boxWrapper.vLine()).append(NEWLINE);
            }
        }

        sb.append(boxWrapper.bLeft()).append(hLines).append(boxWrapper.bRight());
    }


    public static void applyAnsiToBorders(BorderChars borderChar, AnsiCode[] borderColor) {
        final StringBuilder sb = new StringBuilder();
        borderChar.setHLine(formatAndReset(sb, borderChar.hLine(), borderColor));
        borderChar.setVLine(formatAndReset(sb, borderChar.vLine(), borderColor));
        borderChar.setTopLeft(formatAndReset(sb, borderChar.topLeft(), borderColor));
        borderChar.setTopRight(formatAndReset(sb, borderChar.topRight(), borderColor));
        borderChar.setBottomLeft(formatAndReset(sb, borderChar.bottomLeft(), borderColor));
        borderChar.setBottomRight(formatAndReset(sb, borderChar.bottomRight(), borderColor));
    }
}
