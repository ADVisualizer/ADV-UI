package ch.hsr.adv.ui.core.logic.events;

/**
 * An ADVEvent within the framework.
 *
 * All modules can subscribe to these events via the {@link EventManager}.
 */
public enum ADVEvent {
    STEP_FIRST,
    STEP_BACKWARD,
    STEP_FORWARD,
    STEP_LAST,
    SNAPSHOT_ADDED,
    SESSION_ADDED,
    SESSION_REMOVED,
    CURRENT_SESSION_CHANGED,
    NOTIFICATION
}
