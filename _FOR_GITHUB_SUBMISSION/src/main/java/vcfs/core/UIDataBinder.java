package vcfs.core;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.beans.PropertyChangeListener;
import java.util.List;

public class UIDataBinder {

    private ViewModel model;
    public static void bindLabel(JLabel label, ViewModel model, String propertyName, 
                                 ValueFormatter formatter) {
        model.addPropertyChangeListener(evt -> {
            if (propertyName.equals(evt.getPropertyName())) {
                SwingUtilities.invokeLater(() -> {
                    String text = (formatter != null) 
                        ? formatter.format(evt.getNewValue())
                        : (evt.getNewValue() != null ? evt.getNewValue().toString() : "");
                    label.setText(text);
                });
            }
        });
    }
    
    public static void bindLabel(JLabel label, ViewModel model, String propertyName) {
        bindLabel(label, model, propertyName, null);
    }
    
    public static void bindTextField(JTextField field, ViewModel model, String propertyName) {
        model.addPropertyChangeListener(evt -> {
            if (propertyName.equals(evt.getPropertyName())) {
                SwingUtilities.invokeLater(() -> {
                    String text = (evt.getNewValue() != null) ? evt.getNewValue().toString() : "";
                    field.setText(text);
                });
            }
        });
    }
    
    public static void bindTable(JTable table, ViewModel model, String propertyName, 
                                TableDataProvider dataProvider) {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        
        model.addPropertyChangeListener(evt -> {
            if (propertyName.equals(evt.getPropertyName())) {
                SwingUtilities.invokeLater(() -> {
                    tableModel.setRowCount(0);
                    List<Object[]> rows = dataProvider.getRows();
                    for (Object[] row : rows) {
                        tableModel.addRow(row);
                    }
                });
            }
        });
    }
    
    @FunctionalInterface
    public interface ValueFormatter {
        String format(Object value);
    }
    
    @FunctionalInterface
    public interface TableDataProvider {
        List<Object[]> getRows();
    }

    public void bind(String key, Object value) {
        if (model != null && key != null) {
            model.setProperty(key, value);
        }
    }

    public Object getBoundValue(String key) {
        // TODO Auto-generated method stub
        return model != null ? model.getProperty(key) : null;
    }

    public void unbind(String key) {
        // TODO Auto-generated method stub
        if (model != null && key != null) {
            model.setProperty(key, null);
        }
    }

    public void addListener(PropertyChangeListener listener) {
        // TODO Auto-generated method stub
        if (model != null && listener != null) {
            model.addPropertyChangeListener(listener);
        }
    }

    public void clear() {
        // TODO Auto-generated method stub
        if (model != null) {
            model.clear();
        }
    }
}
