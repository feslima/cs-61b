package lab11.graphs;

/**
 * @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private boolean hasCycle = false;
    private int[] invisibleEdgeTo;

    public MazeCycles(Maze m) {
        super(m);
    }

    @Override
    public void solve() {
        invisibleEdgeTo = new int[maze.N() * maze.N()];

        for (int v = 0; v < maze.V(); v++) {
            if (!marked[v] && !hasCycle()) {
                dfs(v, -1);
            }
        }
    }

    private boolean hasCycle() {
        return hasCycle;
    }

    private void dfs(int v, int parent) {
        distTo[v] = v;
        marked[v] = true;
        announce();

        for (int w : maze.adj(v)) {
            if (hasCycle()) {
                return;
            }

            if (!marked[w]) {
                invisibleEdgeTo[w] = v;
                dfs(w, v);
            } else {
                if (parent != w) {
                    edgeTo[w] = v;
                    announce();
                    int x = v;
                    while (x != w) {
                        edgeTo[x] = invisibleEdgeTo[x];
                        announce();
                        x = invisibleEdgeTo[x];
                    }
                    hasCycle = true;
                }
            }

        }
    }

}

