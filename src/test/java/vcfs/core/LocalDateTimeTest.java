package vcfs.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("LocalDateTime - Core Time Wrapper Tests")
public class LocalDateTimeTest {

    @Test
    @DisplayName("Constructor handles valid inputs correctly")
    void testConstructor_ValidInputs() {
        LocalDateTime dt = new LocalDateTime(2026, 4, 8, 14, 30);
        assertEquals("2026-04-08 14:30", dt.toString());
    }

    @Test
    @DisplayName("Constructor throws exception for invalid month")
    void testConstructor_InvalidMonth() {
        assertThrows(java.time.DateTimeException.class, 
            () -> new LocalDateTime(2026, 13, 8, 14, 30));
    }

    @Test
    @DisplayName("isBefore evaluates correctly")
    void testIsBefore() {
        LocalDateTime earlier = new LocalDateTime(2026, 4, 8, 9, 0);
        LocalDateTime later = new LocalDateTime(2026, 4, 8, 9, 30);
        
        assertTrue(earlier.isBefore(later));
        assertFalse(later.isBefore(earlier));
        assertFalse(earlier.isBefore(earlier));
    }

    @Test
    @DisplayName("isAfter evaluates correctly")
    void testIsAfter() {
        LocalDateTime earlier = new LocalDateTime(2026, 4, 8, 9, 0);
        LocalDateTime later = new LocalDateTime(2026, 4, 8, 9, 30);
        
        assertTrue(later.isAfter(earlier));
        assertFalse(earlier.isAfter(later));
        assertFalse(later.isAfter(later));
    }

    @Test
    @DisplayName("isEqual evaluates correctly")
    void testIsEqual() {
        LocalDateTime time1 = new LocalDateTime(2026, 4, 8, 9, 0);
        LocalDateTime time2 = new LocalDateTime(2026, 4, 8, 9, 0);
        LocalDateTime time3 = new LocalDateTime(2026, 4, 8, 9, 1);
        
        assertTrue(time1.isEqual(time2));
        assertFalse(time1.isEqual(time3));
    }

    @Test
    @DisplayName("plusMinutes adds time correctly including midnight rollover")
    void testPlusMinutes() {
        LocalDateTime dt = new LocalDateTime(2026, 4, 8, 9, 0);
        LocalDateTime added = dt.plusMinutes(30);
        assertEquals("2026-04-08 09:30", added.toString());
        
        LocalDateTime late = new LocalDateTime(2026, 4, 8, 23, 45);
        LocalDateTime rollover = late.plusMinutes(30);
        assertEquals("2026-04-09 00:15", rollover.toString());
    }

    @Test
    @DisplayName("minutesUntil calculates difference correctly")
    void testMinutesUntil() {
        LocalDateTime dt1 = new LocalDateTime(2026, 4, 8, 9, 0);
        LocalDateTime dt2 = new LocalDateTime(2026, 4, 8, 9, 30);
        
        assertEquals(30L, dt1.minutesUntil(dt2));
        assertEquals(-30L, dt2.minutesUntil(dt1));
        assertEquals(0L, dt1.minutesUntil(dt1));
    }

    @Test
    @DisplayName("parse method parses valid HH:mm correctly")
    void testParse_Valid() {
        LocalDateTime dt = LocalDateTime.parse("09:30");
        // As per implementation, parse assumes 2026-01-01
        assertEquals("2026-01-01 09:30", dt.toString());
    }

    @Test
    @DisplayName("parse method throws exception for invalid format")
    void testParse_Invalid() {
        assertThrows(IllegalArgumentException.class, () -> LocalDateTime.parse("9:30 AM"));
        assertThrows(IllegalArgumentException.class, () -> LocalDateTime.parse("25:00"));
        assertThrows(IllegalArgumentException.class, () -> LocalDateTime.parse("09:60"));
        assertThrows(IllegalArgumentException.class, () -> LocalDateTime.parse(null));
    }
}
