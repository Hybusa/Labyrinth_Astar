package org.example.model;


import org.example.generator.MazeGenerator;
import org.example.objects.GeneratorRequest;
import org.example.objects.MyPoint;
import org.example.objects.Node;
import org.example.objects.NodeState;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.*;

public class ActualSolver {
    final static int EXIT_AFFINITY = 1;
    final static int DRAW_FREQUENCY = 1;

    public static void SolveMaze(String imagePath,
                                 boolean drawState,
                                 boolean drawGif,
                                 String savePath,
                                 GeneratorRequest generatorRequest) throws IOException {


        List<MyPoint> player = new ArrayList<>();
        List<MyPoint> exit = new ArrayList<>();
        BufferedImage image;
        int width;
        int height;

        if(generatorRequest == null) {
            image = ImageIO.read(new File(imagePath));
        }
        else {
            MazeGenerator mazeGenerator = new MazeGenerator();
            image = mazeGenerator.generateMaze(generatorRequest.getRows(),
                    generatorRequest.getColumns(),
                    generatorRequest.getGeneratorType());
        }

        width = image.getWidth();
        height = image.getHeight();

        Map<Integer, Node> objects = new HashMap<>();

        long timerStart = System.currentTimeMillis();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color pixelColor = new Color(image.getRGB(x, y));

                if (pixelColor.getBlue() < 100 && pixelColor.getRed() < 100 && pixelColor.getGreen() < 100)
                    objects.put(x * width + y, new Node(x, y, NodeState.WALL_POINT, Long.MAX_VALUE));
                else if (pixelColor.getBlue() == 255 && pixelColor.getRed() == 0 && pixelColor.getGreen() == 0)
                    player.add(new MyPoint(x, y));
                else if (pixelColor.getRed() == 255 && pixelColor.getGreen() == 0 && pixelColor.getBlue() == 0)
                    exit.add(new MyPoint(x, y));
            }
        }

        Node start = new Node(MyPoint.calculateCenterPoint(player), NodeState.PLAYER_POINT, 0L);
        Node end = new Node(MyPoint.calculateCenterPoint(exit), NodeState.END_POINT);

        Queue<Node> queue = new PriorityQueue<>();

        List<BufferedImage> arrayForGif = new ArrayList<>();
        arrayForGif.add(ImageProcessor.deepCopy(image));

        queue.add(start);
        long counter = 1;
        boolean done = false;

        while (!queue.isEmpty() && !done) {

            Node current = queue.poll();
            List<Node> neighbours = populateNeighbours(current);
            for (Node n : neighbours) {
                if (n.getX() >= width || n.getX() <= -1 || n.getY() >= height || n.getY() <= -1)
                    continue;
                else if (objects.containsKey(n.getX() * width + n.getY()))
                    continue;
                else if (n.getPoint().equals(end.getPoint())) {
                    end.setCameFrom(current);
                    done = true;
                    continue;
                }

                n.setCost(n.getCost() + ((heuristic(n.getPoint(), end.getPoint())) / EXIT_AFFINITY) + 10);
                n.setCameFrom(current);
                n.setState(NodeState.CHECKED);
                objects.put(n.getX() * width + n.getY(), n);
                queue.add(n);

                if (drawState) {
                    Color color = new Color(Color.HSBtoRGB((float) n.getCost() / (width * height * 10), 1f, 1f));
                    image.setRGB(n.getX(), n.getY(), color.getRGB());
                }
                if (drawState && counter % ((long) width * DRAW_FREQUENCY) == 0) {
                    if (drawGif)
                        arrayForGif.add(ImageProcessor.deepCopy(image));
                    else {
                        File outputfile = new File(savePath + "Maze_" + counter + ".png");
                        ImageIO.write(image, "png", outputfile);
                    }
                }
                counter++;
            }


        }
        List<Node> result = new ArrayList<>();
        Node pathNode = end;
        while (pathNode != null) {
            result.add(pathNode);
            pathNode = pathNode.getCameFrom();
        }

        long timerStop = System.currentTimeMillis();
        System.out.println("Maze solved in: "  + (timerStop - timerStart) + "ms");

        if (drawGif) {
            arrayForGif.add(ImageProcessor.deepCopy(image));
            BufferedImage[] bufferedImages = new BufferedImage[arrayForGif.size()];
            bufferedImages = arrayForGif.toArray(bufferedImages);
            ImageProcessor.createGif(bufferedImages, (savePath + "Maze_Solving.gif"), 100, true);
        }

        if (drawState) {
            //Collections.reverse(result);
            for (Node n : result) {

                Color color = new Color(image.getRGB(n.getX(), n.getY()));
                float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
                hsb[0] = (float) (hsb[0] + 0.5);
                color = new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));

                image.setRGB(n.getX(), n.getY(), color.getRGB());
            }
        } else {
            for (Node n : result) {
                image.setRGB(n.getX(), n.getY(), Color.RED.getRGB());
            }
        }
        image.setRGB(start.getX(), start.getY(), Color.RED.getRGB());
        image.setRGB(end.getX(), end.getY(), Color.MAGENTA.getRGB());

        File outputfile = new File(savePath + "Maze_Solved.png");
        ImageIO.write(image, "png", outputfile);
    }

    private static int heuristic(MyPoint point, MyPoint goal) {
        int dx = Math.abs(point.x - goal.x);
        int dy = Math.abs(point.y - goal.y);
        return dx + dy;
    }

    private static List<Node> populateNeighbours(Node node) {
        return List.of(
                new Node(node.getPoint().x + 1, node.getPoint().y + 0, node.getCost()),
                new Node(node.getPoint().x + 0, node.getPoint().y + 1, node.getCost()),
                new Node(node.getPoint().x - 1, node.getPoint().y + 0, node.getCost()),
                new Node(node.getPoint().x + 0, node.getPoint().y - 1, node.getCost())
        );
    }
}
