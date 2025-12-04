package eu.ntrixner.aoc.day3;

import eu.ntrixner.aoc.ChallengeRunner;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Lobby2 implements ChallengeRunner<String> {
    String input;

    @Override
    public void init(String params) {
        input = params;
    }

    @Override
    public void run() {
        List<String> banks = List.of(input.split("\n"));

        BigInteger[] batteryTotals = banks
                .stream()
                .map(bank -> {
                    String newBank = shortenToTargetSize(bank.trim(), 12);

                    return new BigInteger(newBank);
                })
                .toArray(BigInteger[]::new);
        BigInteger sum = new BigInteger("0");
        for (BigInteger bigInteger : batteryTotals) {
            sum = sum.add(bigInteger);
        }
        log.info("Sum of voltage is {}", sum);
    }

    public String shortenToTargetSize(String input, int length) {
        String out = input;
        int toRemove = input.length() - length;
        for (int r = toRemove; r > 0; r--) {
            List<String> permutations = getStringListWithEachCharacterRemoved(out);
            out = permutations.stream().map(BigInteger::new).max(BigInteger::compareTo).get().toString();
        }
        return out;
    }

    private List<String> getStringListWithEachCharacterRemoved(String out) {
        List<String> permutations = new ArrayList<>();
        for (int i = 0; i < out.length(); i++) {
            //remove character at i index
            permutations.add(out.substring(0, i) + out.substring(i + 1));
        }
        return permutations;
    }
}
