package io.github.kusoroadeolu.clique.configuration;

import io.github.kusoroadeolu.clique.internal.documentation.InternalApi;
import io.github.kusoroadeolu.clique.internal.documentation.Stable;
import io.github.kusoroadeolu.clique.parser.MarkupParser;

import java.util.*;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

/**
 * Immutable configuration for a {@link io.github.kusoroadeolu.clique.components.ProgressBar}.
 *
 * <p>Instances are obtained via {@link #builder()} or {@link #fromPreset(ProgressBarPreset)}.
 * The {@link #DEFAULT} constant is equivalent to {@link ProgressBarPreset#BLOCKS}.
 *
 * The default values are:
 * <ul>
 *   <li>length: {@code 40}</li>
 *   <li>complete: {@code '█'}</li>
 *   <li>incomplete: {@code '░'}</li>
 *   <li>format: {@code ":bar :percent% [:elapsed/:remaining]"}</li>
 *   <li>parser: {@link MarkupParser#DEFAULT}</li>
 *   <li>easing: {@link EasingConfiguration#DEFAULT}</li>
 *   <li>styles: empty (no conditional formats)</li>
 * </ul>
 *
 * <p>The format string controls how the bar is rendered. The following tokens are supported:
 * <ul>
 *   <li>{@code :bar} — the filled/unfilled bar segment</li>
 *   <li>{@code :progress} — current tick, right-aligned to total width</li>
 *   <li>{@code :total} — the total tick count</li>
 *   <li>{@code :percent} — completion percentage, right-aligned to 3 characters</li>
 *   <li>{@code :elapsed} — elapsed time in {@code mm:ss}</li>
 *   <li>{@code :remaining} — estimated remaining time in {@code mm:ss}, or {@code --:--}</li>
 * </ul>
 *
 * <p>Per-range format overrides can be registered via
 * {@link ProgressBarConfigurationBuilder#styleWhen(Predicate, String)} or
 * {@link ProgressBarConfigurationBuilder#styleRange(int, int, String)}. When the current
 * completion percentage matches a registered predicate, its format is used in place of
 * the default. Predicates are evaluated in registration order; the first match wins.
 *
 * <p>This class is immutable and thread-safe. {@link ProgressBarConfigurationBuilder} is
 * <b>not</b> thread-safe; external synchronization is required if a builder instance
 * is shared across threads.
 *
 * <p>Example:
 * <pre>{@code
 * ProgressBarConfiguration config = ProgressBarConfiguration.builder()
 *     .length(50)
 *     .complete('=')
 *     .incomplete('-')
 *     .styleRange(0, 49, "[red]:bar :percent%[/]")
 *     .styleRange(50, 99, "[yellow]:bar :percent%[/]")
 *     .styleRange(100, 100, "[green]:bar :percent%[/]")
 *     .build();
 * }</pre>
 *
 * @since 3.0.0
 */
@Stable(since = "3.2.0")
public final class ProgressBarConfiguration {
    /**
     * A default {@code ProgressBarConfiguration} equivalent to {@link ProgressBarPreset#BLOCKS}.
     */
    public static final ProgressBarConfiguration DEFAULT = ProgressBarPreset.BLOCKS.getConfiguration();

    private final int length;
    private final char complete;
    private final char incomplete;
    private final String format;
    private final MarkupParser parser;
    private final List<ProgressBarPredicate> styles;
    private final EasingConfiguration easingConfiguration;
    private final int tickPerUnit;


    private ProgressBarConfiguration(ProgressBarConfigurationBuilder builder) {
        this.length = builder.length;
        this.complete = builder.complete;
        this.incomplete = builder.incomplete;
        this.format = builder.format;
        this.parser = builder.parser;
        this.styles = Collections.unmodifiableList(builder.styles);
        this.easingConfiguration = builder.easing;
        this.tickPerUnit = builder.tickPerUnit;
    }

    /**
     * Returns a new builder for constructing a {@code ProgressBarConfiguration}.
     *
     * @return a new {@link ProgressBarConfigurationBuilder}
     */
    public static ProgressBarConfigurationBuilder builder() {
        return new ProgressBarConfigurationBuilder();
    }

    /**
     * Returns a builder pre-populated with the values of the given preset.
     *
     * <p>The returned builder can be further customized before calling
     * {@link ProgressBarConfigurationBuilder#build()}. The preset's easing and
     * style predicates are <b>not</b> copied; only {@code length}, {@code complete},
     * {@code incomplete}, and {@code format} are transferred.
     *
     * @param preset the preset to derive initial values from; must not be {@code null}
     * @return a new {@link ProgressBarConfigurationBuilder} seeded from the preset
     * @throws NullPointerException if {@code preset} is {@code null}
     * @since 3.2.1
     */
    @Stable(since = "3.2.1")
    public static ProgressBarConfigurationBuilder fromPreset(ProgressBarPreset preset) {
        Objects.requireNonNull(preset, "Preset cannot be null");
        var config = preset.getConfiguration();
        return ProgressBarConfiguration
                .builder()
                .length(config.length)
                .complete(config.complete)
                .incomplete(config.incomplete)
                .format(config.format);
    }

