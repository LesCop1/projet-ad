package fr.bcecb.state;

import fr.bcecb.Game;
import fr.bcecb.render.RenderEngine;
import fr.bcecb.render.Renderer;
import fr.bcecb.util.Log;

import java.util.Iterator;
import java.util.Stack;

import static fr.bcecb.state.State.StateEvent;

public class StateEngine {
    private final Stack<State> stateStack = new Stack<>();

    public void pushState(State state) {
        StateEvent.Enter event = new StateEvent.Enter(state, getCurrentState());
        Game.getEventBus().post(event);

        if (!event.isCancelled()) {
            state.onEnter();
            stateStack.push(state);
        }
    }

    public void popState() {
        if (!stateStack.empty()) {
            StateEvent.Exit event = new StateEvent.Exit(stateStack.peek());
            Game.getEventBus().post(event);

            if (!event.isCancelled()) {
                stateStack.pop().onExit();
            }
        }
    }

    public void update() {
        if (stateStack.empty()) {
            Game.instance().stop();
        }

        for (State state : stateStack) {
            state.update();

            if (!state.shouldUpdateBelow()) break;
        }
    }

    public void render(RenderEngine renderEngine, double partialTick) {
        renderState(renderEngine, stateStack.iterator(), partialTick);
    }

    private void renderState(RenderEngine renderEngine, Iterator<State> stateIterator, double partialTick) {
        if (stateIterator.hasNext()) {
            State state = stateIterator.next();

            if (state.shouldRenderBelow()) {
                renderState(renderEngine, stateIterator, partialTick);
            }

            Renderer<State> renderer = renderEngine.getRenderManager().getRendererFor(state);

            if (renderer != null) {
                renderer.render(state, partialTick);
            } else Log.warning(Log.RENDER, "State " + state.getName() + " has no renderer !");
        }
    }

    public State getCurrentState() {
        return !stateStack.empty() ? stateStack.peek() : null;
    }

    public Stack<State> getStateStack() {
        return stateStack;
    }
}
