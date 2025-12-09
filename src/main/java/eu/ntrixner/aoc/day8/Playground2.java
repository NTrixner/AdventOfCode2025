package eu.ntrixner.aoc.day8;

import eu.ntrixner.aoc.ChallengeRunner;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.reverseOrder;

@Slf4j
public class Playground2 implements ChallengeRunner<String> {
    private String input;

    @Override
    public void init(String params) {
        this.input = params;
    }

    @Override
    public void run() {
        List<Pos3D> posList = new ArrayList<>();

        String[] lines = input.split("\n");
        for(String line : lines) {
            String[] coordinates =  line.split(",");
            double x = Double.parseDouble(coordinates[0]);
            double y = Double.parseDouble(coordinates[1]);
            double z = Double.parseDouble(coordinates[2]);
            posList.add(new Pos3D(x, y, z));
        }

        List<Groupings> groups = posList.stream().map(Pos3D::getGrouping).collect(Collectors.toList());
        List<Distance> distances = new ArrayList<>();
        for(int i = 0; i < posList.size() - 1; i++) {
            for(int j = i+1; j<posList.size(); j++){
                Pos3D a = posList.get(i);
                Pos3D b = posList.get(j);
                distances.add(new Distance(a, b, a.distance(b)));
            }
        }
        distances.sort(Comparator.comparing(Distance::getDistance));

        int i = 0;
        Distance distance;
        do {
            distance = distances.get(i);

            Pos3D a = distance.getA();
            Pos3D b = distance.getB();
            if(a.grouping != b.grouping){
                a.grouping.combine(b.grouping);
                groups = groups.stream().filter(g -> !g.getNodes().isEmpty()).collect(Collectors.toList());
            }
            i++;
        }while(groups.size() > 1);
        log.info("Multiplication of x coordinates of last connection was  {}", distance.a.x * distance.b.x);
    }
}
