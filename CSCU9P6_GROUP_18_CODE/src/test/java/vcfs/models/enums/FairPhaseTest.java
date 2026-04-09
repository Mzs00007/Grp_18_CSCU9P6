package vcfs.models.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for FairPhase enum.
 * Tests all fair lifecycle phases.
 */
@DisplayName("FairPhase Enum Test Suite")
public class FairPhaseTest {

    // =========================================================
    // ENUM VALUES Tests
    // =========================================================

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @Test
        @DisplayName("Should have DORMANT phase")
        void testHasDormantPhase() {
            assertTrue(FairPhase.DORMANT != null, "Should have DORMANT phase");
        }

        @Test
        @DisplayName("Should have PREPARING phase")
        void testHasPreparingPhase() {
            assertTrue(FairPhase.PREPARING != null, "Should have PREPARING phase");
        }

        @Test
        @DisplayName("Should have BOOKINGS_OPEN phase")
        void testHasBookingsOpenPhase() {
            assertTrue(FairPhase.BOOKINGS_OPEN != null, "Should have BOOKINGS_OPEN phase");
        }

        @Test
        @DisplayName("Should have BOOKINGS_CLOSED phase")
        void testHasBookingsClosedPhase() {
            assertTrue(FairPhase.BOOKINGS_CLOSED != null, "Should have BOOKINGS_CLOSED phase");
        }

        @Test
        @DisplayName("Should have FAIR_LIVE phase")
        void testHasFairLivePhase() {
            assertTrue(FairPhase.FAIR_LIVE != null, "Should have FAIR_LIVE phase");
        }

