
To test the application do the following:

1. Open the Word version of the specification (spec.docx) and save it as 'Web Page, Filtered'.

2. Compile all the java files:

javac -cp ..\..\build\jar\FineFit.jar;. *.java

3. Run FineFit, passing it the HTML specification file followed by the driver:

java -jar ..\..\build\jar\FineFit.jar spec.htm FineFitDriver

