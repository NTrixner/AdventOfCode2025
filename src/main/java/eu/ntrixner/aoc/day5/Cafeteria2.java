package eu.ntrixner.aoc.day5;

import eu.ntrixner.aoc.ChallengeRunner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class Cafeteria2 implements ChallengeRunner<String> {
    String input;

    @Override
    public void init(String params) {
        input = params;
    }

    @Override
    public void run() {
        List<MutablePair<Long, Long>> idRanges = new ArrayList<>();

        //Read file
        List<String> lines = List.of(input.split("\n"));
        for (String line : lines) {
            String clean = line.trim();
            if (StringUtils.isEmpty(clean)) {
                break;
            } else {
                String[] rangeString = clean.split("-");
                MutablePair<Long, Long> range = MutablePair.of(Long.parseLong(rangeString[0]), Long.parseLong(rangeString[1]));
                idRanges.add(range);
            }
        }

        idRanges = idRanges.stream().sorted(Comparator.comparing(MutablePair::getLeft)).collect(Collectors.toList());

        //As seen on Baeldung
        ArrayList<MutablePair<Long, Long>> mergedRanges = new ArrayList<>();
        mergedRanges.add(idRanges.getFirst());

        idRanges.forEach(r -> {
            MutablePair<Long, Long> lastMerged = mergedRanges.getLast();
            if(r.getLeft() <= lastMerged.getRight()) {
                lastMerged.setRight(Math.max(lastMerged.getRight(), r.getRight()));
            } else {
                mergedRanges.add(r);
            }
        });
        long count = mergedRanges.stream().mapToLong(this::countInRange).sum();
        log.info("There are {} fresh ingredient IDs", count);
    }


    private long countInRange(Pair<Long, Long> range) {
        return range.getRight() - range.getLeft() + 1;
    }
}
