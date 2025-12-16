package eu.ntrixner.aoc;


import eu.ntrixner.aoc.day1.SecretEntrance;
import eu.ntrixner.aoc.day1.SecretEntrance2;
import eu.ntrixner.aoc.day10.Factory;
import eu.ntrixner.aoc.day10.Factory2;
import eu.ntrixner.aoc.day11.Generator;
import eu.ntrixner.aoc.day11.Generator2;
import eu.ntrixner.aoc.day12.TreeFarm;
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
import eu.ntrixner.aoc.day7.Laboratories;
import eu.ntrixner.aoc.day7.Laboratories2;
import eu.ntrixner.aoc.day8.Playground;
import eu.ntrixner.aoc.day8.Playground2;
import eu.ntrixner.aoc.day9.MovieTheater;
import eu.ntrixner.aoc.day9.MovieTheater2;
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
        put("Laboratories", new Laboratories());
        put("Laboratories2", new Laboratories2());
        put("Playground", new Playground());
        put("Playground2", new Playground2());
        put("MovieTheater", new MovieTheater());
        put("MovieTheater2", new MovieTheater2());
        put("Factory", new Factory());
        put("Factory2", new Factory2());
        put("Generator", new Generator());
        put("Generator2", new Generator2());
        put("TreeFarm", new TreeFarm());
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
