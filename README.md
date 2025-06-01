This is a Kotlin Multiplatform project targeting Desktop.

It can be run by giving the command "gradlew run" from the directory where it's sources are located.

Afterwards interaction is limited to the 32 by 32 grid of buttons and the four below them,
(start, stop, reset, randomize) and of course the window controls of your given os :)

### Specification

The program implements Conway's classic Game of life in the form of a desktop application 
and follows the rules listed below:The game consists of cells on a grid, each live or dead. 
Cells interact with their eight neighbours on the grid. At each step in time, the following 
transitions occur:

```
Any live cell with fewer than two live neighbours dies, as if by underpopulation.
Any live cell with two or three live neighbours lives on to the next generation.
Any live cell with more than three live neighbours dies, as if by overpopulation.
Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
```

These apply simultaneously to every cell, and create the next state of the grid.
The cells themselves are arranged in a 32 by 32 grid. Cells on the edge of the grid
consider the cells on the "other side" of the grid as their neighbours. For example a
cell along the left edge, marked (C8,1), has neighbours: (C7,1), (C7,2), (C8,2),
(C9,1), (C9,2), (C7,32), (C8,32) and (C9,32). The user may use the mouse to interact
with the grid and select cells to bring alive or let die, or click buttons that
start/stop/reset the simulation and randomize it's state