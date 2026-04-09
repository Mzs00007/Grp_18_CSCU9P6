package vcfs.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for AdminScreenController.
 * Tests organization/booth creation, system configuration, and admin operations.
 */
@DisplayName("AdminScreenController Test Suite")
public class AdminScreenControllerTest {


    private AdminScreenController controller;

    @BeforeEach
    void setUp() {
        controller = new AdminScreenController();
    }

    // =========================================================
    // CREATE ORGANIZATION Tests
    // =========================================================

    @Nested
    @DisplayName("Create Organization Tests")
    class CreateOrganizationTests {

        @Test
        @DisplayName("Should create organization with valid name")
        void testCreateOrganizationValid() {
            assertDoesNotThrow(() -> {
                controller.createOrganization("Tech Corp");
            }, "Should create organization with valid name");
        }

        @Test
        @DisplayName("Should throw exception when name is null")
        void testCreateOrganizationNull() {
            assertThrows(IllegalArgumentException.class, () -> {
                controller.createOrganization(null);
            }, "Should throw exception for null name");
        }

        @Test
        @DisplayName("Should throw exception when name is empty")
        void testCreateOrganizationEmpty() {
            assertThrows(IllegalArgumentException.class, () -> {
                controller.createOrganization("");
            }, "Should throw exception for empty name");
        }

        @Test
        @DisplayName("Should throw exception when name is whitespace only")
        void testCreateOrganizationWhitespace() {
            assertThrows(IllegalArgumentException.class, () -> {
                controller.createOrganization("   ");
            }, "Should throw exception for whitespace-only name");
        }

        @Test
        @DisplayName("Should create organization with trimmed name")
        void testCreateOrganizationTrimmed() {
            assertDoesNotThrow(() -> {
                controller.createOrganization("  Google  ");
            }, "Should trim and create organization");
        }

        @Test
        @DisplayName("Should create organization with special characters")
        void testCreateOrganizationSpecialCharacters() {
            assertDoesNotThrow(() -> {
                controller.createOrganization("Microsoft Corp. (UK)");
            }, "Should handle special characters in name");
        }

        @Test
        @DisplayName("Should create organization with long name")
        void testCreateOrganizationLongName() {
            String longName = "International Business Machines Corporation Technology Services Division";
            assertDoesNotThrow(() -> {
                controller.createOrganization(longName);
            }, "Should handle long organization names");
        }

        @Test
        @DisplayName("Should create multiple independent organizations")
        void testCreateOrganizationMultiple() {
            assertDoesNotThrow(() -> {
                controller.createOrganization("Google");
                controller.createOrganization("Amazon");
                controller.createOrganization("Apple");
            }, "Should create multiple organizations");
        }

        @Test
        @DisplayName("Should handle organization with numbers")
        void testCreateOrganizationWithNumbers() {
            assertDoesNotThrow(() -> {
                controller.createOrganization("Top 10 Tech Companies");
            }, "Should handle numbers in organization name");
        }
    }

    // =========================================================
    // CREATE BOOTH Tests
    // =========================================================

    @Nested
    @DisplayName("Create Booth Tests")
    class CreateBoothTests {

        @Test
        @DisplayName("Should create booth with valid parameters")
        void testCreateBoothValid() {
            // First create an organization
            assertDoesNotThrow(() -> {
                controller.createOrganization("Tech Corp");
                controller.createBooth("Booth A1", "Tech Corp");
            }, "Should create booth with valid parameters");
        }

        @Test
        @DisplayName("Should throw exception when booth name is null")
        void testCreateBoothNameNull() {
            assertDoesNotThrow(() -> {
                controller.createOrganization("Tech Corp");
            });

            assertThrows(IllegalArgumentException.class, () -> {
                controller.createBooth(null, "Tech Corp");
            }, "Should throw exception for null booth name");
        }

        @Test
        @DisplayName("Should throw exception when booth name is empty")
        void testCreateBoothNameEmpty() {
            assertDoesNotThrow(() -> {
                controller.createOrganization("Tech Corp");
            });

            assertThrows(IllegalArgumentException.class, () -> {
                controller.createBooth("", "Tech Corp");
            }, "Should throw exception for empty booth name");
        }

        @Test
        @DisplayName("Should throw exception when organization name is null")
        void testCreateBoothOrgNull() {
            assertThrows(IllegalArgumentException.class, () -> {
                controller.createBooth("Booth A1", null);
            }, "Should throw exception for null organization name");
        }

        @Test
        @DisplayName("Should throw exception when organization name is empty")
        void testCreateBoothOrgEmpty() {
            assertThrows(IllegalArgumentException.class, () -> {
                controller.createBooth("Booth A1", "");
            }, "Should throw exception for empty organization name");
        }

