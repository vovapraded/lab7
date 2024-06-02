package org.example.graphic.scene.util;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BackgroundClickableMaker {
    public void make(Scene scene) {
        var firstPane = scene.getRoot();
        scene.setOnMousePressed(event -> {
            // Перебираем все узлы в корневом узле и проверяем, было ли нажатие внутри какого-либо узла
            boolean clickInsideNode = false;
            var node = firstPane;
            var childrens = handle(new ArrayList<>(List.of(node)));

            for (Node children  : childrens) {
                if (children.getBoundsInParent().contains(event.getX(), event.getY())) {
                    System.out.println(event.getX()+" "+event.getY());
                    clickInsideNode = true;
                    break;
                }
            }

            // Если ни один узел не содержит нажатие, значит, оно произошло вне любого узла интерфейса
            if (!clickInsideNode) {
                scene.getRoot().requestFocus();
                System.out.println("Clicked outside!");
            }
        });
    }
    private List<Node> handle(List<Node> nodes){
        AtomicBoolean flag = new AtomicBoolean(false);
        nodes = nodes.stream().map(nd -> {
                    if (nd instanceof Pane){
                        flag.set(true);
                        return (List<Node>)((Pane) nd).getChildren();
                    }else {
                        return (Node) nd;
                    }
                }) .flatMap(o -> o instanceof List ? ((List<Node>) o).stream() : Stream.of((Node) o))
                .collect(Collectors.toList());

        if (!flag.get()){
            return nodes;
        }
        return  handle(nodes);
    }
}
