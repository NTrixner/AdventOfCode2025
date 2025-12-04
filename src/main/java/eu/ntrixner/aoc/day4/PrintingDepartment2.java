package eu.ntrixner.aoc.day4;

import eu.ntrixner.aoc.ChallengeRunner;
import eu.ntrixner.aoc.utils.ArrayHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PrintingDepartment2 implements ChallengeRunner<String> {
    String input;

    @Override
    public void init(String params) {
        input = params;
    }

    @Override
    public void run() {
        char[][] matrix = ArrayHelper.toMatrix(input);
        int count = 0;
        List<Pair<Integer, Integer>> removables;
        do {
            removables = findRemovables(matrix);
            count += removables.size();
            remove(matrix, removables);
        } while (!removables.isEmpty());

        log.info("{} could be removed", count);
    }

    private void remove(char[][] matrix, List<Pair<Integer, Integer>> removables) {
        for (Pair<Integer, Integer> removable : removables) {
            matrix[removable.getLeft()][removable.getRight()] = '.';
        }
    }

    private static List<Pair<Integer, Integer>> findRemovables(char[][] matrix) {
        List<Pair<Integer, Integer>> toRemove = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == '@') {
                    char[] neighbours = ArrayHelper.find8Neighbours(matrix, i, j);
                    int qCount = 0;
                    for (char neighbour : neighbours) {
                        if (neighbour == '@') {
                            qCount++;
                        }
                    }
                    if (qCount < 4) {
                        toRemove.add(Pair.of(i, j));
                    }
                }
            }
        }
        return toRemove;
    }
}