        @Test
        @DisplayName("Should throw exception when organization not found")
        void testCreateBoothOrgNotFound() {
            assertThrows(IllegalArgumentException.class, () -> {
                controller.createBooth("Booth A1", "NonexistentCorp");
            }, "Should throw exception when organization not found");
        }

        @Test
        @DisplayName("Should create booth with trimmed names")
        void testCreateBoothTrimmed() {
            assertDoesNotThrow(() -> {
                controller.createOrganization("  Tech Corp  ");
                controller.createBooth("  Booth A1  ", "Tech Corp");
            }, "Should trim and create booth");
        }

        @Test
        @DisplayName("Should handle booth name with both whitespace edges")
        void testCreateBoothWhitespaceEdges() {
            assertDoesNotThrow(() -> {
                controller.createOrganization("Google");
                controller.createBooth("   Main Booth   ", "Google");
            }, "Should handle whitespace in booth names");
        }

        @Test
        @DisplayName("Should create multiple booths for same organization")
        void testCreateBoothMultipleSameOrg() {
            assertDoesNotThrow(() -> {
                controller.createOrganization("Tech Corp");
                controller.createBooth("Booth A1", "Tech Corp");
                controller.createBooth("Booth A2", "Tech Corp");
                controller.createBooth("Booth A3", "Tech Corp");
            }, "Should create multiple booths for same organization");
        }

        @Test
        @DisplayName("Should create booth for different organizations")
        void testCreateBoothMultipleOrgs() {
            assertDoesNotThrow(() -> {
                controller.createOrganization("Google");
                controller.createOrganization("Amazon");
                controller.createBooth("Booth G1", "Google");
                controller.createBooth("Booth A1", "Amazon");
            }, "Should create booths for different organizations");
        }

        @Test
        @DisplayName("Should handle booth name with special characters")
        void testCreateBoothSpecialCharacters() {
            assertDoesNotThrow(() -> {
                controller.createOrganization("Tech-Corp (Inc.)");
                controller.createBooth("Booth #A-1", "Tech-Corp (Inc.)");
            }, "Should handle special characters in booth name");
        }

        @Test
        @DisplayName("Should handle booth with numeric naming")
        void testCreateBoothNumericNaming() {
            assertDoesNotThrow(() -> {
                controller.createOrganization("TechCorp");
                controller.createBooth("101", "TechCorp");
                controller.createBooth("202", "TechCorp");
            }, "Should handle purely numeric booth names");
        }
    }

    // =========================================================
    // ADMIN SETUP WORKFLOW Tests
    // =========================================================

    @Nested
    @DisplayName("Admin Setup Workflow Tests")
    class AdminSetupWorkflowTests {

        @Test
        @DisplayName("Should complete full fair setup workflow")
        void testCompleteAdminSetup() {
            assertDoesNotThrow(() -> {
                // Create organizations
                controller.createOrganization("Google");
                controller.createOrganization("Microsoft");
                controller.createOrganization("Amazon");

                // Create booths for each organization
                controller.createBooth("Google Booth 1", "Google");
                controller.createBooth("Google Booth 2", "Google");
                controller.createBooth("Microsoft Booth 1", "Microsoft");
                controller.createBooth("Amazon Booth 1", "Amazon");
            }, "Should complete full admin setup");
        }

        @Test
        @DisplayName("Should handle sequential organization and booth creation")
        void testSequentialCreation() {
            assertDoesNotThrow(() -> {
                for (int i = 1; i <= 5; i++) {
                    String orgName = "Company" + i;
                    controller.createOrganization(orgName);
                    
                    for (int j = 1; j <= 3; j++) {
                        controller.createBooth("Booth" + j, orgName);
                    }
                }
            }, "Should handle sequential creation");
        }

        @Test
        @DisplayName("Should add booth to existing organization")
        void testAddBoothToExistingOrg() {
            assertDoesNotThrow(() -> {
                controller.createOrganization("TechCorp");
                controller.createBooth("Booth 1", "TechCorp");
                
                // Later - add more boths to same org
                controller.createBooth("Booth 2", "TechCorp");
                controller.createBooth("Booth 3", "TechCorp");
            }, "Should add booths to existing organization");
        }
    }

