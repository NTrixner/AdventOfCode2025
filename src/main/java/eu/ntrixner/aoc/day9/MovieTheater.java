package eu.ntrixner.aoc.day9;

import eu.ntrixner.aoc.ChallengeRunner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class MovieTheater implements ChallengeRunner<String> {
    String input;

    @Override
    public void init(String params) {
        this.input = params;
    }

    @Override
    public void run() {
        String[] lines = input.split("\n");
        List<Pair<Long, Long>> redTiles = new ArrayList<>();

        for(String line : lines){
            String[] pair = line.trim().split(",");
            redTiles.add(Pair.of(Long.parseLong(pair[0]), Long.parseLong(pair[1])));
        }

        redTiles.sort(Comparator.comparing(p -> Math.sqrt(Math.pow(p.getLeft(), 2) + Math.pow(p.getRight(), 2))));

        long maxSize = 0;
        for(int i = 0; i < redTiles.size() / 2; i++) {
            for(int j = redTiles.size() - 1; j >= redTiles.size() / 2; j--) {
                Pair<Long, Long> a =  redTiles.get(i);
                Pair<Long, Long> b = redTiles.get(j);

                long x1 =  Math.min(a.getLeft(), b.getLeft());
                long y1 =  Math.min(a.getRight(), b.getRight());
                long x2 =  Math.max(a.getLeft(), b.getLeft());
                long y2 =  Math.max(a.getRight(), b.getRight());

                long size = (x2 - x1 + 1) * (y2 - y1 + 1);

                if(maxSize < size) {
                    maxSize = size;
                }
            }
        }

        log.info("Max size: {}", maxSize);
    }
}
