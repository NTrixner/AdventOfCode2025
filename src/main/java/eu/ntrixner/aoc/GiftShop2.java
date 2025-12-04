package eu.ntrixner.aoc;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class GiftShop2 implements ChallengeRunner<String> {
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
        //use i as the amount of repetitions
        for (int i = 2; i <= val.length(); i++) {
            if(val.length() % i != 0)
                continue;
            String[] partials = splitToChunks(val, i);
            if(allChunksSame(partials))
                return true;
        }
        return false;

    }

    private boolean allChunksSame(String[] partials) {
        for(int i = 0; i < partials.length - 1; i++){
            if(!partials[i].equals(partials[i+1]))
                return false;
        }
        return true;
    }

    private String[] splitToChunks(String val, int numChunks) {
        int chunkSize = val.length() / numChunks;
        String[] chunks = new String[numChunks];

        for(int i = 0; i < numChunks; i++) {
            int start = i * chunkSize;
            int end = (i + 1) * chunkSize;
            chunks[i] = val.substring(start, end);
        }
        return chunks;
    }
}
