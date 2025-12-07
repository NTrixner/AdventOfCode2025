package eu.ntrixner.aoc.day7;

import eu.ntrixner.aoc.ChallengeRunner;
import eu.ntrixner.aoc.utils.ArrayHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Laboratories implements ChallengeRunner<String> {
    String input;
    
    @Override
    public void init(String params) {
        input = params;    
    }

    @Override
    public void run() {
        char[][] matrix = ArrayHelper.toMatrix(input);
        
        List<MutablePair<Integer, Integer>> activeBeams = new ArrayList<>();
        
        for(int y = 0; y < matrix.length; y++) {
            for(int x = 0; x < matrix[y].length; x++) {
                if('S' ==  matrix[y][x]) {
                    activeBeams.add(MutablePair.of(y, x));
                }
            }
        }
        int splits = 0;
        while(!activeBeams.isEmpty()) {
            boolean beamActive = true;
            MutablePair<Integer, Integer> currentBeam = activeBeams.getLast();
            int x = currentBeam.getRight();
            int y = currentBeam.getLeft();
            while(beamActive) {
                if(matrix[y][x] == 'S') {
                    y++;
                } else if(matrix[y][x] == '.'){
                    matrix[y][x] = '|';
                    y++;
                } else if(matrix[y][x] == '^') {
                    boolean split = false;
                    if(x > 0 && matrix[y][x -1] == '.' && !activeBeams.contains(MutablePair.of(y, x-1))) {
                        activeBeams.add(MutablePair.of(y, x - 1));
                        split = true;
                    }
                    if(x < matrix.length - 1 && matrix[y][x +1] == '.' && !activeBeams.contains(MutablePair.of(y, x+1))) {
                        activeBeams.add(MutablePair.of(y, x + 1));
                        split = true;
                    }
                    if(split) {
                        splits++;
                    }
                    activeBeams.remove(currentBeam);
                    beamActive = false;
                } 
                if(y > matrix.length - 1 || matrix[y][x] == '|') {
                    activeBeams.remove(currentBeam);
                    beamActive = false;
                }
            }
        }
        
        log.info("Beams were split {} times", splits);
    }
}
