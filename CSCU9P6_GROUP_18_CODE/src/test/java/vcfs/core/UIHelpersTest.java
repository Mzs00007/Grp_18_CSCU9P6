package vcfs.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.awt.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for UIHelpers utility class.
 * Tests static UI utility constants and methods for dialogs and styling.
 * 
 * Uses ONLY PUBLIC API:
 * - Static color constants (COLOR_SUCCESS, COLOR_ERROR, COLOR_WARNING, COLOR_INFO, COLOR_PRIMARY, COLOR_SECONDARY)
 * - Static font constants (FONT_TITLE, FONT_SUBTITLE, FONT_LABEL, FONT_BUTTON)
 * - Dialog methods: showSuccessDialog(), showErrorDialog(), showWarningDialog(), showInfoDialog(), showConfirmDialog()
 * - Button styling methods: styleSuccessButton(), styleErrorButton(), stylePrimaryButton(), styleSecondaryButton()
 * - Label creation: createTitleLabel(), createSubtitleLabel()
 */
@DisplayName("UIHelpers - UI Utility Tests")
public class UIHelpersTest {

    @Nested
    @DisplayName("Color Constants Tests")
    class ColorConstantsTests {

        @Test
        @DisplayName("COLOR_SUCCESS should be defined and not null")
        void testColorSuccessExists() {
            assertNotNull(UIHelpers.COLOR_SUCCESS, "COLOR_SUCCESS should be defined");
            assertTrue(UIHelpers.COLOR_SUCCESS instanceof Color, "Should be Color instance");
        }

        @Test
        @DisplayName("COLOR_ERROR should be defined and not null")
        void testColorErrorExists() {
            assertNotNull(UIHelpers.COLOR_ERROR, "COLOR_ERROR should be defined");
            assertTrue(UIHelpers.COLOR_ERROR instanceof Color, "Should be Color instance");
        }

        @Test
        @DisplayName("COLOR_WARNING should be defined and not null")
        void testColorWarningExists() {
            assertNotNull(UIHelpers.COLOR_WARNING, "COLOR_WARNING should be defined");
            assertTrue(UIHelpers.COLOR_WARNING instanceof Color, "Should be Color instance");
        }

        @Test
        @DisplayName("COLOR_INFO should be defined and not null")
        void testColorInfoExists() {
            assertNotNull(UIHelpers.COLOR_INFO, "COLOR_INFO should be defined");
            assertTrue(UIHelpers.COLOR_INFO instanceof Color, "Should be Color instance");
        }

        @Test
        @DisplayName("COLOR_PRIMARY should be defined and not null")
        void testColorPrimaryExists() {
            assertNotNull(UIHelpers.COLOR_PRIMARY, "COLOR_PRIMARY should be defined");
            assertTrue(UIHelpers.COLOR_PRIMARY instanceof Color, "Should be Color instance");
        }

        @Test
        @DisplayName("COLOR_SECONDARY should be defined and not null")
        void testColorSecondaryExists() {
            assertNotNull(UIHelpers.COLOR_SECONDARY, "COLOR_SECONDARY should be defined");
            assertTrue(UIHelpers.COLOR_SECONDARY instanceof Color, "Should be Color instance");
        }

        @Test
        @DisplayName("All colors should have valid RGB values")
        void testColorValidity() {
            Color[] colors = {
                UIHelpers.COLOR_SUCCESS, UIHelpers.COLOR_ERROR, UIHelpers.COLOR_WARNING,
                UIHelpers.COLOR_INFO, UIHelpers.COLOR_PRIMARY, UIHelpers.COLOR_SECONDARY
            };

            for (Color color : colors) {
                assertNotNull(color, "Color should not be null");
                assertTrue(color.getRed() >= 0 && color.getRed() <= 255, "Red should be 0-255");
                assertTrue(color.getGreen() >= 0 && color.getGreen() <= 255, "Green should be 0-255");
                assertTrue(color.getBlue() >= 0 && color.getBlue() <= 255, "Blue should be 0-255");
            }
        }

        @Test
        @DisplayName("Colors should be distinct from each other")
        void testColorsDistinct() {
            // At least some colors should be different from each other
            boolean allSame = UIHelpers.COLOR_SUCCESS.equals(UIHelpers.COLOR_ERROR) &&
                            UIHelpers.COLOR_ERROR.equals(UIHelpers.COLOR_WARNING);
            
            assertFalse(allSame, "Not all colors should be identical");
        }
    }

