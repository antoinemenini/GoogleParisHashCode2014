/**
 * Created by guillaume on 4/5/14.
 */
public class Point {
    public double lat, lon;

    Point(){
        lat = 0;
        lon = 0;
    }

    Point(double x, double y){
        lat = x;
        lon = y;
    }

    static Point vect(Point a, Point b){
        return new Point(b.lat-a.lat, b.lon-a.lon);
    }

    void add(Point p){
        this.lat += p.lat;
        this.lon += p.lon;
    }



    void mul(double r){
        this.lat *= r;
        this.lon *= r;
    }

    double squaredDist(Point p){
        double dx = lon-p.lon;
        double dy = lat-p.lat;
        return (dx*dx) + (dy*dy);
    }

    double dotProduct(Point p){
        return (lon*p.lon) + (lat*p.lat);
    }


}
