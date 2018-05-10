package ch.hsr.adv.ui.graph.logic.domain;

import ch.hsr.adv.ui.core.presentation.widgets.LabeledEdge;
import ch.hsr.adv.ui.core.presentation.widgets.LabeledNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Matrix {
    private int[][] matrix;
    private Map<LabeledNode, Integer> indexMap = new HashMap<>();


    public Matrix(List<LabeledNode> vertices) {
        int length = vertices.size();
        matrix = new int[length][length];

        for (int i = 0; i < length; i++) {
            indexMap.put(vertices.get(i), i);
        }
    }

    public void addEdge(LabeledNode start, LabeledNode end) {
        int index1 = indexMap.get(start);
        int index2 = indexMap.get(end);
        int edgeCount = matrix[index1][index2];
        matrix[index1][index2] = ++edgeCount;
    }

    public boolean hasEdges(LabeledNode start, LabeledNode end) {
        int index1 = indexMap.get(start);
        int index2 = indexMap.get(end);
        return matrix[index1][index2] > 0;
    }

    public int getEdgeCount(LabeledNode start, LabeledNode end) {
        int index1 = indexMap.get(start);
        int index2 = indexMap.get(end);
        return matrix[index1][index2];
    }

}
