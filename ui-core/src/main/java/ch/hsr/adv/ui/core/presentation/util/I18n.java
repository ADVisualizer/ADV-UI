/*
 * Copyright (c) 2016 sothawo
 *
 * https://www.sothawo.com/2016/09/how-to-implement-a-javafx-ui-where-the
 * -language-can-be-changed-dynamically/
 */
package ch.hsr.adv.ui.core.presentation.util;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

import java.text.MessageFormat;
import java.util.*;

/**
 * I18N utility class
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
public final class I18n {

    public static final String NOTIFICATION_SESSION_LOAD_EXISTING =
            "notification.session.load.existing";
    public static final String NOTIFICATION_SESSION_LOAD_SUCCESSFUL =
            "notification.session.load.successful";
    public static final String NOTIFICATION_SESSION_LOAD_UNSUCCESSFUL =
            "notification.session.load.unsuccessful";
    public static final String NOTIFICATION_SESSION_SAVE_SUCCESSFUL =
            "notification.session.save.successful";
    public static final String NOTIFICATION_SESSION_SAVE_UNSUCCESSFUL =
            "notification.session.save.unsuccessful";
    public static final String NOTIFICATION_SESSION_CLOSE_UNSUCCESSFUL =
            "notification.session.close.unsuccessful";
    public static final String NOTIFICATION_SESSION_CLOSE_SUCCESSFUL =
            "notification.session.close.successful";
    public static final String NOTIFICATION_SESSION_CLOSE_ALL =
            "notification.session.close.all";


    /**
     * the current selected Locale.
     */
    private static final ObjectProperty<Locale> LOCALE;
    private static final int TOOLTIP_DELAY = 500;

    static {
        LOCALE = new SimpleObjectProperty<>(getDefaultLocale());
        LOCALE.addListener((observable, oldValue, newValue) -> Locale
                .setDefault(newValue));
    }

    /**
     * Creates a String binding to a localized String for the given message
     * bundle key.
     *
     * @param key  key
     * @param args optional arguments for the binding
     * @return String binding
     */
    public static StringBinding createStringBinding(final String key,
                                                    Object... args) {
        return Bindings.createStringBinding(() -> get(key, args), LOCALE);
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
        if (getSupportedLocales().contains(sysDefault)) {
            return sysDefault;
        } else {
            return Locale.UK;
        }
    }

    /**
     * Gets the string with the given key from the resource bundle for the
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
     * Creates a bound Button for the given resourcebundle key.
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
     * Creates a bound Tooltip for the given resourcebundle key.
     *
     * @param key  ResourceBundle key
     * @param args optional arguments for the message
     * @return Label
     */
    public static Tooltip tooltipForKey(final String key, final Object...
            args) {
        Tooltip tooltip = new Tooltip();
        tooltip.textProperty().bind(createStringBinding(key, args));
        tooltip.setShowDelay(new Duration(TOOLTIP_DELAY));
        return tooltip;
    }

    public static Locale getLocale() {
        return LOCALE.get();
    }

    /**
     * Sets the current locale and updates the corresponding property.
     *
     * @param locale to be set
     */
    public static void setLocale(Locale locale) {
        localeProperty().set(locale);
        Locale.setDefault(locale);
    }

    /**
     * @return the locale property
     */
    public static ObjectProperty<Locale> localeProperty() {
        return LOCALE;
    }

}
