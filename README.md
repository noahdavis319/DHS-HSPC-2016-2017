# DHS HSPC 2016-2017
All programs and notes on the **DHS HSPC 2016-2017** programming contest can be found here.

All programs are written in Java to provide compatibility and portability, as well as to make it easier for modifications by others in the future.

**HSPC_GradingProgram** is a small, GUI based, application that scans a user defined submissions director and then grades submissions by inputting and comparing data.
The application then proceeds to update the MySQL database backend on whether or not the user's submission was successful or not.

**HSPC_Scoreboard** is a Tomcat based website that uses a single MySQL query statement to calculate the positions of each team for the leaderboard, as well as allow the teams to view a PDF version of the problems, and the Java API.
Tomcat is a webserver that uses the Java programming language and Java ServerPages to execute server-side code.  Tomcat was chosen for this project as it uses the Java programming language like all of the other applications.

**HSPC_SubmissionsProgram** is a GUI based application where teams can submit solutions to problems, as well as ask for clarifications for problems.

## Development
These projects were created using the **IntelliJ IDEA** IDE for Java due to the available features.
Below is a list of what each folder contains.
* **out** - Compiled class files and artificats (Jars) can be found here.
* **src** - Source code as .java files can be found here.
* **Other files** - Generally all other files are a part of the application, such as a **config.ini** which contains configuration parameters, or a **.iml** file for including libraries.
