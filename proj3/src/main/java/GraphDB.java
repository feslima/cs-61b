import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Objects;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

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
    private static final Set<String> ALLOWED_HIGHWAY_TYPES = new HashSet<>(Arrays.asList
            ("motorway", "trunk", "primary", "secondary", "tertiary", "unclassified",
                    "residential", "living_street", "motorway_link", "trunk_link", "primary_link",
                    "secondary_link", "tertiary_link"));

    private final Map<Long, Node> nodes = new LinkedHashMap<>();
    private final HashMap<Long, WayEdge> ways = new HashMap<>();
    private final HashMap<Long, HashSet<Long>> nodeToWay = new HashMap<>();
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

    public Node getNode(long id) {
        return nodes.get(id);
    }

    public WayEdge getWay(long id) {
        return ways.get(id);
    }

    public void addEdge(WayEdge edge) {
        ways.put(edge.id, edge);
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
        private String name;
        private final double latitude;
        private final double longitude;
        private final HashMap<Long, HashMap<String, String>> neighbors = new HashMap<>();

        Node(long id, double longitude, double latitude) {
            this.id = id;
            this.latitude = latitude;
            this.longitude = longitude;
            this.name = null;

        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
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

        public HashMap<String, String> getNeighborData(long neighborId) {
            return neighbors.get(neighborId);
        }

        public double distanceToCoord(double lon, double lat) {
            return distance(longitude, latitude, lon, lat);
        }

        @Override
        public String toString() {
            return "Node{"
                    + "id=" + id
                    + ", longitude=" + longitude
                    + ", latitude=" + latitude
                    + '}';
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

    /**
     * Computes the A* f(n) function score of nodes given their mappings. Intended use in min-heaps.
     */
    static class NodeScoreComparator implements Comparator<Long> {
        private final Map<Long, Double> gScore;
        private final Map<Long, Double> hScore;

        /**
         * @param gScore g(n) function mapping of node ids to their g values.
         *               g(n) corresponds to computed distances from start to node n.
         * @param hScore h(n) function mapping of node ids to their h values.
         *               h(n) corresponds to computed distances from node n to target.
         */
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

    static class WayEdge {
        private final HashMap<String, String> attributes = new HashMap<>();
        private final ArrayList<Long> nodeIds = new ArrayList<>();
        private final Set<Long> nodeIdsSet = new HashSet<>();
        public final long id;

        WayEdge(long id) {
            this.id = id;
            attributes.put("wayId", String.valueOf(id));
        }

        public boolean isValid() {
            String valid = attributes.get("highway");
            if (valid == null) {
                return false;
            }
            return ALLOWED_HIGHWAY_TYPES.contains(valid);
        }

        public HashMap<String, String> getAttributes() {
            return attributes;
        }

        public void setAttribute(String key, String value) {
            attributes.put(key, value);
        }

        public void addNodeRef(String nodeId) {
            nodeIds.add(Long.parseLong(nodeId));
            nodeIdsSet.add(Long.parseLong(nodeId));
        }

        public List<Long> getNodes() {
            return nodeIds;
        }

        public boolean hasNode(long id) {
            return nodeIdsSet.contains(id);
        }
    }

}
