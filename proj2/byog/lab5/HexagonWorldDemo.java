package byog.lab5;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class HexagonWorldDemo {
    private static final int WIDTH = 60;
    private static final int HEIGHT = 30;

    public static TETile[][] initializeEmptyWorld() {
        TETile[][] world = new TETile[WIDTH][HEIGHT];

        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        return world;
    }

    private static void drawTopLeftQuadrant(TETile[][] world, int hexSideSize, int centerX, int centerY, int startX, int startY) {
        int marker = centerX - (hexSideSize % 2 == 0 ? hexSideSize / 2 : (hexSideSize - 1) / 2);
        int startI = hexSideSize % 2 == 0 ? startX - 1 : startX;
        for (int j = startY; j >= centerY; j--) {
            for (int i = startI; i <= centerX; i++) {
                if (i >= marker) {
                    world[i][j] = Tileset.WALL;
                }
            }
            marker -= 1;
        }
    }

    private static void drawBottomLeftQuadrant(TETile[][] world, int hexSideSize, int centerX, int centerY, int startX, int endY) {
        int marker = centerX - (hexSideSize % 2 == 0 ? hexSideSize / 2 : (hexSideSize - 1) / 2);
        int startI = hexSideSize % 2 == 0 ? startX - 1 : startX;
        for (int j = endY + 1; j < centerY; j++) {
            for (int i = startI; i < centerX; i++) {
                if (i >= marker) {
                    world[i][j] = Tileset.WALL;
                }
            }
            marker -= 1;
        }
    }

    private static void drawTopRightQuadrant(TETile[][] world, int hexSideSize, int centerX, int centerY, int endX, int startY) {
        int marker = centerX + (hexSideSize % 2 == 0 ? hexSideSize / 2 : (hexSideSize - 1) / 2);
        if (hexSideSize % 2 == 0) {
            marker -= 1;
        }

        int startI = (hexSideSize % 2 == 0) ? centerX - 1 : centerX;
        int endI = (hexSideSize % 2 == 0) ? endX - 1 : endX;

        for (int j = startY; j >= centerY; j--) {
            for (int i = startI; i <= endI; i++) {
                if (i <= marker) {
                    world[i][j] = Tileset.WALL;
                }
            }
            marker += 1;
        }
    }

    private static void drawBottomRightQuadrant(TETile[][] world, int hexSideSize, int centerX, int centerY, int endX, int endY) {
        int marker = centerX + (hexSideSize % 2 == 0 ? hexSideSize / 2 : (hexSideSize - 1) / 2);
        if (hexSideSize % 2 == 0) {
            marker -= 1;
        }

        int startI = (hexSideSize % 2 == 0) ? centerX - 1 : centerX;
        int endI = (hexSideSize % 2 == 0) ? endX - 1 : endX;

        for (int j = endY + 1; j <= centerY; j++) {
            for (int i = startI; i <= endI; i++) {
                if (i <= marker) {
                    world[i][j] = Tileset.WALL;
                }
            }
            marker += 1;
        }
    }

    public static void addHexagon(int hexSideSize, TETile[][] world) {
        int hexHeight = 2 * hexSideSize;
        int hexWidth = hexSideSize + (hexSideSize - 1) * 2;

        int centerX = WIDTH / 2;
        int centerY = HEIGHT / 2;

        int startX = hexWidth % 2 == 0 ? centerX - hexSideSize : centerX - (hexWidth - 1) / 2;
        int endX = startX + hexWidth - 1;

        int startY = centerY + hexSideSize - 1;
        int endY = startY - hexHeight;

        drawTopLeftQuadrant(world, hexSideSize, centerX, centerY, startX, startY);
        drawBottomLeftQuadrant(world, hexSideSize, centerX, centerY, startX, endY);
        drawTopRightQuadrant(world, hexSideSize, centerX, centerY, endX, startY);
        drawBottomRightQuadrant(world, hexSideSize, centerX, centerY, endX, endY);

    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] world = initializeEmptyWorld();

        addHexagon(4, world);

        ter.renderFrame(world);
    }

}
