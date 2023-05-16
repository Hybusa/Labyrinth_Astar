package org.example.generator;

import org.example.enums.Direction;
import org.example.enums.GeneratorType;
import org.example.enums.MazePaths;
import org.example.enums.RandomType;
import org.example.model.ImageProcessor;
import org.example.objects.Edge;
import org.example.objects.GeneratorRequest;
import org.example.objects.MyPoint;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.example.model.ImageProcessor.generateMazeImage;

public class MazeGenerator {
    final static int WALL = 1;
    final static int VISITED = 0;
    final static int START = 2;
    final static int FINISH = 3;

    RandomType randomType;
    int[][] maze;
    int width;
    int height;
    boolean drawState;

    private final int GIF_DRAW_FREQ = 10;

    public BufferedImage generateMaze(GeneratorRequest generatorRequest) {

        long start = System.currentTimeMillis();
        int[][] resultMaze;
        this.randomType = generatorRequest.getRandomType();
        GeneratorType generatorType = generatorRequest.getGeneratorType();
        int rows = generatorRequest.getRows();
        int columns = generatorRequest.getColumns();
        drawState = generatorRequest.isGenerateGeneratorGif();

        switch (generatorType) {
            case DEPTH_FIRST_SEARCH:
                resultMaze = dfsGenerator(rows, columns);
                break;
            case KRUSKALS:
                resultMaze = kruskalsGenerator(rows, columns);
                break;
            case GPT:
                resultMaze = generateMazeGpt(rows, columns);
                break;
            default:
                throw new RuntimeException("Smth went wrong in Switch case.");
        }
        maze[1][1] = START;
        maze[height - 2][width - 2] = FINISH;

        BufferedImage mazeImage = generateMazeImage(resultMaze);

        new Thread(() -> {
            File outputfile = new File(MazePaths.SAVE_PATH + "Maze_Generated.png");
            try {
                ImageIO.write(mazeImage, "png", outputfile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

        long stop = System.currentTimeMillis();
        System.out.println("Generated in : " + (stop - start));
        return mazeImage;
    }

    private int[][] kruskalsGenerator(int rows, int columns) {
        height = (rows * 2) + 1;
        width = (columns * 2) + 1;
        maze = new int[height][width];
        for (int[] ints : maze) {
            Arrays.fill(ints, WALL);
        }

        Stack<Edge> edges = new Stack<>();

        Set<Set<MyPoint>> myPointSet = new HashSet<>();
        for (int i = 1; i < height - 1; i += 2) {
            for (int j = 1; j < width - 1; j += 2) {
                Set<MyPoint> tmpPointSet = new HashSet<>();
                tmpPointSet.add(new MyPoint(j, i));
                myPointSet.add(tmpPointSet);
            }
        }

        for (int i = 2; i < height - 1; i += 2) {
            for (int j = 2; j < width - 1; j += 2) {
                edges.add(new Edge(new MyPoint(j, i), Direction.N));
                edges.add(new Edge(new MyPoint(j, i), Direction.W));
            }
        }

        Collections.shuffle(edges);

        kruskals(edges, myPointSet);
        return maze;
    }

    private void kruskals(Stack<Edge> edges, Set<Set<MyPoint>> pointSets) {
        final List<BufferedImage> imagesForGif = new ArrayList<>();

        //ListSet<MyPoint> disjoinedSet = new HashSet<>();

        while (edges.size() > 0) {
            Edge tmp = edges.pop();
            int x = tmp.getPoint().x;
            int y = tmp.getPoint().y;
            Direction direction = tmp.getDirection();

            maze[y][x] = VISITED;

            if (direction == Direction.N) {
                maze[y - 1][x] = VISITED;
                maze[y + 1][x] = VISITED;
            } else {
                maze[y][x - 1] = VISITED;
                maze[y][x + 1] = VISITED;
            }
            edges.removeIf(e -> e.getPoint().equals(tmp.getPoint()));
            if (drawState)
                new Thread(() -> makeGifSnapshot(imagesForGif, tmp.getPoint()));

        }
        if (drawState) {
            new Thread(() -> {
                BufferedImage[] bufferedImages = new BufferedImage[imagesForGif.size()];
                bufferedImages = imagesForGif.toArray(bufferedImages);
                ImageProcessor.createGif(bufferedImages, (MazePaths.SAVE_PATH + "Generated_KRUSKALS.gif"), 10, false);
            }
            ).start();
        }
    }

    private int[][] dfsGenerator(int rows, int columns) {
        height = (rows * 2) + 1;
        width = (columns * 2) + 1;
        maze = new int[height][width];
        for (int[] ints : maze) {
            Arrays.fill(ints, WALL);
        }

        depthFirstSearch(1, 1);

        return maze;
    }

    private int[][] generateMazeGpt(int rows, int columns) {
        height = (rows * 2);
        width = (columns * 2);
        maze = new int[height][width];
        GPT(1, 1);
        return maze;
    }


    public void depthFirstSearch(int r, int c) {
        Stack<MyPoint> myPoints = new Stack<>();
        myPoints.push(new MyPoint(c, r));
        List<BufferedImage> imagesForGif = new ArrayList<>();

        int gifCounter = 0;
        while (!myPoints.empty()) {


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
//                            if (drawState) {
//                                makeGifSnapshot(imagesForGif, current);
//                            }
                            myPoints.push(new MyPoint(current.x, current.y - 2));
                        }
                        break;
                    case 2: // Right
                        if (current.x + 2 >= width - 1)
                            continue;
                        if (maze[current.y][current.x + 2] != VISITED) {
                            maze[current.y][current.x + 2] = VISITED;
                            maze[current.y][current.x + 1] = VISITED;
//                            if (drawState) {
//                                makeGifSnapshot(imagesForGif, current);
//                            }
                            myPoints.push(new MyPoint(current.x + 2, current.y));
                        }
                        break;
                    case 3: // Down
                        if (current.y + 2 >= height - 1)
                            continue;
                        if (maze[current.y + 2][current.x] != VISITED) {
                            maze[current.y + 2][current.x] = VISITED;
                            maze[current.y + 1][current.x] = VISITED;
//                            if (drawState) {
//                                makeGifSnapshot(imagesForGif, current);
//                            }
                            myPoints.push(new MyPoint(current.x, current.y + 2));

                        }
                        break;
                    case 4: // Left
                        if (current.x - 2 <= 0)
                            continue;
                        if (maze[current.y][current.x - 2] != VISITED) {
                            maze[current.y][current.x - 2] = VISITED;
                            maze[current.y][current.x - 1] = VISITED;
//                            if (drawState) {
//                                makeGifSnapshot(imagesForGif, current);
//                            }
                            myPoints.push(new MyPoint(current.x - 2, current.y));
                        }
                        break;
                }
            }

            if (drawState && (gifCounter % GIF_DRAW_FREQ == 0)) {
                makeGifSnapshot(imagesForGif, current);
            }
            gifCounter++;
        }
        if (drawState) {
            new Thread(() -> {
            BufferedImage[] bufferedImages = new BufferedImage[imagesForGif.size()];
            bufferedImages = imagesForGif.toArray(bufferedImages);
            ImageProcessor.createGif(bufferedImages, (MazePaths.SAVE_PATH + "GeneratedDFS.gif"), 200, false);
        }).start();
        }
    }

    private void makeGifSnapshot(List<BufferedImage> imagesForGif, MyPoint current) {
       // int tmp = maze[current.y][current.x];
       // maze[current.y][current.x] = FINISH;
        BufferedImage bufferedImage = ImageProcessor.generateMazeImage(maze);
        imagesForGif.add(ImageProcessor.deepCopy(bufferedImage));
      //  maze[current.y][current.x] = tmp;
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
    }

    private int[] getUnvisitedNeighbors(int row, int col) {
        // create a list of all neighboring cells that haven't been visited yet
        int[] neighbors = new int[4];
        int count = 0;
        if (row > 0 && maze[row - 1][col] == 1) { // top neighbor
            neighbors[count++] = 0;
        }
        if (row < height - 1 && maze[row + 1][col] == 1) { // bottom neighbor
            neighbors[count++] = 1;
        }
        if (col > 0 && maze[row][col - 1] == 1) { // left neighbor
            neighbors[count++] = 2;
        }
        if (col < width - 1 && maze[row][col + 1] == 1) { // right neighbor
            neighbors[count++] = 3;
        }
        int[] result = new int[count];
        System.arraycopy(neighbors, 0, result, 0, count);
        return result;
    }

    private Integer[] generateRandomDirections() {

        switch (this.randomType) {
            case RANDOM:
                return generatePseudoRandomDirections();
            case TRUE_RANDOM:
                return generateTrueRandomDirections();
            default:
                throw new RuntimeException("Smth went wrong in random directions");
        }
    }

    private Integer[] generateTrueRandomDirections() {
        ArrayList<Integer> randoms = new ArrayList<>();
        Random rn = new Random();

        for (int i = 0; i < 4; i++)
            randoms.add(rn.nextInt(4) + 1);

        return randoms.toArray(new Integer[4]);
    }

    private Integer[] generatePseudoRandomDirections() {
        ArrayList<Integer> randoms = new ArrayList<>();

        for (int i = 0; i < 4; i++)
            randoms.add(i + 1);
        Collections.shuffle(randoms);

        return randoms.toArray(new Integer[4]);
    }
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

