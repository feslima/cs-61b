import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.*;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {

    private final Map<Long, Node> nodes = new LinkedHashMap<>();
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     *
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     *
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     * Remove nodes with no connections from the graph.
     * While this does not guarantee that any two nodes in the remaining graph are connected,
     * we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        ArrayList<Long> nodeIdsToRemove = new ArrayList<>();
        for (long id : nodes.keySet()) {
            Node node = nodes.get(id);
            if (node.getNeighbors().isEmpty()) {
                nodeIdsToRemove.add(id);
            }
        }

        for (long id : nodeIdsToRemove) {
            nodes.remove(id);
        }
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     *
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        return nodes.keySet();
    }

    /**
     * Returns ids of all vertices adjacent to v.
     *
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        Node node = nodes.get(v);
        if (node == null) {
            return null;
        }

        return node.getNeighbors();
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     *
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     *
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     *
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        // uses a priority queue with each node computed distance to target
        PriorityQueue<NodeWithDistance> pq = new PriorityQueue<>();
        for (Long vertex : vertices()) {
            pq.add(NodeWithDistance.fromNode(nodes.get(vertex), lon, lat));
        }

        NodeWithDistance closest = pq.poll();
        return closest != null ? closest.getId() : -1;
    }

    /**
     * Gets the longitude of a vertex.
     *
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return nodes.get(v).longitude;
    }

    /**
     * Gets the latitude of a vertex.
     *
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return nodes.get(v).latitude;
    }

    public void addNode(Node node) {
        nodes.put(node.id, node);
    }

    public void connectNodes(long from, long to, HashMap<String, String> extraInfo) {
        Node fromNode = nodes.get(from);
        Node toNode = nodes.get(to);
        if (fromNode == null || toNode == null) {
            throw new IllegalArgumentException("Either or both to and from nodes doesn't exist.");
        }
        fromNode.connectTo(to, extraInfo);
        toNode.connectTo(from, extraInfo);
    }

    static class Node {
        private final long id;
        private final double latitude;
        private final double longitude;
        private final HashMap<Long, HashMap<String, String>> neighbors = new HashMap<>();

        Node(long id, double longitude, double latitude) {
            this.id = id;
            this.latitude = latitude;
            this.longitude = longitude;

        }

        public long getId() {
            return id;
        }

        public void connectTo(long v, HashMap<String, String> wayExtraInfo) {
            HashMap<String, String> wayInfo = neighbors.containsKey(v) ? neighbors.get(v) : new HashMap<>();
            wayInfo.putAll(wayExtraInfo);
            neighbors.put(v, wayInfo);
        }

        public Set<Long> getNeighbors() {
            return neighbors.keySet();
        }

        public double distanceToCoord(double lon, double lat) {
            return distance(longitude, latitude, lon, lat);
        }

        @Override
        public String toString() {
            return "Node{" +
                    "id=" + id +
                    ", longitude=" + longitude +
                    ", latitude=" + latitude +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return id == node.id && Double.compare(latitude, node.latitude) == 0 && Double.compare(longitude, node.longitude) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, latitude, longitude);
        }
    }

    static class NodeScoreComparator implements Comparator<Long> {
        private final Map<Long, Double> gScore;
        private final Map<Long, Double> hScore;

        NodeScoreComparator(Map<Long, Double> gScore, Map<Long, Double> hScore) {
            this.gScore = gScore;
            this.hScore = hScore;
        }

        @Override
        public int compare(Long o1, Long o2) {
            double d1 = gScore.getOrDefault(o1, Double.POSITIVE_INFINITY) + hScore.getOrDefault(o1, Double.POSITIVE_INFINITY);
            double d2 = gScore.getOrDefault(o2, Double.POSITIVE_INFINITY) + hScore.getOrDefault(o2, Double.POSITIVE_INFINITY);
            return Double.compare(d1, d2);
        }
    }

    static class NodeWithDistance extends Node implements Comparable<NodeWithDistance> {
        private final double distance;

        NodeWithDistance(long id, double longitude, double latitude, double lon, double lat) {
            super(id, longitude, latitude);
            this.distance = GraphDB.distance(longitude, latitude, lon, lat);
        }

        static NodeWithDistance fromNode(Node node, double targetLon, double targetLat) {
            return new NodeWithDistance(node.id, node.longitude, node.latitude, targetLon, targetLat);
        }

        @Override
        public int compareTo(NodeWithDistance o) {
            return Double.compare(distance, o.distance);
        }
    }

}
