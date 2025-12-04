package eu.ntrixner.aoc.day1;

import eu.ntrixner.aoc.ChallengeRunner;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Slf4j
@Data
public class SecretEntrance2 implements ChallengeRunner<String> {
    public String input;

    @Override
    public void run() {
        int value = 50;
        int count = 0;
        List<String> lines = List.of(input.split("\n"));
        for(String line : lines){
            if(StringUtils.isBlank(line)){
                continue;
            }
            char direction =  line.charAt(0);
            String amountS = line.substring(1).trim();
            int amount =  Integer.parseInt(amountS);
            int dir = (direction == 'L' ? -1 : 1);

            while(amount > 0) {
                value += dir;
                if(value == 100) {
                    value = 0;
                }
                if(value == -1) {
                    value = 99;
                }
                if(value == 0) {
                    count++;
                }
                amount--;
            }
        }
        log.info("Passed 0 {} times", count);
    }

    @Override
    public void init(String params) {
        this.input = params;
    }
}
