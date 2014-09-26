cellsociety
===========

=======
Chris Bernt, Marcus Cain, Scotty Shaw

Start Date: September 8

Finish Date: September 26

Estimated Work Hours: 75

Responsibilities
=======

Chris: GUI, Graphics, Refactoring, Inspirational Music

Marcus: XML Reader, XML Files, Gridpane, Food Supply

Scotty: Cells, Edges, Grids, Refactoring, Bad Jokes

Resources
=======
Dr. Duvall, Yu Zhou Lee, Carlos Reyes, Jordan Ly, Cody Lieu

Start File: Main.java (cellsociety_team19 package)

Resource Files: XML Files (xmlFiles package)

Important information
=======
Click on the "Open XML File" button, select an XML file, and click "Submit" to run the selected simulation. The various buttons do as described (pause, resume, step, new, reset, quit), and a slider allows the user to change speeds. The user may also click on specific tiles to change the state of a cell and, by extension, the simulation's outcome.

Problems
=======
PredPreyCell does not work as is. This is because we feel it is better to create a program that does not duplicate code through the use of hierarchy, superclasses, and interfaces. However, we did leave in the commented methods in PredPreyCell.java as proof that our implementation and logic were functional. The discrepancy rests in our limited refactoring abilities, which prevented us from fully implementing a sophisticated version of Cell Society. In addition, clicking to change a cell's state results in the program not processing that cell's decision-making for a turn. This is because by the time the user clicks on the cell, the cells would already have calculated their neighbors, and we do not currently recalculate them.

Despite these shortcomings, we accidentally used reflection while refactoring our Cell hierarchy and building our CellFactory. The Edge interface hierarchy is also quite effective due to its simplicity and able to handle new edge strategies. And our Rules interface hierarchy is also able to handle new varieties of procedures for evaluating and acting on the cell's environment. Our program's design is, without a doubt, significantly improved from last week and clearly display how much we learned and improved in just a few days. Given our current skill levels, we feel we learned a lot and put in a lot of impressive work, even if our submitted program is not fully functional.

As our Rules hierarchy and PredPreyCell files show, we were not afraid to refactor our code however we felt was possible. We aimed to have the best design possible, which meant reducing and even eliminating duplicated code and getters and setters as much as possible. Unfortunately, the need to maintain the protected status of many methods and variables resulted in the need for them. Because of our Cell hierarchy and CellFactory, we are very well-equipped to add and create new cells for more simulations, and our Rules and Edges hierarchy will allow easy addition of new behaviors based on the neighbors and edge cases. The Grid hierachy is immature, however, because we ran out of time, but we feel that it is the basis for a more complete implementation that would also easily allow new shapes. Our SimulationLoop and XMLReader are also well-designed to allow new GUI features and various types of XML files for new simulations. We did not have time to refactor those files as much, but the logic is present and functional. 

Impressions
=======
Peer programming was extremely useful during the refactoring process. It helped us learn from each other and think for each other. We enjoyed this project immensely, and feel that we learned a lot. It was a pleasant surprise to realize that we had accidentally implemented toroidal edges ahead of time because of the rule set we found for WaTor World. Ideally, we feel that such a simple implementation should be included in the first week, and it could further highlight the importance of design in the first week when the teams realize that finite edges and toroidal edges could be subinterfaces in a hierarchy.

Design Document
=======
Our Design Document for Cell Society has been added as a PDF named "CellSociety_Team19.pdf" to the src folder. It has not been altered since its original submission, so any current information at the time of submitting the entire Cell Society project will be found above in this README.
