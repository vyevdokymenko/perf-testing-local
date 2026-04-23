package config

class PerfConfig {
    static <T>T getProperty(String propName, Class<T> className) {
        String rawValue = System.getProperty(propName)
        if (rawValue == null || rawValue.trim().isEmpty()) {
            return null
        }
        return rawValue.asType(className) as T
    }
}
