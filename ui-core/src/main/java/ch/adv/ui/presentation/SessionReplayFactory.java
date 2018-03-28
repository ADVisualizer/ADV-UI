package ch.adv.ui.presentation;

/**
 * Factory interface for Guice
 *
 * @see https://github.com/google/guice/wiki/AssistedInject
 */
public interface SessionReplayFactory {

    /**
     * Construct a SessionReplay with dependencies, which are not
     * automatically injectable.
     *
     * @param sessionViewModel related sessionViewModel
     * @return new instance
     */
    SessionReplay create(SessionViewModel sessionViewModel);
}

