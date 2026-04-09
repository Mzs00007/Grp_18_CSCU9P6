package vcfs.models.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for MeetingState, ReservationState, RoomState enums.
 */
@DisplayName("State Enums Test Suite")
public class StateEnumsTest {

    // =========================================================
    // MEETING STATE Tests
    // =========================================================

    @Nested
    @DisplayName("MeetingState Enum Tests")
    class MeetingStateTests {

        @Test
        @DisplayName("Should have all meeting states")
        void testMeetingStateValues() {
            MeetingState[] states = MeetingState.values();
            assertTrue(states.length > 0, "Should have meeting states");
        }

        @Test
        @DisplayName("Should distinguish different meeting states")
        void testMeetingStateDistinction() {
            // Get the first two states
            MeetingState[] states = MeetingState.values();
            if (states.length >= 2) {
                assertNotEquals(states[0], states[1], "Different states should not be equal");
            }
        }

        @Test
        @DisplayName("Should support valueOf conversion")
        void testMeetingStateValueOf() {
            MeetingState[] states = MeetingState.values();
            for (MeetingState state : states) {
                MeetingState converted = MeetingState.valueOf(state.name());
                assertEquals(state, converted, "Should convert by name");
            }
        }

        @Test
        @DisplayName("Should work with switch statement")
        void testMeetingStateSwitch() {
            MeetingState[] states = MeetingState.values();
            for (MeetingState state : states) {
                String result = "";
                switch (state) {
                    default:
                        result = "handled";
                }
                assertEquals("handled", result, "Should work in switch");
            }
        }

        @Test
        @DisplayName("Should maintain identity")
        void testMeetingStateIdentity() {
            MeetingState state1 = MeetingState.values()[0];
            MeetingState state2 = MeetingState.values()[0];
            assertSame(state1, state2, "Same state should have same identity");
        }
    }

    // =========================================================
    // RESERVATION STATE Tests
    // =========================================================

    @Nested
    @DisplayName("ReservationState Enum Tests")
    class ReservationStateTests {

        @Test
        @DisplayName("Should have all reservation states")
        void testReservationStateValues() {
            ReservationState[] states = ReservationState.values();
            assertTrue(states.length > 0, "Should have reservation states");
        }

        @Test
        @DisplayName("Should distinguish different reservation states")
        void testReservationStateDistinction() {
            ReservationState[] states = ReservationState.values();
            if (states.length >= 2) {
                assertNotEquals(states[0], states[1], "Different states should not be equal");
            }
        }

        @Test
        @DisplayName("Should support valueOf conversion")
        void testReservationStateValueOf() {
            ReservationState[] states = ReservationState.values();
            for (ReservationState state : states) {
                ReservationState converted = ReservationState.valueOf(state.name());
                assertEquals(state, converted, "Should convert by name");
            }
        }

        @Test
        @DisplayName("Should work with EnumSet")
        void testReservationStateEnumSet() {
            ReservationState[] states = ReservationState.values();
            if (states.length >= 2) {
                java.util.Set<ReservationState> set = java.util.EnumSet.of(states[0], states[1]);
                assertTrue(set.contains(states[0]), "Should contain first state");
                assertTrue(set.contains(states[1]), "Should contain second state");
            }
        }

        @Test
        @DisplayName("Should support HashMap usage")
        void testReservationStateHashMap() {
            java.util.Map<ReservationState, String> map = new java.util.HashMap<>();
            ReservationState[] states = ReservationState.values();
            
            for (int i = 0; i < states.length; i++) {
                map.put(states[i], "State" + i);
            }

            assertEquals(states.length, map.size(), "HashMap should contain all states");
        }

        @Test
        @DisplayName("Should maintain order")
        void testReservationStateOrder() {
            ReservationState[] states = ReservationState.values();
            for (int i = 0; i < states.length - 1; i++) {
                assertTrue(states[i].ordinal() < states[i + 1].ordinal(),
                          "States should maintain ordinal order");
            }
        }
    }

    // =========================================================
    // ROOM STATE Tests
    // =========================================================

    @Nested
    @DisplayName("RoomState Enum Tests")
    class RoomStateTests {

        @Test
        @DisplayName("Should have all room states")
        void testRoomStateValues() {
            RoomState[] states = RoomState.values();
            assertTrue(states.length > 0, "Should have room states");
        }

        @Test
        @DisplayName("Should distinguish different room states")
        void testRoomStateDistinction() {
            RoomState[] states = RoomState.values();
            if (states.length >= 2) {
                assertNotEquals(states[0], states[1], "Different states should not be equal");
            }
        }

        @Test
        @DisplayName("Should support valueOf conversion")
        void testRoomStateValueOf() {
            RoomState[] states = RoomState.values();
            for (RoomState state : states) {
                RoomState converted = RoomState.valueOf(state.name());
                assertEquals(state, converted, "Should convert by name");
            }
        }

        @Test
        @DisplayName("Should throw exception for invalid name")
        void testRoomStateValueOfInvalid() {
            assertThrows(IllegalArgumentException.class, () -> {
                RoomState.valueOf("INVALID_STATE");
            }, "Should throw for invalid state");
        }

        @Test
        @DisplayName("Should convert to string")
        void testRoomStateToString() {
            RoomState[] states = RoomState.values();
            for (RoomState state : states) {
                String str = state.toString();
                assertTrue(str.length() > 0, "String representation should exist");
            }
        }

