package eu.ntrixner.aoc.day9;

import eu.ntrixner.aoc.ChallengeRunner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class MovieTheater2 implements ChallengeRunner<String> {
    String input;

    @Override
    public void init(String params) {
        this.input = params;
    }

    @Override
    public void run() {
        String[] lines = input.split("\n");
        List<Pair<Integer, Integer>> redTiles = new ArrayList<>();

        for (String line : lines) {
            String[] pair = line.trim().split(",");
            redTiles.add(Pair.of(Integer.parseInt(pair[0]), Integer.parseInt(pair[1])));
        }

        Polygon poly = new Polygon(redTiles.stream().mapToInt(Pair::getLeft).toArray(), redTiles.stream().mapToInt(Pair::getRight).toArray(), redTiles.size());

        redTiles.sort(Comparator.comparing(p -> Math.sqrt(Math.pow(p.getLeft(), 2) + Math.pow(p.getRight(), 2))));

        //Get the last corner
        long maxSize = 0;
        for (int i = 0; i < redTiles.size() - 1; i++) {
            for (int j = redTiles.size() - 1; j > i; j--) {
                Pair<Integer, Integer> a = redTiles.get(i);
                Pair<Integer, Integer> b = redTiles.get(j);

                int x1 = Math.min(a.getLeft(), b.getLeft());
                int y1 = Math.min(a.getRight(), b.getRight());
                int x2 = Math.max(a.getLeft(), b.getLeft());
                int y2 = Math.max(a.getRight(), b.getRight());

                long size = (long) (x2 - x1 + 1) * (y2 - y1 + 1);

                if (maxSize < size && allTilesRedOrGreen(poly, x1, x2, y1, y2)) {
                    maxSize = size;
                }
            }
        }


        log.info("Max size: {}", maxSize);
    }

    private boolean allTilesRedOrGreen(Polygon poly, int x1, int x2, int y1, int y2) {
        //Polygon doesn't check ON the border, so only check inner area - we already know we're ona border because
        //it's between two red tiles.
        return poly.contains(x1 + 1, y1 + 1, x2-x1 - 2, y2 - y1 - 2);
    }
}
