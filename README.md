## How to compile and run
### Compile and Run
- Open a terminal and move to the `src` directory inside the `java` folder that you extracted from `java_chess.zip`.
- Execute `javac com/wizeline/chess/*.java` to compile all files.
- To run the application use `java com.wizeline.chess.Main` (make sure that the `com` and `images` directories are at the same level).

### Generate and run JAR
Another alternative to run the application is to generate a JAR file.
- Open a terminal and move to the `src` directory inside the `java` folder that you extracted from `java_chess.zip`.
- Execute `javac com/wizeline/chess/*.java` to compile all files. Then execute `jar cfe Chess.jar com.wizeline.chess.Main com/wizeline/chess/*.class images`
- Once the jar file is created you can execute it with: `java -jar Chess.jar` or by double click it in the finder/windows explorer.

## How to add pieces to the board
As shown in the constructor of [Window.java](src/com/wizeline/chess/Window.java), the board class is initialized with a HashMap where the key is the position and the value the kind of piece (See Window.java for more information), the following is an example of random pieces being initialized:
```
board = new Board();
// Adding pieces to the board
board.pieces.put("a8", "bR");
board.pieces.put("b8", "bN");
board.pieces.put("c8", "bB");
board.pieces.put("d7", "bP");
board.pieces.put("e1", "wK");
frame.add(board, BorderLayout.CENTER);
```

## How to handle the Input
In this example we handle input written in the following way `e1e4`, which will move the piece found at tile E1 to tile E4.
To execute this example follow these steps:
- Edit file [Window.java](src/com/wizeline/chess/Window.java) under the comment `//TODO: Process input from text field` by adding the following code:
```
String origin = input.substring(0,2);
String target = input.substring(2,4);
String piece = board.pieces.get(origin);
board.pieces.put(target, piece);
board.pieces.remove(origin);
outputLabel.setText("White Player's turn");
board.draw();
```
- Compile and run the application (Check the "How to compile and run" section)
- In the input field type the position of a piece and the target tile (Example: e1e4) . Press the Return/Enter key or click the Submit button.