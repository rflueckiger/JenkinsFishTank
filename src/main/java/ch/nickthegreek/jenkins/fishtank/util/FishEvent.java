package ch.nickthegreek.jenkins.fishtank.util;

import ch.nickthegreek.jenkins.fishtank.FishState;

public class FishEvent implements Comparable<FishEvent> {

    private final long time; // in nano seconds
    private final String name;
    private final FishState state;

    public FishEvent(long time, String name, FishState state) {
        if (time < 0) {
            throw new IllegalArgumentException("time must be a positive.");
        }
        if (name == null) {
            throw new IllegalArgumentException("name must not be null.");
        }
        if (state == null) {
            throw new IllegalArgumentException("state must not be null.");
        }

        this.time = toNanoSeconds(time);
        this.name = name;
        this.state = state;
    }

    @Override
    public int compareTo(FishEvent fishEvent) {
        return this.getTime() > fishEvent.getTime() ? 1 : -1;
    }

    public long getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public FishState getState() {
        return state;
    }

    private long toNanoSeconds(long seconds) {
        return seconds * 1000 * 1000 * 1000;
    }

}
