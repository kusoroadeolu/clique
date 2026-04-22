package io.github.kusoroadeolu.clique.components;

import io.github.kusoroadeolu.clique.configuration.DividerConfiguration;
import io.github.kusoroadeolu.clique.internal.documentation.Stable;
import io.github.kusoroadeolu.clique.internal.utils.StringUtils;
import io.github.kusoroadeolu.clique.style.StyleBuilder;

import java.util.Objects;

import static io.github.kusoroadeolu.clique.internal.Constants.BLANK;

/**
 * A horizontal divider line that can optionally display a title.
 * <p>
 * Divider renders a horizontal line of configurable width. When a title is
 * set via {@link #title(String)}, the title is centered within the divider line.
 */
@Stable(since = "4.0.2")
public class Divider implements Component {

	private final DividerConfiguration configuration;

	private final int width;

	private String title;

	/**
	 * Creates a new Divider with the given width and configuration.
	 *
	 * @param width         the width of the divider; must be greater than 0
	 * @param configuration the divider configuration to use; must not be {@code null}
	 * @throws IllegalArgumentException if {@code width} is not greater than 0
	 * @throws NullPointerException     if {@code configuration} is {@code null}
	 */
	public Divider(int width, DividerConfiguration configuration) {
		validateWidth(width);
		this.width = width;
		this.configuration = Objects.requireNonNull(configuration, "Divider configuration cannot be null");
	}

	private static void validateWidth(int width) {
		if (width <= 0) {
			throw new IllegalArgumentException("Width must be greater than 0.");
		}
	}

	@Override
	public String get() {
		String dividerChar = String.valueOf(configuration.getDividerChar());

		if (title == null || title.isEmpty()) {
			return new StyleBuilder()
					.appendAndReset(dividerChar.repeat(width), configuration.getDividerColor())
					.toString();
		}

		int contentLength = configuration.getParser().getOriginalString(title).length() + 2; // add 2 from padding
		String content = BLANK + StringUtils.parse(title, configuration.getParser()) + BLANK;


		int remaining = width - contentLength;
		int left = remaining / 2;
		int right = remaining - left;

		return new StyleBuilder()
                .appendAndReset(dividerChar.repeat(left), configuration.getDividerColor())
				.appendAndReset(content)
				.appendAndReset(dividerChar.repeat(right), configuration.getDividerColor())
				.toString();
	}

	/**
	 * Sets the title to display centered within the divider line.
	 * <p>
	 *
	 * @param title the title to display; may be {@code null} to render a plain line
     *
     * @throws IllegalArgumentException if the visible title length exceeds or is equal to the divider width
	 * @return this divider instance
	 */
	public Divider title(String title) {
        String visible = configuration.getParser().getOriginalString(title);
        if (visible.length() >= width) {
            throw new IllegalArgumentException(
                    "Title's visible length must be less than divider width."
            );
        }
        this.title = title;
        return this;
	}

	@Override
	public boolean equals(Object object) {
		if (object == null || getClass() != object.getClass()) return false;

		Divider that = (Divider) object;
		return width == that.width && Objects.equals(configuration, that.configuration) && Objects.equals(title, that.title);
	}

	@Override
	public int hashCode() {
		return Objects.hash(width, configuration, title);
	}

	@Override
	public String toString() {
		return "Divider[" +
			   "configuration=" + configuration +
			   ", title='" + title + '\'' +
			   ", width=" + width +
			   ']';
	}
}
