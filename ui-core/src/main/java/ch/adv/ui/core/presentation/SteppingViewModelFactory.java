package ch.adv.ui.core.presentation;

/**
 * Guice factory interface for
 * {@link SteppingViewModel}
 */
public interface SteppingViewModelFactory {

    /**
     * factory method
     *
     * @param stateViewModel state view model
     * @return instance
     */
    SteppingViewModel create(StateViewModel stateViewModel);
}
