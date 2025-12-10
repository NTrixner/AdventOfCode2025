package eu.ntrixner.aoc.day10;

import com.google.ortools.Loader;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.CpSolver;
import com.google.ortools.sat.CpSolverStatus;
import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.LinearExpr;
import com.google.ortools.sat.LinearExprBuilder;
import eu.ntrixner.aoc.ChallengeRunner;
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
public class Factory2 implements ChallengeRunner<String> {
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

        int totalMinimalComboSize = Arrays.stream(machines)
                .parallel()
                .map(String::trim)
                .mapToInt(machine -> {
                    Machine m = getMachine(machine);
                    return m.getMinimumComboSize();
                })
                .sum();

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
        int[][] buttons;
        int[] joltages; //probably used in part 2

        Machine(String indicatorText, String[] buttonsTexts, String joltagesText) {
            indicators = new boolean[indicatorText.length()];
            for(int i = 0; i < indicatorText.length(); i++){
                indicators[i] = indicatorText.toCharArray()[i] == '#';
            }
            buttons = new int[buttonsTexts.length][indicators.length];
            for(int i = 0; i < buttonsTexts.length; i++){
                String button = buttonsTexts[i];
                List<Integer> indizes = Arrays.stream(button.split(",")).map(Integer::parseInt).toList();
                for(int j = 0; j < buttons[i].length; j++){
                    buttons[i][j] = indizes.contains(j) ? 1 : 0;
                }
            }
            joltages = Arrays.stream(joltagesText.split(",")).mapToInt(Integer::parseInt).toArray();
        }

        public int getMinimumComboSize() {
            Loader.loadNativeLibraries();
            CpModel cpModel = new CpModel();
            int numOfButtons = buttons.length;
            int numCircuits =  joltages.length;

            //Upper bound - total of all joltages
            int maxPressesPerButton = Arrays.stream(joltages).sum();

            // Decision variables - number of times button is pressed
            IntVar[] presses = new IntVar[numOfButtons];
            for(int i = 0; i < numOfButtons; i++){
                presses[i] = cpModel.newIntVar(0, maxPressesPerButton, "button_" + i);
            }

            //Constraints - sum of button presses per target joltage
            for(int i = 0; i < numCircuits; i++){
                LinearExprBuilder sum = LinearExpr.newBuilder();
                for(int b = 0; b < numOfButtons; b++){
                    int delta = buttons[b][i];
                    if(delta != 0){
                        sum.addTerm(presses[b], delta);
                    }
                }
                cpModel.addEquality(sum, joltages[i]);
            }

            //Objective: minimize
            LinearExpr totalPresses = LinearExpr.sum(presses);
            cpModel.minimize(totalPresses);

            //Solve
            CpSolver solver = new CpSolver();
            solver.getParameters().setNumSearchWorkers(Runtime.getRuntime().availableProcessors());

            CpSolverStatus status = solver.solve(cpModel);

            if(status == CpSolverStatus.OPTIMAL || status == CpSolverStatus.FEASIBLE) {
                int minPresses = (int) solver.value(totalPresses);
                log.info("One machine done with {} button presses", minPresses);
                return minPresses;
            }
            else {
                log.warn("machine didn't solve optimally or feasibly");
                return -1;
            }
        }
    }
}
