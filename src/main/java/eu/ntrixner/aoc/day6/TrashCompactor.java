package eu.ntrixner.aoc.day6;

import eu.ntrixner.aoc.ChallengeRunner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TrashCompactor implements ChallengeRunner<String> {
    private String input;

    @Override
    public void init(String params) {
        input = params;
    }

    @Override
    public void run() {
        String[] lines = input.split("\n");
        BigInteger sum = BigInteger.ZERO;
        boolean done = false;
        do {
            List<BigInteger> numbers = new ArrayList<>();
            String sign = "";
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i].trim();
                int nextSpace = line.indexOf(' ') + 1;

                String numberOrSign;
                if (nextSpace != 0) {
                    numberOrSign = line.substring(0, nextSpace);
                    numberOrSign = numberOrSign.trim();
                    String newLine = line.substring(nextSpace);
                    lines[i] = newLine;
                } else {
                    numberOrSign = line.trim();
                    done = true;
                }

                if ("+".equals(numberOrSign.trim()) || "*".equals(numberOrSign.trim())) {
                    sign = numberOrSign;
                } else {
                    numbers.add(new BigInteger(numberOrSign.trim()));
                }
            }
            BigInteger result;
            if ("+".equals(sign)) {
                result = numbers.stream().reduce(BigInteger.ZERO, BigInteger::add);
            } else {
                result = numbers.stream().reduce(BigInteger.ONE, BigInteger::multiply);
            }
            sum = sum.add(result);
        } while (!done);

        log.info("The total of the math homework was {}", sum);
    }
}