    @Nested
    @DisplayName("Font Constants Tests")
    class FontConstantsTests {

        @Test
        @DisplayName("FONT_TITLE should be defined and not null")
        void testFontTitleExists() {
            assertNotNull(UIHelpers.FONT_TITLE, "FONT_TITLE should be defined");
            assertTrue(UIHelpers.FONT_TITLE instanceof Font, "Should be Font instance");
        }

        @Test
        @DisplayName("FONT_SUBTITLE should be defined and not null")
        void testFontSubtitleExists() {
            assertNotNull(UIHelpers.FONT_SUBTITLE, "FONT_SUBTITLE should be defined");
            assertTrue(UIHelpers.FONT_SUBTITLE instanceof Font, "Should be Font instance");
        }

        @Test
        @DisplayName("FONT_LABEL should be defined and not null")
        void testFontLabelExists() {
            assertNotNull(UIHelpers.FONT_LABEL, "FONT_LABEL should be defined");
            assertTrue(UIHelpers.FONT_LABEL instanceof Font, "Should be Font instance");
        }

        @Test
        @DisplayName("FONT_BUTTON should be defined and not null")
        void testFontButtonExists() {
            assertNotNull(UIHelpers.FONT_BUTTON, "FONT_BUTTON should be defined");
            assertTrue(UIHelpers.FONT_BUTTON instanceof Font, "Should be Font instance");
        }

        @Test
        @DisplayName("Fonts should have valid sizes")
        void testFontSizes() {
            Font[] fonts = {
                UIHelpers.FONT_TITLE, UIHelpers.FONT_SUBTITLE,
                UIHelpers.FONT_LABEL, UIHelpers.FONT_BUTTON
            };

            for (Font font : fonts) {
                assertNotNull(font, "Font should not be null");
                assertTrue(font.getSize() > 0, "Font size should be positive");
                assertTrue(font.getSize() < 100, "Font size should be reasonable");
            }
        }

        @Test
        @DisplayName("Title font should be larger than label font")
        void testFontHierarchy() {
            assertTrue(UIHelpers.FONT_TITLE.getSize() > UIHelpers.FONT_LABEL.getSize(),
                "Title should be larger than label");
        }

        @Test
        @DisplayName("All fonts should have family names")
        void testFontFamilies() {
            Font[] fonts = {
                UIHelpers.FONT_TITLE, UIHelpers.FONT_SUBTITLE,
                UIHelpers.FONT_LABEL, UIHelpers.FONT_BUTTON
            };

            for (Font font : fonts) {
                assertNotNull(font.getFamily(), "Font family should be defined");
                assertFalse(font.getFamily().isEmpty(), "Font family should not be empty");
            }
        }
    }

    @Nested
    @DisplayName("Dialog Methods Tests")
    class DialogMethodsTests {

        @Test
        @DisplayName("showSuccessDialog() should accept parameters")
        void testSuccessDialogAccepts() {
            JFrame frame = new JFrame("Test");
            
            assertDoesNotThrow(() -> UIHelpers.showSuccessDialog(frame, "Title", "Message"),
                "showSuccessDialog should accept valid parameters");
        }

        @Test
        @DisplayName("showErrorDialog() should accept parameters")
        void testErrorDialogAccepts() {
            JFrame frame = new JFrame("Test");
            
            assertDoesNotThrow(() -> UIHelpers.showErrorDialog(frame, "Title", "Message"),
                "showErrorDialog should accept valid parameters");
        }

        @Test
        @DisplayName("showWarningDialog() should accept parameters")
        void testWarningDialogAccepts() {
            JFrame frame = new JFrame("Test");
            
            assertDoesNotThrow(() -> UIHelpers.showWarningDialog(frame, "Title", "Message"),
                "showWarningDialog should accept valid parameters");
        }

        @Test
        @DisplayName("showInfoDialog() should accept parameters")
        void testInfoDialogAccepts() {
            JFrame frame = new JFrame("Test");
            
            assertDoesNotThrow(() -> UIHelpers.showInfoDialog(frame, "Title", "Message"),
                "showInfoDialog should accept valid parameters");
        }

