package org.example.objects;

public class Node implements Comparable<Node> {
    private final MyPoint point;
    private Long cost;

    private Node cameFrom;
    private NodeState state;


    public void setCameFrom(Node cameFrom) {
        this.cameFrom = cameFrom;
    }

    public Node getCameFrom() {
        return cameFrom;
    }

    public Node(int x, int y, NodeState state, Long cost) {
        this.point = new MyPoint(x, y);
        this.state = state;
        this.cost = cost;
    }
    public Node(int x, int y,Long cost){
        this.point = new MyPoint(x, y);
        this.cost = cost;
    }
    public Node(MyPoint point, NodeState state){
        this.point = point;
        this.state = state;
    }

    public Node(MyPoint point, NodeState state,Long cost){
        this.point = point;
        this.state = state;
        this.cost = cost;
    }

    public Long getCost() {
        return cost;
    }
    public void setCost(Long cost){
        this.cost = cost;
    }

    public MyPoint getPoint() {
        return point;
    }

    public int getX(){
        return this.point.x;
    }
    public int getY(){
        return this.point.y;
    }


    public void setState(NodeState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "point=" + point;
    }

    @Override
    public int compareTo(Node o) {
        return this.cost.compareTo(o.cost);
    }
}
