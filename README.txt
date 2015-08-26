//Michael Limiero 

STARS is a simple game that I wrote for the TI-83. For the final project, I am rewriting it in Java with some improvements and an object-oriented design. The game is simple:
- You control a character - on TI-83 a theta symbol, but in Java the "@" symbol.
- You can move around on the field, but you can't stop and you bounce off the walls.
- You try to get stars
- If you hit mines (,) you die.
- If you hit springs (^) you bounce off and your controls are reversed.
- Every time you get a star, a new obstacle (spring or trap) appears.
- Get as many points as you can.
Gameplay isn't really like anything else, but it was originally inspired by Snake and has a little bit of a Pacman feel. Just play it and you will understand. 
Be careful, though - I've found it to be more addictive than TI-83 Mario.

The program structure is loosely based on a Model-View-Controller pattern, but not really.
- The Stars class sets up the Console, ConsoleView, KeyController, and Game
- Game is the main class, which keeps track of score and coordinates Controller and View.
   It doesn't keep track of the "actors" on the field - View is responsible for that - but it does put them there.
   It handles high score, help screens, ranks, speeds, and punishing cheaters.
   It has a tick() method which is called every 200 milliseconds on the "sane" speed
- ConsoleView holds most of the other code. It uses a char[] array to keep track of the field. It shows the help screens, but Game keeps track of the page number.
- ConsoleFrame is a JFrame containing a Console.
- Console is basically a JTextArea, but sets the font, etc. to something reasonable. Probably unnecessary.
- KeyController is a KeyListener that calls the methods of Game appropriately.
- StarsTask is a java.util.TimerTask that calls Game.tick() and does nothing else.
- JavaMD5Sum is some code I stole off Google that lets me prevent people from editing the high scores.
   I did this on the TI-83 version but with sinh() instead of MD5.