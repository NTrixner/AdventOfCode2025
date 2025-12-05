package eu.ntrixner.aoc.day5;

import eu.ntrixner.aoc.ChallengeRunner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Cafeteria implements ChallengeRunner<String> {
    String input;

    @Override
    public void init(String params) {
        input = params;
    }

    @Override
    public void run() {
        List<Pair<Long, Long>> idRanges = new ArrayList<>();
        List<Long> ids =  new ArrayList<>();

        boolean rangesDone = false;
        //Read file
        List<String> lines = List.of(input.split("\n"));
        for (String line : lines) {
            String clean = line.trim();
            if(!rangesDone) {
                if(StringUtils.isEmpty(clean)) {
                    rangesDone = true;
                } else {
                    String[] rangeString = clean.split("-");
                    Pair<Long, Long> range = Pair.of(Long.parseLong(rangeString[0]), Long.parseLong(rangeString[1]));
                    idRanges.add(range);
                }
            } else {
                ids.add(Long.parseLong(clean));
            }
        }

        //Check ids vs ranges
        long count = ids
                .stream()
                .filter(id -> idRanges.stream().anyMatch(range -> rangeContains(range, id)))
                .count();
        log.info("There are {} fresh ingredients", count);
    }

    private boolean rangeContains(Pair<Long, Long> range, Long id) {
        return id >=  range.getLeft() && id <= range.getRight();
    }
}
