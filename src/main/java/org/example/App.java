package org.example;

import org.example.model.ActualSolver;
import org.example.path.MazePaths;

import java.io.IOException;

public class App 
{
    public static void main(String[] args) throws IOException {

        ActualSolver.SolveMaze(
                MazePaths.MAZE_SQUIRREL.toString(),
                true,
                true,
                MazePaths.SAVE_PATH.toString()
        );
    }
}