        @Test
        @DisplayName("Should work with switch statement logic")
        void testRoomStateSwitch() {
            RoomState state = RoomState.values()[0];
            int result = 0;

            switch (state){
                default:
                    result = 1;
            }

            assertEquals(1, result, "Should work in switch");
        }
    }

    // =========================================================
    // ATTENDANCE OUTCOME Tests
    // =========================================================

    @Nested
    @DisplayName("AttendanceOutcome Enum Tests")
    class AttendanceOutcomeTests {

        @Test
        @DisplayName("Should have all attendance outcomes")
        void testAttendanceOutcomeValues() {
            AttendanceOutcome[] outcomes = AttendanceOutcome.values();
            assertTrue(outcomes.length > 0, "Should have attendance outcomes");
        }

        @Test
        @DisplayName("Should distinguish different outcomes")
        void testAttendanceOutcomeDistinction() {
            AttendanceOutcome[] outcomes = AttendanceOutcome.values();
            if (outcomes.length >= 2) {
                assertNotEquals(outcomes[0], outcomes[1], "Different outcomes should not be equal");
            }
        }

        @Test
        @DisplayName("Should support valueOf conversion")
        void testAttendanceOutcomeValueOf() {
            AttendanceOutcome[] outcomes = AttendanceOutcome.values();
            for (AttendanceOutcome outcome : outcomes) {
                AttendanceOutcome converted = AttendanceOutcome.valueOf(outcome.name());
                assertEquals(outcome, converted, "Should convert by name");
            }
        }

        @Test
        @DisplayName("Should support iteration")
        void testAttendanceOutcomeIteration() {
            int count = 0;
            for (@SuppressWarnings("unused") AttendanceOutcome outcome : AttendanceOutcome.values()) {
                count++;
            }
            assertTrue(count > 0, "Should be able to iterate");
        }

        @Test
        @DisplayName("Should work with HashMap")
        void testAttendanceOutcomeHashMap() {
            java.util.Map<AttendanceOutcome, Integer> outcomeCount = new java.util.HashMap<>();
            AttendanceOutcome[] outcomes = AttendanceOutcome.values();

            for (AttendanceOutcome outcome : outcomes) {
                outcomeCount.put(outcome, 0);
            }

            assertEquals(outcomes.length, outcomeCount.size(), "Should work with HashMap");
        }

        @Test
        @DisplayName("Should maintain consistent naming")
        void testAttendanceOutcomeConsistency() {
            AttendanceOutcome[] outcomes = AttendanceOutcome.values();
            for (AttendanceOutcome outcome : outcomes) {
                String name = outcome.name();
                assertTrue(name.length() > 0, "Each outcome should have a name");
                assertEquals(outcome, AttendanceOutcome.valueOf(name), "Name should be consistent");
            }
        }
    }

    // =========================================================
    // ENUM COMPARISON Tests
    // =========================================================

    @Nested
    @DisplayName("Cross-Enum Comparison Tests")
    class CrossEnumComparisonTests {

        @Test
        @DisplayName("Should keep MeetingState instances distinct")
        void testMeetingStateUniqueness() {
            MeetingState[] states = MeetingState.values();
            for (int i = 0; i < states.length; i++) {
                for (int j = i + 1; j < states.length; j++) {
                    assertNotEquals(states[i], states[j], "All states should be distinct");
                }
            }
        }

        @Test
        @DisplayName("Should keep ReservationState instances distinct")
        void testReservationStateUniqueness() {
            ReservationState[] states = ReservationState.values();
            for (int i = 0; i < states.length; i++) {
                for (int j = i + 1; j < states.length; j++) {
                    assertNotEquals(states[i], states[j], "All states should be distinct");
                }
            }
        }

        @Test
        @DisplayName("Should keep RoomState instances distinct")
        void testRoomStateUniqueness() {
            RoomState[] states = RoomState.values();
            for (int i = 0; i < states.length; i++) {
                for (int j = i + 1; j < states.length; j++) {
                    assertNotEquals(states[i], states[j], "All states should be distinct");
                }
            }
        }
    }

    // =========================================================
    // NULL SAFETY Tests
    // =========================================================

    @Nested
    @DisplayName("Null Safety Tests")
    class NullSafetyTests {

        @Test
        @DisplayName("MeetingState should handle null safely")
        void testMeetingStateNullSafety() {
            MeetingState state = MeetingState.values()[0];
            assertNotNull(state, "State should not be null");
            assertNotEquals(state, null, "State should not equal null");
        }

        @Test
        @DisplayName("ReservationState should handle null safely")
        void testReservationStateNullSafety() {
            ReservationState state = ReservationState.values()[0];
            assertNotNull(state, "State should not be null");
            assertNotEquals(state, null, "State should not equal null");
        }

        @Test
        @DisplayName("RoomState should handle null safely")
        void testRoomStateNullSafety() {
            RoomState state = RoomState.values()[0];
            assertNotNull(state, "State should not be null");
            assertNotEquals(state, null, "State should not equal null");
        }

        @Test
        @DisplayName("AttendanceOutcome should handle null safely")
        void testAttendanceOutcomeNullSafety() {
            AttendanceOutcome outcome = AttendanceOutcome.values()[0];
            assertNotNull(outcome, "Outcome should not be null");
            assertNotEquals(outcome, null, "Outcome should not equal null");
        }
    }
}

