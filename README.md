# Maze Generator
Algorithm for mazes procedural generation for Synthesis game

## Algorithm
We used Wilson's Algorithm for the maze generation. This algorithm is based on a mathematical process called [Loop-erased random walk](https://en.wikipedia.org/wiki/Loop-erased_random_walk).

### How it works
What we do is create a graph, using a simple matrix, to represent the maze itself. Then, we choose an arbitrary point of the graph (`we choosed the bottom left`) to start the maze generation. Then we perform a random walk until we reach a cell already in the maze. However, if at any point the random walk reaches its own path, forming a loop, we erase the loop from the path before proceeding. When the path reaches the maze, we add it to the maze. Then we perform another loop-erased random walk from another arbitrary starting cell, repeating until all cells have been filled.

## How to use the program
First, you choose the maze size (`it is always a NxN maze`). Then, the maze is generated. If you like the maze, you can save it to a json file. If you not, another NxN maze is generated. Simple as that!
