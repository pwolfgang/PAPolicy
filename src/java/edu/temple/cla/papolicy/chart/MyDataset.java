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
 *
 * @author Paul Wolfgang
 */
public class MyDataset implements CategoryDataset, Serializable{
    
    static final long serialVersionUID = 6150912507653373738L;

    private ArrayList<Column> columns;
    private ArrayList<String> columnLabels = new ArrayList<String>();
    private ArrayList<String> rowLabels = new ArrayList<String>();
    
    private SortedMap<String, Integer> columnKeyMap = new TreeMap<String, Integer>();
    private SortedMap<String, Integer> rowKeyMap = new TreeMap<String, Integer>();
    private Number minValue = null;
    private Number maxValue = null;

    
    public MyDataset(ArrayList<Column> columns) {
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
            // Our columns are
            columnLabels.add(key);
            columnKeyMap.put(key, index++);
        }
   }

    public void addChangeListener(DatasetChangeListener listener) {
        // do nothing for now
    }

    public void removeChangeListener(DatasetChangeListener listener) {
        // do nothing for now
    }

    public DatasetGroup getGroup() {
        return null;  // DatasetGroup currently not used
    }

    public void setGroup(DatasetGroup group) {
        // do nothing
    }

    public int getRowCount() {
        return rowLabels.size();
    }

    public int getColumnCount() {
        return columnLabels.size();
    }

    public Number getValue(int row, int column) {
        return getValue(rowLabels.get(row), columnLabels.get(column));
    }

    public int getColumnIndex(Comparable key) {
        return columnKeyMap.get((String)key);
    }

    public Comparable getRowKey(int row) {
        return rowLabels.get(row);
    }

    public int getRowIndex(Comparable key) {
        return rowKeyMap.get((String)key);
    }

    public Comparable getColumnKey(int column) {
        return columnLabels.get(column);
    }

    public List getRowKeys() {
        return new ArrayList<String>(rowKeyMap.keySet());
    }

    public List getColumnKeys() {
        return new ArrayList<String>(columnKeyMap.keySet());
    }

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

    public Units getUnits() {
        return columns.get(0).getUnits();
    }

    public String getAxisTitle() {
        return columns.get(0).getAxisTitle();
    }

    public Number getMinValue() {return minValue;}

    public Number getMaxValue() {return maxValue;}
}
