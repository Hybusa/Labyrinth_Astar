package org.example.generator;

import org.example.enums.GeneratorType;
import org.example.model.ImageProcessor;
import org.example.objects.MyPoint;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;


public class MazeGenerator {

    final static int WALL = 1;
    final static int VISITED = 0;
    final static int START = 2;
    final static int FINISH = 3;

    int[][] maze;
    int width;
    int height;

    public BufferedImage generateMaze(int rows, int columns, GeneratorType generatorType) throws IOException {

        BufferedImage resultMaze;
        switch (generatorType) {
            case DEPTH_FIRST_SEARCH:
                resultMaze = ImageProcessor.generateMazeImage(dfsGenerator(rows, columns));
                break;
            case GPT:
                resultMaze = ImageProcessor.generateMazeImage(generateMazeGpt(rows,columns));
                break;
            default:
                throw new RuntimeException("Smth went wrong in Switch case.");
        }

        return resultMaze;
    }

    private int[][] dfsGenerator(int rows, int columns) {
        height = (rows*2)+1;
        width = (columns*2)+1;
        maze = new int[height][width];
        for (int[] ints : maze) {
            Arrays.fill(ints, WALL);
        }

        depthFirstSearch(1,1);

        maze[1][1] = START;
        maze[height-2][width-2] = FINISH;
        return maze;
    }

    private int[][] generateMazeGpt(int rows, int columns){
        height = (rows*2);
        width = (columns*2);
        maze = new int [height][width];
        GPT(1, 1);
        maze[1][1] = START;
        maze[height-2][width-2] = FINISH;
        return maze;
    }


    public void depthFirstSearch(int r, int c){
        Stack<MyPoint> myPoints = new Stack<>();
        myPoints.push(new MyPoint(c,r));

        while(!myPoints.empty()){
            Integer[] randomDirections = generateRandomDirections();
            MyPoint current = myPoints.pop();
            for (Integer randomDirection : randomDirections) {
                switch (randomDirection) {
                    case 1: // Up
                        if (current.y - 2 <= 0)
                            continue;
                        if (maze[current.y - 2][current.x] != VISITED) {
                            maze[current.y - 2][current.x] = VISITED;
                            maze[current.y - 1][current.x] = VISITED;
                            myPoints.push(new MyPoint(current.x, current.y - 2));
                        }
                        break;
                    case 2: // Right
                        if (current.x + 2 >= width - 1)
                            continue;
                        if (maze[current.y][current.x + 2] != VISITED) {
                            maze[current.y][current.x + 2] = VISITED;
                            maze[current.y][current.x + 1] = VISITED;
                            myPoints.push(new MyPoint(current.x + 2, current.y));
                        }
                        break;
                    case 3: // Down
                        if (current.y + 2 >= height - 1)
                            continue;
                        if (maze[current.y + 2][current.x] != VISITED) {
                            maze[current.y + 2][current.x] = VISITED;
                            maze[current.y + 1][current.x] = VISITED;
                            myPoints.push(new MyPoint(current.x, current.y + 2));

                        }
                        break;
                    case 4: // Left
                        if (current.x - 2 <= 0)
                            continue;
                        if (maze[current.y][current.x - 2] != VISITED) {
                            maze[current.y][current.x - 2] = VISITED;
                            maze[current.y][current.x - 1] = VISITED;
                            myPoints.push(new MyPoint(current.x-2, current.y));
                        }
                        break;
                }
            }
        }
    }

    private void GPT(int row, int col) {
        maze[row][col] = 0; // mark current cell as floor
        Random random = new Random();
        // create a list of all neighboring cells that haven't been visited yet
        int[] neighbors = getUnvisitedNeighbors(row, col);

        while (neighbors.length > 0) {
            // choose a random neighbor
            int idx = random.nextInt(neighbors.length);
            int neighbor = neighbors[idx];

            // remove the wall between the current cell and the chosen neighbor
            int wallRow = (neighbor == 0) ? row - 1 : (neighbor == 1) ? row + 1 : row;
            int wallCol = (neighbor == 2) ? col - 1 : (neighbor == 3) ? col + 1 : col;
            maze[wallRow][wallCol] = 0;

            // recursively visit the chosen neighbor
            GPT(wallRow, wallCol);

            // get a new list of unvisited neighbors
            neighbors = getUnvisitedNeighbors(row, col);
        }
        maze[1][1] = START;
        maze[height-2][width-2] = FINISH;
    }

    private int[] getUnvisitedNeighbors(int row, int col) {
        // create a list of all neighboring cells that haven't been visited yet
        int[] neighbors = new int[4];
        int count = 0;
        if (row > 0 && maze[row-1][col] == 1) { // top neighbor
            neighbors[count++] = 0;
        }
        if (row < height-1 && maze[row+1][col] == 1) { // bottom neighbor
            neighbors[count++] = 1;
        }
        if (col > 0 && maze[row][col-1] == 1) { // left neighbor
            neighbors[count++] = 2;
        }
        if (col < width-1 && maze[row][col+1] == 1) { // right neighbor
            neighbors[count++] = 3;
        }
        int[] result = new int[count];
        System.arraycopy(neighbors, 0, result, 0, count);
        return result;
    }


    private Integer[] generateRandomDirections() {
        ArrayList<Integer> randoms = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            randoms.add(i + 1);
        Collections.shuffle(randoms);

        return randoms.toArray(new Integer[4]);
    }


       /* public void recursion(int r, int c) {

        Integer[] randomDirections = generateRandomDirections();

        for (int i = 0; i < randomDirections.length; i++) {

            switch(randomDirections[i]){
                case 1: // Up
                    if (r - 2 <= 0)
                        continue;
                    if (maze[r - 2][c] != VISITED) {
                        maze[r-2][c] = VISITED;
                        maze[r-1][c] = VISITED;
                        recursion(r - 2, c);
                    }
                    break;
                case 2: // Right
                    if (c + 2 >= width - 1)
                        continue;
                    if (maze[r][c + 2] != VISITED) {
                        maze[r][c + 2] = VISITED;
                        maze[r][c + 1] = VISITED;
                        recursion(r, c + 2);
                    }
                    break;
                case 3: // Down
                    if (r + 2 >= height - 1)
                        continue;
                    if (maze[r + 2][c] != VISITED) {
                        maze[r+2][c] = VISITED;
                        maze[r+1][c] = VISITED;
                        recursion(r + 2, c);
                    }
                    break;
                case 4: // Left
                    if (c - 2 <= 0)
                        continue;
                    if (maze[r][c - 2] != VISITED) {
                        maze[r][c - 2] = VISITED;
                        maze[r][c - 1] = VISITED;
                        recursion(r, c - 2);
                    }
                    break;
            }
        }
    }*/
}
