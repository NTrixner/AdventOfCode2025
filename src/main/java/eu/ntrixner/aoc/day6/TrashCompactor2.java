package eu.ntrixner.aoc.day6;

import eu.ntrixner.aoc.ChallengeRunner;
import eu.ntrixner.aoc.utils.ArrayHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static eu.ntrixner.aoc.utils.ArrayHelper.Direction.CCW;

@Slf4j
public class TrashCompactor2 implements ChallengeRunner<String> {
    private String input;

    @Override
    public void init(String params) {
        input = params;
    }

    @Override
    public void run() {
        //Rotate the whole fucking thing.
        char[][] matrix = ArrayHelper.toMatrix(input);
        char[][] rotated = ArrayHelper.rotate(matrix, CCW, 1);
        
        //change back to lines
        List<String> lines = ArrayHelper.toLines(rotated);
        
        BigInteger sum = BigInteger.ZERO;
        List<BigInteger> partialSum = new ArrayList<>();
        
        for(String line : lines) {
            if(line.endsWith("*") || line.endsWith("+")) {
                char sign =  line.charAt(line.length() - 1);
                String lineWithoutEnd  = line.substring(0, line.length() - 1).trim();
                BigInteger number =  new BigInteger(lineWithoutEnd);
                partialSum.add(number);
                if ('+' == sign) {
                    sum = sum.add(partialSum.stream().reduce(BigInteger.ZERO, BigInteger::add));
                } else {
                    sum =sum.add(partialSum.stream().reduce(BigInteger.ONE, BigInteger::multiply));
                }
                partialSum.clear();
            } else if(!StringUtils.isEmpty(line.trim())) {
                partialSum.add(new BigInteger(line.trim()));
            }
        }
        log.info("The total of the math homework was {}", sum);
    }
}
