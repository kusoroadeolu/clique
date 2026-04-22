package io.github.kusoroadeolu.clique.components;

import io.github.kusoroadeolu.clique.configuration.DividerConfiguration;
import io.github.kusoroadeolu.clique.internal.utils.StringUtils;
import io.github.kusoroadeolu.clique.style.StyleBuilder;

import java.util.Objects;

/**
 * A horizontal divider line that can optionally display a title.
 * <p>
 * Divider renders a horizontal line of configurable width. When a title is
 * set via {@link #title(String)}, the title is centered within the divider line.
 * If the title exceeds the divider width, it is truncated with an ellipsis
 * character.
 */
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
		String dividerChar = String.valueOf(configuration.dividerChar());

		if (title == null || title.isEmpty()) {
			return new StyleBuilder()
					.appendAndReset(dividerChar.repeat(width), configuration.dividerColor())
					.toString();
		}

		if (title.length() >= width) {
			String truncated = (width <= 1)
					? title.substring(0, width)
					: title.substring(0, width - 1) + "…";

			return new StyleBuilder()
					.appendAndReset(truncated, configuration.dividerColor())
					.toString();
		}

		int contentLength = configuration.parser().getOriginalString(title).length() + 2; // add 2 from padding
		String content = " " + StringUtils.parse(title, configuration.parser()) + " ";


		int remaining = width - contentLength;
		int left = remaining / 2;
		int right = remaining - left;

		return new StyleBuilder().appendAndReset(dividerChar.repeat(left), configuration.dividerColor())
				.append(content)
				.appendAndReset(dividerChar.repeat(right), configuration.dividerColor())
				.toString();
	}

	/**
	 * Sets the title to display centered within the divider line.
	 * <p>
	 *
	 * @param title the title to display; may be {@code null} to render a plain line
	 * @return this divider instance
	 */
	public Divider title(String title) {
		this.title = title;
		return this;
	}

	@Override
	public boolean equals(Object object) {
		if (object == null || getClass() != object.getClass()) return false;
		if (!super.equals(object)) return false;

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
