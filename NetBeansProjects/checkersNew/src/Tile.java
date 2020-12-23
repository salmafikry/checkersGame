

import java.awt.Point;
import java.util.Map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author salmafikry
 */
public class Tile 
{
    
    
    private int tileID;
    private boolean occupied;
    //private Piece pieceTile;
    private Point pointTile;
    private Type piece;
    //private static Map<Integer, Point> tileCoord;
    
    public Tile(int tileID, Point pointTile, Type piece, boolean occupied)
    {
        this.tileID= tileID;
        this.occupied= occupied;
        this.pointTile=pointTile;
        this.piece=piece;
    }
    public boolean isEmpty(){
        return !occupied;
    }
    
    public Type getPiece(){
        return piece;
    }
    
    public Point getCoord(){
        return pointTile;
    }
    public int getID(){
        return tileID;
    }
    public void removePiece(){
        piece=null;
        occupied=false;
    }
    public void setPiece(Type newpiece){
        piece=newpiece;
        occupied=true;
    }
    
    public static int getPieceValue(Type piece){
        if (piece==Type.kingBPiece || piece==Type.kingWPiece){
            return 10;
        }
        else if(piece== Type.regBPiece || piece==Type.regWPiece){
            return 5;
        }
        else{
            return 0;
        }
    }
   
    public String tiletoString() 
    {
        return "Tile ID: " + tileID + " occupied: " + occupied + " Piece: " + piece + " Point: " + pointTile;
    }
    
    //public void createTile(int tileID){
       // Tile tile= new Tile(tileID, true);
    //}

}
