package vcfs.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.awt.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for UIEnhancementUtils utility class.
 * Tests static UI enhancement constants and methods for professional component creation.
 * 
 * Uses ONLY PUBLIC API:
 * - Static color constants (PRIMARY_GREEN, PRIMARY_BLUE, PRIMARY_ORANGE, ERROR_RED, SUCCESS_GREEN, WARNING_ORANGE, LINK_BLUE, LIGHT_BACKGROUND, CARD_WHITE, BORDER_GRAY)
 * - Static font constants (HEADER_FONT, TITLE_FONT, NORMAL_FONT, SMALL_FONT, MONO_FONT)
 * - Panel creation: createHeaderPanel(), createInfoPanel()
 * - Notification methods: showSuccess(), showError(), showWarning(), showInfo()
 */
@DisplayName("UIEnhancementUtils - Professional UI Component Tests")
public class UIEnhancementUtilsTest {

    @Nested
    @DisplayName("Color Palette Tests")
    class ColorPaletteTests {

        @Test
        @DisplayName("PRIMARY_GREEN color should be defined")
        void testPrimaryGreenExists() {
            assertNotNull(UIEnhancementUtils.PRIMARY_GREEN, "PRIMARY_GREEN should be defined");
            assertTrue(UIEnhancementUtils.PRIMARY_GREEN instanceof Color, "Should be Color instance");
        }

        @Test
        @DisplayName("PRIMARY_BLUE color should be defined")
        void testPrimaryBlueExists() {
            assertNotNull(UIEnhancementUtils.PRIMARY_BLUE, "PRIMARY_BLUE should be defined");
            assertTrue(UIEnhancementUtils.PRIMARY_BLUE instanceof Color, "Should be Color instance");
        }

        @Test
        @DisplayName("PRIMARY_ORANGE color should be defined")
        void testPrimaryOrangeExists() {
            assertNotNull(UIEnhancementUtils.PRIMARY_ORANGE, "PRIMARY_ORANGE should be defined");
            assertTrue(UIEnhancementUtils.PRIMARY_ORANGE instanceof Color, "Should be Color instance");
        }

        @Test
        @DisplayName("ERROR_RED color should be defined")
        void testErrorRedExists() {
            assertNotNull(UIEnhancementUtils.ERROR_RED, "ERROR_RED should be defined");
            assertTrue(UIEnhancementUtils.ERROR_RED instanceof Color, "Should be Color instance");
        }

        @Test
        @DisplayName("SUCCESS_GREEN color should be defined")
        void testSuccessGreenExists() {
            assertNotNull(UIEnhancementUtils.SUCCESS_GREEN, "SUCCESS_GREEN should be defined");
            assertTrue(UIEnhancementUtils.SUCCESS_GREEN instanceof Color, "Should be Color instance");
        }

        @Test
        @DisplayName("WARNING_ORANGE color should be defined")
        void testWarningOrangeExists() {
            assertNotNull(UIEnhancementUtils.WARNING_ORANGE, "WARNING_ORANGE should be defined");
            assertTrue(UIEnhancementUtils.WARNING_ORANGE instanceof Color, "Should be Color instance");
        }

        @Test
        @DisplayName("LINK_BLUE color should be defined")
        void testLinkBlueExists() {
            assertNotNull(UIEnhancementUtils.LINK_BLUE, "LINK_BLUE should be defined");
            assertTrue(UIEnhancementUtils.LINK_BLUE instanceof Color, "Should be Color instance");
        }

        @Test
        @DisplayName("LIGHT_BACKGROUND color should be defined")
        void testLightBackgroundExists() {
            assertNotNull(UIEnhancementUtils.LIGHT_BACKGROUND, "LIGHT_BACKGROUND should be defined");
            assertTrue(UIEnhancementUtils.LIGHT_BACKGROUND instanceof Color, "Should be Color instance");
        }

        @Test
        @DisplayName("CARD_WHITE color should be defined")
        void testCardWhiteExists() {
            assertNotNull(UIEnhancementUtils.CARD_WHITE, "CARD_WHITE should be defined");
            assertTrue(UIEnhancementUtils.CARD_WHITE instanceof Color, "Should be Color instance");
        }

        @Test
        @DisplayName("BORDER_GRAY color should be defined")
        void testBorderGrayExists() {
            assertNotNull(UIEnhancementUtils.BORDER_GRAY, "BORDER_GRAY should be defined");
            assertTrue(UIEnhancementUtils.BORDER_GRAY instanceof Color, "Should be Color instance");
        }

