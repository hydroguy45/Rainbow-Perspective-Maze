[In Game Controls]
Rotate Left: left arrow
Rotate Right: right arrow
Forwards: up arrow
Backwards: down arrow
Back To Start: escape

[Game Description]
Every wall of the maze is colored along
a spectrum.
	More north ==> more blue
	More west ==> more red
	More east ==> more green
The goal is given a target color, to 
navigate to that wall's location in 
the maze. This game can be played in 
three very different difficulties 
 - Easy: play from a bird's eye view perspective
 - Medium: play from first person with a map
 - Hard: play from first person without a map
 
[Engine Description]
I wrote the 3d graphics engine from scratch. While the 2d mode is quite
intuitive, the 3d modes are a little more complex. Firstly I rotate all walls to the player's
bearing and translate the walls over to the player. Subsequently I remove all walls
that are not in the player's field of vision. I then crop any walls that are seen but not fully.
Now that the set of walls only contains wall in the player's field of vision, I need to
construct a set of walls that are seen (i.e. no wall that are covered by other walls). This
is done by iterating through the current set at pheta intervals and finding that nearest wall for
that pheta interval. Then I add a wall of size pheta interval (~9/50 of a degree), minimum distance,
and color of the closest wall to a new wall set. Finally I have a set of every wall seen. To check this out
press semi-colon in either medium or hard difficulty. After this, I just apply a basic 3d transformation
where height of each wall is proportional to 1/(1+r), so the closest wall will take up the whole screen,
and any wall in the distance will be visible.
	
[Classes]
-Game-
This static class contains the Game while loop. It will attempt to change the from based
on the requestedMode field. This allows for any other class to easily change the screen
and break running code.

-Menu-
This class extends a JPanel and is added to an empty frame by the Game class when requestedMode is Mode.INITIAL. It produce a JPanel with
two buttons: start game and information. Information changes the requestedMode to Mode.INFORMATION and
start game changes to requestedMode to Mode.LEVELCHOICE.

-Information-
This class extends a JPanel and is added to an empty frame by the Game class when requestedMode is Mode.INFORMATION.
It produces a JPanel with a text description of the game and a button, "back to Game", which reverts the requestedMode to Mode.INITIAL.

-LevelSelection-
This class extends a JPanel and is added to an empty frame by the Game class when requestedMode is Mode.LEVELCHOICE. It 
contains Level.levels.length buttons which each change the levelRequested to their respective level number. This
allows for easy level addition later on. After a button is clicked, requestedMode is changed to Mode.DIFFICULTYCHOICE.

-Level-
This class contains static fields and methods for levelGeneration. It contains all the map arrays, goal points, and a method.
Which will take in a level and difficulty and produce a JPanel for that level. As a result, it is crucial to the function of this program.
Most classes reference this class at some point.

-DifficultySelection-
This class extends a JPanel and is added to an empty frame by the Game class when requestedMode is Mode.DIFFICULTYCHOICE. It
contains three buttons: easy, medium, and hard. Which set the difficultyRequested to their respective numbers. After a button
is clicked, requestedMode is changed to Mode.GAME.

-Line-
This is one of the most central classes for the graphics (2d and 3d). When games begin, the level arrays are converted into
Set<Line> to represent all the walls in the game. It also contains most of the methods used in filtering out lines from the set
that aren't visible. Note that pheta1 and pheta2 are only accurate for positive values of y (i.e. they should only be used after filtering out
the Lines that aren't in the field of vision).

-BirdsEyePerspective-
This class extends a JPanel and is instantiated by the Level class. Firstly it converts the level boolean array into a set of lines representing walls.
Next it displays those walls on the screen with the player position and bearing represented by a dot with a line. It takes keyboard input to move the player
around. Finally if the player gets within 0.5 steps (cubic boundary) of the target color which is displayed in the upper right hand corner, it changes requestedMode to
Mode.CONGRATS.

-FirstPersonPerspective-
This class extends a JPanel and is instantiated by the Level class. In addition to changing player bearing, and movement. It filters the set<Line> 
formed by the BirdsEyePerspective class to create a set of Lines within the player's field of vision. After that it skims pheta by pheta to find
the closes line per pheta. This data forms a subsequent set<Line> which contains seenLines (i.e. no lines hidden behind other). This is then
transformed into the rectangles that make up the 3d image. For more information on this process, please see the engine description above. If the
includeMap boolean is true, this class adds a simple block representation of the boolean array to the upper lefthand corner. Additionally, it adds
the target color block to the upper righthand corner. This class also checks to make sure that if the player is within 0.5 steps (cubic boundary) 
of the target color it changes requestedMode to Mode.CONGRATS.

-CongratsScreen-
This class extends a JPanel and is added to an empty frame by the Game class when requestedMode is Mode.CONGRATS.
It produces a JPanel with a congratulatory text and a button which sets the requestedMode to Mode.INITIAL.
				
[Implementation]
-2D arrays-
I will used a 2d boolean array for all of the level maps. This allow level design to be simplified. A true represents a 1x1 cube “wall” while 
a false represents a open area. A boolean array like this is a constructor argument for my BirdsEyeView class (which in turn will be a 
constructor argument for my FirstPersonPerspective). 

-Collections- 
The use of collections greatly simplifies the graphics code. Instead having to recreate an array every time a wall is filtered (for being a duplicate 
or not within sight). I store it all in a set and never have to worry about duplicating and removal is be as simple as a method. Also iteration is much 
easier with foreach loops when it comes time to draw the perspective. 

-Inheritance/Subtyping for Dynamic Dispatch-
Since all of my "screens" extend JPanels, it makes it a lot easier to change from screen to screen (i.e. through modeRequest). This greatly simplfies the code
in my Game class.

-Advance Topic (3D Graphics)-
I wrote a pseudo 3d engine to display a first person POV to the screen. The three difficulty settings implements the 3d code with easy giving the player a 2d bird’s 
eye perspective of the map with a position marker, medium giving the 3d display with the 2d map in the corner without a position marker, and hard giving the player solely the 3d display. 

