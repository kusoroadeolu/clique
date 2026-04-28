package io.github.kusoroadeolu.clique.components;

import io.github.kusoroadeolu.clique.internal.documentation.Stable;

import java.io.PrintStream;

import static java.util.Objects.requireNonNull;

/**
 * Base interface for all renderable components.
 *
 * <p>Implementations produce a fully formatted string via {@link #get()}, which is
 * then written to a {@link PrintStream} by the {@code render} methods. All built-in
 * components — such as {@link Frame} and {@link Tree} — implement this interface.
 *
 * <p>Example:
 * <pre>{@code
 * Component component = new Frame().nest("Hello");
 * component.render();               // prints to System.out
 * component.render(System.err);     // prints to a specific stream
 * String raw = component.get();     // retrieve without printing
 * }</pre>
 *
 * @since 2.0.0
 */
@Stable(since = "3.2.0")
@SuppressWarnings("java:S106")
public interface Component {

    /**
     * Builds and returns the fully formatted string representation of this component.
     *
     * <p>Each call performs a full render — no caching is implied by this interface.
     * Implementations may document their own caching behavior if applicable.
     *
     * @return the rendered content; never {@code null}
     */
    String get();

    /**
     * Prints the rendered component to {@link System#out}, followed by a newline.
     *
     * <p>Equivalent to {@code render(System.out)}.
     */
    default void render() {
        render(System.out);
    }

    /**
     * Prints the rendered component to the given {@link PrintStream}, followed by a newline.
     *
     * @param stream the stream to print to; must not be {@code null}
     * @throws NullPointerException if {@code stream} is {@code null}
     */
    default void render(PrintStream stream) {
        requireNonNull(stream, "Print stream cannot be null");
        stream.println(get());
    }
}