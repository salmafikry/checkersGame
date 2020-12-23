import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.awt.Point;
import static java.lang.Integer.max;
import static java.lang.Integer.min;
import java.util.HashMap;
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
public class Board {
    public static int depthDifficulty;
    public static void displayBoard(Map<Point, Tile> board){
        System.out.print("0  1  2  3  4  5  6  7");
        for (int row=0; row<8; row++){
            int col=0;
            System.out.println("");
            
            while(col<8){
                
                if(board.get(new Point(row,col)).getPiece()==Type.regWPiece){
                System.out.print("RW ");}
                else if(board.get(new Point(row,col)).getPiece()==Type.regBPiece){
                    System.out.print("RB ");
                }
                else if(board.get(new Point(row,col)).getPiece()==Type.kingWPiece){
                    System.out.print("KW ");
                }
                else if(board.get(new Point(row,col)).getPiece()==Type.kingBPiece){
                    System.out.print("KB ");
                }
                else if(board.get(new Point(row,col)).getPiece()== null){
                    System.out.print("-- ");
                }
                col++;
            }
        }
        System.out.println("");
    }

    public static void startGame(){
        
        
        Map<Point, Tile> board= setinitialBoardCreate();
        displayBoard(board);
        System.out.println("Select difficulty level: '1' Easy, '2' Medium, '3' Difficult");
        Scanner inputDiff= new Scanner(System.in);
        String diffLevel= inputDiff.nextLine();
        
        int difficulty= Integer.parseInt(diffLevel);
        if (difficulty==1){
           depthDifficulty=5;
        }
        else if(difficulty==2){
            depthDifficulty=9;
        }
        else if(difficulty==3){
            depthDifficulty=16;
        }
        int player=processUserCommand(getUserCommand());
        
        if(player==1){
            List<Point> move=startMinMax(board, depthDifficulty, PlayerType.AI);
            System.out.println("I think the best move is from " + move.get(0) + " to" + move.get(1));
            List<Boolean> legalandjump=executeAITurn(move, board);
            if(legalandjump.get(1)){
                Move.aiAnotherJump(move.get(1), board);
            }
            displayBoard(board);
            if(!isGameOver(board)){
            takeHumanTurn(board);}
        }
        if (player==2){
            Scanner input = new Scanner(System.in);
            System.out.println("Enter initial coordinate: ");
            String coordinate = input.nextLine();
            String[] parts = coordinate.split(",");
            String x = parts[0].trim().substring(1).trim();
            String y = parts[1].trim().substring(0, parts[1].trim().length() - 1).trim();
            System.out.println("x: " + x + "\ny: " + y);
            int xCoord= Integer.parseInt(x);
            int yCoord= Integer.parseInt(y);
            System.out.println(new Point(xCoord, yCoord));
            Point initial= new Point(xCoord,yCoord);
            
            Scanner input2 = new Scanner(System.in);
        System.out.println("Enter destination coordinate: ");
        String coordinate2 = input2.nextLine();
        String[] parts2 = coordinate2.split(",");
        String x2 = parts2[0].trim().substring(1).trim();
        String y2 = parts2[1].trim().substring(0, parts2[1].trim().length() - 1).trim();
        System.out.println("x: " + x2 + "\ny: " + y2);
        int xCoord2= Integer.parseInt(x2);
        int yCoord2= Integer.parseInt(y2);
        System.out.println(new Point(xCoord2, yCoord2));
        Point destination= new Point(xCoord2,yCoord2);
            
         List<Boolean> legalandjump= Move.makeMove(initial, destination, board);
        if (legalandjump.get(0)){
            if(legalandjump.get(1)){
                Move.makeAnotherJump(destination, board);
            }
        displayBoard(board);
            if(!isGameOver(board)){
            takeAITurn(board, depthDifficulty);}
        }
        else{
            System.out.println("Illegal move, please try another move");
            takeHumanTurn(board);
        }
            
        }
        
    }
    private static void takeAITurn(Map<Point, Tile> board, int depth){
        List<Point> move=startMinMax(board, depth, PlayerType.AI);
        System.out.println("I think the best move is from " + move.get(0) + " to" + move.get(1));
        List<Boolean> legalandjump=executeAITurn(move, board);
        if(legalandjump.get(1)){
            Move.aiAnotherJump(move.get(1), board);
        }
        displayBoard(board);
        if(!isGameOver(board)){
        takeHumanTurn(board);}
    }
    private static void takeHumanTurn(Map<Point, Tile> board){
        
        Scanner input = new Scanner(System.in);
        System.out.println("Enter initial coordinate: ");
        String coordinate = input.nextLine();
        String[] parts = coordinate.split(",");
        String x = parts[0].trim().substring(1).trim();
        String y = parts[1].trim().substring(0, parts[1].trim().length() - 1).trim();
        System.out.println("x: " + x + "\ny: " + y);
        int xCoord= Integer.parseInt(x);
        int yCoord= Integer.parseInt(y);
       
        Point initial= new Point(xCoord,yCoord);
        if(board.get(initial).getPiece()== Type.regWPiece || board.get(initial).getPiece()==Type.kingWPiece){
            System.out.println("Please select a black piece tile.");
            takeHumanTurn(board);
        }
        Scanner input2 = new Scanner(System.in);
        System.out.println("Enter destination coordinate: ");
        String coordinate2 = input2.nextLine();
        String[] parts2 = coordinate2.split(",");
        String x2 = parts2[0].trim().substring(1).trim();
        String y2 = parts2[1].trim().substring(0, parts2[1].trim().length() - 1).trim();
        System.out.println("x: " + x2 + "\ny: " + y2);
        int xCoord2= Integer.parseInt(x2);
        int yCoord2= Integer.parseInt(y2);
        Point destination= new Point(xCoord2,yCoord2);
            
        //Move.makeMove(initial, destination, board);
        //displayBoard(board);
         
        List<Boolean> legalandjump= Move.makeMove(initial, destination, board);
        if (legalandjump.get(0)){
            if(legalandjump.get(1)){
                Move.makeAnotherJump(destination, board);
            }
            
            displayBoard(board);
            if(!isGameOver(board)){
            takeAITurn(board, depthDifficulty);}
        }
        else{
            System.out.println("Illegal move, please try another move");
            takeHumanTurn(board);
        }

    }
    private static String[] getUserCommand() {
        Scanner scanner = new Scanner(System.in);
        String[] output = new String[2];
        System.out.print("Enter '1' for the AI to begin, or '2' to make the first move, or 'q' to quit: ");
        String s = scanner.next();
        if(s.equals("q")) {
            System.out.print("Good Bye!");
            System.exit(0);
        }
        else
            output[0] = s;
        return output;
    }
    private static int processUserCommand(String[] c) {
        try {
            return(Integer.parseInt(c[0]));
        } catch(Exception e) {
            System.out.print("Whatever.");
            System.exit(0);
        } 
        return 0;
    }
    
