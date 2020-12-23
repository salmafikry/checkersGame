
//import java.awt.BorderLayout;
//import java.awt.Dimension;
import javax.swing.*;
import javax.swing.SwingUtilities.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author salmafikry
 */
public class GUIBoard {
    private JFrame gameFrame;
    private BoardPanel boardPanel;
    private Map<Point, Tile> checkersBoard;
    private static final Dimension outerFrame= new Dimension(600,600);
    private static final Dimension tilePanelDimension= new Dimension(10,10);
    private static final Dimension panelDimension= new Dimension(400,350);
    private static String defaultPath= "/Users/salmafikry/Desktop/icons";
    private Tile currentTile;
    private Tile destTile;
    private Type humanPiece;
    public GUIBoard(){
        this.gameFrame= new JFrame("Checkers Board Game");
        this.gameFrame.setLayout(new BorderLayout());
        this.checkersBoard= Board.setinitialBoardCreate();
        JMenuBar boardMenuBar= createMenuBar();
        this.gameFrame.setJMenuBar(boardMenuBar);
        this.gameFrame.setSize(outerFrame);
   
        
        this.boardPanel= new BoardPanel();
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.setVisible(true);
    }
    
    
    private JMenuBar createMenuBar(){
        JMenuBar menuBar= new JMenuBar();
        menuBar.add(createFileMenu());
        return menuBar;
    } 
    
