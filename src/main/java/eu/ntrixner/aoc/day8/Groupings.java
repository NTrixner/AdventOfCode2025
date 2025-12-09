package eu.ntrixner.aoc.day8;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Groupings {
    List<Pos3D> nodes = new ArrayList<>();
    double totalDistance = 0;

    public void combine(Groupings other) {
        for(Pos3D b : other.getNodes()){
            nodes.add(b);
            b.setGrouping(this);
            totalDistance+=1;
        }
        other.getNodes().clear();
        other.setTotalDistance(0);
    }

}