    //Regular move execution method.
    public static void moveSPiece(Point initial, Point destination, Map<Point, Tile> sBoard){
        //Store the initial Tile content
        Tile initialTile= sBoard.get(initial);
        //Get the piece on the initial tile
        Type piecenew= initialTile.getPiece();
        
        //Remove the piece from the initial tile
        initialTile.removePiece();
        //Update the tile in the board
        sBoard.replace(initial, initialTile);
        
        //Store the destination tile content
        Tile destTile= sBoard.get(destination);
        //If the destination coordinates were on the first row on the opponents side, the piece being moved becomes king
        if(piecenew== Type.regWPiece && destination.x== 7){
            piecenew= Type.kingWPiece;
        }
        else if(piecenew==Type.regBPiece && destination.x==0){
            piecenew=Type.kingBPiece;
        }
        
        //Place the piece on the destination tile
        destTile.setPiece(piecenew);
        //Update the destination tile in the board
        sBoard.replace(destination, destTile);
    }
    
    //Capture move execution method
    public static void jumpmovePiece(Point initial, Point destination, Map<Point, Tile> board)
    {
        //Store the initial tile content
        Tile initialTile= board.get(initial);
        //Get the oiece o the initial tile
        Type piecenew= initialTile.getPiece();
        
        //Remove the piece form the initial tile
        initialTile.removePiece();
        //Update the tile in the board
        board.replace(initial, initialTile);
        
        //Remove the opponent's piece being captured/jumped
        Point removePiecee;
        if (destination.y> initial.y){
            if(destination.x> initial.x){
                removePiecee= new Point(initial.x+1, initial.y +1);
            }
            else{
                removePiecee= new Point(initial.x-1, initial.y+1);
            }
        }
        else{
            if(destination.x> initial.x){
                removePiecee= new Point(initial.x+1, initial.y -1);
            }
            else{
                removePiecee= new Point(initial.x-1, initial.y -1);
            }
        }
        
        //Get the tile with the captured piece
        Tile opponentTile= board.get(removePiecee);
        //Get the piece being captures
        Type opponentPiece= opponentTile.getPiece();
        //Remove the captured piece
        opponentTile.removePiece();
        //Update the board 
        board.replace(removePiecee, opponentTile);
        
        //Get the destination tile
        Tile destTile= board.get(destination);
        
        //If the destination coordinates were on the first row on the opponents side, the piece being moved becomes king
        if(piecenew== Type.regWPiece && destination.x== 7){
            piecenew= Type.kingWPiece;
        }
        else if(piecenew==Type.regBPiece && destination.x==0){
            piecenew=Type.kingBPiece;
        }
        
        //If the captured piece is a king piece, the piece doing the capturing becomes king.
        else if(opponentPiece== Type.kingBPiece){
            piecenew= Type.kingWPiece;
        }
        else if(opponentPiece==Type.kingWPiece){
            piecenew= Type.kingBPiece;
        }
        
        //Set piecde on the destination tile
        destTile.setPiece(piecenew);
        //Update board
        board.replace(destination, destTile);
        
  
   
    }
    