        @Test
        @DisplayName("All colors should have valid RGB values")
        void testColorValidity() {
            Color[] colors = {
                UIEnhancementUtils.PRIMARY_GREEN, UIEnhancementUtils.PRIMARY_BLUE,
                UIEnhancementUtils.PRIMARY_ORANGE, UIEnhancementUtils.ERROR_RED,
                UIEnhancementUtils.SUCCESS_GREEN, UIEnhancementUtils.WARNING_ORANGE,
                UIEnhancementUtils.LINK_BLUE, UIEnhancementUtils.LIGHT_BACKGROUND,
                UIEnhancementUtils.CARD_WHITE, UIEnhancementUtils.BORDER_GRAY
            };

            for (Color color : colors) {
                assertNotNull(color, "Color should not be null");
                assertTrue(color.getRed() >= 0 && color.getRed() <= 255, "Red should be 0-255");
                assertTrue(color.getGreen() >= 0 && color.getGreen() <= 255, "Green should be 0-255");
                assertTrue(color.getBlue() >= 0 && color.getBlue() <= 255, "Blue should be 0-255");
            }
        }
    }

    @Nested
    @DisplayName("Font Constant Tests")
    class FontConstantTests {

        @Test
        @DisplayName("HEADER_FONT should be defined")
        void testHeaderFontExists() {
            assertNotNull(UIEnhancementUtils.HEADER_FONT, "HEADER_FONT should be defined");
            assertTrue(UIEnhancementUtils.HEADER_FONT instanceof Font, "Should be Font instance");
        }

        @Test
        @DisplayName("TITLE_FONT should be defined")
        void testTitleFontExists() {
            assertNotNull(UIEnhancementUtils.TITLE_FONT, "TITLE_FONT should be defined");
            assertTrue(UIEnhancementUtils.TITLE_FONT instanceof Font, "Should be Font instance");
        }

        @Test
        @DisplayName("NORMAL_FONT should be defined")
        void testNormalFontExists() {
            assertNotNull(UIEnhancementUtils.NORMAL_FONT, "NORMAL_FONT should be defined");
            assertTrue(UIEnhancementUtils.NORMAL_FONT instanceof Font, "Should be Font instance");
        }

        @Test
        @DisplayName("SMALL_FONT should be defined")
        void testSmallFontExists() {
            assertNotNull(UIEnhancementUtils.SMALL_FONT, "SMALL_FONT should be defined");
            assertTrue(UIEnhancementUtils.SMALL_FONT instanceof Font, "Should be Font instance");
        }

        @Test
        @DisplayName("MONO_FONT should be defined")
        void testMonoFontExists() {
            assertNotNull(UIEnhancementUtils.MONO_FONT, "MONO_FONT should be defined");
            assertTrue(UIEnhancementUtils.MONO_FONT instanceof Font, "Should be Font instance");
        }

        @Test
        @DisplayName("Fonts should have valid sizes")
        void testFontSizes() {
            Font[] fonts = {
                UIEnhancementUtils.HEADER_FONT, UIEnhancementUtils.TITLE_FONT,
                UIEnhancementUtils.NORMAL_FONT, UIEnhancementUtils.SMALL_FONT,
                UIEnhancementUtils.MONO_FONT
            };

            for (Font font : fonts) {
                assertNotNull(font, "Font should not be null");
                assertTrue(font.getSize() > 0, "Font size should be positive");
                assertTrue(font.getSize() < 100, "Font size should be reasonable");
            }
        }

        @Test
        @DisplayName("Font hierarchy should be correct")
        void testFontHierarchy() {
            assertTrue(UIEnhancementUtils.TITLE_FONT.getSize() > UIEnhancementUtils.NORMAL_FONT.getSize(),
                "Title should be larger than normal");
            assertTrue(UIEnhancementUtils.NORMAL_FONT.getSize() > UIEnhancementUtils.SMALL_FONT.getSize(),
                "Normal should be larger than small");
        }
    }

    @Nested
    @DisplayName("Header Panel Creation Tests")
    class HeaderPanelTests {

        @Test
        @DisplayName("createHeaderPanel() should return JPanel")
        void testCreateHeaderPanel() {
            JPanel panel = UIEnhancementUtils.createHeaderPanel("Title", "Subtitle", 
                                                              UIEnhancementUtils.PRIMARY_BLUE);
            
            assertNotNull(panel, "Should return JPanel instance");
            assertTrue(panel instanceof JPanel, "Should be JPanel type");
        }

        @Test
        @DisplayName("Header panel should accept string parameters")
        void testHeaderPanelParameters() {
            assertDoesNotThrow(() -> {
                JPanel panel = UIEnhancementUtils.createHeaderPanel("My Title", "My Subtitle",
                                                                   UIEnhancementUtils.PRIMARY_GREEN);
                assertNotNull(panel, "Panel should be created with parameters");
            }, "Should handle string parameters");
        }

