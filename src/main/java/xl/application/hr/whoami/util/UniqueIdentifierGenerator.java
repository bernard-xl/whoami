package xl.application.hr.whoami.util;

/**
 * Generates sequence of unique identifiers.
 */
public interface UniqueIdentifierGenerator {

    /**
     * Get the next unique identifier.
     * @return Unique identifier in the format of "XXXXXXXX-XXXX-XXXX"
     */
    String next();
}
