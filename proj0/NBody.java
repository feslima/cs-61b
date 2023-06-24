public class NBody {

    public static void main(String[] args) {
        double T = Double.parseDouble(args[0]);
        double dT = Double.parseDouble(args[1]);
        String filename = args[2];
        Planet[] planets = NBody.readPlanets(filename);
        int nPlanets = planets.length;
        double radius = NBody.readRadius(filename);

        StdDraw.setXscale(-radius, radius);
        StdDraw.setYscale(-radius, radius);
        StdDraw.clear();
        StdDraw.enableDoubleBuffering();

        double time = 0.0;

        while (time <= T) {
            double[] xForces = new double[nPlanets];
            double[] yForces = new double[nPlanets];

            for (int i = 0; i < nPlanets; i++) {
                xForces[i] = planets[i].calcNetForceExertedByX(planets);
                yForces[i] = planets[i].calcNetForceExertedByY(planets);
            }
            for (int i = 0; i < nPlanets; i++) {
                planets[i].update(dT, xForces[i], yForces[i]);
            }

            StdDraw.picture(0, 0, "./images/starfield.jpg");

            for (int i = 0; i < nPlanets; i++) {
                planets[i].draw();
            }

            StdDraw.show();
            StdDraw.pause(10);
            time += dT;
        }

        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                    planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
        }
    }

    public static double readRadius(String txtPath) {
        In in = new In(txtPath);
        in.readInt(); // read number of planets
        double radius = in.readDouble();
        return radius;
    }

    public static Planet[] readPlanets(String txtPath) {
        In in = new In(txtPath);
        int nPlanets = in.readInt();
        in.readDouble(); // read radius

        Planet[] planets = new Planet[nPlanets];

        for (int i = 0; i < nPlanets; i++) {
            planets[i] = new Planet(in.readDouble(),
                    in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(), in.readString());
        }

        return planets;
    }
}
