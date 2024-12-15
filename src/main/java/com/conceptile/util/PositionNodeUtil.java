package com.conceptile.util;

import com.conceptile.entity.Node;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class PositionNodeUtil {
    private static final int DEFAULT_START_X = 100;
    private static final int DEFAULT_START_Y = 100;
    private static final int X_OFFSET = 150;
    private static final int Y_OFFSET = 150;
    private static final int MAX_ROW_WIDTH = 1000;

    public static Pair<Integer, Integer> generatePosition(List<Node> existingNodes) {
        if (existingNodes == null || existingNodes.isEmpty()) {
            return Pair.of(DEFAULT_START_X, DEFAULT_START_Y);
        }
        Node lastNode = existingNodes.get(existingNodes.size() - 1);
        int nextX = lastNode.getXAxis() + X_OFFSET; // Move to the right of the last node
        int nextY = lastNode.getYAxis();

        // If X exceeds the max row width, move to the next row
        if (nextX > MAX_ROW_WIDTH) {
            nextX = DEFAULT_START_X; // Reset X to start of the new row
            nextY += Y_OFFSET;       // Move down to the next row
        }
        return Pair.of(nextX, nextY);
    }
}
