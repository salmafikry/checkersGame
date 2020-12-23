
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author salmafikry
 */
public class Move {
    
    //Multiple jump checker for human player (human player chooses whether to execute the jump move or end turn)
    public static void makeAnotherJump(Point currentTile, Map<Point, Tile> board){
        Map<String, Point> legalMoves= getAvailableMoves(currentTile, board);
        for (String s : legalMoves.keySet()){
            if (s.equals("jumpBlack") || s.equals("jumpBlack1") || s.equals("jumpKingBlack") || s.equals("jumpKingBlack1")){
                Point dest= legalMoves.get(s);
                System.out.println("Another jump move available, do you wish to make it?");
                System.out.println("Enter '1' to execute jump, or '2' to finish turn");
                Scanner scanner = new Scanner(System.in);
                String[] output = new String[2];
                String ss = scanner.next();
                if(ss.equals("1")) {
                    System.out.println("Executing Jump");
                    Board.jumpmovePiece(currentTile, dest, board);
                    //recursive call until no more jumps are available
                    makeAnotherJump(dest, board);
                }
                else if (ss.equals("2")){
                System.out.println("Terminate turn");
            }
            output[0] = ss;
        //return output;

            }
        }
    }
    
    //Multiple jump checker for ai player (AI is forced to make it)
    public static void aiAnotherJump(Point currentTile, Map<Point, Tile> board){
        Map<String, Point> legalMoves= getAvailableMoves(currentTile, board);
        for (String s : legalMoves.keySet()){
            if (s.equals("jumpWhite") || s.equals("jumpWhite1") || s.equals("jumpKingWhite") || s.equals("jumpKingWhite1")){
                Point dest= legalMoves.get(s);
                Board.jumpmovePiece(currentTile, dest, board);
                //recursive call until no more jumps are available
                aiAnotherJump(dest, board);
            }
        }
    }
   
