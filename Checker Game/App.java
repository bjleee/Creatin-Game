package Creating-Game.Checker Game;

//import org.reflections.Reflections;
//import org.reflections.scanners.Scanners;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.core.PFont;
import processing.event.MouseEvent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Table.Cell;

import java.awt.Font;
import java.io.*;
import java.util.*;

public class App extends PApplet {

    public static final int CELLSIZE = 48;
    public static final int SIDEBAR = 0;
    public static final int BOARD_WIDTH = 8;
    public static final int[] BLACK_RGB = {181, 136, 99};
    public static final int[] WHITE_RGB = {240, 217, 181};
    public static final float[][][] coloursRGB = new float[][][] {
        //default - white & black
        {
                {WHITE_RGB[0], WHITE_RGB[1], WHITE_RGB[2]},
                {BLACK_RGB[0], BLACK_RGB[1], BLACK_RGB[2]}
        },
        //green
        {
                {105, 138, 76}, //when on white cell
                {105, 138, 76} //when on black cell
        },
        //blue
        {
                {196,224,232},
                {170,210,221}
        }
	};

    public static int WIDTH = CELLSIZE*BOARD_WIDTH+SIDEBAR;
    public static int HEIGHT = BOARD_WIDTH*CELLSIZE;
    PImage gameOverImage; 
    public static final int FPS = 60;


    public App() {
        
    }