        @Test
        @DisplayName("Should have all phases")
        void testAllPhasesExist() {
            FairPhase[] phases = FairPhase.values();
            assertEquals(5, phases.length, "Should have exactly 5 phases");
        }
    }

    // =========================================================
    // PHASE SEQUENCE Tests
    // =========================================================

    @Nested
    @DisplayName("Phase Sequence Tests")
    class PhaseSequenceTests {

        @Test
        @DisplayName("Should have logical phase progression")
        void testPhaseProgression() {
            int dormantOrdinal = FairPhase.DORMANT.ordinal();
            int preparingOrdinal = FairPhase.PREPARING.ordinal();
            int bookingsOpenOrdinal = FairPhase.BOOKINGS_OPEN.ordinal();
            int bookingsClosedOrdinal = FairPhase.BOOKINGS_CLOSED.ordinal();
            int fairLiveOrdinal = FairPhase.FAIR_LIVE.ordinal();

            assertTrue(dormantOrdinal < preparingOrdinal, "DORMANT should come before PREPARING");
            assertTrue(preparingOrdinal < bookingsOpenOrdinal, "PREPARING should come before BOOKINGS_OPEN");
            assertTrue(bookingsOpenOrdinal < bookingsClosedOrdinal, "BOOKINGS_OPEN should come before BOOKINGS_CLOSED");
            assertTrue(bookingsClosedOrdinal < fairLiveOrdinal, "BOOKINGS_CLOSED should come before FAIR_LIVE");
        }

        @Test
        @DisplayName("Should transition from DORMANT to PREPARING")
        void testDormantToPreparingTransition() {
            assertTrue(FairPhase.DORMANT.ordinal() < FairPhase.PREPARING.ordinal(),
                      "Should transition to PREPARING");
        }

        @Test
        @DisplayName("Should transition to FAIR_LIVE as final phase")
        void testFinalPhaseIsLive() {
            FairPhase lastPhase = FairPhase.FAIR_LIVE;
            for (FairPhase phase : FairPhase.values()) {
                assertTrue(phase.ordinal() <= lastPhase.ordinal(),
                          "FAIR_LIVE should be last phase");
            }
        }
    }

    // =========================================================
    // PHASE COMPARISON Tests
    // =========================================================

    @Nested
    @DisplayName("Phase Comparison Tests")
    class PhaseComparisonTests {

        @Test
        @DisplayName("Should distinguish between different phases")
        void testPhaseDistinction() {
            assertNotEquals(FairPhase.DORMANT, FairPhase.PREPARING, "Phases should differ");
            assertNotEquals(FairPhase.BOOKINGS_OPEN, FairPhase.FAIR_LIVE, "Phases should differ");
            assertNotEquals(FairPhase.PREPARING, FairPhase.BOOKINGS_CLOSED, "Phases should differ");
        }

        @Test
        @DisplayName("Should recognize same phase as equal")
        void testSamePhaseEquality() {
            FairPhase phase1 = FairPhase.BOOKINGS_OPEN;
            FairPhase phase2 = FairPhase.BOOKINGS_OPEN;

            assertEquals(phase1, phase2, "Same phase should be equal");
            assertSame(phase1, phase2, "Same phase should have same identity");
        }
    }

    // =========================================================
    // PHASE SEMANTICS Tests
    // =========================================================

    @Nested
    @DisplayName("Phase Semantics Tests")
    class PhaseSemanticTests {

        @Test
        @DisplayName("DORMANT should be initial phase")
        void testDormantIsInitial() {
            FairPhase[] phases = FairPhase.values();
            assertEquals(0, phases[0].ordinal(), "First phase should be DORMANT");
        }

        @Test
        @DisplayName("PREPARING should prepare fair")
        void testPreparingPhase() {
            assertTrue(FairPhase.PREPARING.ordinal() > FairPhase.DORMANT.ordinal(),
                      "PREPARING should come after DORMANT");
        }

        @Test
        @DisplayName("BOOKINGS_OPEN should allow candidate bookings")
        void testBookingsOpenPhase() {
            assertTrue(FairPhase.BOOKINGS_OPEN.ordinal() > FairPhase.PREPARING.ordinal(),
                      "BOOKINGS_OPEN should come after PREPARING");
        }

        @Test
        @DisplayName("BOOKINGS_CLOSED should close bookings")
        void testBookingsClosedPhase() {
            assertTrue(FairPhase.BOOKINGS_CLOSED.ordinal() > FairPhase.BOOKINGS_OPEN.ordinal(),
                      "BOOKINGS_CLOSED should come after BOOKINGS_OPEN");
        }

        @Test
        @DisplayName("FAIR_LIVE should be active fair")
        void testFairLivePhase() {
            assertTrue(FairPhase.FAIR_LIVE.ordinal() > FairPhase.BOOKINGS_CLOSED.ordinal(),
                      "FAIR_LIVE should come after BOOKINGS_CLOSED");
        }
    }

    // =========================================================
    // PHASE CONVERSION Tests
    // =========================================================

    @Nested
    @DisplayName("Phase Conversion Tests")
    class PhaseConversionTests {

        @Test
        @DisplayName("Should convert string to DORMANT")
        void testValueOfDormant() {
            FairPhase phase = FairPhase.valueOf("DORMANT");
            assertEquals(FairPhase.DORMANT, phase, "Should convert to DORMANT");
        }

        @Test
        @DisplayName("Should convert string to FAIR_LIVE")
        void testValueOfFairLive() {
            FairPhase phase = FairPhase.valueOf("FAIR_LIVE");
            assertEquals(FairPhase.FAIR_LIVE, phase, "Should convert to FAIR_LIVE");
        }

        @Test
        @DisplayName("Should throw exception for invalid phase")
        void testValueOfInvalid() {
            assertThrows(IllegalArgumentException.class, () -> {
                FairPhase.valueOf("INVALID_PHASE");
            }, "Should throw for invalid phase");
        }

        @Test
        @DisplayName("Should convert to string representation")
        void testToString() {
            String phase = FairPhase.BOOKINGS_OPEN.toString();
            assertTrue(phase.contains("BOOKINGS_OPEN"), "Should contain phase name");
        }
    }

    // =========================================================
    // PHASE USAGE Tests
    // =========================================================

    @Nested
    @DisplayName("Phase Usage Tests")
    class PhaseUsageTests {

        @Test
        @DisplayName("Should use in switch statement")
        void testSwitchStatement() {
            FairPhase phase = FairPhase.BOOKINGS_OPEN;
            String result = "";

            switch (phase) {
                case DORMANT:
                    result = "DORMANT";
                    break;
                case PREPARING:
                    result = "PREPARING";
                    break;
                case BOOKINGS_OPEN:
                    result = "BOOKINGS_OPEN";
                    break;
                case BOOKINGS_CLOSED:
                    result = "BOOKINGS_CLOSED";
                    break;
                case FAIR_LIVE:
                    result = "FAIR_LIVE";
                    break;
            }

            assertEquals("BOOKINGS_OPEN", result, "Should use in switch");
        }

        @Test
        @DisplayName("Should store in array")
        void testStoreInArray() {
            FairPhase[] phases = FairPhase.values();

            assertEquals(5, phases.length, "Should have 5 phases in array");
            assertEquals(FairPhase.PREPARING, phases[1], "Should access from array");
        }

        @Test
        @DisplayName("Should work with EnumSet")
        void testEnumSetUsage() {
            java.util.Set<FairPhase> activePhases = java.util.EnumSet.of(
                FairPhase.BOOKINGS_OPEN,
                FairPhase.BOOKINGS_CLOSED,
                FairPhase.FAIR_LIVE
            );

            assertTrue(activePhases.contains(FairPhase.BOOKINGS_OPEN), "Should contain BOOKINGS_OPEN");
            assertFalse(activePhases.contains(FairPhase.DORMANT), "Should not contain DORMANT");
        }

        @Test
        @DisplayName("Should work with HashMap")
        void testHashMapUsage() {
            java.util.Map<FairPhase, String> descriptions = new java.util.HashMap<>();

            descriptions.put(FairPhase.DORMANT, "Fair not started");
            descriptions.put(FairPhase.PREPARING, "Setting up fair");
            descriptions.put(FairPhase.BOOKINGS_OPEN, "Candidates booking interviews");
            descriptions.put(FairPhase.BOOKINGS_CLOSED, "Bookings limited");
            descriptions.put(FairPhase.FAIR_LIVE, "Fair is happening");

            assertEquals("Candidates booking interviews", 
                        descriptions.get(FairPhase.BOOKINGS_OPEN),
                        "Should work with HashMap");
        }
    }

    // =========================================================
    // PHASE ITERATION Tests
    // =========================================================

    @Nested
    @DisplayName("Phase Iteration Tests")
    class PhaseIterationTests {

        @Test
        @DisplayName("Should iterate through all phases")
        void testIterateAllPhases() {
            int count = 0;
            for (@SuppressWarnings("unused") FairPhase phase : FairPhase.values()) {
                count++;
            }
            assertEquals(5, count, "Should iterate through 5 phases");
        }

        @Test
        @DisplayName("Should iterate in order")
        void testIterateInOrder() {
            FairPhase[] phases = FairPhase.values();

            for (int i = 0; i < phases.length - 1; i++) {
                assertTrue(phases[i].ordinal() < phases[i + 1].ordinal(),
                          "Phases should be in order");
            }
        }
    }
}

