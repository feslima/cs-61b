package lab11.graphs;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */

    private int s;
    private int t;
    private boolean targetFound = false;
    private final Maze maze;
    private final Queue<Integer> queue;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);

        maze = m;
        queue = new ArrayDeque<>();

        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;

        marked[s] = true;
        announce();
        queue.offer(s);
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        while (!queue.isEmpty() && !targetFound) {
            int current = queue.remove();

            if (current == t) {
                targetFound = true;
                announce();
                break;
            }

            for (int w : maze.adj(current)) {
                if (!marked[w]) {
                    marked[w] = true;
                    edgeTo[w] = current;
                    announce();
                    distTo[w] = distTo[current] + 1;

                    queue.offer(w);
                }
            }
        }
    }


    @Override
    public void solve() {
        bfs();
    }
}

