package eu.ntrixner.aoc.day11;

import eu.ntrixner.aoc.ChallengeRunner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class Generator implements ChallengeRunner<String> {
    private String input;
    @Override
    public void init(String params) {
        this.input = params;
    }

    @Override
    public void run() {
        String[] lines =  input.split("\n");
        List<Device> devices = new ArrayList<>();
        for(String line : lines) {
            String deviceName = line.substring(0, line.indexOf(":"));
            String[] connections = line.substring(line.indexOf(":") + 1).split(" ");
            List<String> conStrings = Stream.of(connections).map(String::trim).filter(c -> !StringUtils.isAllBlank(c)).toList();

            devices.add(new Device(deviceName, new ArrayList<>(), conStrings));
        }

        //Since there is no actual "out" device, we need to create it manually
        Device out = new Device("out", null, null);

        for(Device device : devices) {
            List<Device> collect = devices.stream().filter(d -> device.connectionNames.contains(d.name)).toList();
            device.connections.addAll(collect);
            if(device.connectionNames.contains("out")){
                device.connections.add(out);
            }
        }

        Device start = devices.stream().filter(d -> "you".equals(d.name)).findFirst().orElseThrow();

        List<List<Device>> paths = dfs(start, new ArrayList<>(), "out").stream().filter(l -> !l.isEmpty()).toList();

        log.info("There are {} paths", paths.size());


    }

    private List<List<Device>> dfs(Device current, List<Device> path, String target) {
        List<Device> newPath = path.stream().collect(Collectors.toList());
        newPath.add(current);
        List<List<Device>> paths = new ArrayList<>();
        if(target.equals(current.name)) {
            paths.add(newPath);
            return paths;
        }
        for(Device next : current.connections) {
            if(!newPath.contains(next)) {
                paths.addAll(dfs(next, newPath, target));
            }
        }

        return paths;
    }

    @Data
    @AllArgsConstructor
    private static class Device {
        String name;
        List<Device> connections;
        List<String> connectionNames;
        public String toString(){
            return name;
        }
    }
}
