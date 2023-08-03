import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    public static final double ROOT_ULLAT = 37.892195547244356, ROOT_ULLON = -122.2998046875,
            ROOT_LRLAT = 37.82280243352756, ROOT_LRLON = -122.2119140625;
    public static final int TILE_SIZE = 256;

    public Rasterer() {
        // YOUR CODE HERE
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     * <p>
     * The grid of images must obey the following properties, where image in the
     * grid is referred to as a "tile".
     * <ul>
     *     <li>The tiles collected must cover the most longitudinal distance per pixel
     *     (LonDPP) possible, while still covering less than or equal to the amount of
     *     longitudinal distance per pixel in the query box for the user viewport size. </li>
     *     <li>Contains all tiles that intersect the query bounding box that fulfill the
     *     above condition.</li>
     *     <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     * </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     * forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        Map<String, Object> results = new HashMap<>();
        if (!checkfIfParamsAreValid(params)) {
            results.put("render_grid", null);
            results.put("raster_ul_lon", null);
            results.put("raster_ul_lat", null);
            results.put("raster_lr_lon", null);
            results.put("raster_lr_lat", null);
            results.put("depth", -1);
            results.put("query_success", false);
        }

        double ullon = params.get("ullon");
        double ullat = params.get("ullat");
        double lrlon = params.get("lrlon");
        double lrlat = params.get("lrlat");
        double w = params.get("w");
        double h = params.get("h");

        double queryLonDPP = (lrlon - ullon) / w;
        int imageDepth = getImageDepth(queryLonDPP);
        int nCols = 1 << imageDepth; // 2 ^ imageDepth
        double lonDegreesPerColumn = (ROOT_LRLON - ROOT_ULLON) / nCols;
        double latDegreesPerRow = (ROOT_ULLAT - ROOT_LRLAT) / nCols;

        int xRasterUllon = (int) Math.floor((ullon - ROOT_ULLON) / lonDegreesPerColumn);
        int xRasterLrlon = (int) Math.ceil((lrlon - ROOT_ULLON) / lonDegreesPerColumn);
        double rasterUllon = ROOT_ULLON + xRasterUllon * lonDegreesPerColumn;
        double rasterLrlon = ROOT_ULLON + xRasterLrlon * lonDegreesPerColumn;

        int yRasterUllat = (int) Math.floor((ROOT_ULLAT - ullat) / latDegreesPerRow);
        int yRasterLrlat = (int) Math.ceil((ROOT_ULLAT - lrlat) / latDegreesPerRow);
        double rasterUllat = ROOT_ULLAT - yRasterUllat * latDegreesPerRow;
        double rasterLrlat = ROOT_ULLAT - yRasterLrlat * latDegreesPerRow;

        String[][] grid = new String[yRasterLrlat - yRasterUllat][xRasterLrlon - xRasterUllon];
        for (int y = yRasterUllat; y < yRasterLrlat; y++) {
            for (int x = xRasterUllon; x < xRasterLrlon; x++) {
                grid[y - yRasterUllat][x - xRasterUllon] = "d" + imageDepth + "_x" + x + "_y" + y + ".png";
            }
        }

        results.put("render_grid", grid);
        results.put("raster_ul_lon", rasterUllon);
        results.put("raster_ul_lat", rasterUllat);
        results.put("raster_lr_lon", rasterLrlon);
        results.put("raster_lr_lat", rasterLrlat);
        results.put("depth", imageDepth);
        results.put("query_success", true);

        return results;
    }

    private int getImageDepth(double queryBoxLonDPP) {
        int desiredImageDepth = 0;
        double desiredLonDPP = (ROOT_LRLON - ROOT_ULLON) / TILE_SIZE;

        while (desiredLonDPP >= queryBoxLonDPP && desiredImageDepth < 7) {
            desiredLonDPP /= 2.0;
            desiredImageDepth += 1;
        }

        return desiredImageDepth;
    }

    private boolean checkfIfParamsAreValid(Map<String, Double> params) {
        double ullon = params.get("ullon");
        double ullat = params.get("ullat");
        double lrlon = params.get("lrlon");
        double lrlat = params.get("lrlat");

        if (ullon > lrlon || ullat < lrlat) {
            return false;
        }

        return true;
    }
}