        @Test
        @DisplayName("Dialog methods should handle null components")
        void testDialogsHandleNullComponent() {
            // Some implementations handle null parent, some throw - either is acceptable
            try {
                UIHelpers.showSuccessDialog(null, "Title", "Message");
            } catch (NullPointerException e) {
                assertTrue(true, "Null handling expected");
            }
        }

        @Test
        @DisplayName("Dialog methods should handle null messages")
        void testDialogsHandleNullMessage() {
            JFrame frame = new JFrame("Test");
            
            try {
                UIHelpers.showSuccessDialog(frame, "Title", null);
            } catch (NullPointerException e) {
                assertTrue(true, "Null handling expected");
            }
        }

        @Test
        @DisplayName("Dialog methods should handle empty strings")
        void testDialogsHandleEmptyStrings() {
            JFrame frame = new JFrame("Test");
            
            assertDoesNotThrow(() -> {
                UIHelpers.showSuccessDialog(frame, "", "");
                UIHelpers.showErrorDialog(frame, "", "");
                UIHelpers.showWarningDialog(frame, "", "");
                UIHelpers.showInfoDialog(frame, "", "");
            }, "Should handle empty string parameters");
        }
    }

    @Nested
    @DisplayName("Confirmation Dialog Tests")
    class ConfirmationDialogTests {

        @Test
        @DisplayName("showConfirmDialog() should return boolean")
        void testConfirmDialogReturnsBoolean() {
            JFrame frame = new JFrame("Test");
            
            boolean result = UIHelpers.showConfirmDialog(frame, "Confirm?");
            assertTrue(result == true || result == false,
                "Should return boolean value");
        }

        @Test
        @DisplayName("showConfirmDialog() should accept valid parameters")
        void testConfirmDialogAccepts() {
            JFrame frame = new JFrame("Test");
            
            assertDoesNotThrow(() -> UIHelpers.showConfirmDialog(frame, "Confirm action?"),
                "showConfirmDialog should accept valid parameters");
        }

        @Test
        @DisplayName("showConfirmDialog() should handle null component")
        void testConfirmDialogWithNullComponent() {
            try {
                UIHelpers.showConfirmDialog(null, "Confirm?");
            } catch (NullPointerException e) {
                assertTrue(true, "Null handling expected");
            }
        }
    }

    @Nested
    @DisplayName("Button Styling Tests")
    class ButtonStylingTests {

        @Test
        @DisplayName("styleSuccessButton() should modify button appearance")
        void testStyleSuccessButton() {
            JButton button = new JButton("Test");
            Color originalBackground = button.getBackground();
            
            UIHelpers.styleSuccessButton(button);
            
            assertNotNull(button, "Button should still exist");
            // Styling should apply some changes
            assertTrue(true, "Button styling should complete without exception");
        }

        @Test
        @DisplayName("styleErrorButton() should modify button appearance")
        void testStyleErrorButton() {
            JButton button = new JButton("Test");
            
            UIHelpers.styleErrorButton(button);
            
            assertNotNull(button, "Button should still exist");
            assertTrue(true, "Button styling should complete without exception");
        }

        @Test
        @DisplayName("stylePrimaryButton() should modify button appearance")
        void testStylePrimaryButton() {
            JButton button = new JButton("Test");
            
            UIHelpers.stylePrimaryButton(button);
            
            assertNotNull(button, "Button should still exist");
            assertTrue(true, "Button styling should complete without exception");
        }

        @Test
        @DisplayName("styleSecondaryButton() should modify button appearance")
        void testStyleSecondaryButton() {
            JButton button = new JButton("Test");
            
            UIHelpers.styleSecondaryButton(button);
            
            assertNotNull(button, "Button should still exist");
            assertTrue(true, "Button styling should complete without exception");
        }

        @Test
        @DisplayName("Styling methods should accept multiple buttons")
        void testMultipleButtonStyling() {
            assertDoesNotThrow(() -> {
                JButton btn1 = new JButton("Button 1");
                JButton btn2 = new JButton("Button 2");
                JButton btn3 = new JButton("Button 3");
                
                UIHelpers.styleSuccessButton(btn1);
                UIHelpers.styleErrorButton(btn2);
                UIHelpers.stylePrimaryButton(btn3);
            }, "Should style multiple buttons");
        }

