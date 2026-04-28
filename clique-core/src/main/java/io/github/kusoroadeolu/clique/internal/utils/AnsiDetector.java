package io.github.kusoroadeolu.clique.internal.utils;

import io.github.kusoroadeolu.clique.internal.documentation.InternalApi;

import static io.github.kusoroadeolu.clique.internal.utils.Constants.*;

@InternalApi(since = "3.2.0")
public class AnsiDetector {

    private AnsiDetector() {}

    private static volatile boolean ansiEnabled = autoDetect();

    public static void refresh() {
        ansiEnabled = autoDetect();
    }

    public static boolean ansiEnabled() {
        return ansiEnabled;
    }

    public static void enableCliqueColors() {
        System.setProperty(CLIQUE_COLOR, ALWAYS);
        ansiEnabled = true;
    }

    public static void disableCliqueColors() {
        System.setProperty(CLIQUE_COLOR, NEVER);
        ansiEnabled = false;
    }

    private static boolean autoDetect() {
        String noColor = System.getenv(NO_COLOR);
        if (noColor != null && !noColor.isEmpty()) return false;

        String cliqueColor = System.getProperty(CLIQUE_COLOR);
        if (ALWAYS.equals(cliqueColor)) return true;
        if (NEVER.equals(cliqueColor)) return false;

        String cliColorForce = System.getenv(CLI_COLOR_FORCE);
        if (cliColorForce != null && !cliColorForce.isEmpty() && !cliColorForce.equals("0")) return true;

        String forceColor = System.getenv(FORCE_COLOR);
        if (forceColor != null && !forceColor.isEmpty()) return true;

        String colorTerm = System.getenv(COLOR_TERM);
        if (colorTerm != null) return true;
        String os = System.getProperty(OS_NAME, EMPTY).toLowerCase();
        if (System.console() == null && !os.contains(WIN)) return false;

        final String term = System.getenv(TERM);
        if (term == null) {
            if (System.getenv(WT_SESSION) != null) return true;
            return os.contains(WIN);
        }

        return !term.equalsIgnoreCase(DUMB) && !term.equalsIgnoreCase(PLAIN);
    }



}
