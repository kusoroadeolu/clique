package io.github.kusoroadeolu.clique.internal;

import io.github.kusoroadeolu.clique.internal.utils.Constants;
import io.github.kusoroadeolu.clique.internal.utils.StringUtils;
import io.github.kusoroadeolu.clique.spi.RGBAnsiCode;
import io.github.kusoroadeolu.clique.style.StyleCode;

public record Gradient(RGBAnsiCode from, RGBAnsiCode to) {

    public Gradient(String fromHex, String toHex) {
        this(StringUtils.hex(fromHex), StringUtils.hex(toHex));
    }

    public String apply(String text) {
        int colorIdx = 0;
        int i = 0;
        boolean inEscape = false;
        StringBuilder sb = new StringBuilder();

        while (i < text.length()) {
            char ch = text.charAt(i);
            if (ch == Constants.ESC && StringUtils.nextCharEquals(text, i + 1, Constants.LBRACKET)) {
                inEscape = true;
                sb.append(ch);
            } else if (inEscape) {
                sb.append(ch);
                if (ch == Constants.ANSI_END) inEscape = false;
            } else {
                double t = (double) colorIdx / Math.max(text.length() - 1, 1);
                int r = (int) (from.red() + t * (to.red() - from.red()));
                int g = (int) (from.green() + t * (to.green() - from.green()));
                int b = (int) (from.blue() + t * (to.blue() - from.blue()));

                sb.append(new RGBColor(r, g, b).ansiSequence()).append(ch);
                colorIdx++;
            }

            i++;
        }

        return sb.append(StyleCode.RESET).toString();
    }
}
