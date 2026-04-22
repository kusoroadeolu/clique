package io.github.kusoroadeolu.clique;

import io.github.kusoroadeolu.clique.components.*;
import io.github.kusoroadeolu.clique.configuration.*;
import io.github.kusoroadeolu.clique.internal.TableFactory;
import io.github.kusoroadeolu.clique.spi.AnsiCode;
import io.github.kusoroadeolu.clique.style.ColorCode;

import java.util.Collection;

/**
 * Sub-facade for all rendering components: boxes, tables, frames, trees, indenters, and progress bars.
 */
final class CliqueComponents {

    private CliqueComponents() {
        throw new AssertionError();
    }

    // TABLE

    public static PendingTable table() {
        return TableFactory.getTableBuilder(TableType.BOX_DRAW);
    }

    public static PendingTable table(TableConfiguration configuration) {
        return TableFactory.getTableBuilder(TableType.BOX_DRAW, configuration);
    }

    public static PendingTable table(TableType type, TableConfiguration configuration) {
        return TableFactory.getTableBuilder(type, configuration);
    }

    public static PendingTable table(TableType type) {
        return TableFactory.getTableBuilder(type);
    }

    public static PendingTable table(String borderColor) {
        return table(TableConfiguration.builder().borderColor(borderColor).build());
    }

    public static PendingTable table(TableType type, String borderColor) {
        return table(type, TableConfiguration.builder().borderColor(borderColor).build());
    }

    public static PendingTable table(AnsiCode... borderColor) {
        return table(TableConfiguration.builder().borderColor(borderColor).build());
    }

    public static PendingTable table(TableType type, AnsiCode... borderColor) {
        return table(type, TableConfiguration.builder().borderColor(borderColor).build());
    }

    // BOX
    public static Box box() {
        return new Box();
    }

    public static Box box(BoxConfiguration configuration) {
        return new Box(configuration);
    }

    public static Box box(BoxType type, BoxConfiguration configuration) {
        return new Box(type, configuration);
    }

    public static Box box(BoxType type) {
        return new Box(type);
    }

    public static Box box(AnsiCode... borderColor) {
        return new Box(BoxType.ROUNDED, BoxConfiguration.builder().borderColor(borderColor).build());
    }

    public static Box box(BoxType type, AnsiCode... borderColor) {
        return new Box(type, BoxConfiguration.builder().borderColor(borderColor).build());
    }

    public static Box box(String borderColor) {
        return new Box(BoxType.ROUNDED, BoxConfiguration.builder().borderColor(borderColor).build());
    }

    public static Box box(BoxType type, String borderColor) {
        return new Box(type, BoxConfiguration.builder().borderColor(borderColor).build());
    }

    // DIVIDER
    public static Divider divider(int width) {
        return new Divider(width, DividerConfiguration.builder().build());
    }

    public static Divider divider(int width, String color) {
        return new Divider(width, DividerConfiguration.builder()
                .dividerColor(color)
                .build());
    }

    public static Divider divider(int width, ColorCode color) {
        return new Divider(width, DividerConfiguration.builder()
                .dividerColor(color)
                .build());
    }

    public static Divider divider(int width, DividerConfiguration config) {
        return new Divider(width, config);
    }

    // INDENTER
    public static ItemList list() {
        return new ItemList();
    }

    public static ItemList list(ItemListConfiguration itemListConfiguration) {
        return new ItemList(itemListConfiguration);
    }


    // PROGRESS BAR

    public static ProgressBar progressBar(int total) {
        return new ProgressBar(total);
    }

    public static ProgressBar progressBar(int total, ProgressBarConfiguration configuration) {
        return new ProgressBar(total, configuration);
    }

    public static ProgressBar progressBar(int total, ProgressBarPreset preset) {
        return new ProgressBar(total, preset.getConfiguration());
    }

    public static ProgressBar progressBar(int total, EasingConfiguration easing) {
        return new ProgressBar(total, ProgressBarConfiguration.fromEasing(easing));
    }

    public static <T>IterableProgressBar<T> progressBar(Collection<T> collection) {
        return new IterableProgressBar<>(collection);
    }

    public static <T>IterableProgressBar<T> progressBar(Collection<T> collection, ProgressBarConfiguration configuration) {
        return new IterableProgressBar<>(collection, configuration);
    }

    // FRAME
    public static Frame frame() {
        return new Frame();
    }

    public static Frame frame(FrameConfiguration configuration) {
        return new Frame(configuration);
    }

    public static Frame frame(BoxType type) {
        return new Frame(type);
    }

    public static Frame frame(BoxType type, FrameConfiguration configuration) {
        return new Frame(configuration, type);
    }

    public static Frame frame(String borderColor) {
        return new Frame(FrameConfiguration.builder().borderColor(borderColor).build());
    }

    public static Frame frame(BoxType type, String borderColor) {
        return new Frame(FrameConfiguration.builder().borderColor(borderColor).build(), type);
    }

    public static Frame frame(AnsiCode... borderColor) {
        return new Frame(FrameConfiguration.builder().borderColor(borderColor).build());
    }

    public static Frame frame(BoxType type, AnsiCode... borderColor) {
        return new Frame(FrameConfiguration.builder().borderColor(borderColor).build(), type);
    }


    // TREE
    public static Tree tree(String label) {
        return new Tree(label);
    }

    public static Tree tree(String label, TreeConfiguration configuration) {
        return new Tree(label, configuration);
    }

    public static Tree tree(String label, String connectorColor) {
        return new Tree(label, TreeConfiguration.builder().connectorColor(connectorColor).build());
    }

    public static Tree tree(String label, AnsiCode... connectorColor) {
        return new Tree(label, TreeConfiguration.builder().connectorColor(connectorColor).build());
    }

}