package io.github.kusoroadeolu.clique.internal.utils;

import io.github.kusoroadeolu.clique.configuration.CellAlign;
import io.github.kusoroadeolu.clique.internal.WidthAwareList;
import io.github.kusoroadeolu.clique.internal.documentation.InternalApi;

import java.util.List;
import java.util.Map;

import static io.github.kusoroadeolu.clique.internal.utils.Constants.BLANK;

@InternalApi(since = "3.2.0")
public class TableUtils {
    private TableUtils(){}

    public static String align(CellAlign align, StringBuilder sb, int offset, String styled, String vLine) {

        final String spaces = BLANK.repeat(offset);
        return switch (align) {
            case LEFT -> sb.append(styled).append(spaces).append(vLine).toString();
            case RIGHT -> sb.append(spaces).append(styled).append(vLine).toString();
            case CENTER -> {
                final int len = spaces.length(); //Get the height of the spaces
                final int rem = len % 2;
                final int leftOffset = (len - rem) - (len / 2);
                final int rightOffset = len - leftOffset;
                yield sb.append(BLANK.repeat(leftOffset)).append(styled).append(BLANK.repeat(rightOffset)).append(vLine).toString();
            }
        };
    }

    public static String handleNulls(String val, String nullReplacement) {
        if (val != null) return val;
        return nullReplacement;
    }

    public static void validateRowIndex(int rowIdx, List<WidthAwareList> rows) {
        if (rowIdx > (rows.size() - 1)) throw new IllegalArgumentException("Row: " + rowIdx + "does not exist");
    }

    public static void validateHeaders(int idx) {
        if (idx == 0) throw new IllegalArgumentException("Cannot remove a header from a table");
    }

    public static void validateColumnIndex(int colIdx, List<WidthAwareList> cols) {
        if (colIdx > (cols.size() - 1)) throw new IllegalArgumentException("Column: " + colIdx + "does not exist");
    }

    public static CellAlign chooseColAlignment(int colIdx, CellAlign defAlign, Map<Integer, CellAlign> cAlign) {
        return cAlign.get(colIdx) == null ? defAlign : cAlign.get(colIdx);
    }


}