    //Configures the initial checkers board
    public static Map<Point, Tile> setinitialBoardCreate(){
        Map<Point, Tile> board= new HashMap<>(64);
        int countID=-1;
        for (int row=0; row<8; row++)
        {
            if(row<3)
            {
                if (row%2==0)
                {
                    for (int col=0; col<8; col++)
                    {
                        if (col%2==0)
                        {
                            countID++;
                            board.put(new Point(row,col), new Tile(countID, new Point(row,col), null,false));
                        }
                        else
                        {
                            countID++;
                            board.put(new Point(row,col), new Tile(countID, new Point(row,col), Type.regWPiece,true));
                        }
                    }  
                }
                else
                {
                    for(int col=0; col<8; col++)
                    {
                        if (col%2==0)
                        {
                            countID++;
                            board.put(new Point(row,col), new Tile(countID, new Point(row,col), Type.regWPiece,true));
                        }
                        else
                        {
                            countID++;
                            board.put(new Point(row,col), new Tile(countID, new Point(row,col), null,false));
                        }
                    }
                }
            }
            else if (row>4)
            {
                if (row%2==0)
                {
                    for (int col=0; col<8; col++)
                    {
                        if (col%2==0)
                        {
                            countID++;
                            board.put(new Point(row,col), new Tile(countID, new Point(row,col), null,false));
                        }
                        else
                        {
                            countID++;
                            board.put(new Point(row,col), new Tile(countID, new Point(row,col), Type.regBPiece,true));
                        }
                    }
                }
                else
                {
                   for(int col=0; col<8; col++)
                   {
                        if (col%2==0)
                        {
                            countID++;
                            board.put(new Point(row,col), new Tile(countID, new Point(row,col), Type.regBPiece,true));
                        }
                        else
                        {
                            countID++;
                            board.put(new Point(row,col), new Tile(countID, new Point(row,col), null,false));
                        }
                    }
                }
            }
            else 
            {
                for (int col=0; col<8; col++)
                {
                    if (col%2==0)
                    {
                        countID++;
                        board.put(new Point(row,col), new Tile(countID, new Point(row,col), null,false));
                    }
                    else
                    {
                        countID++;
                        board.put(new Point(row,col), new Tile(countID, new Point(row,col), null,false));
                    }
                }
            }
        }
        return board;
        
    }
    
  //Evaluation method for minimax
  public static int evaluation(Map<Point, Tile> board)
  {
        //counts the number of white vs black pieces.
        //more weight is added to the king pieces.
        int AIPieces=0;
        int HumanPieces=0;
        int value;
        for (Tile t : board.values()){
            if (t.getPiece()== Type.kingWPiece || t.getPiece()== Type.regWPiece){
                value=Tile.getPieceValue(t.getPiece());
                
                AIPieces= AIPieces+ value;
            }
            else if(t.getPiece()==Type.regBPiece || t.getPiece()== Type.kingBPiece){
                value=Tile.getPieceValue(t.getPiece());
                HumanPieces= HumanPieces+ value;
            }
        }
        //Heuristic: number of pieces left (number of wbhite pieces(AI pieces)- number of black pieces(Human pieces))the higher the better for the AI.
        int heuristic= AIPieces- HumanPieces;
        return heuristic;
  }

