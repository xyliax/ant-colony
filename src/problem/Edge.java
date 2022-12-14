package problem;

import lombok.Value;

@Value
public class Edge {
    Node from;
    Node to;
    double distance;

    public Edge(Node from, Node to, String kind) {
        this.from = from;
        this.to = to;
        double fromX = from.x(), fromY = from.y();
        double toX = to.x(), toY = to.y();
        if (kind.equals("EUC_2D"))
            this.distance = Math.sqrt((fromX - toX) * (fromX - toX) + (fromY - toY) * (fromY - toY));
        else if (kind.equals("LAT_LONG")) {
            double a2r = Math.PI / 180;
            double C = Math.cos(from.y() * a2r) * Math.cos(to.y() * a2r) * Math.cos((from.x() - to.x()) * a2r)
                       + Math.sin(from.y() * a2r) * Math.sin(to.y() * a2r);
            this.distance = 6371.004 * Math.acos(C);
        } else throw new RuntimeException(String.format("Unknown graph kind '%s'!", kind));
    }
}
