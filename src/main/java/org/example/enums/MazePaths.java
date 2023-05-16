package org.example.enums;

public enum MazePaths {
    MAZE_TEST("C:/Users/KuZzz/Desktop/MAZES/MAZE_TEST.png"),
    MAZE_TEST2("C:/Users/KuZzz/Desktop/MAZES/MAZE_TEST2.png"),
    MAZE_TEST3("C:/Users/KuZzz/Desktop/MAZES/MAZE_TEST3.png"),
    MAZE_4("C:/Users/KuZzz/Desktop/MAZES/MAZE_4.png"),
    MAZE_5("C:/Users/KuZzz/Desktop/MAZES/MAZE_5.png"),
    MAZE_6("C:/Users/KuZzz/Desktop/MAZES/MAZE_6.png"),
    MAZE_ULTIMATE("C:/Users/KuZzz/Desktop/MAZE_ULTIMATE.png"),
    MAZE_SQUIRREL("C:/Users/KuZzz/Desktop/MAZES/Maze_Squirrel.png"),
    CIRCLE_MAZE("C:/Users/KuZzz/Desktop/MAZES/Circle_Maze.png"),
    MAZE_8("C:/Users/KuZzz/Desktop/MAZES/MAZE_8.png"),
    MAZE_9("C:/Users/KuZzz/Desktop/MAZES/MAZE_9.png"),
    MAZE_GARRIK ("C:/Users/KuZzz/Desktop/MAZES/MAZE_GARRIK.png"),
    SAVE_PATH("X:/MAZES/Java/");



    private final String path;
    MazePaths(String s) {
        this.path = s;
    }

    @Override
    public String toString() {
        return this.path;
    }
}
