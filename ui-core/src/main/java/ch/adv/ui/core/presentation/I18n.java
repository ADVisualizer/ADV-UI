/*
 * Copyright (c) 2016 sothawo
 *
 * https://www.sothawo.com/2016/09/how-to-implement-a-javafx-ui-where-the
 * -language-can-be-changed-dynamically/
 */
package ch.adv.ui.core.presentation;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * I18N utility class
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
public final class I18n {
    /**
     * the current selected Locale.
     */
    private static final ObjectProperty<Locale> LOCALE;

    static {
        LOCALE = new SimpleObjectProperty<>(getDefaultLocale());
        LOCALE.addListener((observable, oldValue, newValue) -> Locale
                .setDefault(newValue));
    }

    /**
     * creates a String binding to a localized String for the given message
     * bundle key
     *
     * @param key key
     * @return String binding
     */
    public static StringBinding createStringBinding(final String key,
                                                    Object... args) {
        return Bindings.createStringBinding(() -> get(key, args), LOCALE);
    }

    /**
     * creates a String Binding to a localized String that is computed by
     * calling the given func
     *
     * @param func function called on every change
     * @return StringBinding
     */
    public static StringBinding createStringBinding(Callable<String> func) {
        return Bindings.createStringBinding(func, LOCALE);
    }

    /**
     * Returns the supported Locales.
     *
     * @return List of Locale objects.
     */
    private static List<Locale> getSupportedLocales() {
        return new ArrayList<>(Arrays.asList(Locale.ENGLISH, Locale.GERMAN));
    }

    /**
     * Returns the default LOCALE. This is the systems default if contained in
     * the supported locales, english otherwise.
     *
     * @return the default LOCALE
     */
    private static Locale getDefaultLocale() {
        Locale sysDefault = Locale.getDefault();
        return getSupportedLocales()
                .contains(sysDefault) ? sysDefault : Locale.UK;
    }

    /**
     * gets the string with the given key from the resource bundle for the
     * current LOCALE and uses it as first argument
     * to MessageFormat.format, passing in the optional args and returning
     * the result.
     *
     * @param key  message key
     * @param args optional arguments for the message
     * @return localized formatted string
     */
    public static String get(final String key, final Object... args) {
        ResourceBundle bundle = ResourceBundle
                .getBundle("bundles.ADVBundle", getLocale());
        return MessageFormat.format(bundle.getString(key), args);
    }

    /**
     * creates a bound Label whose value is computed on language change.
     *
     * @param func the function to compute the value
     * @return Label
     */
    public static Label labelForValue(Callable<String> func) {
        Label label = new Label();
        label.textProperty().bind(createStringBinding(func));
        return label;
    }

    /**
     * creates a bound Button for the given resourcebundle key
     *
     * @param key  ResourceBundle key
     * @param args optional arguments for the message
     * @return Button
     */
    public static Button buttonForKey(final String key, final Object... args) {
        Button button = new Button();
        button.textProperty().bind(createStringBinding(key, args));
        return button;
    }

    /**
     * creates a bound Tooltip for the given resourcebundle key
     *
     * @param key  ResourceBundle key
     * @param args optional arguments for the message
     * @return Label
     */
    public static Tooltip tooltipForKey(final String key, final Object...
            args) {
        Tooltip tooltip = new Tooltip();
        tooltip.textProperty().bind(createStringBinding(key, args));
        return tooltip;
    }

    public static Locale getLocale() {
        return LOCALE.get();
    }

    public static void setLocale(Locale locale) {
        localeProperty().set(locale);
        Locale.setDefault(locale);
    }

    public static ObjectProperty<Locale> localeProperty() {
        return LOCALE;
    }

}