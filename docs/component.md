# Component

`Component` is the base interface for all renderable Clique components. Every built-in component implements this interface, giving them a consistent API for rendering and string retrieval.

Components that implement `Component`:
- [`Box`](box.md) - bordered text containers
- [`Divider`](divider.md) - horizontal line separators
- [`Frame`](frame.md) - layout containers that stack nested components
- [`Tree`](tree.md) - hierarchical data with connector lines
- [`ItemList`](item-list.md) - symbol-driven nested lists
- [`Table`](tables.md) - structured tabular data
- [`ProgressBar`](progress-bars.md) - visual progress tracking

## Methods

### `get()`

Builds and returns the fully formatted string representation of the component without printing it. Useful when you want to compose output, store it, or pass it elsewhere.

```java
String rendered = Clique.divider(40).get();
```

> Each call performs a full render — no caching is guaranteed by this interface. Individual implementations may document their own caching behavior.

### `render()`

Prints the component to `System.out`, followed by a newline. This is the most common way to display a component.

```java
Clique.box()
    .content("Hello, World!")
    .render();
```

### `render(PrintStream stream)`

Prints the component to a specific `PrintStream`. Use this to redirect output to `System.err`, a file stream, or any custom stream.

```java
Clique.box()
    .content("[red, bold]Something went wrong[/]")
    .render(System.err);
```

> Passing `null` as the stream throws a `NullPointerException`.

## Using `Component` as a Type

Since all Clique components share the `Component` interface, you can store and pass them around generically:

```java
Component warning = Clique.box()
    .dimensions(50, 5)
    .content("[yellow]Proceed with caution[/]");

warning.render();
```

This also makes it easy to build collections of components and render them together:

```java
List<Component> report = List.of(
    Clique.divider(60, "blue").title("Summary"),
    Clique.table(TableType.BOX_DRAW).headers("Key", "Value").row("Status", "OK"),
    Clique.divider(60, "blue")
);

report.forEach(Component::render);
```

## Implementing `Component`

You can implement `Component` directly for custom renderable output. Only `get()` is required — `render()` and `render(PrintStream)` are provided as defaults.

```java
Component clock = () -> {
    var now = java.time.LocalTime.now();
    return "[cyan]%02d:%02d:%02d[/]".formatted(now.getHour(), now.getMinute(), now.getSecond());
};

clock.render(); // prints current time to stdout
```

Custom components can also be nested inside a `Frame`, since `Frame.nest()` accepts any `Component`:

```java
Clique.frame()
    .nest(clock)
    .render(); // clock is re-evaluated on every render call
```

## Things to Watch Out For

- `get()` never returns `null`
- `render()` always appends a newline after the output
- `render(null)` throws a `NullPointerException`
- Each call to `get()` or `render()` is a full re-render — if your component does expensive work, handle caching yourself

## See Also

- [Markup Reference](markup-reference.md) - Styling options for component content
- [Parser Documentation](parser.md) - How markup parsing works