package ch.nickthegreek.jenkins.fishtank;

public enum FishState {

    ALIVE,          // fish is alive and well, swimming around
    ALIVE_PENDING,  // fish is alive and well, but events are pending which might change its fate
    SICK,           // fish is sick, swimming around
    SICK_PENDING,   // fish is sick, swimming around, but events are pending which might change its fate
    DEAD,           // fish is dead, drifting at the surface
    DEAD_PENDING,   // fish is dead, drifting at the surface, but events are pending which might change its fate
    GHOST           // fish is a ghost, swims around, but no cares about it

}
