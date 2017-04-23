package tg.logic;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Tomasz Gwoździk on 23.04.2017.
 */
public final class Context {
    private static ArrayList<ContextChangeListener> listeners = new ArrayList<>();

    private static Locale locale;
    private static ResourceBundle bundle;
    private static String baseName = "Locales";

    public static String getString(String key) {
        String message = "";

        try {
            message = bundle.getString(key);

            return new String(message.getBytes("ISO-8859-1"), "UTF-8");
        } catch(Exception e) {}

        return message;
    }

    public static void setLocale(Locale newLocale) {
        if (locale != null && locale.equals(newLocale)) {
            return;
        }

        ResourceBundle newBundle = ResourceBundle.getBundle(baseName, newLocale);
        if (newBundle == null) {
            throw new IllegalArgumentException("No resource bundle for: "
                    + locale.getLanguage());
        }
        locale = newLocale;
        bundle = newBundle;

        fireContextChangedEvent();
    }

    public static synchronized void addContextChangeListener(ContextChangeListener listener) {
        listeners.add(listener);
    }

    public static void removeComponentFromList(ContextChangeListener component) {
        listeners.remove(component);
    }

    private static void fireContextChangedEvent() {
        final Runnable dispatcher = () -> {
            synchronized (Context.class) {
                for (ContextChangeListener i : listeners) {
                    i.contextChanged();
                }
            }
        };

        if (SwingUtilities.isEventDispatchThread()) {
            dispatcher.run();
        } else {
            SwingUtilities.invokeLater(dispatcher);
        }
    }
}
