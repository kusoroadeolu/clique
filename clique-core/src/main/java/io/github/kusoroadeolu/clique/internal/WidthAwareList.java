package io.github.kusoroadeolu.clique.internal;

import io.github.kusoroadeolu.clique.internal.documentation.InternalApi;

import java.util.ArrayList;
import java.util.List;

@InternalApi(since = "3.2.0")
public class WidthAwareList {
    private final List<Cell> list;
    private int longest;

    public WidthAwareList() {
        this(new ArrayList<>());
    }

    public WidthAwareList(List<Cell> list) {
        this.list = list;
        if (list.isEmpty()) longest = 0;
        else longest = calculateLongest();
    }


    public void add(Cell c) {
        this.updateLongest(c);
        this.list.add(c);
    }

    public void update(int i, Cell c) {
        this.updateLongest(c);
        this.list.set(i, c);
    }


    public void updateLongest(Cell c) {
        final int len = c.width();
        if (len > this.longest) {
            this.longest = len;
        }
    }

    public void remove(int index) {
        Cell c = this.list.get(index);
        this.list.remove(index);

        if (this.list.isEmpty()) this.longest = 0;
        else if (c.width() == this.longest) this.longest = calculateLongest();
    }


    //Gets the styled text from the table
    public String getStyledText(int pos) {
        return this.list.get(pos).styledText();
    }

    public Cell get(int pos) {
        return this.list.get(pos);
    }


    public int longest() {
        return this.longest;
    }

    //Get the styled text from the list
    public List<String> list() {
        return this.list.stream()
                .map(Cell::styledText)
                .toList();
    }

    public List<Cell> cells() {
        return new ArrayList<>(list);
    }

    public int size() {
        return this.list.size();
    }

    private int calculateLongest() {
        return this.list.stream()
                .mapToInt(Cell::width)
                .max()
                .orElse(0);
    }

}
