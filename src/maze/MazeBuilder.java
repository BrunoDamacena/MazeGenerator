package maze;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.ArrayList;
import java.util.Random;

public class MazeBuilder {

    class GPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        @Override
        public void paintComponent(Graphics g) {
            g.setColor(Color.RED);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());

            for (int i = 0; i < maze.cells.length; i++) {
                for (int j = 0; j < maze.cells[i].length; j++) {
                    g.setColor(maze.cells[i][j].getColor());
                    g.fillRect(j * Cell.WIDTH, i * Cell.HEIGHT, Cell.WIDTH, Cell.HEIGHT);
                }
            }
        }
    }

    JFrame frame;
    MazeGraph maze;
    public static int MAZESIZE;
    public static final int MILLISECONDSPERFRAME = 1;

    public MazeBuilder(int mazeSize) {
        MAZESIZE = mazeSize;
        maze = new MazeGraph(mazeSize);
    }

    public void run() {
        //create a screen to vizualize the maze
        frame = new JFrame("Maze");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GPanel panel = new GPanel();
        frame.getContentPane().add(BorderLayout.CENTER, panel);

        frame.setSize(MAZESIZE * Cell.WIDTH, MAZESIZE * Cell.HEIGHT + 50);
        frame.setLocationByPlatform(true);
        frame.setResizable(false);
        frame.setVisible(true);
        //start wilsons algorithm
        move();
    }

    private void move() {
        //ðefine the start point for the alg
        maze.cells[maze.cells.length - 2][1].setColor(Color.WHITE);

        Cell nextStart = maze.cells[maze.cells.length - 2][3]; //the first start point.
        while (nextStart != null) {
            erasedLoopWalk(nextStart.getx(), nextStart.gety());
            nextStart = findNextStartCell(nextStart);
        }
    }

    private void erasedLoopWalk(int x, int y) {
        ArrayList<Cell> trail = new ArrayList<Cell>();
        ArrayList<Integer> moveLog = new ArrayList<Integer>(); //should always have half as many items as trail
        Random rand = new Random();

        trail.add(maze.cells[y][x]);
        maze.cells[y][x].setColor(Color.RED);

        moveLog.add(-1);//placeholder that hopefully won't break anything
        while (true) {
            //decide candidates based on borders and previous move
            ArrayList<Integer> candidates = new ArrayList<Integer>();
            int prev = moveLog.get(moveLog.size() - 1);
            if (x - 2 > 0 && prev != 1) {
                candidates.add(0);
            }
            if (x + 2 < maze.cells[0].length - 1 && prev != 0) {
                candidates.add(1);
            }
            if (y - 2 > 0 && prev != 3) {
                candidates.add(2);
            }
            if (y + 2 < maze.cells.length - 1 && prev != 2) {
                candidates.add(3);
            }

            int move = candidates.get(rand.nextInt(candidates.size()));

            for (int i = 0; i < 2; i++) { //do twice so that each move moves 2 spaces
                //choose a direction and walk a step
                if (move == 0) {
                    x--;
                }
                if (move == 1) {
                    x++;
                }
                if (move == 2) {
                    y--;
                }
                if (move == 3) {
                    y++;
                }

                trail.add(maze.cells[y][x]); //add the new piece of the trail
                maze.cells[y][x].setColor(Color.RED); //change its color for collision and visual purposes

                //look for loops and erase back if found
                boolean looped = false;
                if (maze.cells[y][x + 1].getColor().equals(Color.RED) && move != 0) {
                    x++;
                    looped = true;
                } else if (maze.cells[y][x - 1].getColor().equals(Color.RED) && move != 1) {
                    x--;
                    looped = true;
                } else if (maze.cells[y + 1][x].getColor().equals(Color.RED) && move != 2) {
                    y++;
                    looped = true;
                } else if (maze.cells[y - 1][x].getColor().equals(Color.RED) && move != 3) {
                    y--;
                    looped = true;
                } else if (maze.cells[y - 1][x - 1].getColor().equals(Color.RED) && maze.cells[y - 1][x - 1] != trail.get(trail.size() - 3)) {
                    x--;
                    y--;
                    looped = true;
                } else if (maze.cells[y - 1][x + 1].getColor().equals(Color.RED) && maze.cells[y - 1][x + 1] != trail.get(trail.size() - 3)) {
                    x++;
                    y--;
                    looped = true;
                } else if (maze.cells[y + 1][x - 1].getColor().equals(Color.RED) && maze.cells[y + 1][x - 1] != trail.get(trail.size() - 3)) {
                    x--;
                    y++;
                    looped = true;
                } else if (maze.cells[y + 1][x + 1].getColor().equals(Color.RED) && maze.cells[y + 1][x + 1] != trail.get(trail.size() - 3)) {
                    x++;
                    y++;
                    looped = true;
                }

                if (looped) {
                    int retrace = trail.indexOf(maze.cells[y][x]);

                    for (int j = retrace + 1; j < trail.size(); j++) {
                        trail.get(j).setColor(Color.BLACK);
                    }

                    trail.subList(retrace + 1, trail.size()).clear();
                    moveLog.subList(retrace / 2 + 1, moveLog.size()).clear();
                    frame.repaint();

                    break; //breaks the for loop to avoid more collision detection
                }

                //look for connections and commit if found
                if (i == 0 && (maze.cells[y][x - 1].getColor().equals(Color.WHITE)
                        || maze.cells[y][x + 1].getColor().equals(Color.WHITE)
                        || maze.cells[y - 1][x].getColor().equals(Color.WHITE)
                        || maze.cells[y + 1][x].getColor().equals(Color.WHITE))) {
                    for (Cell c : trail) {
                        c.setColor(Color.WHITE);
                    }
                    frame.repaint();
                    return;
                }
            }

            moveLog.add(move);
            frame.repaint();
            try {
                Thread.sleep(MILLISECONDSPERFRAME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Cell findNextStartCell(Cell lastStart) {
        //search bottom to top, left to right, for a black cell with two adjacent black cells

        boolean firstLoop = true;
        for (int y = maze.cells.length - 2; y >= 1; y--) {
            for (int x = 1; x <= maze.cells[y].length - 2; x++) {
                if (firstLoop) {
                    x = lastStart.getx();
                    y = lastStart.gety();
                    firstLoop = false;
                }

                int adjacentBlackCounter = 0;

                if (maze.cells[y][x - 1].getColor().equals(Color.BLACK)) {
                    adjacentBlackCounter++;
                }
                if (maze.cells[y][x + 1].getColor().equals(Color.BLACK)) {
                    adjacentBlackCounter++;
                }
                if (maze.cells[y - 1][x].getColor().equals(Color.BLACK)) {
                    adjacentBlackCounter++;
                }
                if (maze.cells[y + 1][x].getColor().equals(Color.BLACK)) {
                    adjacentBlackCounter++;
                }
                if (maze.cells[y - 1][x - 1].getColor().equals(Color.BLACK)) {
                    adjacentBlackCounter++;
                }
                if (maze.cells[y - 1][x + 1].getColor().equals(Color.BLACK)) {
                    adjacentBlackCounter++;
                }
                if (maze.cells[y + 1][x - 1].getColor().equals(Color.BLACK)) {
                    adjacentBlackCounter++;
                }
                if (maze.cells[y + 1][x + 1].getColor().equals(Color.BLACK)) {
                    adjacentBlackCounter++;
                }

                if (adjacentBlackCounter == 8) {
                    return maze.cells[y][x];
                }
            }
        }
        return null;
    }

    //create a json file containing the maze
    public void cellToJsonString(String fileName) throws IOException {
        PrintWriter writer = new PrintWriter(fileName, "UTF-8");
        String json = new String();
        json += "{";
        for (int i = 0; i < MAZESIZE; i++) {
            json += "{";
            for (int j = 0; j < MAZESIZE; j++) {
                json += maze.cells[i][j].isWalkable();
                if (j != MAZESIZE - 1) {
                    json += ",";
                }
            }
            if (i + 1 != MAZESIZE) {
                json += "},";
            } else {
                json += "}";
            }
        }
        json += "}";
        writer.print(json);
        writer.close();
    }
}