    // =========================================================
    // ERROR HANDLING Tests
    // =========================================================

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should throw IllegalArgumentException with meaningful message")
        void testErrorMessageContent() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                controller.createOrganization(null);
            });

            assertTrue(exception.getMessage().contains("cannot be") || 
                      exception.getMessage().contains("empty") ||
                      exception.getMessage().contains("null"),
                "Error message should be meaningful");
        }

        @Test
        @DisplayName("Should handle mixed null and empty errors")
        void testMixedErrorHandling() {
            assertThrows(IllegalArgumentException.class, () -> {
                controller.createOrganization(null);
            });

            assertThrows(IllegalArgumentException.class, () -> {
                controller.createOrganization("");
            });

            assertThrows(IllegalArgumentException.class, () -> {
                controller.createBooth("", "AnyOrg");
            });
        }

        @Test
        @DisplayName("Should not crash on rapid error attempts")
        void testRapidErrorHandling() {
            for (int i = 0; i < 10; i++) {
                try {
                    controller.createOrganization("");
                } catch (IllegalArgumentException e) {
                    // Expected
                }
            }
            
            // Should still work after errors
            assertDoesNotThrow(() -> {
                controller.createOrganization("ValidCorp");
            }, "Should recover after multiple errors");
        }
    }

    // =========================================================
    // STATE INDEPENDENCE Tests
    // =========================================================

    @Nested
    @DisplayName("State Independence Tests")
    class StateIndependenceTests {

        @Test
        @DisplayName("Should maintain independent state between different instances")
        void testInstanceIndependence() {
            AdminScreenController controller1 = new AdminScreenController();
            AdminScreenController controller2 = new AdminScreenController();

            assertDoesNotThrow(() -> {
                controller1.createOrganization("Company1");
                controller2.createOrganization("Company2");
                
                controller1.createBooth("BoothA", "Company1");
                controller2.createBooth("BoothB", "Company2");
            }, "Should maintain independent state");
        }

        @Test
        @DisplayName("Should handle organization creation errors independently")
        void testErrorIndependence() {
            AdminScreenController controller1 = new AdminScreenController();
            AdminScreenController controller2 = new AdminScreenController();

            try {
                controller1.createOrganization(null);
            } catch (IllegalArgumentException e) {
                // Expected
            }

            // Controller2 should still work
            assertDoesNotThrow(() -> {
                controller2.createOrganization("ValidCorp");
            }, "Error in controller1 should not affect controller2");
        }
    }

    // =========================================================
    // VALIDATION Tests
    // =========================================================

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should validate organization name not null")
        void testValidateOrgNotNull() {
            assertThrows(Exception.class, () -> {
                controller.createOrganization(null);
            });
        }

        @Test
        @DisplayName("Should validate organization name not empty after trim")
        void testValidateOrgNotEmptyAfterTrim() {
            assertThrows(Exception.class, () -> {
                controller.createOrganization("     ");
            });
        }

        @Test
        @DisplayName("Should validate booth name not null")
        void testValidateBoothNotNull() {
            assertDoesNotThrow(() -> {
                controller.createOrganization("Org1");
            });

            assertThrows(Exception.class, () -> {
                controller.createBooth(null, "Org1");
            });
        }

        @Test
        @DisplayName("Should validate booth organization exists")
        void testValidateBoothOrgExists() {
            assertThrows(Exception.class, () -> {
                controller.createBooth("Booth1", "NonexistentOrg");
            });
        }

        @Test
        @DisplayName("Should accept valid organization names")
        void testAcceptValidOrgNames() {
            assertDoesNotThrow(() -> {
                controller.createOrganization("A");
                controller.createOrganization("Google");
                controller.createOrganization("Acme Corporation Limited");
                controller.createOrganization("123 Tech");
                controller.createOrganization("Tech-Corp & Co.");
            }, "Should accept all valid organization names");
        }
    }

    // =========================================================
    // SCALE Tests
    // =========================================================

    @Nested
    @DisplayName("Scale Tests")
    class ScaleTests {

        @Test
        @DisplayName("Should handle many organizations")
        void testHandleManyOrganizations() {
            assertDoesNotThrow(() -> {
                for (int i = 0; i < 50; i++) {
                    controller.createOrganization("Organization" + i);
                }
            }, "Should handle 50 organizations");
        }

        @Test
        @DisplayName("Should handle many booths per organization")
        void testHandleManyBoothsPerOrg() {
            assertDoesNotThrow(() -> {
                controller.createOrganization("MainOrg");
                for (int i = 0; i < 30; i++) {
                    controller.createBooth("Booth" + i, "MainOrg");
                }
            }, "Should handle 30 booths per organization");
        }

        @Test
        @DisplayName("Should handle complex fair setup with many companies and booths")
        void testComplexFairSetup() {
            assertDoesNotThrow(() -> {
                for (int i = 1; i <= 10; i++) {
                    String orgName = "Company" + i;
                    controller.createOrganization(orgName);
                    
                    for (int j = 1; j <= 5; j++) {
                        controller.createBooth("Booth" + j, orgName);
                    }
                }
            }, "Should handle 10 companies with 5 booths each");
        }
    }
}
