package io.github.kusoroadeolu.clique.internal.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;

import static io.github.kusoroadeolu.clique.internal.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SystemStubsExtension.class)
class AnsiDetectorTest {

    @SystemStub
    private EnvironmentVariables envVars;

    @BeforeEach
    void clearProperties() {
        System.clearProperty(CLIQUE_COLOR);
        envVars.remove(COLOR_TERM);
    }

    @Test
    void noColor_nonEmpty_disablesAnsi() {
        envVars.set(NO_COLOR, "1");
        AnsiDetector.refresh();
        assertFalse(AnsiDetector.ansiEnabled());
    }

    @Test
    void noColor_empty_doesNotDisableAnsi() {
        envVars.set(NO_COLOR, "");
        envVars.set(COLOR_TERM, "truecolor");
        AnsiDetector.refresh();
        assertTrue(AnsiDetector.ansiEnabled());
    }


    @Test
    void cliColorForce_set_enablesAnsi() {
        envVars.set(CLI_COLOR_FORCE, "1");
        AnsiDetector.refresh();
        assertTrue(AnsiDetector.ansiEnabled());
    }


    @Test
    void colorterm_set_enablesAnsi() {
        envVars.set(COLOR_TERM, "truecolor");
        AnsiDetector.refresh();
        assertTrue(AnsiDetector.ansiEnabled());
    }


    @Test
    void term_dumb_disablesAnsi() {
        envVars.set(TERM, "dumb");
        AnsiDetector.refresh();
        assertFalse(AnsiDetector.ansiEnabled());
    }

    @Test
    void term_plain_disablesAnsi() {
        envVars.set(TERM, "plain");
        AnsiDetector.refresh();
        assertFalse(AnsiDetector.ansiEnabled());
    }

    @Test
    void enableCliqueColors_updatesCache() {
        AnsiDetector.disableCliqueColors();
        assertFalse(AnsiDetector.ansiEnabled());
        AnsiDetector.enableCliqueColors();
        assertTrue(AnsiDetector.ansiEnabled());
    }

    @Test
    void disableCliqueColors_updatesCache() {
        AnsiDetector.enableCliqueColors();
        assertTrue(AnsiDetector.ansiEnabled());
        AnsiDetector.disableCliqueColors();
        assertFalse(AnsiDetector.ansiEnabled());
    }

}