    @InternalApi(since = "3.2.1")
    public static ProgressBarConfiguration fromEasing(EasingConfiguration easing) {
        Objects.requireNonNull(easing, "Easing config cannot be null");
        return ProgressBarConfiguration
                .builder()
                .easing(easing)
                .build();
    }

    // Get the format based on current percent
    @InternalApi(since = "3.2.1")
    public String getFormatForPercent(int percent) {
        return styles.stream()
                .filter(style -> style.matches(percent))
                .findFirst()
                .map(ProgressBarPredicate::format)
                .orElse(format);  // Fall back to default format
    }

    /**
     * Returns the markup parser used to interpret inline style tags in the rendered output.
     *
     * @return the parser; never {@code null}
     */
    public MarkupParser getParser() {
        return parser;
    }

    /**
     * Returns the total character length of the bar segment rendered for {@code :bar}.
     *
     * @return the bar length; always {@code >= 0}
     */
    public int getLength() {
        return length;
    }

    /**
     * Returns the character used to represent completed progress in the bar segment.
     *
     * @return the completion character
     */
    public char getComplete() {
        return complete;
    }

    /**
     * Returns the character used to represent remaining progress in the bar segment.
     *
     * @return the incomplete character
     */
    public char getIncomplete() {
        return incomplete;
    }

    /**
     * Returns the default format string used when no style predicate matches.
     *
     * <p>See the class-level documentation for the list of supported tokens.
     *
     * @return the format string; never {@code null}
     */
    public String getFormat() {
        return format;
    }

    /**
     * Returns the easing configuration applied during animated ticks.
     *
     * @return the easing configuration; never {@code null}
     */
    public EasingConfiguration getEasing() {
        return easingConfiguration;
    }

    public int tickPerUnit() {
        return tickPerUnit;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;

        ProgressBarConfiguration that = (ProgressBarConfiguration) object;
        return length == that.length && complete == that.complete && incomplete == that.incomplete && Objects.equals(format, that.format) && Objects.equals(parser, that.parser) && styles.equals(that.styles) && Objects.equals(easingConfiguration, that.easingConfiguration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(length, complete, incomplete, format, parser, styles, easingConfiguration);
    }

    @Override
    public String toString() {
        return "ProgressBarConfiguration[" +
                "height=" + length +
                ", complete=" + complete +
                ", incomplete=" + incomplete +
                ", format='" + format + '\'' +
                ", parser=" + parser +
                ", styles=" + styles +
                ", easingConfiguration=" + easingConfiguration +
                ']';
    }

    /**
     * Builder for {@link ProgressBarConfiguration}.
     *
     * <p>This builder is <b>not</b> thread-safe.
     */
    public static class ProgressBarConfigurationBuilder {
        private int length = 40;
        private char complete = '█';
        private char incomplete = '░';
        private int tickPerUnit = 1;
        private String format = ":bar :percent% [:elapsed/:remaining]";
        private MarkupParser parser = MarkupParser.DEFAULT;
        private final List<ProgressBarPredicate> styles = new ArrayList<>();
        private EasingConfiguration easing = EasingConfiguration.DEFAULT;
        private static final String FORMAT_ERR_MESSAGE = "Format cannot be null";

        public ProgressBarConfigurationBuilder length(int length) {
            if (length < 0) throw new IllegalArgumentException("Length must be positive");
            this.length = length;
            return this;
        }

        public ProgressBarConfigurationBuilder complete(char complete) {
            this.complete = complete;
            return this;
        }

        public ProgressBarConfigurationBuilder tickPerUnit(int tickPerUnit) {
            if (tickPerUnit <= 0) throw new IllegalArgumentException("Tick per unit cannot be less than 0");
            this.tickPerUnit = tickPerUnit;
            return this;
        }

        public ProgressBarConfigurationBuilder incomplete(char incomplete) {
            this.incomplete = incomplete;
            return this;
        }

        public ProgressBarConfigurationBuilder format(String format) {
            requireNonNull(format, FORMAT_ERR_MESSAGE);
            this.format = format;
            return this;
        }

        public ProgressBarConfigurationBuilder parser(MarkupParser parser) {
            this.parser = parser;
            return this;
        }

        public ProgressBarConfigurationBuilder styleWhen(Predicate<Integer> condition, String format) {
            requireNonNull(format, FORMAT_ERR_MESSAGE);
            requireNonNull(condition, "Condition cannot be null");
            this.styles.add(new ProgressBarPredicate(condition, format));
            return this;
        }

        public ProgressBarConfigurationBuilder styleRange(int min, int max, String format) {
            requireNonNull(format, FORMAT_ERR_MESSAGE);
            if (min < 0) throw new IllegalArgumentException("Min must be positive");
            return this.styleWhen(p -> p >= min && p <= max, format);
        }

        public ProgressBarConfigurationBuilder styles(Collection<ProgressBarPredicate> formats) {
            requireNonNull(formats, "Format styles cannot be null");
            this.styles.addAll(formats);
            return this;
        }

        public ProgressBarConfigurationBuilder easing(EasingConfiguration easing) {
            requireNonNull(easing, "Easing configuration cannot be null");
            this.easing = easing;
            return this;
        }

        public ProgressBarConfiguration build() {
            return new ProgressBarConfiguration(this);
        }
    }
}