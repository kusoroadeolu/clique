/*
 * Copyright Kusoro Victor
 * Portions Copyright TamboUI Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.kusoroadeolu.clique.internal.utils;

import io.github.kusoroadeolu.clique.internal.documentation.InternalApi;

import java.util.Arrays;

@InternalApi(since = "3.2.0")
public final class CharWidth {

    // Pre-computed width lookup for BMP characters (0x0000-0xFFFF).
    // Each byte stores the display width (0, 1, or 2) for that code point.
    private static final byte[] BMP_WIDTHS = new byte[0x10000];
    // Sorted start values of supplementary plane wide ranges
    private static final int[] SUPPLEMENTARY_WIDE_STARTS = {
            0x1F000, 0x1F100, 0x1F200, 0x1F300, 0x1F600, 0x1F680,
            0x1F7E0, 0x1F900, 0x1FA00, 0x1FA70, 0x20000
    };
    private static final int[] SUPPLEMENTARY_WIDE_ENDS = {
            0x1F0FF, 0x1F1FF, 0x1F2FF, 0x1F5FF, 0x1F67F, 0x1F6FF,
            0x1F7FF, 0x1F9FF, 0x1FA6F, 0x1FAFF, 0x2FA1F
    };

    // Sorted start/end values of supplementary plane zero-width ranges
    private static final int[] SUPPLEMENTARY_ZERO_STARTS = {
            0x1F3FB, // Emoji Modifier Fitzpatrick Type-1-2 through Type-6 (skin tones)
            0xE0001, // Tags
            0xE0100 // Variation Selectors Supplement (VS17-VS256)
    };
    private static final int[] SUPPLEMENTARY_ZERO_ENDS = {
            0x1F3FF, 0xE007F, 0xE01EF
    };

    static {
        // Initialize all BMP code points to width 1 (default)
        Arrays.fill(BMP_WIDTHS, (byte) 1);

        // Mark zero-width BMP ranges
        int[][] zeroWidthRanges = {
                {0x00AD, 0x00AD},   // Soft hyphen
                {0x0300, 0x036F},   // Combining Diacritical Marks
                {0x0483, 0x0489},   // Cyrillic combining marks
                {0x0591, 0x05BD},   // Hebrew combining marks
                {0x05BF, 0x05BF},
                {0x05C1, 0x05C2},
                {0x05C4, 0x05C5},
                {0x05C7, 0x05C7},
                {0x0610, 0x061A},   // Arabic combining marks
                {0x064B, 0x065F},
                {0x0670, 0x0670},
                {0x06D6, 0x06DC},
                {0x06DF, 0x06E4},
                {0x06E7, 0x06E8},
                {0x06EA, 0x06ED},
                {0x0711, 0x0711},   // Syriac
                {0x0730, 0x074A},
                {0x0900, 0x0902},   // Devanagari combining marks
                {0x093A, 0x093A},
                {0x093C, 0x093C},
                {0x0941, 0x0948},
                {0x094D, 0x094D},
                {0x0951, 0x0957},
                {0x0962, 0x0963},
                {0x0981, 0x0981},   // Bengali combining marks
                {0x09BC, 0x09BC},
                {0x09C1, 0x09C4},
                {0x09CD, 0x09CD},
                {0x09E2, 0x09E3},
                {0x0A01, 0x0A02},   // Gurmukhi combining marks
                {0x0A3C, 0x0A3C},
                {0x0A41, 0x0A42},
                {0x0A47, 0x0A48},
                {0x0A4B, 0x0A4D},
                {0x0A51, 0x0A51},
                {0x0A70, 0x0A71},
                {0x0A75, 0x0A75},
                {0x0E31, 0x0E31},   // Thai combining marks
                {0x0E34, 0x0E3A},
                {0x0E47, 0x0E4E},
                {0x1AB0, 0x1AFF},   // Combining Diacritical Marks Extended
                {0x1DC0, 0x1DFF},   // Combining Diacritical Marks Supplement
                {0x200B, 0x200F},   // Zero-width space, ZWNJ, ZWJ, directional marks
                {0x2028, 0x202F},   // Line/paragraph separators, directional formatting
                {0x2060, 0x2064},   // Word joiner, invisible operators
                {0x2066, 0x206F},   // Directional isolates and formatting
                {0x20D0, 0x20FF},   // Combining Diacritical Marks for Symbols
                {0xFE00, 0xFE0F},   // Variation Selectors (VS1-VS16)
                {0xFE20, 0xFE2F},   // Combining Half Marks
                {0xFEFF, 0xFEFF}   // Zero-width no-break space (BOM)
        };
        for (int[] range : zeroWidthRanges) {
            for (int cp = range[0]; cp <= range[1]; cp++) {
                BMP_WIDTHS[cp] = 0;
            }
        }

        // Mark wide (width 2) BMP ranges
        // For U+2000-U+2BFF: only characters with East_Asian_Width=W or
        // Emoji_Presentation property (rendered as 2-wide by terminals).
        int[][] wideRanges = {
                // CJK and legacy wide blocks
                {0x2E80, 0x2FDF}, {0x2FF0, 0x303E}, {0x3041, 0x33FF}, {0x3400, 0x4DBF},
                {0x4E00, 0x9FFF}, {0xA000, 0xA4CF}, {0xAC00, 0xD7AF}, {0xF900, 0xFAFF},
                {0xFF01, 0xFF60}, {0xFFE0, 0xFFE6},
                // Emoji with Emoji_Presentation=Yes (Always Wide)
                {0x231A, 0x231B}, {0x23E9, 0x23EC}, {0x23F0, 0x23F0}, {0x23F3, 0x23F3},
                {0x25FD, 0x25FE}, {0x2614, 0x2615}, {0x2648, 0x2653}, {0x267F, 0x267F},
                {0x2693, 0x2693}, {0x26A1, 0x26A1}, {0x26AA, 0x26AB}, {0x26BD, 0x26BE},
                {0x26C4, 0x26C5}, {0x26CE, 0x26CE}, {0x26D4, 0x26D4}, {0x26EA, 0x26EA},
                {0x26F2, 0x26F3}, {0x26F5, 0x26F5}, {0x26FA, 0x26FA}, {0x26FD, 0x26FD},
                {0x2705, 0x2705}, {0x270A, 0x270B}, {0x2728, 0x2728}, {0x274C, 0x274C},
                {0x274E, 0x274E}, {0x2753, 0x2755}, {0x2757, 0x2757}, {0x2795, 0x2797},
                {0x27B0, 0x27B0}, {0x27BF, 0x27BF}, {0x2B1B, 0x2B1C}, {0x2B50, 0x2B50},
                {0x2B55, 0x2B55}
        };

        for (int[] range : wideRanges) {
            for (int cp = range[0]; cp <= range[1]; cp++) {
                BMP_WIDTHS[cp] = 2;
            }
        }
    }

    private CharWidth() {
    }

    /**
     * Returns the display width (0, 1, or 2) of a Unicode code point.
     *
     * @param codePoint the Unicode code point
     * @return 0 for zero-width characters, 2 for wide characters, 1 otherwise
     */
    public static int ofCodePoint(int codePoint) {
        if (codePoint < 0x10000) {
            return BMP_WIDTHS[codePoint];
        }

        // Supplementary plane: check zero-width first (skin tones overlap with emoji ranges)
        if (inRanges(codePoint, SUPPLEMENTARY_ZERO_STARTS, SUPPLEMENTARY_ZERO_ENDS)) {
            return 0;
        }

        if (inRanges(codePoint, SUPPLEMENTARY_WIDE_STARTS, SUPPLEMENTARY_WIDE_ENDS)) {
            return 2;
        }

        return 1;
    }

    /**
     * Returns the total display width of a string.
     * <p>
     * Handles grapheme clusters correctly:
     * <ul>
     *   <li>ZWJ sequences (e.g., 👨‍👦): width 2 for the combined glyph</li>
     *   <li>Regional Indicator pairs (flags, e.g., 🇫🇷): width 2</li>
     *   <li>Skin tone modifiers: zero-width (added to base emoji)</li>
     * </ul>
     *
     * @param s the string to measure
     * @return the total display width in terminal columns
     */
    public static int of(String s) {
        if (s == null || s.isEmpty()) return 0;
        int width = 0;
        int i = 0;
        int n = s.length();

        while (i < n) {
            int codePoint = s.codePointAt(i);
            int charCount = Character.charCount(codePoint);
            int nextIdx = i + charCount;

            // Regional Indicator pair (country flags)
            if (isRegionalIndicator(codePoint)) {
                if (nextIdx < n && isRegionalIndicator(s.codePointAt(nextIdx))) {
                    width += 2;
                    i = nextIdx + Character.charCount(s.codePointAt(nextIdx));
                } else {
                    width += 1;
                    i = nextIdx;
                }
                continue;
            }

            // Modifier/ZWJ cluster
            int lookupIdx = nextIdx;
            boolean hasCluster = false;

            while (lookupIdx < n) {
                int m = s.codePointAt(lookupIdx);
                if (isModifier(m)) {
                    lookupIdx += Character.charCount(m);
                    hasCluster = true;
                } else if (m == 0x200D) {
                    lookupIdx += Character.charCount(m);
                    if (lookupIdx < n) {
                        int joined = s.codePointAt(lookupIdx);
                        lookupIdx += Character.charCount(joined);
                        // skip modifiers on joined component
                        while (lookupIdx < n && isModifier(s.codePointAt(lookupIdx))) {
                            lookupIdx += Character.charCount(s.codePointAt(lookupIdx));
                        }
                    }
                    hasCluster = true;
                } else {
                    break;
                }
            }

            width += hasCluster ? 2 : ofCodePoint(codePoint);
            i = hasCluster ? lookupIdx : nextIdx;
        }

        return width;
    }

    private static boolean isModifier(int cp) {
        return cp == 0xFE0F || (cp >= 0x1F3FB && cp <= 0x1F3FF);
    }

    private static boolean isTagCharacter(int cp) {
        return cp >= 0xE0020 && cp <= 0xE007F;
    }

    /**
     * Returns true if the code point is a Regional Indicator symbol (U+1F1E6-U+1F1FF).
     * Regional Indicator pairs form flag emoji.
     */
    private static boolean isRegionalIndicator(int codePoint) {
        return codePoint >= 0x1F1E6 && codePoint <= 0x1F1FF;
    }


    private static boolean inRanges(int codePoint, int[] starts, int[] ends) {
        int idx = Arrays.binarySearch(starts, codePoint);
        if (idx >= 0) {
            // Exact match on a start value - it's within the range
            return true;
        }
        // Insertion point: the index of the first element greater than codePoint
        int insertionPoint = -(idx + 1);
        // Check if codePoint falls within the preceding range
        return insertionPoint > 0 && codePoint <= ends[insertionPoint - 1];
    }
}