package org.example.objects;

import org.example.enums.Direction;

import java.util.Objects;

public class Edge {
    private final MyPoint point;
    private final Direction direction;

    public Edge(MyPoint point, Direction direction) {
        this.point = point;
        this.direction = direction;
    }

    public MyPoint getPoint() {
        return point;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(point, edge.point) && direction == edge.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(point, direction);
    }
}
