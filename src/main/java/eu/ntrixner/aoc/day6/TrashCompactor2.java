package eu.ntrixner.aoc.day6;

import eu.ntrixner.aoc.ChallengeRunner;
import eu.ntrixner.aoc.utils.ArrayHelper;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        ArrayHelper.rotate(matrix, CCW, 1);
        log.info("The total of the math homework was {}", sum);
    }
}
