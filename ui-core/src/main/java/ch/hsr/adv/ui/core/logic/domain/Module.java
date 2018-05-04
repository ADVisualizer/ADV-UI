package ch.hsr.adv.ui.core.logic.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * To be used on every module implementation with the name/key of the module
 * as a value.
 *
 * @author mtrentini
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Module {
    /**
     * @return the name/key of the module
     */
    String value() default "";
}
