This directory contains a simple example that demonstrates how to use the FineFit testing
tool. The sample consists of the following files:

Login.java 					a very simple application 
Login.html 					a FineFit specification for Login
FineFitDriver.java  a FineFit driver for the login application. 

To test the application do the following:

1. Compile Login.java and FineFitDriver.java 

javac -cp <path to alloy4.2.jar>;. Login.java 
javac -cp <path to alloy4.2.jar>;<path to finefit.jar>;. FineFitDriver.java

2. Run FineFit passing it first the name of the driver followed by the specification:

java -jar <path to finefit.jar> FineFitDriver Login.html