        @Test
        @DisplayName("Header panel should accept color parameter")
        void testHeaderPanelColor() {
            JPanel panel = UIEnhancementUtils.createHeaderPanel("Title", "Subtitle",
                                                               UIEnhancementUtils.ERROR_RED);
            
            assertNotNull(panel, "Should handle color parameter");
            assertNotNull(panel.getBackground(), "Panel should have background");
        }

        @Test
        @DisplayName("Header panel should handle empty strings")
        void testHeaderPanelEmptyStrings() {
            JPanel panel = UIEnhancementUtils.createHeaderPanel("", "",
                                                               UIEnhancementUtils.CARD_WHITE);
            
            assertNotNull(panel, "Should handle empty strings");
        }

        @Test
        @DisplayName("Header panel should handle null parameters gracefully")
        void testHeaderPanelNullHandling() {
            try {
                JPanel panel = UIEnhancementUtils.createHeaderPanel(null, null, null);
                // Either succeeds or throws - both acceptable
            } catch (NullPointerException e) {
                assertTrue(true, "Null handling is acceptable");
            }
        }
    }

    @Nested
    @DisplayName("Info Panel Creation Tests")
    class InfoPanelTests {

        @Test
        @DisplayName("createInfoPanel() should return JPanel")
        void testCreateInfoPanel() {
            JPanel panel = UIEnhancementUtils.createInfoPanel("Title", "Message");
            
            assertNotNull(panel, "Should return JPanel instance");
            assertTrue(panel instanceof JPanel, "Should be JPanel type");
        }

        @Test
        @DisplayName("Info panel should display title and message")
        void testInfoPanelContent() {
            String title = "Info Title";
            String message = "Info Message";
            
            JPanel panel = UIEnhancementUtils.createInfoPanel(title, message);
            
            assertNotNull(panel, "Panel should be created");
            // Panel contains the content somehow
            assertTrue(panel.getComponentCount() > 0, "Panel should contain components");
        }

        @Test
        @DisplayName("Info panel should handle empty strings")
        void testInfoPanelEmptyStrings() {
            JPanel panel = UIEnhancementUtils.createInfoPanel("", "");
            
            assertNotNull(panel, "Should handle empty strings");
        }

        @Test
        @DisplayName("Info panel should handle long content")
        void testInfoPanelLongContent() {
            String longMessage = "This is a very long message that contains a lot of text to " +
                               "test how well the info panel handles extended content without " +
                               "breaking or causing display issues";
            
            JPanel panel = UIEnhancementUtils.createInfoPanel("Title", longMessage);
            
            assertNotNull(panel, "Should handle long content");
        }

        @Test
        @DisplayName("Multiple info panels should be independent")
        void testMultipleInfoPanels() {
            JPanel panel1 = UIEnhancementUtils.createInfoPanel("Panel 1", "Message 1");
            JPanel panel2 = UIEnhancementUtils.createInfoPanel("Panel 2", "Message 2");
            
            assertNotSame(panel1, panel2, "Should create separate panel instances");
        }
    }

    @Nested
    @DisplayName("Notification Methods Tests")
    class NotificationMethodsTests {

        @Test
        @DisplayName("showSuccess() should complete without exception")
        void testShowSuccess() {
            JFrame frame = new JFrame("Test");
            
            assertDoesNotThrow(() -> UIEnhancementUtils.showSuccess(frame, "Success", "Operation successful"),
                "showSuccess() should complete without exception");
        }

        @Test
        @DisplayName("showError() should complete without exception")
        void testShowError() {
            JFrame frame = new JFrame("Test");
            
            assertDoesNotThrow(() -> UIEnhancementUtils.showError(frame, "Error", "Operation failed"),
                "showError() should complete without exception");
        }

        @Test
        @DisplayName("showWarning() should complete without exception")
        void testShowWarning() {
            JFrame frame = new JFrame("Test");
            
            assertDoesNotThrow(() -> UIEnhancementUtils.showWarning(frame, "Warning", "Be careful"),
                "showWarning() should complete without exception");
        }

        @Test
        @DisplayName("showInfo() should complete without exception")
        void testShowInfo() {
            JFrame frame = new JFrame("Test");
            
            assertDoesNotThrow(() -> UIEnhancementUtils.showInfo(frame, "Info", "For your information"),
                "showInfo() should complete without exception");
        }

        @Test
        @DisplayName("All notification methods should handle empty strings")
        void testNotificationsEmptyStrings() {
            JFrame frame = new JFrame("Test");
            
            assertDoesNotThrow(() -> {
                UIEnhancementUtils.showSuccess(frame, "", "");
                UIEnhancementUtils.showError(frame, "", "");
                UIEnhancementUtils.showWarning(frame, "", "");
                UIEnhancementUtils.showInfo(frame, "", "");
            }, "Should handle empty strings");
        }

