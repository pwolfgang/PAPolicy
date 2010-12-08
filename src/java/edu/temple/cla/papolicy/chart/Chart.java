/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy.chart;

import edu.temple.cla.papolicy.Units;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Stroke;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.PieDataset;

/**
 *
 * @author Paul Wolfgang
 */
public class Chart extends HttpServlet {

    static final long serialVersionUID = -7696314789843526771L;

    private static final Logger logger = Logger.getLogger(Chart.class);

    private static CategoryPlot createLinePlot(List<MyDataset> datasets) {
        CategoryPlot plot = new CategoryPlot();
        int index = 0;
        for (MyDataset dataset : datasets) {
            ValueAxis rangeAxis = null;
            if (dataset.getUnits() == Units.RANK) {
                String[] axisLables = createAxisLables(dataset.getMaxValue().doubleValue());
                rangeAxis = new SymbolAxis(dataset.getAxisTitle(), axisLables);
            } else {
                rangeAxis = new NumberAxis(dataset.getAxisTitle());
            }
            plot.setDataset(index, dataset);
            plot.setRangeAxis(index, rangeAxis);
            if (index % 2 == 0) {
                plot.setRangeAxisLocation(index, AxisLocation.TOP_OR_LEFT);
            } else {
                plot.setRangeAxisLocation(index, AxisLocation.BOTTOM_OR_RIGHT);
            }
            plot.mapDatasetToRangeAxis(index, index);
            LineAndShapeRenderer renderer = new LineAndShapeRenderer();
            Stroke newStroke = new BasicStroke(3);
            renderer.setBaseStroke(newStroke);
            renderer.setAutoPopulateSeriesStroke(false);
            plot.setRenderer(index, renderer);
            ++index;
        }
        CategoryAxis domainAxis = new CategoryAxis("");
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
        plot.setDomainAxis(domainAxis);
        return plot;
    }


    private static CategoryPlot createBarPlot(MyDataset dataset) {
        CategoryPlot plot = new CategoryPlot();
        ValueAxis rangeAxis = null;
        if (dataset.getUnits() == Units.RANK) {
            String[] axisLables = createAxisLables(dataset.getMaxValue().doubleValue());
            rangeAxis = new SymbolAxis(dataset.getAxisTitle(), axisLables);
        } else {
            rangeAxis = new NumberAxis(dataset.getAxisTitle());
        }
        plot.setDataset(0, dataset);
        plot.setRangeAxis(0, rangeAxis);
        plot.setRangeAxisLocation(0, AxisLocation.TOP_OR_LEFT);
        plot.mapDatasetToRangeAxis(0, 0);
        CategoryItemRenderer renderer = new BarRenderer();
        plot.setRenderer(0, renderer);
        CategoryAxis domainAxis = new CategoryAxis("");
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
        plot.setDomainAxis(domainAxis);
        return plot;
    }

    private static PiePlot createPiePlot(PieDataset dataset) {
        PiePlot plot = new PiePlot(dataset);
        return plot;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String fileName = request.getParameter("dataset");
        try {
            FileInputStream in = new FileInputStream(fileName);
            OutputStream out = response.getOutputStream();
            response.setContentType("image/png");
            int c;
            while ((c = in.read()) != -1) out.write(c);
        } catch(IOException ioex) {
            logger.error(ioex);
        }
    }


    /**
     * Method to create the chart and write it as a .png file to a temporary
     * location.
     * @param datasets The arraylist of datasets to be graphed
     * @return The name of the file containing the chart image
     */
    public static String createChart(List<MyDataset> datasets) {
        try {
            File outputFile = File.createTempFile("chart", ".png");
            FileOutputStream out = new FileOutputStream(outputFile);
            Plot plot = null;
            if (datasets.size() == 1) {
                MyDataset dataset0 = datasets.get(0);
                if (dataset0.getColumnCount() == 1) {
                    PieDataset pds = DatasetUtilities.createPieDatasetForColumn(dataset0, 0);
                    plot = createPiePlot(pds);
                } else if (dataset0.getRowCount() == 1) {
                    plot = createBarPlot(dataset0);
                } else {
                    plot = createLinePlot(datasets);
                }
            } else {
                plot = createLinePlot(datasets);
            }
            JFreeChart chart = new JFreeChart("",
                    new Font("SanSerif", Font.BOLD, 12),
                    plot,
                    true);
            ChartUtilities.writeChartAsPNG(out, chart, 800, 600);
            out.close();
            return outputFile.getAbsolutePath();
        } catch (IOException ioex) {
            logger.error(ioex);
            return null;
        }
    }

    private static String[] createAxisLables(double maxValue) {
        int maxValueInt = (int)Math.floor(maxValue);
        String[] result = new String[maxValueInt];
        for (int i = 0; i < maxValueInt; i++) {
            int val = maxValueInt - i;
            if (val > 3) {
                result[i] = Integer.toString(val) + "th";
            } else {
                switch (val) {
                    case 1: result[i] = "1st"; break;
                    case 2: result[i] = "2nd"; break;
                    case 3: result[i] = "3rd"; break;
                }
            }
        }
        return result;
    }

}
