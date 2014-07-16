package ch.nickthegreek.jenkins.fishtank.util;

import ch.nickthegreek.jenkins.fishtank.FishState;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class FishEventScript {

    private final Queue<FishEvent> events = new PriorityQueue<>();

    private boolean started = false;
    private long startTime;

    public void start(long startTime) {
        this.startTime = startTime;
        started = true;
    }

    public void add(long time, String name, FishState state) {
        events.add(new FishEvent(time, name, state));
    }

    public List<FishEvent> getEvents(long now) {
        long elapsedTime = now - startTime;

        List<FishEvent> result = new ArrayList<>();

        while (!events.isEmpty()) {
            if (elapsedTime > events.peek().getTime()) {
                result.add(events.poll());

            } else {
                // queue is sorted, no need to look further
                break;
            }
        }

        return result;
    }

    public boolean isStarted() {
        return started;
    }
}
