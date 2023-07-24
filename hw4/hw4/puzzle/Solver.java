package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Comparator;

public class Solver {

    private static class SearchNodeComparator implements Comparator<SearchNode> {

        @Override
        public int compare(SearchNode t1, SearchNode t2) {
            int t1Cost = t1.distance + t1.moves;
            int t2Cost = t2.distance + t2.moves;
            return t1Cost - t2Cost;
        }
    }

    private static class SearchNode {
        public WorldState state;
        public int moves;
        public SearchNode previous;

        public int distance;

        public SearchNode(WorldState state, int moves, SearchNode previous) {
            this.state = state;
            this.moves = moves;
            this.previous = previous;

            // cache distance calculation result on instantiation (2nd optimization)
            this.distance = state.estimatedDistanceToGoal();
        }

        @Override
        public String toString() {
            return state.toString() + " | " + distance;
        }
    }

    private final ArrayList<WorldState> paths;
    private int moves;

    public Solver(WorldState initial) {
        /* first step of the algorithm is to create a priority queue of search nodes, and insert
         * an “initial search node” into the priority queue
         */
        MinPQ<SearchNode> pq = new MinPQ<>(new SearchNodeComparator());
        moves = 0;
        pq.insert(new SearchNode(initial, moves, null));
        paths = new ArrayList<>();

        while (!pq.isEmpty()) {
            SearchNode x = pq.delMin();

            if (x.state.isGoal()) {
                moves = x.moves;
                getSolution(x);
                break;
            }

            for (WorldState neighbor : x.state.neighbors()) {
                /* don’t enqueue a neighbor if its world state is the same as the world state
                 * of the previous search node (1st optimization)
                 */
                if (x.previous == null || !neighbor.equals(x.previous.state)) {
                    pq.insert(new SearchNode(neighbor, x.moves + 1, x));
                }
            }
        }

    }

    /**
     * Backtrack from node until initial one and store each state found.
     *
     * @param node Node that contains the state which is the goal of the puzzle
     */
    private void getSolution(SearchNode node) {
        SearchNode p = node;
        ArrayList<WorldState> reversedPaths = new ArrayList<>();
        while (p.previous != null) {
            reversedPaths.add(p.state);
            p = p.previous;
        }

        for (int i = reversedPaths.size() - 1; i >= 0; i--) {
            paths.add(reversedPaths.get(i));
        }
    }

    public int moves() {
        return moves;
    }

    public Iterable<WorldState> solution() {
        return paths;
    }
}
