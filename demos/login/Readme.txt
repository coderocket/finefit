This directory contains a simple example that demonstrates how to use the FineFit testing
tool. The sample consists of the following files:

Login.java		a very simple application 
Login.html		a FineFit specification for Login
FineFitDriver.java  	a FineFit driver for the login application. 

To test the application do the following:

1. Compile Login.java and FineFitDriver.java 

javac -cp ..\..\lib\alloy4.2.jar;. Login.java 
javac -cp ..\..\lib\alloy4.2.jar;..\..\lib\fit.jar;..\..\build\jar\FineFit.jar;. FineFitDriver.java

2. Run FineFit passing it first the name of the driver followed by the specification:

java -jar ..\..\build\jar\FineFit.jar Login.html FineFitDriver


