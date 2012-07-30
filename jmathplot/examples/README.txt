First, build the main repository (../)
ant [return]

Then, you can compile the examples manually:

javac -classpath ../dist/jmathplot.jar:../lib/jmathio.jar GridPlotsExample.java

# And run it:
java -classpath ../dist/jmathplot.jar:../lib/jmathio.jar:./ GridPlotsExample


# GridPlotData:  Parse some textual data and graph the results
javac -classpath ../dist/jmathplot.jar:../lib/jmathio.jar GridPlotData.java
java -classpath ../dist/jmathplot.jar:../lib/jmathio.jar:./ GridPlotData ct.txt

