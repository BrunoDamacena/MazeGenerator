/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maze;

/**
 *
 * @author bruno
 */
public class MazeGraph {
    public Cell[][] cells;
    
    public MazeGraph(int mazeSize) {
        cells = new Cell[mazeSize][mazeSize];
            for (int i = 0; i < cells.length; i++) {
                for (int j = 0; j < cells[i].length; j++) {
                    cells[i][j] = new Cell(j,i);
                }
        }
    }
}
