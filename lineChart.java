import java.awt.Color;
import java.util.HashMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A simple demonstration application showing how to create a line chart using data from an
 * {@link XYDataset}.
 *
 */
public class lineChart extends ApplicationFrame {
	
	HashMap<String,XYSeries> map = new HashMap<>();
	/**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
    public lineChart(final String title) {

        super(title);

        final XYDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }
    
    /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    
    public void addData(double[] data,String title){
    	XYSeries series = map.get(title);
    	if(series==null){
    		series = new XYSeries(title);
    		map.put(title, series);
    	}
    	series.add(data[0],data[1]);
    }
    
    public void redraw(){
//    	this.removeAll();
    	final XYDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
        this.pack();
    }
    
    private XYDataset createDataset() {

        final XYSeriesCollection dataset = new XYSeriesCollection();
        for(String key : map.keySet()){
        	XYSeries s = map.get(key);
        	dataset.addSeries(s);
        }
        return dataset;
    }
    
    /**
     * Creates a chart.
     * 
     * @param dataset  the data for the chart.
     * 
     * @return a chart.
     */
    private static JFreeChart createChart(final XYDataset dataset) {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createXYLineChart(
            "Line Chart Demo 6",      // chart title
            "X",                      // x axis label
            "Y",                      // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            true,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);

//        final StandardLegend legend = (StandardLegend) chart.getLegend();
  //      legend.setDisplaySeriesShapes(true);
        
        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
    //    plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, false);
        renderer.setSeriesShapesVisible(1, false);
        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        // OPTIONAL CUSTOMISATION COMPLETED.
                
        return chart;
        
    }

    // ****************************************************************************
    // * JFREECHART DEVELOPER GUIDE                                               *
    // * The JFreeChart Developer Guide, written by David Gilbert, is available   *
    // * to purchase from Object Refinery Limited:                                *
    // *                                                                          *
    // * http://www.object-refinery.com/jfreechart/guide.html                     *
    // *                                                                          *
    // * Sales are used to provide funding for the JFreeChart project - please    * 
    // * support us so that we can continue developing free software.             *
    // ****************************************************************************
    
    
    
    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(final String[] args) {

//        final lineChart demo = new lineChart("Line Chart Demo 6");
//        demo.setVisible(true);
//        demo.addData(new double[]{1,1}, "f");
//        demo.addData(new double[]{2,1}, "f");
//        demo.addData(new double[]{3,1}, "f");
//        demo.addData(new double[]{4,1}, "f");
//        demo.addData(new double[]{5,1}, "f");
//        
//        demo.redraw();
//        demo.addData(new double[]{5,1}, "s");
//        demo.addData(new double[]{5,2}, "s");
//        demo.addData(new double[]{5,3}, "s");
//        demo.addData(new double[]{5,4}, "s");
//        demo.redraw();
    }

}
