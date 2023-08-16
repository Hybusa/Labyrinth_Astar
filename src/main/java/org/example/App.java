package org.example;


import org.example.enums.MazePaths;
import org.example.model.ActualSolver;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        System.out.println("Maze solved in : " + ActualSolver.SolveMaze(
                MazePaths.MAZE_TEST2.toString(),
                true,
                true,
                MazePaths.SAVE_PATH.toString(),
                null
                ));
        long stop = System.currentTimeMillis();
        System.out.println("All process : " + (stop - start));

//        new GeneratorRequest(
//                200,
//                200,
//                GeneratorType.DEPTH_FIRST_SEARCH,
//                RandomType.TRUE_RANDOM,
//                true)
    }
}

