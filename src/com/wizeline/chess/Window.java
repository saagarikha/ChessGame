package com.wizeline.chess;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.lang.*;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import static com.sun.java.accessibility.util.AWTEventMonitor.addMouseListener;

/*
The Window class handles the User Interface. It is divided in two sections:
- The chess board which is handle by the Board class.
- The input and output, in which the user can input text to make the movements of the 
  pieces. Text can also be displayed to show messages for the players.
  
To handle the input from the input text field (when the Enter/Return key is pressed or
the Submit button is clicked) modify the actionPerformed method from the private class InputActionListener.
 
To display information such as whose turn it is, if a player is in check, error messages and more
use the JLabel outputLabel. To modify its value use its method setText(String).

The Window class contains an instance from the Board class.
The Board class is used to draw the board and the pieces according to the values of its
public HashMap<String, String> variable "pieces" which must contain a key for every
active piece on the board. The key corresponds to a board coordinate (example: a1, f5,
etc.), the value corresponding to that key must be the piece that lives on that square.
A piece is represented by a string composed of the color of the piece (b or w) followed
by the type of the piece. The different piece types are:
K - King
Q - Queen
N - kNight
B - Bishop
R - Rook
P - Pawn

You are expected to ask the user for their next movement (example: a2a4, will move the
piece in a2 to the tile a4), and redraw the board after every successfully state change
on the board. The Board class has a public method draw() that will draw the pieces according
to how they are in the public HashMap<String, String> variable "pieces".

Remember, we'll evaluate both design and implementation, even when implementing many
parts of the game is good, it's better to have a solid design.

NOTE: Make sure that the images folder is inside the src folder or at the same path than the
com directory

Some rules to remember:
    * General rules of chess (https://en.wikipedia.org/wiki/Rules_of_chess)
    * Castiling (http://en.wikipedia.org/wiki/Castling)
    * En Passant (http://en.wikipedia.org/wiki/En_passant)
    * If you're on check the only valid moves are those that let you on a no-check state
    * If a move lets you on a check state that move is invalid
    * If the current player is not on check but has no valid moves available the game
      ends as a draw
    * If the current player is on check and has no way to remove its status a checkmate
      is declared
    * Every time a player puts its opponent on check the event must be declared
    * A pawn becomes a queen if they get to the opponent's first row
*/

public class Window{
    private Board board;
    private JPanel bottomPanel;
    private JButton submitButton;
    private JFrame frame;
    
    public JLabel outputLabel;
    public JTextField textField;
    public boolean current;

    public void addRook()
    {
        board.pieces.put("a8", "bR");
        board.pieces.put( "h8", "bR");
        board.pieces.put( "a1","wR" );
        board.pieces.put( "h1" ,"wR");
        addKnight();
    }

    public void addKnight()
    {
        board.pieces.put( "b8" ,"bN");
        board.pieces.put( "g8" ,"bN");
        board.pieces.put( "b1" ,"wN");
        board.pieces.put( "g1" ,"wN");
        addBishop();
    }

    public void addBishop()
    {
        board.pieces.put( "c8" ,"bB");
        board.pieces.put( "f8" ,"bB");
        board.pieces.put( "c1" ,"wB");
        board.pieces.put( "f1" ,"wB");
        addKingQueen();
    }

    public void addKingQueen()
    {
        board.pieces.put( "d8" ,"bQ");
        board.pieces.put( "e8" ,"bK");
        board.pieces.put( "e1" ,"wQ");
        board.pieces.put( "d1" ,"wK");
        addPawn();
    }

    public void addPawn()
    {
        for(int i=97;i<=104;i++)
        {
            StringBuilder sb = new StringBuilder( );
            sb.append( (char)i  );
            sb.append( 2 );
            board.pieces.put( sb.toString(),"wP" );
            sb.deleteCharAt( sb.length()-1 );
            sb.append( 7 );
            board.pieces.put( sb.toString(),"bP" );
        }
    }
    public Window() {

        initializeWindow();
        board = new Board();
        current = true;
        //TODO: Add the pieces to the board
        addRook();
        frame.add(board, BorderLayout.CENTER);
        
        initializeGraphicalComponents();
        displayWindow();
    }

