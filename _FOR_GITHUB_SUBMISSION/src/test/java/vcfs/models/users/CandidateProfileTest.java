package vcfs.models.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Comprehensive JUnit tests for CandidateProfile class.
 * Tests candidate profile information including skills, CV, and preferences.
 * 
 * CSCU9P6 Group Project - Test Suite Implementation
 * Author: Zaid Siddiqui (mzs00007)
 */
@DisplayName("CandidateProfile - Candidate Profile Information")
public class CandidateProfileTest {

    private CandidateProfile profile;

    @BeforeEach
    void setUp() {
        profile = new CandidateProfile();
    }

    // ========== CONSTRUCTOR & INITIALIZATION ==========

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("CandidateProfile constructor creates empty instance")
        void testConstructorCreatesEmpty() {
            assertNotNull(profile);
            assertNull(profile.getCV());
            assertNull(profile.getSkills());
            assertNull(profile.getPreferences());
        }

        @Test
        @DisplayName("Multiple profiles are independent instances")
        void testMultipleProfilesIndependent() {
            CandidateProfile profile1 = new CandidateProfile();
            CandidateProfile profile2 = new CandidateProfile();
            
            assertNotSame(profile1, profile2);
        }
    }

    // ========== CV MANAGEMENT ==========

    @Nested
    @DisplayName("CV Management Tests")
    class CVManagementTests {

        @Test
        @DisplayName("getCV returns null initially")
        void testGetCVInitiallyNull() {
            assertNull(profile.getCV());
        }

        @Test
        @DisplayName("setCV stores CV content")
        void testSetCV() {
            String cvContent = "CV: Bachelor's in Computer Science";
            profile.setCV(cvContent);
            assertEquals(cvContent, profile.getCV());
        }

        @Test
        @DisplayName("setCV can be set to null")
        void testSetCVNull() {
            profile.setCV("Some CV");
            profile.setCV(null);
            assertNull(profile.getCV());
        }

        @Test
        @DisplayName("setCV with empty string")
        void testSetCVEmpty() {
            profile.setCV("");
            assertEquals("", profile.getCV());
        }

        @Test
        @DisplayName("setCV with long content")
        void testSetCVLongContent() {
            String longCV = "PROFESSIONAL SUMMARY\n" +
                           "Experienced software developer with 5+ years of experience\n" +
                           "TECHNICAL SKILLS: Java, Python, SQL, etc.";
            profile.setCV(longCV);
            assertEquals(longCV, profile.getCV());
        }

        @Test
        @DisplayName("setCV can be updated multiple times")
        void testUpdateCV() {
            profile.setCV("Original CV");
            assertEquals("Original CV", profile.getCV());
            
            profile.setCV("Updated CV");
            assertEquals("Updated CV", profile.getCV());
        }
    }

    // ========== SKILLS MANAGEMENT ==========

    @Nested
    @DisplayName("Skills Management Tests")
    class SkillsManagementTests {

        @Test
        @DisplayName("getSkills returns null initially")
        void testGetSkillsInitiallyNull() {
            assertNull(profile.getSkills());
        }

        @Test
        @DisplayName("setSkills stores skills")
        void testSetSkills() {
            String skills = "Java, Python, SQL, Spring Boot";
            profile.setSkills(skills);
            assertEquals(skills, profile.getSkills());
        }

        @Test
        @DisplayName("setSkills can be set to null")
        void testSetSkillsNull() {
            profile.setSkills("Java, Python");
            profile.setSkills(null);
            assertNull(profile.getSkills());
        }

        @Test
        @DisplayName("setSkills with empty string")
        void testSetSkillsEmpty() {
            profile.setSkills("");
            assertEquals("", profile.getSkills());
        }

        @Test
        @DisplayName("setSkills with various programming languages")
        void testSetSkillsVariousLanguages() {
            String skills = "JavaScript, TypeScript, React, Node.js, SQL, MongoDB";
            profile.setSkills(skills);
            assertEquals(skills, profile.getSkills());
        }

        @Test
        @DisplayName("setSkills can be updated multiple times")
        void testUpdateSkills() {
            profile.setSkills("Java, Python");
            assertEquals("Java, Python", profile.getSkills());
            
            profile.setSkills("Java, Python, C++, Rust");
            assertEquals("Java, Python, C++, Rust", profile.getSkills());
        }
    }

    // ========== PREFERENCES MANAGEMENT ==========

    @Nested
    @DisplayName("Preferences Management Tests")
    class PreferencesManagementTests {

        @Test
        @DisplayName("getPreferences returns null initially")
        void testGetPreferencesInitiallyNull() {
            assertNull(profile.getPreferences());
        }

        @Test
        @DisplayName("setPreferences stores preferences")
        void testSetPreferences() {
            String preferences = "Graduate positions, Tech companies, London area";
            profile.setPreferences(preferences);
            assertEquals(preferences, profile.getPreferences());
        }

        @Test
        @DisplayName("setPreferences can be set to null")
        void testSetPreferencesNull() {
            profile.setPreferences("Some preferences");
            profile.setPreferences(null);
            assertNull(profile.getPreferences());
        }

        @Test
        @DisplayName("setPreferences with empty string")
        void testSetPreferencesEmpty() {
            profile.setPreferences("");
            assertEquals("", profile.getPreferences());
        }

        @Test
        @DisplayName("setPreferences with detailed information")
        void testSetPreferencesDetailed() {
            String preferences = "Location: Remote, Contract: Full-time, " +
                               "Industry: Tech, Salary: Competitive";
            profile.setPreferences(preferences);
            assertEquals(preferences, profile.getPreferences());
        }

        @Test
        @DisplayName("setPreferences can be updated")
        void testUpdatePreferences() {
            profile.setPreferences("Original preferences");
            assertEquals("Original preferences", profile.getPreferences());
            
            profile.setPreferences("Updated preferences");
            assertEquals("Updated preferences", profile.getPreferences());
        }
    }

    // ========== INDEPENDENT FIELD UPDATES ==========

    @Nested
    @DisplayName("Independent Field Updates Tests")
    class IndependentFieldUpdatesTests {

        @Test
        @DisplayName("Setting CV does not affect skills or preferences")
        void testCVDoesNotAffectOtherFields() {
            profile.setSkills("Java");
            profile.setPreferences("Tech roles");
            profile.setCV("Some CV");
            
            assertEquals("Java", profile.getSkills());
            assertEquals("Tech roles", profile.getPreferences());
            assertEquals("Some CV", profile.getCV());
        }

        @Test
        @DisplayName("Setting skills does not affect CV or preferences")
        void testSkillsDoNotAffectOtherFields() {
            profile.setCV("Some CV");
            profile.setPreferences("Tech roles");
            profile.setSkills("Python, JavaScript");
            
            assertEquals("Some CV", profile.getCV());
            assertEquals("Tech roles", profile.getPreferences());
            assertEquals("Python, JavaScript", profile.getSkills());
        }

        @Test
        @DisplayName("Setting preferences does not affect CV or skills")
        void testPreferencesDoNotAffectOtherFields() {
            profile.setCV("Some CV");
            profile.setSkills("Java");
            profile.setPreferences("Remote work");
            
            assertEquals("Some CV", profile.getCV());
            assertEquals("Java", profile.getSkills());
            assertEquals("Remote work", profile.getPreferences());
        }

        @Test
        @DisplayName("Multiple field updates in sequence")
        void testMultipleFieldUpdatesInSequence() {
            profile.setCV("CV content");
            profile.setSkills("Java, Python");
            profile.setPreferences("London, Tech industry");
            
            assertEquals("CV content", profile.getCV());
            assertEquals("Java, Python", profile.getSkills());
            assertEquals("London, Tech industry", profile.getPreferences());
        }
    }

    // ========== COMPLETE PROFILE SCENARIO ==========

    @Nested
    @DisplayName("Complete Profile Scenario Tests")
    class CompleteProfileScenarioTests {

        @Test
        @DisplayName("Complete profile with all fields populated")
        void testCompleteProfileAllFieldsPopulated() {
            String cv = "Master's Degree in Computer Science\nExperience: 2 years";
            String skills = "Java, Spring Boot, SQL, Docker, Kubernetes";
            String preferences = "Full-time, London, Competitive salary";
            
            profile.setCV(cv);
            profile.setSkills(skills);
            profile.setPreferences(preferences);
            
            assertEquals(cv, profile.getCV());
            assertEquals(skills, profile.getSkills());
            assertEquals(preferences, profile.getPreferences());
        }

        @Test
        @DisplayName("Reset profile fields to null")
        void testResetProfileToNull() {
            profile.setCV("CV");
            profile.setSkills("Skills");
            profile.setPreferences("Preferences");
            
            profile.setCV(null);
            profile.setSkills(null);
            profile.setPreferences(null);
            
            assertNull(profile.getCV());
            assertNull(profile.getSkills());
            assertNull(profile.getPreferences());
        }

        @Test
        @DisplayName("Partial profile with only some fields populated")
        void testPartialProfileSomeFieldsPopulated() {
            profile.setSkills("Python, JavaScript");
            profile.setPreferences("Tech company");
            
            assertNull(profile.getCV());
            assertEquals("Python, JavaScript", profile.getSkills());
            assertEquals("Tech company", profile.getPreferences());
        }
    }
}
