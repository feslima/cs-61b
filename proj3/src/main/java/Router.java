import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     *
     * @param g       The graph to use.
     * @param stlon   The longitude of the start location.
     * @param stlat   The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        Map<Long, Long> cameFrom = new HashMap<>();
        Map<Long, Double> gScore = new HashMap<>();
        Map<Long, Double> hScore = new HashMap<>();

        GraphDB.NodeScoreComparator nc = new GraphDB.NodeScoreComparator(gScore, hScore);
        PriorityQueue<Long> pq = new PriorityQueue<>(nc);

        GraphDB.Node currentNode = new GraphDB.Node(g.closest(stlon, stlat), stlon, stlat);
        double startToCurrentDistance = currentNode.distanceToCoord(stlon, stlat);
        double currentToTargetDistance = currentNode.distanceToCoord(destlon, destlat);

        long targetNodeId = g.closest(destlon, destlat);
        double targetNodeLon = g.lon(targetNodeId);
        double targetNodeLat = g.lat(targetNodeId);

        pq.add(currentNode.getId());

        gScore.put(currentNode.getId(), startToCurrentDistance);
        hScore.put(currentNode.getId(), currentToTargetDistance);

        boolean success = false;
        while (!pq.isEmpty()) {
            long currentId = pq.poll();
            currentNode = new GraphDB.Node(currentId, g.lon(currentId), g.lat(currentId));

            if (currentNode.distanceToCoord(targetNodeLon, targetNodeLat) <= 1e-6) {
                success = true;
                break;
            }

            for (long neighbor : g.adjacent(currentId)) {
                double lonNeighbor = g.lon(neighbor);
                double latNeighbor = g.lat(neighbor);
                double currentDistanceToNeighbor = currentNode.distanceToCoord(lonNeighbor, latNeighbor);

                startToCurrentDistance = gScore.get(currentId) + currentDistanceToNeighbor;

                if (startToCurrentDistance < gScore.getOrDefault(neighbor, Double.POSITIVE_INFINITY)) {
                    // found a better path (or it's an uncharted/undiscovered node)
                    GraphDB.Node neighborNode = new GraphDB.Node(neighbor, lonNeighbor, latNeighbor);

                    gScore.put(neighbor, startToCurrentDistance);
                    hScore.put(neighbor, neighborNode.distanceToCoord(destlon, destlat));
                    cameFrom.put(neighbor, currentId);

                    if (!pq.contains(neighbor)) {
                        pq.add(neighbor);
                    }
                }
            }

        }

        if (!success) {
            return new ArrayList<>();
        }

        return getSolution(currentNode.getId(), cameFrom);
    }

    private static ArrayList<Long> getSolution(Long current, Map<Long, Long> cameFrom) {
        ArrayList<Long> reversedPaths = new ArrayList<>();
        reversedPaths.add(current);

        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            reversedPaths.add(current);
        }
        ArrayList<Long> paths = new ArrayList<>();

        for (int i = reversedPaths.size() - 1; i >= 0; i--) {
            long id = reversedPaths.get(i);
            paths.add(id);
        }

        return paths;
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     *
     * @param g     The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        ArrayList<NavigationDirection> directions = new ArrayList<>();
        int numNodes = route.size();

        int i = 1;

        long currentId = route.get(i);
        long previousId = route.get(i - 1);
        double previousLon = g.lon(previousId);
        double previousLat = g.lat(previousId);

        GraphDB.Node currentNode = g.getNode(route.get(i));
        if (currentNode == null) {
            return directions;
        }

        HashMap<String, String> currentEdge = currentNode.getNeighborData(previousId);

        NavigationDirection current = new NavigationDirection();
        current.direction = NavigationDirection.START;
        String currentWayName = currentEdge.get("name");
        current.way = currentWayName;
        while (i < numNodes) {
            long currentWayId = Long.parseLong(currentEdge.get("wayId"));
            GraphDB.WayEdge way = g.getWay(currentWayId);

            while (way.hasNode(currentId)) {
                current.distance += currentNode.distanceToCoord(previousLon, previousLat);
                i += 1;

                if (i >= numNodes) {
                    directions.add(current);
                    current.direction = setDirection(g, currentId, previousId);
                    break;
                }

                currentId = route.get(i);
                previousId = route.get(i - 1);
                previousLon = g.lon(previousId);
                previousLat = g.lat(previousId);
                currentNode = g.getNode(currentId);
            }

            currentEdge = currentNode.getNeighborData(previousId);
            String wayStr = currentEdge.getOrDefault("name", "");
            if (!wayStr.equals(currentWayName)) {
                currentWayName = wayStr;
                directions.add(current);
                current = new NavigationDirection();
                current.way = currentWayName;
                current.direction = setDirection(g, route.get(i - 2), route.get(i - 1));
            }

        }

        return directions;
    }

    private static int setDirection(GraphDB g, long previousId, long currentId) {

        double bearingAngle = g.bearing(previousId, currentId);

        /*
         * Between -15 and 15 degrees the direction should be “Continue straight”.
         * Beyond -15 and 15 degrees but between -30 and 30 degrees the direction should be “Slight left/right”.
         * Beyond -30 and 30 degrees but between -100 and 100 degrees the direction should be “Turn left/right”.
         * Beyond -100 and 100 degrees the direction should be “Sharp left/right”.
         * */
        if (bearingAngle >= -15.0 && bearingAngle <= 15.0) {
            return NavigationDirection.STRAIGHT;
        } else if (bearingAngle > 15.0 && bearingAngle <= 30.0) {
            return NavigationDirection.SLIGHT_RIGHT;
        } else if (bearingAngle > 30.0 && bearingAngle <= 100.0) {
            return NavigationDirection.RIGHT;
        } else if (bearingAngle > 100.0) {
            return NavigationDirection.SHARP_RIGHT;
        } else if (bearingAngle < -15.0 && bearingAngle >= -30.0) {
            return NavigationDirection.SLIGHT_LEFT;
        } else if (bearingAngle < -30.0 && bearingAngle >= -100.0) {
            return NavigationDirection.LEFT;
        } else if (bearingAngle < -100.0) {
            return NavigationDirection.SHARP_LEFT;
        }

        return NavigationDirection.START;
    }

    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /**
         * Integer constants representing directions.
         */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /**
         * Number of directions supported.
         */
        public static final int NUM_DIRECTIONS = 8;

        /**
         * A mapping of integer values to directions.
         */
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /**
         * Default name for an unknown way.
         */
        public static final String UNKNOWN_ROAD = "unknown road";

        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /**
         * The direction a given NavigationDirection represents.
         */
        int direction;
        /**
         * The name of the way I represent.
         */
        String way;
        /**
         * The distance along this way I represent.
         */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         *
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                        && way.equals(((NavigationDirection) o).way)
                        && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
