package ch.hsr.adv.ui.core.presentation;

/**
 * Guice factory interface for
 * {@link SteppingViewModel}
 *
 * @author mwieland
 */
public interface SteppingViewModelFactory {

    /**
     * Constructs a SteppingViewModel with dependencies which are not
     * automatically injectable.
     *
     * @param stateViewModel state view model
     * @return instance
     */
    SteppingViewModel create(StateViewModel stateViewModel);
}
