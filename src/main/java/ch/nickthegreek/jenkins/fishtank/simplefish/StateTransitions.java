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
        addTransition(null,          ALIVE,          SwimAnimation::green);
        addTransition(null,          ALIVE_PENDING,  SwimAnimation::green);
        addTransition(null,          SICK,           SwimAnimation::yellow);
        addTransition(null,          SICK_PENDING,   SwimAnimation::yellow);
        addTransition(null,          DEAD,           SurfaceAnimation::red, FloatAnimation::red);
        addTransition(null,          DEAD_PENDING,   SurfaceAnimation::red, FloatAnimation::red);
        addTransition(null,          GHOST,          SwimAnimation::grey);

        addTransition(ALIVE,         ALIVE_PENDING,  SwimAnimation::green);
        addTransition(ALIVE,         SICK,           SwimAnimation::yellow);
        addTransition(ALIVE,         SICK_PENDING,   SwimAnimation::yellow);
        addTransition(ALIVE,         DEAD,           SurfaceAnimation::red, FloatAnimation::red);
        addTransition(ALIVE,         DEAD_PENDING,   SurfaceAnimation::red, FloatAnimation::red);
        addTransition(ALIVE,         GHOST,          SwimAnimation::grey);

        addTransition(ALIVE_PENDING, ALIVE,          SwimAnimation::green);
        addTransition(ALIVE_PENDING, SICK,           SwimAnimation::yellow);
        addTransition(ALIVE_PENDING, SICK_PENDING,   SwimAnimation::yellow);
        addTransition(ALIVE_PENDING, DEAD,           SurfaceAnimation::red, FloatAnimation::red);
        addTransition(ALIVE_PENDING, DEAD_PENDING,   SurfaceAnimation::red, FloatAnimation::red);
        addTransition(ALIVE_PENDING, GHOST,          SwimAnimation::grey);

        addTransition(SICK,          ALIVE,          SwimAnimation::green);
        addTransition(SICK,          ALIVE_PENDING,  SwimAnimation::green);
        addTransition(SICK,          SICK_PENDING,   SwimAnimation::yellow);
        addTransition(SICK,          DEAD,           SurfaceAnimation::red, FloatAnimation::red);
        addTransition(SICK,          DEAD_PENDING,   SurfaceAnimation::red, FloatAnimation::red);
        addTransition(SICK,          GHOST,          SwimAnimation::grey);
        
        addTransition(SICK_PENDING,  ALIVE,          SwimAnimation::green);
        addTransition(SICK_PENDING,  ALIVE_PENDING,  SwimAnimation::green);
        addTransition(SICK_PENDING,  SICK,           SwimAnimation::yellow);
        addTransition(SICK_PENDING,  DEAD,           SurfaceAnimation::red, FloatAnimation::red);
        addTransition(SICK_PENDING,  DEAD_PENDING,   SurfaceAnimation::red, FloatAnimation::red);
        addTransition(SICK_PENDING,  GHOST,          SwimAnimation::grey);

        addTransition(DEAD,          ALIVE,          SwimAnimation::green);
        addTransition(DEAD,          ALIVE_PENDING,  SwimAnimation::green);
        addTransition(DEAD,          SICK,           SwimAnimation::yellow);
        addTransition(DEAD,          SICK_PENDING,   SwimAnimation::yellow);
        addTransition(DEAD,          DEAD_PENDING,   FloatAnimation::red);
        addTransition(DEAD,          GHOST,          SwimAnimation::grey);

        addTransition(DEAD_PENDING,  ALIVE,          SwimAnimation::green);
        addTransition(DEAD_PENDING,  ALIVE_PENDING,  SwimAnimation::green);
        addTransition(DEAD_PENDING,  SICK,           SwimAnimation::yellow);
        addTransition(DEAD_PENDING,  SICK_PENDING,   SwimAnimation::yellow);
        addTransition(DEAD_PENDING,  DEAD,           FloatAnimation::red);
        addTransition(DEAD_PENDING,  GHOST,          SwimAnimation::grey);

        addTransition(GHOST,         ALIVE,          SwimAnimation::green);
        addTransition(GHOST,         ALIVE_PENDING,  SwimAnimation::green);
        addTransition(GHOST,         SICK,           SwimAnimation::yellow);
        addTransition(GHOST,         SICK_PENDING,   SwimAnimation::yellow);
        addTransition(GHOST,         DEAD,           SurfaceAnimation::red, FloatAnimation::red);
        addTransition(GHOST,         DEAD_PENDING,   SurfaceAnimation::red, FloatAnimation::red);
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
