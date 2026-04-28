package io.github.kusoroadeolu.clique.internal.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CharWidthTest {

    @ParameterizedTest
    @CsvSource({
            "😀, 2",       // Basic SMILING FACE (Supplementary Plane)
            "©, 1",       // Copyright (BMP, default narrow)
            "©️, 2",      // Copyright + VS16 (Emoji presentation)
            "⚠️, 2",      // Warning + VS16
            "1, 1",       // Standard Digit
            "1️⃣, 2",     // Keycap Sequence (Digit + VS16 + Keycap)
            "A, 1",       // Latin Letter
            "你好, 4"     // CJK Characters (2x2)
    })
    void testBasicClusters(String input, int expectedWidth) {
        assertEquals(expectedWidth, CharWidth.of(input),
                "Failed for sequence: " + input);
    }

    @Test
    void testComplexZwjSequences() {
        // People with Bunny Ears: Skin Tone 1 + ZWJ + Bunny + ZWJ + Skin Tone 2
        String bunnyEars = "\uD83E\uDDD1\uD83C\uDFFF\u200D\uD83D\uDC30\u200D\uD83E\uDDD1\uD83C\uDFFB";
        assertEquals(2, CharWidth.of(bunnyEars), "Multi-tone ZWJ sequence should be width 2");

        // Family: Man + Woman + Girl + Boy with ZWJ links
        String family = "👨‍👩‍👧‍👦";
        assertEquals(2, CharWidth.of(family), "Standard family ZWJ should be width 2");
    }

    @Test
    void testRegionalIndicators() {
        // US (U+1F1FA U+1F1F8)
        String usa = "\uD83C\uDDFA\uD83C\uDDF8";
        assertEquals(2, CharWidth.of(usa), "Country flag (RI pair) should be width 2");
    }

    @Test
    void testSkinTones() {
        // 👍 (Width 2) + 🏽 (Modifier)
        String thumbsUpMedium = "👍🏽";
        assertEquals(2, CharWidth.of(thumbsUpMedium), "Emoji with skin tone should be width 2");

        // Person Bouncing Ball (26F9) + Skin Tone
        // This is a BMP character that becomes wide only with a modifier
        String bouncingBallDark = "⛹🏿";
        assertEquals(2, CharWidth.of(bouncingBallDark), "BMP Emoji with modifier should be width 2");
    }

    @Test
    void testMixedString() {
        String mixed = "Status: 🟢 Online";
        // "Status: " (8) + "🟢" (2) + " Online" (7) = 17
        assertEquals(17, CharWidth.of(mixed));
    }

    @Test
    void testZeroWidth() {
        // Test that ZWJ alone doesn't add width (if your ofCodePoint handles it)
        assertEquals(0, CharWidth.of("\u200D"), "Isolated ZWJ should be width 0");

        // Soft Hyphen
        assertEquals(0, CharWidth.of("\u00AD"), "Soft hyphen should be width 0");
    }
}