package org.example;

import org.example.enums.GeneratorType;
import org.example.enums.MazePaths;
import org.example.model.ActualSolver;
import org.example.objects.GeneratorRequest;

import java.io.IOException;

public class App 
{
    public static void main(String[] args) throws IOException {


        ActualSolver.SolveMaze(
                MazePaths.MAZE_TEST3.toString(),
                true,
                true,
                MazePaths.SAVE_PATH.toString(),
                new GeneratorRequest(100,100,GeneratorType.DEPTH_FIRST_SEARCH)
        );
    }
}
