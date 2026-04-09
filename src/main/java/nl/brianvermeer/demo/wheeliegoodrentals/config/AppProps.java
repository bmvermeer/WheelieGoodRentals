package nl.brianvermeer.demo.wheeliegoodrentals.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Lightweight properties loader for classes that are not managed by Spring
 * (e.g. LangChain4j guardrails instantiated via {@code new}).
 * Reads {@code application.properties} from the classpath and resolves
 * simple {@code ${ENV_VAR}} placeholders via {@link System#getenv}.
 */
public final class AppProps {

    private static final Properties PROPS = load();

    private AppProps() {}

    private static Properties load() {
        Properties p = new Properties();
        try (InputStream in = AppProps.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (in == null) {
                throw new IllegalStateException(
                        "application.properties not found on the classpath");
            }
            p.load(in);
            return p;
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Failed to load application.properties", e);
        }
    }

    /**
     * Returns the value for {@code key}.
     * If the raw value is a {@code ${VAR}} placeholder it is resolved via the
     * environment; an {@link IllegalStateException} is thrown when the value
     * or the referenced env-var is absent or blank.
     */
    public static String getRequired(String key) {
        String raw = PROPS.getProperty(key);
        if (raw == null || raw.isBlank()) {
            throw new IllegalStateException("Missing required property: " + key);
        }
        if (raw.startsWith("${") && raw.endsWith("}")) {
            String envName = raw.substring(2, raw.length() - 1);
            String envValue = System.getenv(envName);
            if (envValue == null || envValue.isBlank()) {
                throw new IllegalStateException(
                        "Property '" + key + "' points to env var '" + envName
                        + "' which is not set");
            }
            return envValue;
        }
        return raw;
    }

    /** Convenience overload that returns {@code defaultValue} when the property is absent. */
    public static String get(String key, String defaultValue) {
        try {
            return getRequired(key);
        } catch (IllegalStateException e) {
            return defaultValue;
        }
    }
}

