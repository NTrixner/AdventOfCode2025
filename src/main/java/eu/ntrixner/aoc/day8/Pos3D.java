package eu.ntrixner.aoc.day8;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Pos3D {
    double x, y, z;
    Groupings grouping;

    public Pos3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.grouping = new Groupings();
        this.grouping.getNodes().add(this);
        this.grouping.setTotalDistance(1);
    }

    public double distance(Pos3D other) {
        return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2) + Math.pow(z - other.z, 2));
    }

    public String toString(){
        return x + "," + y + "," + z;
    }
}
