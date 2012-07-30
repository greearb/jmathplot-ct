/* Parse and graph something like this:

DESC Transmitted bits-per-second for endpoint: scr-test-1-A.
Z-axis Transmitted bits-per-second
X-axis packet-size
Y-axis axis is rate.
DATA
                       60          128          256          512         1024         1280         1460         1472         1514
    10000000      7367520     10002227      9999974      9999155     10000793     10000384      9998080      9997824      9999667
   100000000      7315200     15358976     30236672     59785216    100025958    100030464     99978464    100030054    100003939
  1000000000      7336320     15435776     30363648     59772928    115146752    143831040    163998880    165664768    137289520
*/

import javax.swing.*;
import java.util.*;
import java.io.*;
import org.math.plot.*;
 
import static java.lang.Math.*;
 
public class GridPlotData {
   public static void main(String[] args) {
      if (args.length < 1) {
         System.out.println("Must specify data file on command line.\n");
         System.exit(1);
      }

      String fileName = args[0];
      Vector<String> lines = new Vector();
      try {
         Scanner scanner = new Scanner(new FileInputStream(fileName));
         try {
            while (scanner.hasNextLine()){
               lines.addElement(scanner.nextLine());
            }
         }
         finally{
            scanner.close();
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }

      GridData3D gd = new GridData3D(lines);

      Plot3DPanel plot = new Plot3DPanel("SOUTH");
 
      // add grid plot to the PlotPanel
      plot.addGridPlot(gd.z_title, gd.x, gd.y, gd.z);
      plot.getAxis(0).setLegend(gd.x_title);
      plot.getAxis(1).setLegend(gd.y_title);
      plot.getAxis(2).setLegend(gd.z_title);

      // put the PlotPanel in a JFrame like a JPanel
      JFrame frame = new JFrame("a plot panel");
      frame.setSize(600, 600);
      frame.setContentPane(plot);
      frame.setVisible(true);
 
   }
}



/* Parse and graph something like this:

DESC Transmitted bits-per-second for endpoint: scr-test-1-A.
X-axis packet-size
Y-axis axis is rate.
Z-axis Transmitted bits-per-second
START_DATA
                       60          128          256          512         1024         1280         1460         1472         1514
    10000000      7367520     10002227      9999974      9999155     10000793     10000384      9998080      9997824      9999667
   100000000      7315200     15358976     30236672     59785216    100025958    100030464     99978464    100030054    100003939
  1000000000      7336320     15435776     30363648     59772928    115146752    143831040    163998880    165664768    137289520
*/
class GridData3D {
   String desc = "";
   String x_title;
   String y_title;
   String z_title;

   double[] x;
   double[] y;
   double[][] z;

   public GridData3D(Vector<String> lines) {
      Vector<String> yv = new Vector();
      Vector<Vector> zv = new Vector();

      for (int i = 0; i<lines.size(); i++) {
         String ln = lines.elementAt(i);
         if (ln.startsWith("DESC ")) {
            desc += (ln.substring("DESC ".length())) + "\n";
         }
         else if (ln.startsWith("X-axis ")) {
            x_title = ln.substring("X-axis ".length());
         }
         else if (ln.startsWith("Y-axis ")) {
            y_title = ln.substring("Y-axis ".length());
         }
         else if (ln.startsWith("Z-axis ")) {
            z_title = ln.substring("Z-axis ".length());
         }
         else if (ln.startsWith("START_DATA")) {
            // next line has x data
            Vector<String> xv = new Vector();
            StringTokenizer st = new StringTokenizer(lines.elementAt(i+1));
            while (st.hasMoreTokens()) {
               xv.addElement(st.nextToken());
            }
            x = new double[xv.size()];
            for (int q = 0; q<xv.size(); q++) {
               x[q] = Double.parseDouble(xv.elementAt(q));
            }
            i++;
         }
         else {
            // Must be a data line.
            StringTokenizer st = new StringTokenizer(ln);
            yv.addElement(st.nextToken());
            Vector<String> zrow = new Vector();
            zv.addElement(zrow);

            while (st.hasMoreTokens()) {
               zrow.addElement(st.nextToken());
            }
         }
      }

      // Convert vectors to arrays.
      y = new double[yv.size()];
      for (int q = 0; q<yv.size(); q++) {
         y[q] = Double.parseDouble(yv.elementAt(q));
      }

      z = new double[y.length][x.length];
      for (int q = 0; q<y.length; q++) {
         Vector<String> vr = zv.elementAt(q);
         for (int r = 0; r<x.length; r++) {
            z[q][r] = Double.parseDouble(vr.elementAt(r));
         }
      }
   }
}