    private void initializeWindow()
    {
        frame = new JFrame( "Chess Player white starts first" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setResizable( false );

    }

    private void initializeGraphicalComponents() {
        // Creating the bottom panel that will hold the 
        // output text label, input text and submit button
        bottomPanel = new JPanel(new BorderLayout());
        outputLabel = new JLabel("Output text in this label", SwingConstants.CENTER);
        bottomPanel.add(outputLabel, BorderLayout.NORTH);
        
        InputActionListener inputListener = new InputActionListener();
        JPanel inputPanel = new JPanel(new FlowLayout());
        JLabel inputLabel = new JLabel("Input: ");
        textField = new JTextField(30);
        textField.addActionListener(inputListener);
        inputPanel.add(inputLabel);
        inputPanel.add(textField);
        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        
        submitButton = new JButton("Submit");
        submitButton.addActionListener(inputListener);
        bottomPanel.add(submitButton, BorderLayout.SOUTH);
        frame.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void displayWindow() {
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }



    private class InputActionListener implements ActionListener
    {
        public String[] validInput( String input )
        {
            String[] result = new String[ 2 ];
            if ( input.length() != 4 )
                return null;
            else
            {
                String from = input.substring( 0, 2 );
                String to = input.substring( 2, 4 );

                int begin = ( int ) from.charAt( 0 );
                int end = ( int ) to.charAt( 0 );
                if ( begin >= 97 && begin <= 104 && end >= 97 && end <= 104 )
                {
                    begin = from.charAt( 1 )-48;
                    end = to.charAt( 1 ) -48;
                    if ( begin >= 1 && end <= 8 && begin <= 8 && end >= 1 )
                    {
                        result[ 0 ] = from;
                        result[ 1 ] = to;
                        return result;
                    }
                    return null;
                }
                return null;
            }
        }

        public void actionPerformed( ActionEvent e )
        {
            // Text is read from the input field and input field is cleared
            String input = textField.getText();
            textField.setText( "" );

            //TODO: Process input read from text field and redraw the board
            String[] result = validInput( input );


            if ( result != null && board.pieces.containsKey( result[ 0 ] ) ) //add turns here later
            {
                String piece = board.pieces.get( result[ 0 ] );
                String turn = piece.substring( 0, 1 );
                String power = piece.substring( 1, 2 );

                if(current == true && !turn.equals( "w" ))
                {
                    //violation
                    textField.setText( "VIOLATION. PLAYER WHITE'S TURN");
                    return;
                }

                else if ( current == false && !turn.equals( "b" ) )
                {
                    textField.setText( "VIOLATION. PLAYER BLACK'S TURN");
                    return;
                }

                ArrayList<String> res = new ArrayList<>(  );
                if(power.equals( "Q" ))
                {
                    res = getQueenMove( result[0], result[1] );
                }
                else if ( power.equals( "R" ) )
                {
                    res = getRookMove( result[0], result[1] );
                }
                else if ( power.equals( "B" ) )
                {
                    res = getBishopMove( result[0] , result[1]);
                }

                else if ( power.equals( "N" ) )
                {
                    res = getKnightMove( result[0] );
                }

                else if ( power.equals( "P" ) )
                {
                    res = getPawnMove( result[0],turn );
                }

                else if ( power.equals( "K" ) )
                {
                    res = getKingMove( result[0] );
                }

                if ( res.size()>0 && res.contains( result[1] ) )
                {
                    if ( board.pieces.containsKey( result[1] ) )
                    {
                        String toPiece = board.pieces.get( result[1] );
                        String toTurn = toPiece.substring( 0,1 );

                        if ( toTurn.equals( turn ) )
                        {
                            //violation
                            textField.setText( "PLEASE DON'T KILL YOURSELF" );
                        }

                        else
                        {
                            System.out.println("Log Message: Player "+turn+" killed Player "+toTurn +" and piece destroyed: "+ toPiece.substring( 1,2 ));
                        }
                    }
                    board.pieces.remove( result[0] );
                    board.pieces.put( result[1],piece );
                    board.draw();
                }
                else
                {
                    textField.setText( "VIOLATION : NOT A VALID MOVE. TRY AGAIN!!" );
                    return;
                }
                current = !current;
            }

            else
            {
                textField.setText( "VIOLATION: INVALID INPUT" );
            }
        }

        public ArrayList<String> getRookMove( String from ,String to)
        {
            int begin = from.charAt( 0 );
            int end = from.charAt( 1 )-48;

            ArrayList<String> result = new ArrayList<>();
            boolean isSet = false;
            for ( int i = begin; i >= 97; i-- )
            {
                StringBuilder sb = new StringBuilder();
                sb.append( ( char ) i );
                sb.append( end );
                boolean isPresent = board.pieces.containsKey( sb.toString() ) & !sb.toString().equals( from ) &!sb.toString().equals( to );

                if ( !isPresent && !isSet )
                    result.add( sb.toString() );
                else if ( isPresent )
                    isSet = true;
            }

            isSet = false;
            for ( int i=begin;i<=104;i++ )
            {
                StringBuilder sb = new StringBuilder();
                sb.append( ( char ) i );
                sb.append( end );
                boolean isPresent = board.pieces.containsKey( sb.toString() ) & !sb.toString().equals( from ) &!sb.toString().equals( to );

                if ( !isPresent && !isSet )
                    result.add( sb.toString() );
                else if ( isPresent )
                    isSet = true;
            }
            isSet = false;

            for ( int i = end; i <= 8; i++ )
            {
                StringBuilder sb = new StringBuilder();
                sb.append( (char) begin );
                sb.append( i );
                boolean isPresent = board.pieces.containsKey( sb.toString() ) & !sb.toString().equals( from ) &!sb.toString().equals( to );

                if ( !isPresent && !isSet )
                    result.add( sb.toString() );
                else if ( isPresent )
                    isSet = true;
            }

            isSet = false;
            for ( int i=end;i>=1;i-- )
            {
                StringBuilder sb = new StringBuilder();
                sb.append( (char) begin );
                sb.append( i );
                boolean isPresent = board.pieces.containsKey( sb.toString() ) & !sb.toString().equals( from ) &!sb.toString().equals( to );

                if ( !isPresent && !isSet )
                    result.add( sb.toString() );
                else if ( isPresent )
                    isSet = true;
            }
            return result;
        }

        public ArrayList<String> getBishopMove( String from ,String to)
        {
            int begin = from.charAt( 0 );
            int end = from.charAt( 1 )-48;

            boolean isSetUp = false, isSetDown = false;

            int up=end,down = end;
            ArrayList<String> result = new ArrayList<>(  );
            for ( int i=begin+1;i<=104;i++ )
            {
                up++;down--;
                StringBuilder upw = new StringBuilder(  );
                StringBuilder dwn = new StringBuilder(  );

                if ( up >=1 && up<=8 )
                {
                    upw.append( (char) i);
                    upw.append( up );
                    boolean isPresentUp = board.pieces.containsKey( upw.toString() ) & !upw.toString().equals( from ) &!upw.toString().equals( to );
                    if ( !isPresentUp && !isSetUp )
                    {
                        result.add( upw.toString() );
                    }

                    else if ( isPresentUp )
                        isSetUp = true;
                }
                if ( down >=1 && down<=8 )
                {
                    dwn.append( (char)i );
                    dwn.append( down );

                    boolean isPresentDown = board.pieces.containsKey( dwn.toString() ) & !dwn.toString().equals( from ) &!dwn.toString().equals( to );
                    if ( !isPresentDown && !isSetDown)
                    {
                        result.add( dwn.toString() );
                    }

                    else if ( isPresentDown )
                        isSetDown = true;
                }
            }

            up = end; down = end;
            isSetUp = false;
            isSetDown = false;
            for ( int i=begin-1;i>=97;i-- )
            {
                up++;down--;
                StringBuilder upw = new StringBuilder(  );
                StringBuilder dwn = new StringBuilder(  );

                if ( up >=1 && up<=8 )
                {
                    upw.append( (char) i);
                    upw.append( up );
                    boolean isPresentUp = board.pieces.containsKey( upw.toString() ) & !upw.toString().equals( from ) &!upw.toString().equals( to );
                    if ( !isPresentUp && !isSetUp )
                    {
                        result.add( upw.toString() );
                    }

                    else if ( isPresentUp )
                        isSetUp = true;
                }
                if ( down >=1 && down<=8 )
                {
                    dwn.append( (char)i );
                    dwn.append( down );

                    boolean isPresentDown = board.pieces.containsKey( dwn.toString() ) & !dwn.toString().equals( from ) &!dwn.toString().equals( to );
                    if ( !isPresentDown && !isSetDown)
                    {
                        result.add( dwn.toString() );
                    }

                    else if ( isPresentDown )
                        isSetDown = true;
                }
            }
            return result;
        }

        public ArrayList<String> getQueenMove(String from, String to)
        {
            ArrayList<String> result= getRookMove( from ,to);
            result.addAll( getBishopMove( from ,to ) );
            return result;
        }

        public ArrayList<String> getKnightMove(String from)
        {
            int begin = from.charAt( 0 );
            int end = from.charAt( 1 )-48;

            ArrayList<String> result = new ArrayList<>(  );

            StringBuilder sb = new StringBuilder(  );

            if ( begin+2<=107 )
            {
                sb.append( (char) (begin+2));
                if ( end-1>=1 && end-1 <=8 )
                {
                    sb.append( end-1 );
                    result.add( sb.toString() );
                }
                if ( end+1 >=1 && end+1<=8 )
                {
                    if ( sb.length()>1 )
                        sb.deleteCharAt( sb.length()-1 );
                    sb.append( end+1 );
                    result.add( sb.toString() );
                }
            }

            sb= new StringBuilder(  );
            if ( begin-2<=107  && begin-2 >=97)
            {
                sb.append( (char) (begin-2));
                if ( end-1>=1 && end-1 <=8 )
                {
                    sb.append( end-1 );
                    result.add( sb.toString() );
                }
                if ( end+1 >=1 && end+1<=8 )
                {
                    if ( sb.length()>1 )
                        sb.deleteCharAt( sb.length()-1 );
                    sb.append( end+1 );
                    result.add( sb.toString() );
                }
            }
            sb = new StringBuilder(  );
            if ( begin+1<=107 )
            {
                sb.append( (char) (begin+1));
                if ( end-2>=1 && end-2 <=8 )
                {
                    sb.append( end-2 );
                    result.add( sb.toString() );
                }
                if ( end+2 >=1 && end+2<=8 )
                {
                    if ( sb.length()>1 )
                        sb.deleteCharAt( sb.length()-1 );
                    sb.append( end+2 );
                    result.add( sb.toString() );
                }
            }

            sb = new StringBuilder(  );
            if ( begin-1<=107 && begin-1>=97 )
            {
                sb.append( (char) (begin-1));
                if ( end-2>=1 && end-2 <=8 )
                {
                    sb.append( end-2 );
                    result.add( sb.toString() );
                }
                if ( end+2 >=1 && end+2<=8 )
                {
                    if ( sb.length()>1 )
                        sb.deleteCharAt( sb.length()-1 );
                    sb.append( end+2 );
                    result.add( sb.toString() );
                }
            }

            return result;
        }

        public ArrayList<String> getPawnMove(String from,String turn)
        {
            int begin = from.charAt( 0 );
            int to = from.charAt( 1 )-48;

            ArrayList<String> result = new ArrayList<>(  );
            if ( to ==2 || to ==7 )
            {
                StringBuilder sb= new StringBuilder(  );
                sb.append( (char)begin );
                    if(to==7)
                        sb.append( to-2 );
                    else
                        sb.append( to+2 );
                result.add( sb.toString() );
            }
            else
            {
                int left = begin-1;
                int right = begin+1;

                int end = to;
                if ( turn.equals( "w" ) )
                    end+=1;
                else
                    end-=1;

                StringBuilder sb = new StringBuilder(  );
                if ( left>=97 )
                {
                    sb.append( (char)left );
                    sb.append( end );
                    result.add( sb.toString() );
                }
                sb = new StringBuilder(  );
                if ( right<=107 )
                {
                    sb.append( (char)right );
                    sb.append( end );
                    result.add( sb.toString() );
                }

                sb= new StringBuilder(  );

                sb.append( (char)begin );
                sb.append( end );
                result.add( sb.toString() );
            }
            return result;
        }

        public ArrayList<String> getKingMove(String from)
        {
            ArrayList<String> result = new ArrayList<>();

            int begin = from.charAt( 0 );
            int end = from.charAt( 1 ) - 48;

            StringBuilder sb = new StringBuilder();
            if ( begin - 1 >= 97 )
            {
                sb.append( ( char ) ( begin - 1 ) );
                sb.append( end );
                result.add( sb.toString() );
                sb.deleteCharAt( sb.length() - 1 );

                if ( end - 1 >= 1 )
                {
                    sb.append( end - 1 );
                    result.add( sb.toString() );
                    sb = new StringBuilder();

                    sb.append( ( char ) begin );
                    sb.append( end - 1 );
                    result.add( sb.toString() );

                    sb.deleteCharAt( sb.length() - 1 );
                }

                if ( end + 1 <= 8 )
                {
                    sb.append( end + 1 );
                    result.add( sb.toString() );
                    sb = new StringBuilder();

                    sb.append( ( char ) ( begin - 1 ) );
                    sb.append( end - 1 );
                    result.add( sb.toString() );
                }
            }

            if ( begin + 1 <= 104 )
            {
                sb.append( ( char ) ( begin + 1 ) );
                sb.append( end );
                result.add( sb.toString() );
                sb.deleteCharAt( sb.length() - 1 );

                if ( end - 1 >= 1 )
                {
                    sb.append( end - 1 );
                    result.add( sb.toString() );
                    sb.deleteCharAt( sb.length() - 1 );
                }

                if ( end + 1 <= 8 )
                {
                    sb.append( end + 1 );
                    result.add( sb.toString() );
                }
            }
            return result;
        }
    }

}
