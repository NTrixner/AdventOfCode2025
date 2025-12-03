package eu.ntrixner.aoc;

public interface ChallengeRunner<T> extends Runnable {
    void init(T params);
}
