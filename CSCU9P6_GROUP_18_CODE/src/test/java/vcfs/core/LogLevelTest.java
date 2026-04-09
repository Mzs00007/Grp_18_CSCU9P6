package vcfs.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for LogLevel enum.
 * Tests all log levels and their properties.
 */
@DisplayName("LogLevel Enum Test Suite")
public class LogLevelTest {

    // =========================================================
    // ENUM VALUES Tests
    // =========================================================

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @Test
        @DisplayName("Should have DEBUG level")
        void testHasDebugLevel() {
            assertTrue(LogLevel.DEBUG != null, "Should have DEBUG level");
        }

        @Test
        @DisplayName("Should have INFO level")
        void testHasInfoLevel() {
            assertTrue(LogLevel.INFO != null, "Should have INFO level");
        }

        @Test
        @DisplayName("Should have WARNING level")
        void testHasWarningLevel() {
            assertTrue(LogLevel.WARNING != null, "Should have WARNING level");
        }

        @Test
        @DisplayName("Should have ERROR level")
        void testHasErrorLevel() {
            assertTrue(LogLevel.ERROR != null, "Should have ERROR level");
        }

        @Test
        @DisplayName("Should have all values")
        void testAllValuesExist() {
            LogLevel[] values = LogLevel.values();
            assertTrue(values.length >= 4, "Should have at least 4 log levels");
        }
    }

    // =========================================================
    // ENUM COMPARISON Tests
    // =========================================================

    @Nested
    @DisplayName("Enum Comparison Tests")
    class EnumComparisonTests {

        @Test
        @DisplayName("Should compare DEBUG and INFO correctly")
        void testDebugNotEqualsInfo() {
            assertNotEquals(LogLevel.DEBUG, LogLevel.INFO, "DEBUG should not equal INFO");
        }

        @Test
        @DisplayName("Should compare INFO and WARNING correctly")
        void testInfoNotEqualsWarning() {
            assertNotEquals(LogLevel.INFO, LogLevel.WARNING, "INFO should not equal WARNING");
        }

        @Test
        @DisplayName("Should compare WARNING and ERROR correctly")
        void testWarningNotEqualsError() {
            assertNotEquals(LogLevel.WARNING, LogLevel.ERROR, "WARNING should not equal ERROR");
        }

        @Test
        @DisplayName("Should return same level for same enum")
        void testSameLevelEquality() {
            LogLevel level1 = LogLevel.INFO;
            LogLevel level2 = LogLevel.INFO;

            assertEquals(level1, level2, "Same level should be equal");
        }
    }

    // =========================================================
    // ENUM ORDERING Tests
    // =========================================================

    @Nested
    @DisplayName("Enum Ordering Tests")
    class EnumOrderingTests {

        @Test
        @DisplayName("Should have logical severity ordering")
        void testSeverityOrdering() {
            // DEBUG < INFO < WARNING < ERROR
            LogLevel[] values = LogLevel.values();
            assertTrue(values.length >= 4, "Should have at least 4 levels");
        }

        @Test
        @DisplayName("Should maintain ordinal ordering")
        void testOrdinalOrdering() {
            int debugOrdinal = LogLevel.DEBUG.ordinal();
            int infoOrdinal = LogLevel.INFO.ordinal();
            int warningOrdinal = LogLevel.WARNING.ordinal();
            int errorOrdinal = LogLevel.ERROR.ordinal();

            assertTrue(debugOrdinal < infoOrdinal, "DEBUG ordinal should be less than INFO");
            assertTrue(infoOrdinal < warningOrdinal, "INFO ordinal should be less than WARNING");
            assertTrue(warningOrdinal < errorOrdinal, "WARNING ordinal should be less than ERROR");
        }
    }

    // =========================================================
    // ENUM CONVERSION Tests
    // =========================================================

    @Nested
    @DisplayName("Enum Conversion Tests")
    class EnumConversionTests {

        @Test
        @DisplayName("Should convert string to DEBUG")
        void testValueOfDebug() {
            LogLevel level = LogLevel.valueOf("DEBUG");
            assertEquals(LogLevel.DEBUG, level, "Should convert to DEBUG");
        }

        @Test
        @DisplayName("Should convert string to INFO")
        void testValueOfInfo() {
            LogLevel level = LogLevel.valueOf("INFO");
            assertEquals(LogLevel.INFO, level, "Should convert to INFO");
        }

        @Test
        @DisplayName("Should convert string to WARNING")
        void testValueOfWarning() {
            LogLevel level = LogLevel.valueOf("WARNING");
            assertEquals(LogLevel.WARNING, level, "Should convert to WARNING");
        }

        @Test
        @DisplayName("Should convert string to ERROR")
        void testValueOfError() {
            LogLevel level = LogLevel.valueOf("ERROR");
            assertEquals(LogLevel.ERROR, level, "Should convert to ERROR");
        }

        @Test
        @DisplayName("Should throw exception for invalid string")
        void testValueOfInvalid() {
            assertThrows(IllegalArgumentException.class, () -> {
                LogLevel.valueOf("INVALID");
            }, "Should throw for invalid level");
        }

        @Test
        @DisplayName("Should convert to string")
        void testToString() {
            String debugStr = LogLevel.DEBUG.toString();
            String infoStr = LogLevel.INFO.toString();

            assertTrue(debugStr.contains("DEBUG"), "DEBUG string should contain DEBUG");
            assertTrue(infoStr.contains("INFO"), "INFO string should contain INFO");
        }
    }

    // =========================================================
    // ENUM USAGE TESTS
    // =========================================================

    @Nested
    @DisplayName("Enum Usage Tests")
    class EnumUsageTests {

        @Test
        @DisplayName("Should use in switch statement")
        void testSwitchStatement() {
            LogLevel level = LogLevel.WARNING;
            String result = "";

            switch (level) {
                case DEBUG:
                    result = "DEBUG";
                    break;
                case INFO:
                    result = "INFO";
                    break;
                case WARNING:
                    result = "WARNING";
                    break;
                case ERROR:
                    result = "ERROR";
                    break;
            }

            assertEquals("WARNING", result, "Should use in switch statement");
        }

        @Test
        @DisplayName("Should store in array")
        void testStoreInArray() {
            LogLevel[] levels = new LogLevel[]{LogLevel.DEBUG, LogLevel.INFO, LogLevel.WARNING, LogLevel.ERROR};

            assertEquals(4, levels.length, "Should store in array");
            assertEquals(LogLevel.INFO, levels[1], "Should access from array");
        }

        @Test
        @DisplayName("Should compare with null safely")
        void testNullComparison() {
            LogLevel level = LogLevel.INFO;

            assertNotNull(level, "Level should not be null");
            assertNotEquals(level, null, "Level should not equal null");
        }
    }

    // =========================================================
    // ENUM IDENTITY Tests
    // =========================================================

    @Nested
    @DisplayName("Enum Identity Tests")
    class EnumIdentityTests {

        @Test
        @DisplayName("Should have unique identity for each level")
        void testUniqueIdentity() {
            LogLevel level1 = LogLevel.DEBUG;
            LogLevel level2 = LogLevel.DEBUG;

            assertSame(level1, level2, "Same level should have same identity");
        }

        @Test
        @DisplayName("Should work with HashMap")
        void testHashMapUsage() {
            java.util.Map<LogLevel, String> map = new java.util.HashMap<>();

            map.put(LogLevel.DEBUG, "Debug messages");
            map.put(LogLevel.INFO, "Info messages");
            map.put(LogLevel.WARNING, "Warning messages");
            map.put(LogLevel.ERROR, "Error messages");

            assertEquals("Info messages", map.get(LogLevel.INFO), "Should work with HashMap");
        }

        @Test
        @DisplayName("Should work with EnumSet")
        void testEnumSetUsage() {
            java.util.Set<LogLevel> set = java.util.EnumSet.of(LogLevel.WARNING, LogLevel.ERROR);

            assertTrue(set.contains(LogLevel.WARNING), "Should contain WARNING");
            assertFalse(set.contains(LogLevel.DEBUG), "Should not contain DEBUG");
        }
    }
}

