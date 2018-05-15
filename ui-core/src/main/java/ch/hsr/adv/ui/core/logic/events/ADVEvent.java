package ch.hsr.adv.ui.core.logic.events;

/**
 * An ADVEvent within the framework.
 *
 * All module can subscribe for these events.
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
    NOTIFICATION;
}