    /**
     * Initialise the setting of the window size.
    */
	@Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }
    // Create board
    private Piece[][] board = new Piece[BOARD_WIDTH][BOARD_WIDTH];
    private void initializeBoard() {
        for (int row = 0; row < BOARD_WIDTH; row++) {
            for (int col = 0; col < BOARD_WIDTH; col ++) {
                if ((row < 3 || row >= 5) && (row + col) % 2 != 0) {
                    if (row < 3) {
                        // Save position for white piece
                        board[row][col] = new Piece(false, false);
                    } else {
                        // Save position for black piece
                        board[row][col] = new Piece(true, false);
                    }
                }
            }
        }
    }
	@Override
    public void setup() {
        frameRate(FPS);
        gameOverImage = loadImage("/home/htra0336/INFO1113/TUTORIAL/src/warm-up/checkers_scaffold/game-over-glitch.gif");
		//Set up the data structures used for storing data in the game
        initializeBoard();
    }


    /**
     * Receive key pressed signal from the keyboard.
    */
	@Override
    public void keyPressed(){

    }
    
    /**
     * Receive key released signal from the keyboard.
    */
	@Override
    public void keyReleased(){

    }
    private int currentPlayer = 1;
    private boolean moved =true;
    private void switchPlayer(){
        if(currentPlayer == 1){
            currentPlayer = 2;
        }
        else{
            currentPlayer = 1;
        }
    }
    private ArrayList<int[]> highlightPiece = new ArrayList<>();
    private int pickedPieceCol = -1;
    private int pickedPieceRow = -1;
    private ArrayList<int[]> potentialMoves = new ArrayList<>();
    private ArrayList<int[]> potentialMove(int currentRow, int currentCol){
        ArrayList<int[]> coordinate = new ArrayList<>();
        int[][] vectorsK = {{1,1},{1,-1},{2,2},{2,-2},{-1,1},{-1,-1},{-2,2},{-2,-2}};
        int[][] vectorsP = {{1,1},{1,-1},{2,2},{2,-2}};
        // white piece
        if(board[currentRow][currentCol].isKing){
            if(!board[currentRow][currentCol].isBlack){
                for(int[] vector : vectorsK){
                    int newRow = currentRow + vector[0];
                    int newCol = currentCol + vector[1];
                    // check within the bounds of board
                    if(newRow >= 0 && newRow < BOARD_WIDTH && newCol >= 0 && newCol < BOARD_WIDTH){
                        if(board[newRow][newCol] != null){
                            continue;
                        }
                        else{
                            int differenceRow = Math.abs(newRow-currentRow);
                            int differenceCol = Math.abs(newCol-currentCol);
                            if(differenceRow == 2 && differenceCol == 2){
                                // check middle piece
                                int middleRow = (newRow + currentRow) /2;
                                int middleCol = (newCol + currentCol) /2;
                                if(board[middleRow][middleCol] != null){
                                    int[] move = new int[]{newRow, newCol};
                                    coordinate.add(move);
                                }
                            }
                            else if(differenceCol == 1 && differenceRow == 1){
                                // store the potential move
                                int[] move = new int[]{newRow,newCol};
                                coordinate.add(move);
                            }
                        }
                        
                    }
            }
        }
        else if(board[currentRow][currentCol].isBlack){
            for(int[] vector : vectorsK){
                int newRow = currentRow - vector[0];
                int newCol = currentCol - vector[1];
                // check within the bounds of board
                if(newRow >= 0 && newRow < BOARD_WIDTH && newCol >= 0 && newCol < BOARD_WIDTH){
                    if(board[newRow][newCol] != null){
                        continue;
                    }
                    else{
                        int differenceRow = Math.abs(newRow-currentRow);
                        int differenceCol = Math.abs(newCol-currentCol);
                        if(differenceRow == 2 && differenceCol == 2){
                            // check middle piece
                            int middleRow = (newRow + currentRow) /2;
                            int middleCol = (newCol + currentCol) /2;
                            if(board[middleRow][middleCol] != null){
                                int[] move = new int[]{newRow, newCol};
                                coordinate.add(move);
                            }
                        }
                        else if(differenceCol == 1 && differenceRow == 1){
                            // store the potential move
                            int[] move = new int[]{newRow,newCol};
                            coordinate.add(move);
                        }
                    }
                    
                }
        }
        }
        }
        else{
            if(!board[currentRow][currentCol].isBlack){
                for(int[] vector : vectorsP){
                    int newRow = currentRow + vector[0];
                    int newCol = currentCol + vector[1];
                    // check within the bounds of board
                    if(newRow >= 0 && newRow < BOARD_WIDTH && newCol >= 0 && newCol < BOARD_WIDTH){
                        if(board[newRow][newCol] != null){
                            continue;
                        }
                        else{
                            int differenceRow = Math.abs(newRow-currentRow);
                            int differenceCol = Math.abs(newCol-currentCol);
                            if(differenceRow == 2 && differenceCol == 2){
                                // check middle piece
                                int middleRow = (newRow + currentRow) /2;
                                int middleCol = (newCol + currentCol) /2;
                                if(board[middleRow][middleCol] != null){
                                    int[] move = new int[]{newRow, newCol};
                                    coordinate.add(move);
                                }
                            }
                            else if(differenceCol == 1 && differenceRow == 1){
                                // store the potential move
                                int[] move = new int[]{newRow,newCol};
                                coordinate.add(move);
                            }
                        }
                        
                    }
            }
        }
        else if(board[currentRow][currentCol].isBlack){
            for(int[] vector : vectorsP){
                int newRow = currentRow - vector[0];
                int newCol = currentCol - vector[1];
                // check within the bounds of board
                if(newRow >= 0 && newRow < BOARD_WIDTH && newCol >= 0 && newCol < BOARD_WIDTH){
                    if(board[newRow][newCol] != null){
                        continue;
                    }
                    else{
                        int differenceRow = Math.abs(newRow-currentRow);
                        int differenceCol = Math.abs(newCol-currentCol);
                        if(differenceRow == 2 && differenceCol == 2){
                            // check middle piece
                            int middleRow = (newRow + currentRow) /2;
                            int middleCol = (newCol + currentCol) /2;
                            if(board[middleRow][middleCol] != null){
                                int[] move = new int[]{newRow, newCol};
                                coordinate.add(move);
                            }
                        }
                        else if(differenceCol == 1 && differenceRow == 1){
                            // store the potential move
                            int[] move = new int[]{newRow,newCol};
                            coordinate.add(move);
                        }
                    }
                    
                }
        }
        }
        }
    return coordinate;
}
    private boolean colorPiece(){
        if (currentPlayer == 1){
            return false;
        }
        else{
            return true;
        }
    }

    private void processMove(ArrayList<int[]> validPiece, int nextRow, int nextCol){
        int[] currentPiece = validPiece.get(validPiece.size() - 1);
        int currentRow = currentPiece[0];
        int currentCol = currentPiece[1];
        int[] nextMove = new int[]{nextRow, nextCol};
        boolean moveFound = false;
        for(int[] potentialMove : potentialMoves){
            if(Arrays.equals(potentialMove, nextMove)){
                moveFound = true;
                break;
            }
        }
        if(moveFound){
            int differenceRow = Math.abs(nextRow-currentRow);
            int differenceCol = Math.abs(nextCol-currentCol);
            if (differenceRow == 2 && differenceCol == 2){
                int middleRow = (nextRow + currentRow) /2;
                int middleCol = (nextCol + currentCol) /2;
                if(board[middleRow][middleCol] != null && board[middleRow][middleCol].isBlack != colorPiece()){
                    board[middleRow][middleCol] = null;
                }
            }
        //TODO: Check if user clicked on an available move - move the selected piece there. 
		//TODO: Remove captured pieces from the board
		//TODO: Check if piece should be promoted and promote it
		//TODO: Then it's the other player's turn.
            // handle to become king
            if((currentPlayer == 1 && nextRow == 7) || (currentPlayer == 2 && nextRow == 0) || board[currentRow][currentCol].isKing){
                board[nextRow][nextCol] = new Piece(colorPiece(), true);
                board[currentRow][currentCol] = null;
            }
            else{
                board[nextRow][nextCol] = new Piece(colorPiece(), false);
                board[currentRow][currentCol] = null;
            }
            switchPlayer();
            
        }
    }

    private boolean belongPotentialMove(int[] nextMove){
        for(int[] move : potentialMoves){
            if(Arrays.equals(move, nextMove)){
                return true;
            }
        }
        return false;
    }
    @Override
    public void mousePressed(MouseEvent e) {
        //Check if the user clicked on a piece which is theirs - make sure only whoever's current turn it is, can click on pieces	
        int currentRow = mouseY/CELLSIZE;
        int currentCol = mouseX/CELLSIZE;
        
        if(currentCol >= 0 && currentCol < BOARD_WIDTH && currentRow >=0 && currentRow < BOARD_WIDTH){
            if(currentPlayer == 1){
                // Otherwise, check if the clicked cell contains a valid piece
                if (board[currentRow][currentCol] != null && !board[currentRow][currentCol].isBlack) {
                    System.out.println("tao day");
                    moved = false;
                    potentialMoves = potentialMove(currentRow, currentCol);
                    pickedPieceRow = currentRow;
                    pickedPieceCol = currentCol;
                    highlightPiece.add(new int[]{pickedPieceRow, pickedPieceCol});
                }
                else if(board[currentRow][currentCol] == null && belongPotentialMove(new int[]{currentRow, currentCol}) ){
                    processMove(highlightPiece, currentRow, currentCol);
                    moved = true;
                }
            }
            else if(currentPlayer == 2){
                // Otherwise, check if the clicked cell contains a valid piece
                if (board[currentRow][currentCol] != null && board[currentRow][currentCol].isBlack) {
                    System.out.println("tao ms dung");
                    moved = false;
                    potentialMoves = potentialMove(currentRow, currentCol);
                    pickedPieceRow = currentRow;
                    pickedPieceCol = currentCol;
                    highlightPiece.add(new int[]{pickedPieceRow,pickedPieceCol});
                }
                else if (board[currentRow][currentCol] == null && belongPotentialMove(new int[]{currentRow, currentCol})) {
                    processMove(highlightPiece, currentRow, currentCol);
                    moved = true;
                }
            }
        }
    
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        
    }
    /**
     * Draw all elements in the game by current frame. 
    */
	@Override
    public void draw() {
        this.noStroke();
        background(180);
		//draw the board
		drawBoard();
		//draw highlighted cells
        if(!moved){
            drawHighlightedCell();
        }
        drawPiece();
		//check if the any player has no more pieces. The winner is the player who still has pieces remaining
        if(gameOver()){
            image(gameOverImage, 0, 0, width, height);

            // Exit the method to prevent further drawing
            return;
        }
    }
    private void drawBoard() {
        for (int row = 0; row < BOARD_WIDTH; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                if ((row + col) % 2 == 0) {
                    setFill(0, 0); // White cell
                } else {
                    setFill(0, 1); // Black cell
                }
                rect(col * CELLSIZE, row * CELLSIZE, CELLSIZE, CELLSIZE);
            }
        }
    }
    private void drawPiece() {
        for(int row = 0; row < BOARD_WIDTH; row++){
            for(int col = 0; col < BOARD_WIDTH; col++){
                if (board[row][col] != null) {
                    if (board[row][col].isKing){
                        if (board[row][col].isBlack) {
                            drawBlackKing(row,col);
                        } else {
                            drawWhiteKing(row, col);
                        }
                    }
                    else{
                        if (board[row][col].isBlack) {
                            drawBlackPiece(row,col);
                        } else {
                            drawWhitePiece(row,col);
                        }
                    }
                }
                else{
                    continue;
                }
            }
        }
    }
    private void drawBlackPiece(int row, int col){
        fill(50,50,50);
        stroke(255);
        strokeWeight(5);

        // Draw black piece
        ellipse((col + 0.5f) * CELLSIZE, (row + 0.5f) * CELLSIZE, CELLSIZE * 0.75f, CELLSIZE * 0.75f);
    }
    private void drawWhitePiece(int row, int col){
        setFill(0, 0);
        stroke(0);
        strokeWeight(5);    
        // Draw black piece
        ellipse((col + 0.5f) * CELLSIZE, (row + 0.5f) * CELLSIZE, CELLSIZE * 0.75f, CELLSIZE * 0.75f);
    }
    private void drawBlackKing(int row, int col){
        float centerX = (col + 0.5f) * CELLSIZE;
        float centerY = (row + 0.5f) * CELLSIZE;

        noFill();
        stroke(255);
        strokeWeight(9);
        ellipse(centerX, centerY, 39, 39);
        noFill();
        stroke(0);
        strokeWeight(10);
        ellipse(centerX, centerY, 29, 29);
        fill(0);
        stroke(255);
        strokeWeight(7);
        ellipse(centerX, centerY, 22, 22);
        noStroke();
    }
    private void drawWhiteKing(int row, int col){
        float centerX = (col + 0.5f) * CELLSIZE;
        float centerY = (row + 0.5f) * CELLSIZE;

        noFill();
        stroke(0);
        strokeWeight(9);
        ellipse(centerX, centerY, 39, 39);
        noFill();
        stroke(255);
        strokeWeight(10);
        ellipse(centerX, centerY, 29, 29);
        fill(255);
        stroke(0);
        strokeWeight(7);
        ellipse(centerX, centerY, 22, 22);
        noStroke();
    }
    private void drawHighlightedCell(){
        for(int row = 0 ; row < BOARD_WIDTH; row++){
            for(int col = 0; col < BOARD_WIDTH; col++){
                // System.out.println(pickedPieceRow);
                // System.out.println(pickedPieceCol);
                // System.out.println(board[pickedPieceRow][pickedPieceCol]);
                if(row == pickedPieceRow && col == pickedPieceCol){
                    if(board[pickedPieceRow][pickedPieceCol].isBlack){
                        setFill(1,1);
                    }
                    else{
                        setFill(1, 0);
                    }
                    rect(col*CELLSIZE, row*CELLSIZE, CELLSIZE,CELLSIZE);
                    for(int[] move: potentialMoves){
                        int moveRow = move[0];
                        int moveCol = move[1];
                        setFill(2, 0);
                        rect(moveCol*CELLSIZE, moveRow*CELLSIZE, CELLSIZE, CELLSIZE);
                    }
                } 

            }
        }
    }
    private boolean gameOver(){
        int countBlack = 0, countWhite = 0;
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if(board[row][col]!= null){
                    if (board[row][col].isBlack){
                        countBlack += 1;
                    }
                    else{
                        countWhite += 1;
                    }
                }
            }
        }
        if (countBlack == 0 || countWhite == 0) {
            return true;
        }
        return false;
    }
    
	/**
     * Set fill colour for cell background
     * @param colourCode The colour to set
     * @param blackOrWhite Depending on if 0 (white) or 1 (black) then the cell may have different shades
     */
	public void setFill(int colourCode, int blackOrWhite) {
		this.fill(coloursRGB[colourCode][blackOrWhite][0], coloursRGB[colourCode][blackOrWhite][1], coloursRGB[colourCode][blackOrWhite][2]);
	}

    public static void main(String[] args) {
        PApplet.main("Checkers.App");
    }


}
