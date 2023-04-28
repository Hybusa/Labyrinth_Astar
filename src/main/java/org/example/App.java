package org.example;

import org.example.enums.GeneratorType;
import org.example.enums.MazePaths;
import org.example.enums.RandomType;
import org.example.model.ActualSolver;
import org.example.objects.GeneratorRequest;

import java.io.IOException;

public class App 
{
    public static void main(String[] args) throws IOException {


        ActualSolver.SolveMaze(
                MazePaths.MAZE_8.toString(),
                true,
                true,
                MazePaths.SAVE_PATH.toString(),
               null
        );
        new GeneratorRequest(
                200,
                200,
                GeneratorType.DEPTH_FIRST_SEARCH,
                RandomType.TRUE_RANDOM);
    }
}