  //Game ends if either players have no more legal moves left to execute
  public static boolean isGameOver(Map<Point, Tile> board){
      boolean endGame;
      endGame = Move.getAllBlackMoves(board).isEmpty() || Move.getAllWhiteMoves(board).isEmpty() || humanWon(board) || aiWon(board);
      return endGame;
  }
  
  public static boolean humanWon(Map<Point, Tile> board){
      //Human wins if there are no AI white pieces left ot if the AI player has no legal moves.
      boolean win;
      for (Tile t: board.values()){
          if (t.getPiece()==Type.kingWPiece || t.getPiece()==Type.regWPiece){
              win=false;
              return win;
          }
      }
      if(Move.getAllWhiteMoves(board).isEmpty()){
          win=true;
          return true;
      }
      win=true;
      return win;
  }
  
  public static boolean aiWon(Map<Point, Tile> board){
      //AI wins if there are no human black pieces left or if the human player has no legal moves.
      boolean win;
      for (Tile t: board.values()){
          if (t.getPiece()==Type.kingBPiece || t.getPiece()==Type.regBPiece){
              win=false;
              return win;
          }
      }
      if(Move.getAllBlackMoves(board).isEmpty()){
          win=true;
          return win;
      }
      win=true;
      return win;
  }
  
  //minimising player method, tries to minimise the evaluation function(heuristic)
  public static int min(Map<Point, Tile> simulatedBoard, int depth){
      
      //simulate moves until the depth level is 0 or the game ends
      if (depth==0 || isGameOver(simulatedBoard)){
          return evaluation(simulatedBoard);
      }
      
      int currentValue;
      //bestValue is set to the worst possible value at first, in the case of the minimising player the worst value will be a very high number
      int bestValue= Integer.MAX_VALUE;
      
      for (Point p : simulatedBoard.keySet()){
              if (simulatedBoard.get(p).getPiece()== Type.regBPiece || simulatedBoard.get(p).getPiece()== Type.kingBPiece){
                  //Try and evaluate all the possible moves at the current level
                  Map<String, Point> legalMoves= Move.getAvailableMoves(p, simulatedBoard);
                  if(!legalMoves.isEmpty()){
                  for (String s : legalMoves.keySet()){
                      List<Boolean> legalandjump=Move.makeMove(p, legalMoves.get(s), simulatedBoard);
                      System.out.println("min board at depth " + depth);
                      displayBoard(simulatedBoard);
                      //check if the move was executed
                      if(legalandjump.get(0)){
                      //corecursive call
                      currentValue= max(simulatedBoard, depth-1);
                      if(currentValue<= bestValue){
                          bestValue= currentValue;
                      }
                      
                  }}
              }
              
              }
          }
      return bestValue;
  }
  
  //maximising player method, tries to maximise the evaluation funtion(heuristic)
  public static int max(Map<Point, Tile> simulatedBoard, int depth){
      if (depth==0 ){
          return evaluation(simulatedBoard);
      }
      //bestValue is set to the worst possible value at first, in the case of the minimising player the worst value will be a very low number
      int bestValue= Integer.MIN_VALUE;
      int currentValue;
      for (Point p : simulatedBoard.keySet()){
              if (simulatedBoard.get(p).getPiece()== Type.regWPiece || simulatedBoard.get(p).getPiece()== Type.kingWPiece){
                  //Try and evaluate all the possible moves at the current level
                  Map<String, Point> legalMoves= Move.getAvailableMoves(p, simulatedBoard);
                  if(!legalMoves.isEmpty()){
                  for (String s : legalMoves.keySet()){
                      List <Boolean> legalandjump= Move.makeMove(p, legalMoves.get(s), simulatedBoard);
                      System.out.println("max board at depth " + depth);
                      displayBoard(simulatedBoard);
                      if (legalandjump.get(0)){
                      //corecursive call
                      currentValue= min(simulatedBoard, depth-1);
                      if(currentValue>= bestValue){
                          bestValue= currentValue;
                      }
                      }
                  }
              }
                  
              }
          }
      return bestValue;
  }
  
