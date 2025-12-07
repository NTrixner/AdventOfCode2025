package eu.ntrixner.aoc.day7;

import eu.ntrixner.aoc.ChallengeRunner;
import eu.ntrixner.aoc.utils.ArrayHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@Slf4j
public class Laboratories2 implements ChallengeRunner<String> {
    String input;
    
    @Override
    public void init(String params) {
        input = params;    
    }

    @Override
    public void run() {
        char[][] matrix = ArrayHelper.toMatrix(input);
        
        long[][] countMatrix = new long[matrix.length][matrix[0].length];
        for(int y = 0; y < matrix.length; y++){
            for(int x = 0; x < matrix[0].length; x++){
                if(matrix[y][x] == 'S'){
                    countMatrix[y][x] = 1;
                } else if(matrix[y][x] == '.'){
                    countMatrix[y][x] = 0;
                }
            }
        }
        
        int y = 0;
        do {
            long[] currentLine = countMatrix[y];
            long[] nextLine = countMatrix[y+1];
            
            for(int x = 0; x < currentLine.length; x++){
                
                if(matrix[y][x] == '^') {
                    if(x - 1 >= 0) {
                        nextLine[x - 1] += currentLine[x];
                    } 
                    if(x + 1 <= currentLine.length - 1) {
                        nextLine[x + 1] += currentLine[x];
                    }
                } else {
                    nextLine[x] += currentLine[x];
                }
            }
            y++;
        } while(y < countMatrix.length - 1);
        
        long[] lastLine = countMatrix[countMatrix.length - 1];
        long timelines = LongStream.of(lastLine).filter(i -> i != -1).sum();
        log.info("There were {} timelines", timelines);
    }
}
