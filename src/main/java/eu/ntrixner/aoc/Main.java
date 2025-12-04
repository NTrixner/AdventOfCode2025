package eu.ntrixner.aoc;


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
    }};

    static void main(String[] args) {
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
    }
}