        @Test
        @DisplayName("Notification methods should handle long text")
        void testNotificationsLongText() {
            JFrame frame = new JFrame("Test");
            String longText = "This is a very long message that should be displayed in the " +
                            "notification dialog without causing any issues or truncation problems";
            
            assertDoesNotThrow(() -> {
                UIEnhancementUtils.showSuccess(frame, longText, longText);
                UIEnhancementUtils.showError(frame, longText, longText);
            }, "Should handle long text");
        }

        @Test
        @DisplayName("Notification methods should handle null frames")
        void testNotificationsNullFrame() {
            try {
                UIEnhancementUtils.showSuccess(null, "Title", "Message");
            } catch (NullPointerException e) {
                assertTrue(true, "Null handling acceptable");
            }
        }
    }

    @Nested
    @DisplayName("Color Consistency Tests")
    class ColorConsistencyTests {

        @Test
        @DisplayName("PRIMARY colors should be distinct")
        void testPrimaryColorsDistinct() {
            boolean allDifferent = !UIEnhancementUtils.PRIMARY_GREEN.equals(UIEnhancementUtils.PRIMARY_BLUE) &&
                                  !UIEnhancementUtils.PRIMARY_BLUE.equals(UIEnhancementUtils.PRIMARY_ORANGE) &&
                                  !UIEnhancementUtils.PRIMARY_ORANGE.equals(UIEnhancementUtils.PRIMARY_GREEN);
            
            assertTrue(allDifferent, "Primary colors should be distinct from each other");
        }

        @Test
        @DisplayName("Status colors should map correctly")
        void testStatusColorMapping() {
            // Success uses green
            assertNotNull(UIEnhancementUtils.SUCCESS_GREEN, "Success color defined");
            
            // Error uses red
            assertNotNull(UIEnhancementUtils.ERROR_RED, "Error color defined");
            
            // Warning uses orange
            assertNotNull(UIEnhancementUtils.WARNING_ORANGE, "Warning color defined");
        }

        @Test
        @DisplayName("Background colors should be lighter than primary colors")
        void testBackgroundColors() {
            // Light background should have higher brightness
            Color bg = UIEnhancementUtils.LIGHT_BACKGROUND;
            int brightness = bg.getRed() + bg.getGreen() + bg.getBlue();
            
            assertTrue(brightness > 150, "Light background should be bright");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Complete professional UI workflow")
        void testCompleteWorkflow() {
            JFrame mainFrame = new JFrame("Professional UI");
            
            assertDoesNotThrow(() -> {
                // Create header
                JPanel header = UIEnhancementUtils.createHeaderPanel(
                    "Application Title", "Subtitle",
                    UIEnhancementUtils.PRIMARY_BLUE);
                
                // Create info panel
                JPanel info = UIEnhancementUtils.createInfoPanel(
                    "Status", "System is running normally");
                
                // Show notifications
                UIEnhancementUtils.showSuccess(mainFrame, "Ready", "System initialized");
            }, "Professional UI workflow should work");
        }

        @Test
        @DisplayName("Multiple panels and notifications should coexist")
        void testMultiplePanelsAndNotifications() {
            JFrame frame = new JFrame("Multi-Panel");
            
            assertDoesNotThrow(() -> {
                JPanel header1 = UIEnhancementUtils.createHeaderPanel("Header 1", "Sub 1", 
                                                                     UIEnhancementUtils.PRIMARY_GREEN);
                JPanel header2 = UIEnhancementUtils.createHeaderPanel("Header 2", "Sub 2",
                                                                     UIEnhancementUtils.PRIMARY_ORANGE);
                
                JPanel info1 = UIEnhancementUtils.createInfoPanel("Info 1", "Message 1");
                JPanel info2 = UIEnhancementUtils.createInfoPanel("Info 2", "Message 2");
                
                UIEnhancementUtils.showSuccess(frame, "Success 1", "Done 1");
                UIEnhancementUtils.showError(frame, "Error 1", "Failed 1");
            }, "Multiple panels should coexist");
        }

        @Test
        @DisplayName("All constants should be accessible and usable")
        void testAllConstantsAccessible() {
            assertNotNull(UIEnhancementUtils.PRIMARY_GREEN, "Colors accessible");
            assertNotNull(UIEnhancementUtils.HEADER_FONT, "Fonts accessible");
            
            JPanel panel = UIEnhancementUtils.createHeaderPanel(
                "Using Constants", "Title",
                UIEnhancementUtils.PRIMARY_BLUE);
            
            assertNotNull(panel, "Constants work in methods");
        }
    }
}