    private JMenu createFileMenu(){
        JMenu fileM= new JMenu("File");
        JMenuItem exitMenu= new JMenuItem("Exit");
        exitMenu.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
               System.exit(0); 
            }
        });
        fileM.add(exitMenu);
        return fileM;
    }
    
    public class BoardPanel extends JPanel {
    java.util.List<TilePanel> boardTiles;
    
    
    BoardPanel(){
        super(new GridLayout(8,8));
        this.boardTiles= new ArrayList<>();
        for (int i=0; i<64; i++){
            TilePanel tile= new TilePanel(this, i);
            this.boardTiles.add(tile);
            add(tile);
        }
        setPreferredSize(panelDimension);
        validate();
    }
    public void drawBoard(Map<Point, Tile> board){
        removeAll();
        for(TilePanel tp: boardTiles){
            tp.drawTile(board);
            add(tp);
            
        }
        validate();
        repaint();
    }
    
    
}

    
    
    
    
    
    class TilePanel extends JPanel {
    private int tileID;
    
    public final java.util.List<Boolean> row1 = initRow(0);
    public final java.util.List<Boolean> row2 = initRow(8);
    public final java.util.List<Boolean> row3 = initRow(16);
    public final java.util.List<Boolean> row4 = initRow(24);
    public final java.util.List<Boolean> row5 = initRow(32);
    public final java.util.List<Boolean> row6 = initRow(40);
    public final java.util.List<Boolean> row7 = initRow(48);
    public final java.util.List<Boolean> row8 = initRow(56);
      TilePanel(BoardPanel boardPanel, int tileID){
        super(new GridBagLayout());
        this.tileID= tileID;
        setPreferredSize(tilePanelDimension);
        assignTileCOlor();
        assignPieces(checkersBoard);
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Clicked");
                if(SwingUtilities.isLeftMouseButton(e)){
                    //for(Point p: checkersBoard.keySet()){
                        //if (checkersBoard.get(p).getID()==tileID)
                        //{
                            //System.out.println(tileID);
                            if(currentTile==null)
                            {
                                for (Point p: checkersBoard.keySet()){
                                if (checkersBoard.get(p).getID()==tileID){
                                //first mouse click that selects the starting tile
                                currentTile= checkersBoard.get(p);
                                //highlightLegalTiles(checkersBoard);
                                humanPiece= currentTile.getPiece();
                                System.out.println(humanPiece);
                                if(humanPiece==null)
                                {
                                    currentTile=null;
                                }
                                }
                                }
                            }
                            else
                            {
                                for (Point p: checkersBoard.keySet()){
                                if (checkersBoard.get(p).getID()==tileID){
                                //second mouse click that selects the destination tile
                                destTile= checkersBoard.get(p);
                                System.out.println("move not made" + destTile.getPiece());
                                Move.makeMove(currentTile.getCoord(), destTile.getCoord(), checkersBoard);
                                System.out.println("move made" + destTile.getPiece());
                                
                            }
                           
                        }
                        
                        //After you make the move reset everything back to null
                            currentTile=null;
                            destTile= null;
                            humanPiece=null; 
                        
                    }
                    SwingUtilities.invokeLater(new Runnable(){ @Override 
                        public void run(){
                            boardPanel.drawBoard(checkersBoard);
                        }
                        });
                }
                else if(SwingUtilities.isRightMouseButton(e)){
                    //right click cancels all selections
                    currentTile=null;
                    destTile=null;
                    humanPiece=null; 
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        validate();
    }
      public void highlightLegalTiles(Map<Point, Tile> board){
          Tile targetTile=null;
          for (Point p: checkersBoard.keySet()){
          if (checkersBoard.get(p).getID()==tileID){
              targetTile= checkersBoard.get(p);
          }}
          
          if(humanPiece!=null){
              
              Map<String, Point> legalMoves= Move.getAvailableMoves(targetTile.getCoord(), board);
              for (Point p : legalMoves.values()){
                  try{
                      //add(new JLabel(new ImageIcon(ImageIO.read(new File(defaultPath + "/legal.jpg")))));
                      BufferedImage icon= ImageIO.read(new File(defaultPath + "/legal.jpg"));
                      ImageIcon imgIcon= new ImageIcon(icon);
                    Image img= imgIcon.getImage();
                    Image newimg = img.getScaledInstance(50, 200,  java.awt.Image.SCALE_SMOOTH);

                    add(new JLabel(new ImageIcon(newimg)));
                  }
                  catch(Exception e){
                      e.printStackTrace();
                  }
              }
          
          }
          
      }
      public void drawTile(Map<Point, Tile> board){
          assignTileCOlor();
          assignPieces(board);
          validate();
          repaint();
      }

    private void assignTileCOlor() {
        if (row1.get(this.tileID) || row3.get(this.tileID) || row5.get(this.tileID) || row7.get(this.tileID) ){
            if (this.tileID % 2==0){
                setBackground(Color.decode("#57623F"));
            }
            else{
                setBackground(Color.decode("#BEBDB8"));
            }
        }
        else if (row2.get(this.tileID) || row4.get(this.tileID) || row6.get(this.tileID) || row8.get(this.tileID) ){
            if (this.tileID % 2==0){
                setBackground(Color.decode("#BEBDB8"));
                
            }
            else{
                setBackground(Color.decode("#57623F"));
            }
        }
        
        
    }
    private void assignPieces(Map<Point, Tile> board){
        this.removeAll();
        for (Point p : board.keySet()){
            if (board.get(p).getID()==this.tileID){
                try{
                    if (board.get(p).getPiece()== Type.regBPiece){
                    BufferedImage icon= ImageIO.read(new File(defaultPath + "/blackPiece.jpg"));
                    
                    ImageIcon imgIcon= new ImageIcon(icon);
                    Image img= imgIcon.getImage();
                    Image newimg = img.getScaledInstance(40, 55,  java.awt.Image.SCALE_SMOOTH);

                    add(new JLabel(new ImageIcon(newimg)));}
                    else if(board.get(p).getPiece()== Type.regWPiece){
                    BufferedImage icon= ImageIO.read(new File(defaultPath + "/token.png"));
                    ImageIcon imgIcon= new ImageIcon(icon);
                    Image img= imgIcon.getImage();
                    Image newimg = img.getScaledInstance(40, 55,  java.awt.Image.SCALE_SMOOTH);

                    add(new JLabel(new ImageIcon(newimg)));
                    }
                    else if(board.get(p).getPiece()== Type.kingBPiece){
                       BufferedImage icon= ImageIO.read(new File(defaultPath + "/KingPieceBlack.png")); 
                       ImageIcon imgIcon= new ImageIcon(icon);
                    Image img= imgIcon.getImage();
                    Image newimg = img.getScaledInstance(40, 55,  java.awt.Image.SCALE_SMOOTH);

                    add(new JLabel(new ImageIcon(newimg)));
                    }
                    else if(board.get(p).getPiece()== Type.kingWPiece){
                       BufferedImage icon= ImageIO.read(new File(defaultPath + "/KingPieceWhite.png")); 
                       ImageIcon imgIcon= new ImageIcon(icon);
                    Image img= imgIcon.getImage();
                    Image newimg = img.getScaledInstance(40, 55,  java.awt.Image.SCALE_SMOOTH);

                    add(new JLabel(new ImageIcon(newimg)));
                    }
                    
                }
                catch(IOException e){
                   e.printStackTrace();
                }
            }
        }
        
    }
    

    private java.util.List<Boolean> initRow(int rowNum) {
        Boolean[] row = new Boolean[64];
        for(int i = 0; i < row.length; i++) {
            row[i] = false;
        }
        do {
            row[rowNum] = true;
            rowNum++;
        } while(rowNum % 8 != 0);
        return Collections.unmodifiableList(Arrays.asList(row));
    }
     
}
    
   
}