    public static List<Boolean> makeMove(Point currentPTile, Point destination, Map<Point, Tile> board){
        
        Map<Point, Map<String, Point>> allLegalMoves= getAllLegalMoves(board); 
        
        boolean legal=false;
        boolean jump=false;
        boolean jumpExecuted=false;
        List<Boolean> legalandjump=Arrays.asList(new Boolean[2]);
        legalandjump.set(0,legal);
        legalandjump.set(1,jumpExecuted);
        Type pieceType= board.get(currentPTile).getPiece();
        if (pieceType== Type.regWPiece || pieceType== Type.kingWPiece){
            for (Map<String, Point> m : allLegalMoves.values()){
                for (String s: m.keySet()){
                    if (s.equals("jumpWhite") || s.equals("jumpWhite1") || s.equals("jumpKingWhite") || s.equals("jumpKingWhite1")){
                        jump=true;
                        
                    }
                }
            }
        }
        if (pieceType== Type.regBPiece || pieceType== Type.kingBPiece){
            for (Map<String, Point> m : allLegalMoves.values()){
                for (String s: m.keySet()){
                    if (s.equals("jumpBlack") || s.equals("jumpBlack1") || s.equals("jumpKingBlack") || s.equals("jumpKingBlack1")){
                        jump=true;
                    }
                }
            }
        }
        Map <String, Point> legalMoves= getAvailableMoves(currentPTile, board);
        
        
        if(!legalMoves.isEmpty()){
        for (String s : legalMoves.keySet()){    
            if (legalMoves.get(s).equals(destination)){
                
                if (jump){
                    if(s.equals("jumpWhite") || s.equals("jumpWhite1") || s.equals("jumpKingWhite") || s.equals("jumpKingWhite1") || s.equals("jumpBlack") || s.equals("jumpBlack1") || s.equals("jumpKingBlack") || s.equals("jumpKingBlack1") ){
                        System.out.println("JUMPPP");
                        legal=true;
                        System.out.println("Legal move!");
                        Board.jumpmovePiece(currentPTile, destination, board);
                        jumpExecuted=true;
                        legalandjump.set(0,legal);
                        legalandjump.set(1, jumpExecuted);
                        return legalandjump; 
                    }
                    else{
                        System.out.println("Invalid move because a jump move exists"); 
                    }
                }
                else{
                    legal=true;
                    System.out.println("Legal move!");
                    Board.moveSPiece(currentPTile, destination, board);
                    jumpExecuted=false;
                    legalandjump.set(0,legal);
                    legalandjump.set(1, jumpExecuted); 
                   return legalandjump;
                }
                
            }   
        }
     
        if(!legal){
            System.out.println("Invalid move");
            
        }}
        return legalandjump;
    }
    
    
    public static Map<String,Point> getAvailableMoves(Point currentPTile, Map<Point, Tile> board)
    {
        //get the tile id to access the corresponding tile information
        Map<String, Point> legalJump= new HashMap<>();
        List<Point> legalMoves= new ArrayList();
       //int currentID= Board.getIDfromPoint(currentPTile);
        
        //store the current tile
        Tile currentTile= board.get(currentPTile);
        //store the piece trype on the current tile
        Type pieceType= currentTile.getPiece();
        
        //white pieces legal moves
        if (pieceType==Type.regWPiece || pieceType== Type.kingWPiece)
        {
            //every regular white piece has 2 possible moves
            //get the tile coordinate of the first possible move from the current tile
            Point targetTileCoord= new Point(currentPTile.x+1, currentPTile.y-1);
            if(targetTileCoord.x>=0 && targetTileCoord.y>=0 && targetTileCoord.x<=7 && targetTileCoord.y<=7){
            //get the id of the first possible target tile
            
            //int key= Board.getIDfromPoint(targetTileCoord);
            
            //store the target tile
            Tile targetTile= board.get(targetTileCoord);
            //check if the target tile is empty
            if(targetTile.isEmpty())
            {
                //add the coordinates of the legal target tile
                legalMoves.add(targetTileCoord);
                legalJump.put("regularWhite", targetTileCoord);
            }
            
            //check if the target tile is occupied with the openents piece
            else if (targetTile.getPiece()==Type.regBPiece || targetTile.getPiece()==Type.kingBPiece)
            {
                //check if the next legal tile after the tile occupied by the oppenent is empty
                Point newtargetTileCoord= new Point(currentPTile.x+2, currentPTile.y-2);
                if(newtargetTileCoord.x>=0 && newtargetTileCoord.y>=0 && newtargetTileCoord.x<=7 && newtargetTileCoord.y<=7){
                //int newkey= Board.getIDfromPoint(newtargetTileCoord);
                Tile newtargetTile= board.get(newtargetTileCoord);
                if(newtargetTile.isEmpty())
                {
                    //add the coordinates of the legal target tile 
                    legalMoves.add(newtargetTileCoord);
                    legalJump.put("jumpWhite", newtargetTileCoord);
                }
                
            }}}
            
            //get the tile coordinates of the second possible move from the current tile
            Point targetTileCoord1= new Point(currentPTile.x+1, currentPTile.y+1);
            if(targetTileCoord1.x>=0 && targetTileCoord1.y>=0 && targetTileCoord1.x<=7 && targetTileCoord1.y<=7){
            //int key1= Board.getIDfromPoint(targetTileCoord1);
            Tile targetTile1= board.get(targetTileCoord1);
            //check if the target tile is empty
            if(targetTile1.isEmpty())
            {
                //add the coordinates of the legal target tile 
                legalMoves.add(targetTileCoord1);
                legalJump.put("regularWhite1", targetTileCoord1);
            }
          
            else if (targetTile1.getPiece()==Type.regBPiece || targetTile1.getPiece()==Type.kingBPiece)
            {
                //check if the next legal tile after the tile occupied by the oppenent is empty
                Point newtargetTileCoord= new Point(currentPTile.x+2, currentPTile.y+2);
                if(newtargetTileCoord.x>=0 && newtargetTileCoord.y>=0 && newtargetTileCoord.x<=7 && newtargetTileCoord.y<=7){
                //int newkey= Board.getIDfromPoint(newtargetTileCoord);
                Tile newtargetTile= board.get(newtargetTileCoord);
                if(newtargetTile.isEmpty())
                {
                    //add the coordinates of the legal target tile 
                    legalMoves.add(newtargetTileCoord);
                    legalJump.put("jumpWhite1", newtargetTileCoord);
                }
                }
            }}
            
            if(pieceType== Type.kingWPiece){
                Point targetTileCoordKing= new Point(currentPTile.x-1, currentPTile.y-1);
                if(targetTileCoordKing.x>=0 && targetTileCoordKing.y>=0 && targetTileCoordKing.x<=7 && targetTileCoordKing.y<=7){
            //get the id of the first possible target tile
            //int key= Board.getIDfromPoint(targetTileCoord);
            //store the target tile
            Tile targetTileKing= board.get(targetTileCoordKing);
            //check if the target tile is empty
            if(targetTileKing.isEmpty())
            {
                //add the coordinates of the legal target tile
                legalMoves.add(targetTileCoordKing);
                legalJump.put("kingWhite", targetTileCoordKing);
            }
            
            //check if the target tile is occupied with the openents piece
            else if (!targetTileKing.isEmpty() && (targetTileKing.getPiece()==Type.regBPiece || targetTileKing.getPiece()==Type.kingBPiece))
            {
                //check if the next legal tile after the tile occupied by the oppenent is empty
                Point newtargetTileCoord= new Point(currentPTile.x-2, currentPTile.y-2);
                if(newtargetTileCoord.x>=0 && newtargetTileCoord.y>=0 && newtargetTileCoord.x<=7 && newtargetTileCoord.y<=7){
                //int newkey= Board.getIDfromPoint(newtargetTileCoord);
                Tile newtargetTile= board.get(newtargetTileCoord);
                if(newtargetTile.isEmpty())
                {
                    //add the coordinates of the legal target tile 
                    legalMoves.add(newtargetTileCoord);
                    legalJump.put("jumpKingWhite", newtargetTileCoord);
                }
                } 
            }}
            
            //get the tile coordinates of the second possible move from the current tile
            Point targetTileCoordKing1= new Point(currentPTile.x-1, currentPTile.y+1);
            if(targetTileCoordKing1.x>=0 && targetTileCoordKing1.y>=0 && targetTileCoordKing1.x<=7 && targetTileCoordKing1.y<=7){
            //int key1= Board.getIDfromPoint(targetTileCoord1);
            Tile targetTileKing1= board.get(targetTileCoordKing1);
            //check if the target tile is empty
            if(targetTileKing1.isEmpty())
            {
                //add the coordinates of the legal target tile
                legalMoves.add(targetTileCoordKing1);
                legalJump.put("kingWhite1", targetTileCoordKing1);
            }
             else if (!targetTileKing1.isEmpty() && (targetTileKing1.getPiece()==Type.regBPiece || targetTileKing1.getPiece()==Type.kingBPiece))
            {
                //check if the next legal tile after the tile occupied by the oppenent is empty
                Point newtargetTileCoord= new Point(currentPTile.x-2, currentPTile.y+2);
                if(newtargetTileCoord.x>=0 && newtargetTileCoord.y>=0 && newtargetTileCoord.x<=7 && newtargetTileCoord.y<=7){
                //int newkey= Board.getIDfromPoint(newtargetTileCoord);
                Tile newtargetTile= board.get(newtargetTileCoord);
                if(newtargetTile.isEmpty())
                {
                    //add the coordinates of the legal target tile
                    legalMoves.add(newtargetTileCoord);
                    legalJump.put("jumpKingWhite1", newtargetTileCoord);
                }
                
            } }
            }
        }}
        
        


        //black pieces legal moves
        else if (pieceType==Type.regBPiece || pieceType== Type.kingBPiece)
        {
            //every regular black piece has 2 possible moves
            //get the tile coordinate of the first possible move from the current tile
            
            Point targetTileCoord= new Point(currentPTile.x-1, currentPTile.y-1);
            if(targetTileCoord.x>=0 && targetTileCoord.y>=0 && targetTileCoord.x<=7 && targetTileCoord.y<=7)  {
            //get the id of the first possible target tile
            //int key= Board.getIDfromPoint(targetTileCoord);
            //store the target tile
            Tile targetTile= board.get(targetTileCoord);
            //check if the target tile is empty
            if(targetTile.isEmpty())
            {
                //add the coordinates of the legal target tile
                legalMoves.add(targetTileCoord);
                legalJump.put("regularBlack", targetTileCoord);
            }
            
            //check if the target tile is occupied with the openents piece
            else if (!targetTile.isEmpty() && (targetTile.getPiece()==Type.regWPiece || targetTile.getPiece()==Type.kingWPiece))
            {
                //check if the next legal tile after the tile occupied by the oppenent is empty
                Point newtargetTileCoord= new Point(currentPTile.x-2, currentPTile.y-2);
                if(newtargetTileCoord.x>=0 && newtargetTileCoord.y>=0 && newtargetTileCoord.x<=7 && newtargetTileCoord.y<=7){
                //int newkey= Board.getIDfromPoint(newtargetTileCoord);
                Tile newtargetTile= board.get(newtargetTileCoord);
                if(newtargetTile.isEmpty())
                {
                    //add the coordinates of the legal target tile 
                    legalMoves.add(newtargetTileCoord);
                    legalJump.put("jumpBlack", newtargetTileCoord);
                }}
                 
            }}
            
            
            //get the tile coordinates of the second possible move from the current tile
            Point targetTileCoord1= new Point(currentPTile.x-1, currentPTile.y+1);
            if(targetTileCoord1.x>=0 && targetTileCoord1.y>=0 && targetTileCoord1.x<=7 && targetTileCoord1.y<=7){
            //int key1= Board.getIDfromPoint(targetTileCoord1);
            Tile targetTile1= board.get(targetTileCoord1);
            //check if the target tile is empty
            if(targetTile1.isEmpty())
            {
                //add the coordinates of the legal target tile
                legalMoves.add(targetTileCoord1);
                legalJump.put("regularBlack1", targetTileCoord1);
            }
            //check if the target tile is occupied with the openents piece
            else if (!targetTile1.isEmpty() && (targetTile1.getPiece()==Type.regWPiece || targetTile1.getPiece()==Type.kingWPiece))
            {
                //check if the next legal tile after the tile occupied by the oppenent is empty
                Point newtargetTileCoord= new Point(currentPTile.x-2, currentPTile.y+2);
                if(newtargetTileCoord.x>=0 && newtargetTileCoord.y>=0 && newtargetTileCoord.x<=7 && newtargetTileCoord.y<=7){
                //int newkey= Board.getIDfromPoint(newtargetTileCoord);
                Tile newtargetTile= board.get(newtargetTileCoord);
                if(newtargetTile.isEmpty())
                {
                    //add the coordinates of the legal target tile
                    legalMoves.add(newtargetTileCoord);
                    legalJump.put("jumpBlack1", newtargetTileCoord);
                }}
                
            } }
            
            if (pieceType==Type.kingBPiece){
             Point targetTileCoordKing= new Point(currentPTile.x+1, currentPTile.y-1);
             if(targetTileCoordKing.x>=0 && targetTileCoordKing.y>=0 && targetTileCoordKing.x<=7 && targetTileCoordKing.y<=7){
            //get the id of the first possible target tile
            
            //int key= Board.getIDfromPoint(targetTileCoord);
            
            //store the target tile
            Tile targetTileKing= board.get(targetTileCoordKing);
            //check if the target tile is empty
            if(targetTileKing.isEmpty())
            {
                //add the coordinates of the legal target tile
                legalMoves.add(targetTileCoordKing);
                legalJump.put("kingBlack", targetTileCoordKing);
            }
            
            //check if the target tile is occupied with the openents piece
            else if (targetTileKing.getPiece()==Type.regWPiece || targetTileKing.getPiece()==Type.kingWPiece)
            {
                //check if the next legal tile after the tile occupied by the oppenent is empty
                Point newtargetTileCoord= new Point(currentPTile.x+2, currentPTile.y-2);
                if(newtargetTileCoord.x>=0 && newtargetTileCoord.y>=0 && newtargetTileCoord.x<=7 && newtargetTileCoord.y<=7){
                //int newkey= Board.getIDfromPoint(newtargetTileCoord);
                Tile newtargetTile= board.get(newtargetTileCoord);
                if(newtargetTile.isEmpty())
                {
                    //add the coordinates of the legal target tile 
                    legalMoves.add(newtargetTileCoord);
                    legalJump.put("jumpKingBlack", newtargetTileCoord);
                }  
            }}}
            
            //get the tile coordinates of the second possible move from the current tile
            Point targetTileCoordKing1= new Point(currentPTile.x+1, currentPTile.y+1);
            if(targetTileCoordKing1.x>=0 && targetTileCoordKing1.y>=0 && targetTileCoordKing1.x<=7 && targetTileCoordKing1.y<=7){
            //int key1= Board.getIDfromPoint(targetTileCoord1);
            Tile targetTileKing1= board.get(targetTileCoordKing1);
            //check if the target tile is empty
            if(targetTileKing1.isEmpty())
            {
                //add the coordinates of the legal target tile 
                legalMoves.add(targetTileCoordKing1);
                legalJump.put("kingBlack1", targetTileCoordKing1);
            }
            else if (targetTileKing1.getPiece()==Type.regWPiece || targetTileKing1.getPiece()==Type.kingWPiece)
            {
                //check if the next legal tile after the tile occupied by the oppenent is empty
                Point newtargetTileCoord= new Point(currentPTile.x+2, currentPTile.y+2);
                if(newtargetTileCoord.x>=0 && newtargetTileCoord.y>=0 && newtargetTileCoord.x<=7 && newtargetTileCoord.y<=7){
                //int newkey= Board.getIDfromPoint(newtargetTileCoord);
                Tile newtargetTile= board.get(newtargetTileCoord);
                if(newtargetTile.isEmpty())
                {
                    //add the coordinates of the legal target tile 
                    legalMoves.add(newtargetTileCoord);
                    legalJump.put("jumpKingBlack1", newtargetTileCoord);
                }
            }}
            }
            } 
        }
        return legalJump;
    }
    
