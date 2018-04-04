package ch.adv.ui.core.logic;

/**
 * An ADVEvent within the framework.
 *
 * All module can subscribe for these events.
 */
public enum ADVEvent {
    STEP_FIRST(),
    STEP_BACKWARD,
    STEP_FORWARD,
    STEP_LAST;
}
