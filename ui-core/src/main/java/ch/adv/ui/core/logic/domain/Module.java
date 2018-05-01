package ch.adv.ui.core.logic.domain;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * To be used on every module implementation with the name/key of the module
 * as a value.
 *
 * @author mtrentini
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Module {
    /**
     * @return the name/key of the module
     */
    String value() default "";
}
