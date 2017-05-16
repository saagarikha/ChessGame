package com.wizeline.chess;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")

public class Board extends JPanel {
    public static final int ROWS = 8;
    public static final int COLS = 8;
    public static final String WHITE_PIECE = "w";
    public static final String BLACK_PIECE = "b";
    public static final String[] COLUMN_NAMES = 
        new String[] {"a", "b", "c", "d", "e", "f", "g", "h"};
	
    // Constants for graphics. These variables represent units measured in pixels
    private final int tileWidth = 60;
    private final int tileHeight = 60;
    private final int xOffset = 25;
    private final int yOffset = 25;
    private final int panelWidth = 525;
    private final int panelHeight = 540;
    
    private HashMap<String, BufferedImage> piecesImages;
    
    public HashMap<String, String> pieces;
    
    public Board() {
        loadImages();
        // Creating the map that will hold the pieces.
        pieces = new HashMap<String, String>();
    }
    
    private void loadImages() {
        piecesImages = new HashMap<String, BufferedImage>();
        piecesImages.put("wP", loadImage("white_pawn"));
        piecesImages.put("wR", loadImage("white_rook"));
        piecesImages.put("wB", loadImage("white_bishop"));
        piecesImages.put("wN", loadImage("white_knight"));
        piecesImages.put("wQ", loadImage("white_queen"));
        piecesImages.put("wK", loadImage("white_king"));
        piecesImages.put("bP", loadImage("black_pawn"));
        piecesImages.put("bR", loadImage("black_rook"));
        piecesImages.put("bB", loadImage("black_bishop"));
        piecesImages.put("bN", loadImage("black_knight"));
        piecesImages.put("bQ", loadImage("black_queen"));
        piecesImages.put("bK", loadImage("black_king"));
    }

    private BufferedImage loadImage(String imageName) {
        try {
            java.net.URL url = getClass().getResource("/images/" + imageName + ".png");
            if(url == null) {
                url = getClass().getResource("/" + imageName + ".png");
            }
            return ImageIO.read(url);
        } catch(IOException e) {
            System.out.println("Error loading image");
            return null;
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(panelWidth, panelHeight);
    }

    private Color swapColor(Color color) {
        if(color.equals(Color.WHITE)) {
            return Color.GRAY;
        }
        return Color.WHITE;
    }
    
    private void drawTileBorder(Graphics graphics, int x, int y) {
        graphics.setColor(Color.BLACK);
        graphics.drawRect(x, y, tileWidth, tileHeight);
    }   

    private void fillTile(Graphics graphics, Color color, int x, int y) {
        graphics.setColor(color);
        graphics.fillRect(x, y, tileWidth, tileHeight);
    }   

    private int calculateCoordinate(int offset, int tileMeasurement, int rowOrCol) {
        return calculateCoordinate(offset, tileMeasurement, rowOrCol, false);
    }   

    private int calculateCoordinate(int offset, int tileMeasurement, int rowOrCol, boolean addHalfMeasurement) {
        return offset + (tileMeasurement * rowOrCol) + (addHalfMeasurement ? tileMeasurement/2 : 0);
    }   

    private void drawNumbers(Graphics graphics) {
        graphics.setColor(Color.BLACK);
        char number = '8';
        for(int row = 0; row < ROWS; row++) {
    	    String numberStr = Character.toString(number);
    	    graphics.drawString(numberStr, 10, calculateCoordinate(yOffset, tileHeight, row, true));
    	    number--;
        }
    }   

    private void drawLetters(Graphics graphics) {
        char letter = 'A';
        for(int col = 0; col < COLS; col++) {
        String letterStr = Character.toString(letter);
            graphics.drawString(letterStr, calculateCoordinate(xOffset, tileWidth, col, true), panelHeight - yOffset/2);
            letter++;
        }
    }

    private void drawTiles(Graphics graphics) {
        Color color = Color.WHITE;
        for(int row = 0; row < ROWS; row++) {
            color = swapColor(color);
            for(int col = 0; col < COLS; col++) {
                int x = calculateCoordinate(xOffset, tileWidth, col);
                int y = calculateCoordinate(yOffset, tileHeight, row);
                drawTileBorder(graphics, x, y);
                color = swapColor(color);
                fillTile(graphics, color, x, y);
            }
        }
    }
    
    private void drawPieces(Graphics graphics) {
    	for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
            	if(pieces != null) {
	                String piece = pieces.get(getChessNotation(row, col));
	                if(piece != null) {
	                	graphics.drawImage(getImage(piece), calculateCoordinate(xOffset, tileWidth, col), 
							calculateCoordinate(yOffset, tileHeight, row), tileWidth, tileHeight, null);
	                }
            	}
            }
        }
    }
    
    private void clearPanel(Graphics graphics) {
        graphics.clearRect(0, 0, panelWidth, panelHeight);
    }
    
    private String getColumnChessNotation(int col) {
        return COLUMN_NAMES[col];
    }

    private String getChessNotation(int row, int col) {
        return getColumnChessNotation(col) + Integer.toString(ROWS - row);
    }
    
    public void draw() {
        if(pieces != null) {
            repaint();
        }
    }
    
    public void paint(Graphics graphics) {
        clearPanel(graphics);
        drawTiles(graphics);
        drawNumbers(graphics);
        drawLetters(graphics);
        drawPieces(graphics);
    }
    
    private BufferedImage getImage(String piece) {
        if(piecesImages.containsKey(piece)) {
            return piecesImages.get(piece);
        }
        return null;
    }
}
