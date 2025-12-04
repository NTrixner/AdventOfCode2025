package eu.ntrixner.aoc.day4;

import eu.ntrixner.aoc.ChallengeRunner;
import eu.ntrixner.aoc.utils.ArrayHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrintingDepartment implements ChallengeRunner<String> {
    String input;
    @Override
    public void init(String params) {
        input = params;
    }

    @Override
    public void run() {
        char[][] matrix = ArrayHelper.toMatrix(input);
        int count = 0;

        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[i].length; j++){
                if(matrix[i][j] == '@'){
                    char[] neighbours = ArrayHelper.find8Neighbours(matrix, i, j);
                    int qCount = 0;
                    for (char neighbour : neighbours) {
                        if (neighbour == '@') {
                            qCount++;
                        }
                    }
                    if(qCount < 4){
                        count++;
                    }
                }
            }
        }
        log.info("{} rolls have fewer than 4 neighbours", count);
    }
}