  //this method starts the minimasx algorithm, the parameters are the maximum depth level, the maximising player, and the current board
  //It will return the best move achievable for the player who initiated the algorithm
  public static List<Point> startMinMax(Map<Point, Tile> board, int depth, PlayerType player){
      //create a deep copy of the currentboard to test out the simulated moves
      Map<Point,Tile> simulatedBoard= Board.copy(board);
      List<PositionsandScores> successorEvaluations =new ArrayList();
      //variable that will store the best initial point
      Point bestMove=null;
      //variable that will store the best destination point
      Point bestDest=null;
      
      //add the initial and destination points to an array list that will be returned at the end of minimax in order to carry out the move in the actual board
      List<Point> moveCoords = Arrays.asList(new Point[2]);
      
      //bestScore for the max player
      int bestValueMax= Integer.MIN_VALUE;
      
      //bestScore for the min player
      int bestValueMin= Integer.MAX_VALUE;
      
      //variable to store the result of the evaluation function
      int currentValue;
      //stop minimax when depth is 0 or game is over
      if (depth==0 || isGameOver(simulatedBoard)){
          return moveCoords;
      }
      
      
      for (Point p: simulatedBoard.keySet())
      {
          if(player==PlayerType.AI){
              
          if (simulatedBoard.get(p).getPiece()== Type.regWPiece || simulatedBoard.get(p).getPiece()== Type.kingWPiece){
              Map<String, Point> legalMoves= Move.getAvailableMoves(p, simulatedBoard);
              if(!legalMoves.isEmpty()){
              for (String s : legalMoves.keySet()){
                  List <Boolean> legalandjump= Move.makeMove(p, legalMoves.get(s), simulatedBoard);
                  
                  System.out.println("minimax with ai player board at depth " + depth);
                  displayBoard(simulatedBoard);
                  if (legalandjump.get(0)){
                      currentValue= min(simulatedBoard, depth-1);
                  
                  if(currentValue>=bestValueMax){
                      bestValueMax= currentValue;
                      bestMove=p;
                      bestDest=legalMoves.get(s);
                      moveCoords.set(0, bestMove);
                      moveCoords.set(1, bestDest);
                      successorEvaluations.add(new PositionsandScores(bestValueMax, moveCoords));
                  }
                  }
              
          }
          }
                  
          }
          }
          else if(player==PlayerType.human){
           if(simulatedBoard.get(p).getPiece()== Type.regBPiece || simulatedBoard.get(p).getPiece()== Type.kingBPiece){
               Map<String, Point> legalMoves= Move.getAvailableMoves(p, simulatedBoard);
               if(!legalMoves.isEmpty()){
               for (String s : legalMoves.keySet()){
                   List<Boolean> legalandjump=Move.makeMove(p, legalMoves.get(s), simulatedBoard);
                   System.out.println("minimax board with human player at depth " + depth);
                      displayBoard(simulatedBoard);
                   if (legalandjump.get(0)){
                       currentValue= max(simulatedBoard, depth-1);
                   
                   if(currentValue<=bestValueMin){
                       bestValueMin=currentValue;
                       bestMove=p;
                       bestDest=legalMoves.get(s);
                       moveCoords.set(0, bestMove);
                      moveCoords.set(1, bestDest);
                      successorEvaluations.add(new PositionsandScores(bestValueMax, moveCoords));
                   }}
                   
               }
           }
           
           }
          }
      }
      System.out.println(bestDest);
      return moveCoords;
      
  }
  
  //execute the ai move returned by minimax
  public static List<Boolean> executeAITurn(List<Point> move, Map<Point, Tile> board){
      Point initial= move.get(0);
      Point dest= move.get(1);
      List<Boolean> legalandjump=Move.makeMove(initial, dest, board);
      return legalandjump;
  }

//Creates a deep copy of the original board to pass it to Minimax
public static Map<Point, Tile> copy(Map<Point, Tile> original)
{
    Map<Point, Tile> copy = new HashMap<Point, Tile>();
    for (Map.Entry<Point, Tile> entry : original.entrySet())
    {
        Tile tile= entry.getValue();
        int id= tile.getID();
        Point coord= tile.getCoord();
        Type piece= tile.getPiece();
        boolean occ= !tile.isEmpty();
        copy.put(entry.getKey(),new Tile(id, coord, piece, occ));
    }
    return copy;
}
  
          
}
