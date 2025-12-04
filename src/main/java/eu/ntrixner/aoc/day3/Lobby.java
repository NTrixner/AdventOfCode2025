package eu.ntrixner.aoc.day3;

import eu.ntrixner.aoc.ChallengeRunner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
public class Lobby implements ChallengeRunner<String> {
    String input;

    @Override
    public void init(String params) {
        input = params;
    }

    @Override
    public void run() {
        List<String> banks = List.of(input.split("\n"));

        int[] batteryTotals = banks
                .stream()
                .mapToInt(bank -> {
                    char[] chars = bank.trim().toCharArray();
                    int[] batteries = new int[chars.length];
                    for (int i = 0; i < chars.length; i++) {
                        batteries[i] = Integer.parseInt(chars[i] + "");
                    }

                    Pair<Integer, Integer> biggest = findBiggest(batteries);

                    if (biggest.getLeft() == batteries.length - 1) {
                        //biggest number is on the end, find second biggest number as ten-digit
                        Pair<Integer, Integer> secondBiggest = findBiggest(Arrays.copyOfRange(batteries, 0, batteries.length - 1));
                        return secondBiggest.getRight() * 10 + biggest.getRight();
                    } else {
                        //biggest number is not on the end, find biggest number right of it
                        Pair<Integer, Integer> secondBiggest = findBiggest(Arrays.copyOfRange(batteries, biggest.getLeft() + 1, batteries.length));
                        return biggest.getRight() * 10 + secondBiggest.getRight();
                    }
                })
                .toArray();
        log.info("Sum of voltage is {}", IntStream.of(batteryTotals).sum());
    }

    public Pair<Integer, Integer> findBiggest(int[] array) {
        int biggest = 0;
        int index = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > biggest) {
                biggest = array[i];
                index = i;
            }
        }
        return Pair.of(index, biggest);
    }
}