    public static Map<Point, Map<String,Point>> getAllLegalMoves(Map<Point, Tile> board){
        Map<String, Point> legalMoves= new HashMap<>();
        Map<Point, Map<String,Point>> allLegalMoves= new HashMap<>();
        for (Tile t : board.values()){
            if (!t.isEmpty()){
                legalMoves= getAvailableMoves(t.getCoord(), board);
                if(!legalMoves.isEmpty()){
                allLegalMoves.put(t.getCoord(), legalMoves);}
            }
        }
        return allLegalMoves;
    }
    public static Map<Point, Map<String, Point>> getAllBlackMoves(Map<Point, Tile> board){
        Map<String, Point> legalMoves= new HashMap<>();
        Map<Point, Map<String,Point>> allLegalBlackMoves= new HashMap<>();
        for (Tile t : board.values()){
            if (t.getPiece()== Type.kingBPiece || t.getPiece()== Type.regBPiece){
                legalMoves= getAvailableMoves(t.getCoord(), board);
                if(!legalMoves.isEmpty()){
                allLegalBlackMoves.put(t.getCoord(), legalMoves);
            }
            }
        }
        return allLegalBlackMoves;
    }
    public static Map<Point, Map<String, Point>> getAllWhiteMoves(Map<Point, Tile> board){
        Map<String, Point> legalMoves= new HashMap<>();
        Map<Point, Map<String,Point>> allLegalWhiteMoves= new HashMap<>();
        for (Tile t : board.values()){
            if (t.getPiece()== Type.kingWPiece || t.getPiece()== Type.regWPiece){
                legalMoves= getAvailableMoves(t.getCoord(), board);
                if(!legalMoves.isEmpty())
                {
                allLegalWhiteMoves.put(t.getCoord(), legalMoves);}
            }
        }
        return allLegalWhiteMoves;
    }
    
    
    
  
}