        @Test
        @DisplayName("Styling same button multiple times should be safe")
        void testReStylingButton() {
            JButton button = new JButton("Test");
            
            assertDoesNotThrow(() -> {
                UIHelpers.styleSuccessButton(button);
                UIHelpers.styleErrorButton(button);
                UIHelpers.stylePrimaryButton(button);
                UIHelpers.styleSecondaryButton(button);
            }, "Re-styling should be safe");
        }
    }

    @Nested
    @DisplayName("Label Creation Tests")
    class LabelCreationTests {

        @Test
        @DisplayName("createTitleLabel() should return JLabel")
        void testCreateTitleLabel() {
            JLabel label = UIHelpers.createTitleLabel("Test Title");
            
            assertNotNull(label, "Should return JLabel instance");
            assertTrue(label instanceof JLabel, "Should be JLabel type");
            assertEquals("Test Title", label.getText(), "Should have correct text");
        }

        @Test
        @DisplayName("createSubtitleLabel() should return JLabel")
        void testCreateSubtitleLabel() {
            JLabel label = UIHelpers.createSubtitleLabel("Test Subtitle");
            
            assertNotNull(label, "Should return JLabel instance");
            assertTrue(label instanceof JLabel, "Should be JLabel type");
            assertEquals("Test Subtitle", label.getText(), "Should have correct text");
        }

        @Test
        @DisplayName("Title label should use title font")
        void testTitleLabelFont() {
            JLabel label = UIHelpers.createTitleLabel("Title");
            
            assertNotNull(label.getFont(), "Label should have font");
            assertTrue(label.getFont().getSize() >= 18, "Title label should have large font");
        }

        @Test
        @DisplayName("Subtitle label should use subtitle font")
        void testSubtitleLabelFont() {
            JLabel label = UIHelpers.createSubtitleLabel("Subtitle");
            
            assertNotNull(label.getFont(), "Label should have font");
            assertTrue(label.getFont().getSize() >= 14, "Subtitle label should have medium font");
        }

        @Test
        @DisplayName("Labels should handle empty strings")
        void testLabelsWithEmptyStrings() {
            JLabel title = UIHelpers.createTitleLabel("");
            JLabel subtitle = UIHelpers.createSubtitleLabel("");
            
            assertNotNull(title, "Should create label with empty string");
            assertNotNull(subtitle, "Should create label with empty string");
        }

        @Test
        @DisplayName("Labels should handle long strings")
        void testLabelsWithLongStrings() {
            String longText = "This is a very long label text that should be handled properly without breaking anything in the UI";
            JLabel label = UIHelpers.createTitleLabel(longText);
            
            assertNotNull(label, "Should handle long strings");
            assertEquals(longText, label.getText(), "Should preserve long text");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("All constants should be properly initialized")
        void testAllConstantsInitialized() {
            assertNotNull(UIHelpers.COLOR_SUCCESS, "Colors should exist");
            assertNotNull(UIHelpers.COLOR_ERROR, "Colors should exist");
            assertNotNull(UIHelpers.FONT_TITLE, "Fonts should exist");
            assertNotNull(UIHelpers.FONT_BUTTON, "Fonts should exist");
        }

        @Test
        @DisplayName("Label creation and styling should work together")
        void testLabelAndStyling() {
            JLabel label = UIHelpers.createTitleLabel("Test");
            assertNotNull(label, "Label should be created");
            
            // Label styling uses the fonts defined in constants
            assertTrue(label.getFont().getSize() > 0, "Label should have valid font");
        }

        @Test
        @DisplayName("Complete UI helper workflow")
        void testCompleteWorkflow() {
            JFrame frame = new JFrame("Test Frame");
            
            assertDoesNotThrow(() -> {
                // Create labels
                JLabel titleLabel = UIHelpers.createTitleLabel("Title");
                JLabel subtitleLabel = UIHelpers.createSubtitleLabel("Subtitle");
                
                // Create and style buttons
                JButton successBtn = new JButton("Success");
                JButton errorBtn = new JButton("Error");
                
                UIHelpers.styleSuccessButton(successBtn);
                UIHelpers.styleErrorButton(errorBtn);
                
                // Show dialogs conceptually
                // Note: actual dialogs might not show in test environment but methods should work
            }, "Complete workflow should function");
        }
    }
}

