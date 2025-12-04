package eu.ntrixner.aoc;

import lombok.extern.slf4j.Slf4j;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Slf4j
public class GiftShop implements ChallengeRunner<String> {
    String input;
    List<Long> invalidIds = new ArrayList<>();


    @Override
    public void init(String params) {
        input = params;
    }

    @Override
    public void run() {
        List<String> ranges = List.of(input.split(","));
        ranges.forEach(this::checkRange);
        long sum = invalidIds.stream().mapToLong(i -> i).sum();
        log.info("Sum of all invalid IDs is {}", sum);
    }

    private void checkRange(String s) {
        String[] range = s.split("-");
        if (range.length != 2) {
            throw new IllegalArgumentException();
        }
        List<Long> values = new ArrayList<>();
        long begin = Long.parseLong(range[0]);
        long end = Long.parseLong(range[1]);
        for (long i = begin; i <= end; i++) {
            values.add(i);
        }

        for (long value : values) {
            String val = Long.toString(value);
            if (checkIdInvalid(val)) {
                invalidIds.add(value);
            }
        }
    }

    private boolean checkIdInvalid(String val) {
        for (int i = 1; i <= val.length() - 1; i++) {
            String partial = val.substring(0, i);
            String rest = val.substring(i);
            if (Objects.equals(partial, rest)) {
                return true;
            }
        }
        return false;

    }
}
