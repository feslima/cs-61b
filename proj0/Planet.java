public class Planet {
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;

    public static double G = 6.67e-11;

    public Planet(double xP, double yP, double xV, double yV, double m, String img){
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }

    public Planet(Planet p) {
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        mass = p.mass;
        imgFileName = p.imgFileName;
    }

    public double calcDistance(Planet p) {
        double dx = this.xxPos - p.xxPos;
        double dy = this.yyPos - p.yyPos;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double calcForceExertedBy(Planet p) {
        double r = this.calcDistance(p);
        double F = (G * this.mass * p.mass) / (r * r);
        return F;
    }

    public double calcForceExertedByX(Planet p) {
        double dx = p.xxPos - this.xxPos;
        double r = this.calcDistance(p);
        if (r == 0.0) {
            return 0.0;
        }
        return this.calcForceExertedBy(p) * dx / r;
    }

    public double calcNetForceExertedByX(Planet[] planets) {
        double result = 0.0;
        for (int i = 0; i < planets.length; i++) {
            result += this.calcForceExertedByX(planets[i]);
        }
        return result;
    }

    public double calcForceExertedByY(Planet p) {
        double dy = p.yyPos - this.yyPos;
        double r = this.calcDistance(p);
        if (r == 0.0) {
            return 0.0;
        }
        return this.calcForceExertedBy(p) * dy / r;
    }

    public double calcNetForceExertedByY(Planet[] planets) {
        double result = 0.0;
        for (int i = 0; i < planets.length; i++) {
            result += this.calcForceExertedByY(planets[i]);
        }
        return result;
    }

    public void update(double t, double Fx, double Fy) {
        double ax = Fx / this.mass;
        double ay = Fy / this.mass;

        double newXVel = this.xxVel + ax * t;
        double newYVel = this.yyVel + ay * t;

        double newXPos = this.xxPos + newXVel * t;
        double newYPos = this.yyPos + newYVel * t;

        this.xxVel = newXVel;
        this.yyVel = newYVel;

        this.xxPos = newXPos;
        this.yyPos = newYPos;
    }

    public void draw() {
        StdDraw.picture(this.xxPos, this.yyPos, "./images/" + this.imgFileName);
    }
}
