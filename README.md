# LightOutPuzzle
JavaFX implemention of LightOut Puzzle

**JavaFX no longer comes in the same bundle as standard Java.**

When this JavaFx application is launched it asks for dimension N. And then it creates a N*N grid where some squares are light up while some
are not. When a cell is pressed, it lights up or blacks out the cells on the either side of it or above or below (if they exist) depending on 
the initial condition. The goal of this game is to have all the square light out. It is ensured that all the puzzles are solvable and 
randomized. There are two buttons : Randomize and Chase Lights. 

The button **Randomize** randomizes the puzzle and creates a new one which is solvable.

The button **ChaseLights** takes the puzzle to its most basic state from which it can easily be solved. 
