package org.example.graphic.node;

import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import lombok.Getter;

@Getter
public class TableColumnAdapter extends Node {
    private final TableColumn tableColumn;

    public TableColumnAdapter(TableColumn tableColumn) {
        this.tableColumn = tableColumn;
    }
}
