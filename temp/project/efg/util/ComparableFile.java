package project.efg.util;

/**
 * used for string comparison.
 */
public interface ComparableFile {

    public boolean greaterThan(ComparableFile comp);

    public String getStringValue();

}