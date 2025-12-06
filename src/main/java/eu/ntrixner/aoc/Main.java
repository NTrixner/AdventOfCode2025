package eu.ntrixner.aoc;


import eu.ntrixner.aoc.day1.SecretEntrance;
import eu.ntrixner.aoc.day1.SecretEntrance2;
import eu.ntrixner.aoc.day2.GiftShop;
import eu.ntrixner.aoc.day2.GiftShop2;
import eu.ntrixner.aoc.day3.Lobby;
import eu.ntrixner.aoc.day3.Lobby2;
import eu.ntrixner.aoc.day4.PrintingDepartment;
import eu.ntrixner.aoc.day4.PrintingDepartment2;
import eu.ntrixner.aoc.day5.Cafeteria;
import eu.ntrixner.aoc.day5.Cafeteria2;
import eu.ntrixner.aoc.day6.TrashCompactor;
import eu.ntrixner.aoc.day6.TrashCompactor2;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Main {
    private static final Map<String, ChallengeRunner<String>> ACTIONS = new HashMap<>() {{
        put("SecretEntrance", new SecretEntrance());
        put("SecretEntrance2", new SecretEntrance2());
        put("GiftShop", new GiftShop());
        put("GiftShop2", new GiftShop2());
        put("Lobby", new Lobby());
        put("Lobby2", new Lobby2());
        put("PrintingDepartment", new PrintingDepartment());
        put("PrintingDepartment2", new PrintingDepartment2());
        put("Cafeteria", new Cafeteria());
        put("Cafeteria2", new Cafeteria2());
        put("TrashCompactor", new TrashCompactor());
        put("TrashCompactor2", new TrashCompactor2());
    }};

    static void main(String[] args) {
        long currentTime = System.currentTimeMillis();
        try {
            if (args != null && args.length > 0) {
                String action = args[0];
                String inputFile = Files.readString(Path.of("src", "main", "resources", args[1]));
                ChallengeRunner<String> runner = ACTIONS.get(action);
                runner.init(inputFile);
                runner.run();
            }
        } catch (IOException e) {
            log.error("Error reading input file", e);
        }
        log.info("run took {}ms", System.currentTimeMillis() - currentTime);
    }
}
