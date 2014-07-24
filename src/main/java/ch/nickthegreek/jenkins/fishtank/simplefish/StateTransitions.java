package ch.nickthegreek.jenkins.fishtank.simplefish;

import ch.nickthegreek.jenkins.fishtank.FishState;

import java.util.HashMap;
import java.util.Map;

import static ch.nickthegreek.jenkins.fishtank.FishState.*;

public class StateTransitions {
    
    private final Map<FishState, Map<FishState, AnimationFactoryMethod[]>> transitions = new HashMap<>();

    interface AnimationFactoryMethod {
        Animation get();
    }

    {
        addTransition(null,          ALIVE,          AliveSwimAnimation::create);
        addTransition(null,          ALIVE_PENDING,  AliveSwimAnimation::create);
        addTransition(null,          SICK,           SickSwimAnimation::create);
        addTransition(null,          SICK_PENDING,   SickSwimAnimation::create);
        addTransition(null,          DEAD,           SurfaceAnimation::create, FloatAnimation::create);
        addTransition(null,          DEAD_PENDING,   SurfaceAnimation::create, FloatAnimation::create);
        addTransition(null,          GHOST,          GhostSwimAnimation::create);

        addTransition(ALIVE,         ALIVE_PENDING,  AliveSwimAnimation::create);
        addTransition(ALIVE,         SICK,           SickSwimAnimation::create);
        addTransition(ALIVE,         SICK_PENDING,   SickSwimAnimation::create);
        addTransition(ALIVE,         DEAD,           SurfaceAnimation::create, FloatAnimation::create);
        addTransition(ALIVE,         DEAD_PENDING,   SurfaceAnimation::create, FloatAnimation::create);
        addTransition(ALIVE,         GHOST,          GhostSwimAnimation::create);

        addTransition(ALIVE_PENDING, ALIVE,          AliveSwimAnimation::create);
        addTransition(ALIVE_PENDING, SICK,           SickSwimAnimation::create);
        addTransition(ALIVE_PENDING, SICK_PENDING,   SickSwimAnimation::create);
        addTransition(ALIVE_PENDING, DEAD,           SurfaceAnimation::create, FloatAnimation::create);
        addTransition(ALIVE_PENDING, DEAD_PENDING,   SurfaceAnimation::create, FloatAnimation::create);
        addTransition(ALIVE_PENDING, GHOST,          GhostSwimAnimation::create);

        addTransition(SICK,          ALIVE,          AliveSwimAnimation::create);
        addTransition(SICK,          ALIVE_PENDING,  AliveSwimAnimation::create);
        addTransition(SICK,          SICK_PENDING,   SickSwimAnimation::create);
        addTransition(SICK,          DEAD,           SurfaceAnimation::create, FloatAnimation::create);
        addTransition(SICK,          DEAD_PENDING,   SurfaceAnimation::create, FloatAnimation::create);
        addTransition(SICK,          GHOST,          GhostSwimAnimation::create);
        
        addTransition(SICK_PENDING,  ALIVE,          AliveSwimAnimation::create);
        addTransition(SICK_PENDING,  ALIVE_PENDING,  AliveSwimAnimation::create);
        addTransition(SICK_PENDING,  SICK,           SickSwimAnimation::create);
        addTransition(SICK_PENDING,  DEAD,           SurfaceAnimation::create, FloatAnimation::create);
        addTransition(SICK_PENDING,  DEAD_PENDING,   SurfaceAnimation::create, FloatAnimation::create);
        addTransition(SICK_PENDING,  GHOST,          GhostSwimAnimation::create);

        addTransition(DEAD,          ALIVE,          AliveSwimAnimation::create);
        addTransition(DEAD,          ALIVE_PENDING,  AliveSwimAnimation::create);
        addTransition(DEAD,          SICK,           SickSwimAnimation::create);
        addTransition(DEAD,          SICK_PENDING,   SickSwimAnimation::create);
        addTransition(DEAD,          DEAD_PENDING,   FloatAnimation::create);
        addTransition(DEAD,          GHOST,          GhostSwimAnimation::create);

        addTransition(DEAD_PENDING,  ALIVE,          AliveSwimAnimation::create);
        addTransition(DEAD_PENDING,  ALIVE_PENDING,  AliveSwimAnimation::create);
        addTransition(DEAD_PENDING,  SICK,           SickSwimAnimation::create);
        addTransition(DEAD_PENDING,  SICK_PENDING,   SickSwimAnimation::create);
        addTransition(DEAD_PENDING,  DEAD,           FloatAnimation::create);
        addTransition(DEAD_PENDING,  GHOST,          GhostSwimAnimation::create);

        addTransition(GHOST,         ALIVE,          AliveSwimAnimation::create);
        addTransition(GHOST,         ALIVE_PENDING,  AliveSwimAnimation::create);
        addTransition(GHOST,         SICK,           SickSwimAnimation::create);
        addTransition(GHOST,         SICK_PENDING,   SickSwimAnimation::create);
        addTransition(GHOST,         DEAD,           SurfaceAnimation::create, FloatAnimation::create);
        addTransition(GHOST,         DEAD_PENDING,   SurfaceAnimation::create, FloatAnimation::create);
    }
    
    private void addTransition(FishState from, FishState to, AnimationFactoryMethod... animationsFactoryMethods) {
        Map<FishState, AnimationFactoryMethod[]> fromMap = transitions.get(from);
        if (fromMap == null) {
            fromMap = new HashMap<>();
            transitions.put(from, fromMap);
        }
        fromMap.put(to, animationsFactoryMethods);
    }
    
    public Animation[] nextAnimations(FishState from, FishState to) {
        Map<FishState, AnimationFactoryMethod[]> fromMap = transitions.get(from);
        if (fromMap == null) {
            throw new IllegalArgumentException(String.format("unknown transition origin state: %s", from));
        }
        AnimationFactoryMethod[] animationsFactoryMethods = fromMap.get(to);
        if (animationsFactoryMethods == null) {
            throw new IllegalArgumentException(String.format("unknown transition target state: %s", to));
        }

        Animation[] result = new Animation[animationsFactoryMethods.length];
        for (int i = 0; i < animationsFactoryMethods.length; i++) {
            result[i] = animationsFactoryMethods[i].get();
        }
        return result;
    }
    
}
