/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.chart;

import edu.temple.cla.papolicy.Column;
import edu.temple.cla.papolicy.Units;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;

/**
 * An implementation of the JFreeChart CategoryDataset interface. A MyDataset
 * object is created for each group data that is in common units. Note that
 * what is a column in the results table, is considered a row by JFreeChart.
 * @author Paul Wolfgang
 */
public class MyDataset implements CategoryDataset, Serializable{
    
    static final long serialVersionUID = 6150912507653373738L;

    private final List<Column> columns;
    private final List<String> columnLabels = new ArrayList<>();
    private final List<String> rowLabels = new ArrayList<>();
    
    private final SortedMap<String, Integer> columnKeyMap = new TreeMap<>();
    private final SortedMap<String, Integer> rowKeyMap = new TreeMap<>();
    private Number minValue = null;
    private Number maxValue = null;

    
    /**
     * Construct a MyDataset from a list of result Columns.
     * @param columns The Column objects to be included in this MyDataset
     */
    public MyDataset(List<Column> columns) {
        this.columns = columns;
        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            String columnLabel = column.toString();
            rowLabels.add(columnLabel);
            // Our columns are Dataset rows
            rowKeyMap.put(columnLabel, i);
            Number colMinValue = column.getMinValue();
            Number colMaxValue = column.getMaxValue();
            if (colMinValue != null) {
                if (minValue == null) {
                    minValue = colMinValue;
                } else {
                    if (minValue.doubleValue() > colMinValue.doubleValue()) {
                        minValue = colMinValue;
                    }
                }
            }
            if (colMaxValue != null) {
                if (maxValue == null) {
                    maxValue = colMaxValue;
                } else {
                    if (maxValue.doubleValue() < colMaxValue.doubleValue()) {
                        maxValue = colMaxValue;
                    }
                }
            }
        }
        Set<String> columnKeys = columns.get(0).getRowKeys();
        int index = 0;
        for (String key : columnKeys) {
            // Our columns are their rows.
            columnLabels.add(key);
            columnKeyMap.put(key, index++);
        }
   }

    /**
     * No-op implementation of this method.
     * @param listener Ignored parameter
     */
    @Override
    public void addChangeListener(DatasetChangeListener listener) {
        // do nothing for now
    }

    /**
     * No-op implementation of this method.
     * @param listener Ignored parameter
     */
    @Override
    public void removeChangeListener(DatasetChangeListener listener) {
        // do nothing for now
    }

    /**
     * No-op implemention of this method
     * @return null
     */
    @Override
    public DatasetGroup getGroup() {
        return null;  // DatasetGroup currently not used
    }

    /**
     * No-op implementation of this method.
     * @param group Ignored parameter
     */
    @Override
    public void setGroup(DatasetGroup group) {
        // do nothing
    }

    /**
     * Determine the number of rows (our columns)
     * @return The number of rows
     */
    @Override
    public int getRowCount() {
        return rowLabels.size();
    }

    /**
     * Determine the number of columns (our rows)
     * @return The number of columns
     */
    @Override
    public int getColumnCount() {
        return columnLabels.size();
    }

    /**
     * Get the value st the specified row and column
     * @param row The specified row
     * @param column The specified column
     * @return The value at the specified row and column
     */
    @Override
    public Number getValue(int row, int column) {
        return getValue(rowLabels.get(row), columnLabels.get(column));
    }

    /**
     * Determine the index of the specified column
     * @param key The key that represents this column
     * @return The index of this column
     */
    @Override
    public int getColumnIndex(Comparable key) {
        return columnKeyMap.get((String)key);
    }

    /**
     * Determine the key of the row at a given index
     * @param row The row index
     * @return The corresponding key
     */
    @Override
    public Comparable getRowKey(int row) {
        return rowLabels.get(row);
    }

    /**
     * Determine the index of the specified row
     * @param key The key that represents this row
     * @return The index of this row
     */
    @Override
    public int getRowIndex(Comparable key) {
        return rowKeyMap.get((String)key);
    }

    /**
     * Determine the key of the column at a given index
     * @param column The column index
     * @return The corresponding key
     */
    @Override
    public Comparable getColumnKey(int column) {
        return columnLabels.get(column);
    }

    /**
     * Return a list of the row keys.
     * @return A list of the row keys.
     */
    @Override
    public List getRowKeys() {
        return new ArrayList<>(rowKeyMap.keySet());
    }

    /**
     * Return a list of the column keys.
     * @return A list of the column keys.
     */
    @Override
    public List getColumnKeys() {
        return new ArrayList<>(columnKeyMap.keySet());
    }

    /**
     * Get the value at a given row and column as specified by their keys.
     * If the units are RANK then the value is "reversed" so that 1st plots
     * higher than 2nd, etc.
     * @param rowKey The row key
     * @param columnKey The column key
     * @return The value at the given row and column
     */
    @Override
    public Number getValue(Comparable rowKey, Comparable columnKey) {
        int rowIndex = getRowIndex(rowKey);
        // Remember Dataset rows are our columns
        Number returnValue = columns.get(rowIndex).getDisplayedValue((String)columnKey);
        if (getUnits() == Units.RANK) {
            if (returnValue != null && returnValue.doubleValue() != 0) {
                returnValue = new Double(Math.floor(maxValue.doubleValue()) - returnValue.doubleValue());
            }
        }
        return returnValue;
    }

    /**
     * Return the units of the data in this dataset.
     * @return The units of the data in this data set.
     */
    public Units getUnits() {
        return columns.get(0).getUnits();
    }

    /**
     * Return the x-axis title for this dataset
     * @return The x-axis title for this data set.
     */
    public String getAxisTitle() {
        return columns.get(0).getAxisTitle();
    }

    /**
     * Return the minimum value of all the data in this dataset.
     * @return The minimum value of all the data in this dataset.
     */
    public Number getMinValue() {return minValue;}

    /**
     * Return the maximum value of all the data in this dataset.
     * @return The maximum value of all the data in this dataser.
     */
    public Number getMaxValue() {return maxValue;}
}
