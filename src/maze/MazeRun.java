/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maze;

import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author bruno
 */
public class MazeRun {
    private static Scanner scan;
    private static int mazeSize;
    
    public static void main(String[] args) throws IOException {
        scan = new Scanner(System.in);
        System.out.println("Choose the maze size:");
        mazeSize = scan.nextInt();
        generateMaze();
        System.exit(0);       
    }
    
    private static void generateMaze() throws IOException {
        scan = new Scanner(System.in);
        MazeBuilder m = new MazeBuilder(mazeSize);
        scan = new Scanner(System.in);
        String ans;              
        m.run();        
        System.out.println("Would you like to save this maze?");
        ans = scan.nextLine();
        while(!ans.equalsIgnoreCase("yes") && !ans.equalsIgnoreCase("no")) {
            System.out.println("Invalid input! Type 'yes' or 'no'.");
            ans = scan.nextLine();
        }
        if(ans.equalsIgnoreCase("yes")) {
            scan = new Scanner(System.in);
            String name;
            System.out.println("Choose the file name:");
            name = scan.nextLine();
            m.cellToJsonString(name + ".json");
        }
        else {
            m.frame.dispose();
            generateMaze();
        }
    }
}
