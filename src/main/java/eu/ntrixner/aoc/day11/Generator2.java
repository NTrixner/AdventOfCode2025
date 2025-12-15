package eu.ntrixner.aoc.day11;

import eu.ntrixner.aoc.ChallengeRunner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class Generator2 implements ChallengeRunner<String> {
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

            devices.add(new Device(deviceName,  new ArrayList<>(), conStrings, 0, false, false, false));
        }

        //Since there is no actual "out" device, we need to create it manually
        Device out = new Device("out", new ArrayList<>(), new ArrayList<>(),  0,false, false, false);

        devices.add(out);

        Device start = devices.stream().filter(d -> "svr".equals(d.name)).findFirst().orElseThrow();
        Device fft = devices.stream().filter(d -> "fft".equals(d.name)).findFirst().orElseThrow();
        Device dac = devices.stream().filter(d -> "dac".equals(d.name)).findFirst().orElseThrow();

        for(int i = 0; i < devices.size(); i++) {
            Device device = devices.get(i);
            device.setId(i);
            List<Device> collect = devices.stream().filter(d -> device.connectionNames.contains(d.name)).toList();
            device.connections.addAll(collect);
        }

        boolean[] canVisitFft = new boolean[devices.size()];
        boolean[] canVisitOut = new boolean[devices.size()];
        boolean[] canVisitDac = new boolean[devices.size()];
        for(int i = 0; i < devices.size(); i++) {
            Device device = devices.get(i);
            device.canVisitDac = canReach(device, dac.id, devices.size());
            device.canVisitFft = canReach(device, fft.id, devices.size());
            device.canVisitOut = canReach(device, out.id, devices.size());

            canVisitFft[i] = device.canVisitFft;
            canVisitDac[i] = device.canVisitDac;
            canVisitOut[i] = device.canVisitOut;

        }
        //long count = exhaustiveDfsFast(start, "out", devices.size());

        long count2 = countPathsDag(devices, start.id, out.id, canVisitOut, canVisitDac, canVisitFft);
        //List<List<Device>> paths = dfs(start, new ArrayList<>(), "out", false, false).stream().filter(l -> !l.isEmpty()).toList();

        log.info("There are {} paths", count2);


    }

    private boolean canReach(Device start, int targetId, int deviceCount) {
        if (start.id == targetId) return true;

        boolean[] visited = new boolean[deviceCount];
        Deque<Device> stack = new ArrayDeque<>();
        stack.push(start);

        while (!stack.isEmpty()) {
            Device cur = stack.pop();
            if (cur.id == targetId) return true;

            visited[cur.id] = true;
            for (Device next : cur.connections) {
                if (!visited[next.id]) stack.push(next);
            }
        }
        return false;
    }

    //State
    //0 = neither visited
    //1 = DAC visited
    //2 = FFT visited
    //3 = both visited
    private static final int DAC = 1;
    private static final int FFT = 2;

    public static long countPathsDag(
            List<Device> devices,
            int startId,
            int outId,
            boolean[] canReachOut,
            boolean[] canReachDac,
            boolean[] canReachFft
    ) {
        int deviceCount = devices.size();

        int[] topo = topologicalOrder(devices, deviceCount);

        // waysToReachWithState[nodeId][state] = number of ways to reach nodeId with visited-state
        long[][] waysToReachWithState = new long[deviceCount][4];

        Device start = devices.get(startId);
        int startState = 0;
        if ("dac".equals(start.name)) startState |= DAC;
        if ("fft".equals(start.name)) startState |= FFT;
        waysToReachWithState[startId][startState] = 1;

        for (int idx = 0; idx < deviceCount; idx++) {
            int currentDeviceId = topo[idx];
            if (!canReachOut[currentDeviceId]) continue; // prune nodes that can't reach out immediately

            for (int state = 0; state < 4; state++) {
                long ways = waysToReachWithState[currentDeviceId][state];
                if (ways == 0) continue;

                Device from = devices.get(currentDeviceId);

                for (Device outgoingDevice : from.connections) {
                    int id = outgoingDevice.id;

                    if (!canReachOut[id]) continue;
                    if ((state & DAC) == 0 && !canReachDac[id]) continue;
                    if ((state & FFT) == 0 && !canReachFft[id]) continue;

                    int newState = state;
                    if ("dac".equals(outgoingDevice.name)) newState |= DAC;
                    if ("fft".equals(outgoingDevice.name)) newState |= FFT;

                    if ((newState & DAC) == 0 && !outgoingDevice.canVisitDac) continue;
                    if ((newState & FFT) == 0 && !outgoingDevice.canVisitFft) continue;
                    if (!outgoingDevice.canVisitOut && id != outId) continue;

                    waysToReachWithState[id][newState] += ways;
                }
            }
        }

        return waysToReachWithState[outId][DAC | FFT];
    }

    // Topological sort (Kahn). If not all nodes emitted -> graph has a cycle.
    private static int[] topologicalOrder(List<Device> devices, int deviceCount) {
        int[] inConnsPerDev = new int[deviceCount];
        //Add +1 for each connection that leads to the device
        devices.forEach(device -> device.connections.forEach(conn -> inConnsPerDev[conn.id]++));

        Deque<Integer> noIncomingIdQueue = new ArrayDeque<>();
        for (int i = 0; i < deviceCount; i++) if (inConnsPerDev[i] == 0) noIncomingIdQueue.add(i);

        int[] topo = new int[deviceCount];
        int topoIndex = 0;
        while (!noIncomingIdQueue.isEmpty()) {
            int deviceId = noIncomingIdQueue.removeFirst();
            topo[topoIndex++] = deviceId;
            for (Device outgoing : devices.get(deviceId).connections) {
                if (--inConnsPerDev[outgoing.id] == 0) noIncomingIdQueue.add(outgoing.id);
            }
        }
        if (topoIndex != deviceCount) {
            //And this is where we learned that I completely overengineered earlier with DFS
            throw new IllegalStateException("Graph has cycles; DAG DP not applicable.");
        }
        return topo;
    }

    @Data
    @AllArgsConstructor
    private static class Device {
        String name;
        List<Device> connections;
        List<String> connectionNames;
        int id;
        boolean canVisitDac = false;
        boolean canVisitFft = false;
        boolean canVisitOut = false;
        public String toString(){
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Device device = (Device) o;
            return Objects.equals(name, device.name);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(name);
        }
    }

    private static final class Frame {
        final Device device;
        int nextIndex;
        final boolean prevFft;
        final boolean prevDac;

        Frame(Device device, boolean prevFft, boolean prevDac) {
            this.device = device;
            this.prevFft = prevFft;
            this.prevDac = prevDac;
            this.nextIndex = 0;
        }
    }

}
