# Stuck - An Island RPG Game

*Stuck* is a mini-RPG game I created in my senior year of high school, as a final project:  

 **<p align="center"> You are a mushroom who has crash-landed onto a near-deserted island.  
 With the help of the resident hermit, use the island's resources to find your way back home!** </p>  

<p align="center"> <img src="http://g.recordit.co/radkGD4eJF.gif" width="400"> <img src="http://g.recordit.co/w1DzTyxLst.gif" width="400"> 
<img src="http://g.recordit.co/3ppfMhsS0n.gif" width="400"> <img src="http://g.recordit.co/t4bFPEMftB.gif" width="400">  </p>
                        
*<p align="center"> This project was created in spring 2020.   
  It is coded entirely in Java and uses the Java Graphics library.* </p>

## Compiling & Running
Open the folder in Terminal. Compile by running these commands:
```
javac *.java
java Runner
```
Alternatively, if you would like to create a JAR file for ease of access, a `manifest.txt` has been provided. In Terminal, run the command:
```
jar -cvmf manifest.txt Runner.jar *.class fonts images sounds
```
A `Runner.jar` file will be created. **Double-click** or run through Terminal with: 
```
java -jar Runner.jar
```
