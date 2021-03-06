import java.util.*;
import java.io.*;
public class Maze{

  private char[][] maze;
  private boolean animate;//false by default

    /*Constructor loads a maze text file, and sets animate to false by default.
      1. The file contains a rectangular ascii maze, made with the following 4 characters:
      '#' - Walls - locations that cannot be moved onto
      ' ' - Empty Space - locations that can be moved onto
      'E' - the location of the goal (exactly 1 per file)
      'S' - the location of the start(exactly 1 per file)
      2. The maze has a border of '#' around the edges. So you don't have to check for out of bounds!
      3. When the file is not found OR the file is invalid (not exactly 1 E and 1 S) then:
         throw a FileNotFoundException or IllegalStateException
    */

  public Maze(String filename) throws FileNotFoundException{
    animate = false;
    int r = 0;
    int c = 0;
    File text = new File(filename);
    Scanner scanner = new Scanner(text);
    while(scanner.hasNextLine()) {
      String line = scanner.nextLine();
      c = line.length();
      r++;
    }
    int index = 0;
    maze = new char[r][c];
    Scanner scan = new Scanner(text);
    int numE = 0;
    int numS = 0;
    while(scan.hasNextLine()) {
      String line = scan.nextLine();
      for (int i = 0; i < c; i++) {
        maze[index][i] = line.charAt(i);
        if (maze[index][i] == 'E')
          numE++;
        if (maze[index][i] == 'S')
          numS++;
      }
      index++;
    }
    if (numE != 1 || numS != 1)
      throw new IllegalStateException();
  }

  private void wait(int millis){
    try {
      Thread.sleep(millis);
    }
    catch (InterruptedException e) {
    }
  }

  public String toString() {
    String result = "";
    for (int r = 0; r < maze.length; r++) {
      for (int c = 0; c < maze[0].length; c++) {
        result += maze[r][c];
      }
      result += "\n";
    }
    return result;
  }

  public void setAnimate(boolean b){
    animate = b;
  }


  public void clearTerminal(){
    //erase terminal, go to top left of screen.
    System.out.println("\033[2J\033[1;1H");

  }

    /*
    Wrapper Solve Function returns the helper function
    Note the helper function has the same name, but different parameters.
    Since the constructor exits when the file is not found or is missing an E or S, we can assume it exists.
    find the location of the S.
    erase the S
    and start solving at the location of the s.
    return solve(???,???);
    */
  public int solve(){
    int row = 0;
    int col = 0;
    for (int r = 0; r < maze.length; r++) {
      for (int c = 0; c < maze[0].length; c++) {
        if (maze[r][c] == 'S') {
          row = r;
          col = c;
        }
      }
    }
    maze[row][col] = ' ';
    return solve(row, col);
  }

    /*
      Recursive Solve function:
      A solved maze has a path marked with '@' from S to E.
      Returns the number of @ symbols from S to E when the maze is solved,
      Returns -1 when the maze has no solution.

      Postcondition:
        The S is replaced with '@' but the 'E' is not.
        All visited spots that were not part of the solution are changed to '.'
        All visited spots that are part of the solution are changed to '@'
    */
  private int solve(int row, int col){//you can add more parameters since this is private
    if(animate){
      clearTerminal();
      System.out.println(this);
      wait(70);
    }
    int[][] moves = {{1,0}, {-1,0}, {0,1}, {0,-1}};
    int steps = 0;
    if (maze[row][col] == 'E')
      return steps;
    maze[row][col] = '@';
    for (int i = 0; i < moves.length; i++) {
      if (maze[row+moves[i][0]][col+moves[i][1]] != '.' &&
          maze[row+moves[i][0]][col+moves[i][1]] != '#' &&
          maze[row+moves[i][0]][col+moves[i][1]] != '@') {
        steps = solve(row+moves[i][0], col+moves[i][1]);
        if (steps != -1)
          return steps+1;
          }
    }
    maze[row][col] = '.';
    return -1;
  }

}
