package eu.ntrixner.aoc.day10;

import eu.ntrixner.aoc.ChallengeRunner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class Factory implements ChallengeRunner<String> {
    Pattern squarePattern = Pattern.compile("\\[(.*?)\\]");
    Pattern parenPattern = Pattern.compile("\\((.*?)\\)");
    Pattern curlyPattern = Pattern.compile("\\{(.*?)\\}");

    private String input;
    @Override
    public void init(String params) {
        this.input = params;
    }

    @Override
    public void run() {
        String[] machines =  input.split("\n");

        int totalMinimalComboSize = 0;
        for(String machine : machines){
            Machine m = getMachine(machine);
            int comboSize = m.getMinimumComboSize();
            totalMinimalComboSize += comboSize;
        }

        log.info("Total minimal button presses is " + totalMinimalComboSize);

    }

    private Machine getMachine(String machine) {
        Matcher m;

        // Extract content inside []
        m = squarePattern.matcher(machine);
        String a = m.find() ? m.group(1) : null;

        // Extract content inside ()
        List<String> bList = new ArrayList<>();
        m = parenPattern.matcher(machine);
        while (m.find()) {
            bList.add(m.group(1));
        }
        String[] b = bList.toArray(new String[0]);

        // Extract content inside {}
        m = curlyPattern.matcher(machine);
        String c = m.find() ? m.group(1) : null;

        return new Machine(a, b, c);
    }

    @Data
    public class Machine {
        boolean[] indicators;
        boolean[][] buttons;
        int[] joltages; //probably used in part 2

        Machine(String indicatorText, String[] buttonsTexts, String joltagesText) {
            indicators = new boolean[indicatorText.length()];
            for(int i = 0; i < indicatorText.length(); i++){
                indicators[i] = indicatorText.toCharArray()[i] == '#';
            }
            buttons = new boolean[buttonsTexts.length][indicators.length];
            for(int i = 0; i < buttonsTexts.length; i++){
                String button = buttonsTexts[i];
                List<Integer> indizes = Arrays.stream(button.split(",")).map(Integer::parseInt).toList();
                for(int j = 0; j < buttons[i].length; j++){
                    buttons[i][j] = indizes.contains(j);
                }
            }
            joltages = Arrays.stream(joltagesText.split(",")).mapToInt(Integer::parseInt).toArray();
        }

        public int getMinimumComboSize() {
            for(int comboSize = 1; comboSize < buttons.length; comboSize++) {
                List<List<boolean[]>> combos = getCombinations(comboSize);
                for(List<boolean[]> combo : combos) {
                    if(checkCombo(combo)) {
                        return comboSize;
                    }
                }
            }
            log.warn("Machine didn't fit any combinations!");
            return buttons.length;
        }

        List<List<boolean[]>> getCombinations(int size) {
            List<boolean[]> feed = Arrays.stream(buttons).collect(Collectors.toList());
            return getCombinationsInternal(feed, size).toList();
        }

        private Stream<List<boolean[]>> getCombinationsInternal(List<boolean[]> feed, int size) {
            int n = feed.size();
            if (size == 0) {
                return Stream.of(Collections.emptyList());
            }
            if (size > n) {
                return Stream.empty();
            }
            if (n == 0) {
                return Stream.empty();
            }

            boolean[] first = feed.get(0);
            List<boolean[]> rest = feed.subList(1, feed.size());

            Stream<List<boolean[]>> withFirst = getCombinationsInternal(rest, size - 1)
                    .map(sub -> {
                        List<boolean[]> result = new ArrayList<>(sub.size() + 1);
                        result.add(first);
                        result.addAll(sub);
                        return result;
                    });
            Stream<List<boolean[]>> withoutFirst = getCombinationsInternal(rest, size);

            return Stream.concat(withFirst, withoutFirst);
        }

        public boolean checkCombo(List<boolean[]> combo) {
            for(int i = 0; i <indicators.length; i++) {
                boolean target = indicators[i];
                boolean real = false;
                for(boolean[] button : combo) {
                    real ^= button[i];
                }
                if(real != target) {
                    return false;
                }
            }
            return true;
        }
    }
}
