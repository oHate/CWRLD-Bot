package com.customwrld.bot.util;

public final class Table {

    private static final String EMPTY = "(empty)";
    private static final String ANSI_COLORS = "\u001B\\[[;\\d]*m";

    public static String of(String[][] data) {
        if (data == null) throw new NullPointerException("data == null");
        return new Table(data).toString();
    }

    private final String[][] data;
    private final int columns;
    private final int[] columnWidths;
    private final int emptyWidth;

    private Table(String[][] data) {
        this.data = data;

        columns = 2;
        columnWidths = new int[columns];
        for (int row = 0; row < data.length; row++) {
            String[] rowData = data[row];
            if (rowData.length != columns) {
                throw new IllegalArgumentException(
                        String.format("Row %s's %s columns != %s columns", row + 1, rowData.length, columns));
            }
            for (int column = 0; column < columns; column++) {
                for (String rowDataLine : rowData[column].split("\\n")) {
                    String rowDataWithoutColor = rowDataLine.replaceAll(ANSI_COLORS, "");
                    columnWidths[column] = Math.max(columnWidths[column], rowDataWithoutColor.length());
                }
            }
        }

        int emptyWidth = 3 * (columns - 1);
        for (int columnWidth : columnWidths) {
            emptyWidth += columnWidth;
        }
        this.emptyWidth = emptyWidth;

        if (emptyWidth < EMPTY.length()) {
            columnWidths[columns - 1] += EMPTY.length() - emptyWidth;
        }
    }

    private void printDivider(StringBuilder out, String format) {
        for (int column = 0; column < columns; column++) {
            out.append(column == 0 ? format.charAt(0) : format.charAt(2));
            out.append(pad(columnWidths[column], "").replace(' ', format.charAt(1)));
        }
        out.append(format.charAt(4)).append('\n');
    }

    private void printData(StringBuilder out, String[] data) {
        for (int line = 0, lines = 1; line < lines; line++) {
            for (int column = 0; column < columns; column++) {
                out.append('│');
                String[] cellLines = data[column].split("\\n");
                lines = Math.max(lines, cellLines.length);
                String cellLine = line < cellLines.length ? cellLines[line] : "";
                out.append(pad(columnWidths[column], cellLine));
            }
            out.append("│\n");
        }
    }

    private static String pad(int width, String data) {
        return String.format(" %1$-" + width + "s ", data);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        printDivider(builder, "┌─┬─┐");
        if (data.length == 0) {
            printDivider(builder, "├─┴─┤");
            builder.append('│').append(pad(emptyWidth, EMPTY)).append("│\n");
            printDivider(builder, "└───┘");
        } else {
            for (String[] datum : data) {
                printData(builder, datum);
            }
            printDivider(builder, "└─┴─┘");
        }
        return builder.toString();
    }
}