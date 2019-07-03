import java.awt.Color;
import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import jdk.nashorn.internal.ir.Flags;
import jdk.nashorn.internal.runtime.regexp.joni.EncodingHelper;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class Project001AD {
    static int pause = 0;
    static int count = 0;
    static JFrame app = new JFrame("Project001 Of Algorithm Design");
    static int Screensize = 600;
    static int matrixSize = 8;
    static TextArea txta_Movements = new TextArea();
    static TextArea txta_Heuristics = new TextArea();
    static TextArea txta_points = new TextArea();
    static JButton btn_colorAndpoints = new JButton("Show Color and Points");
    static JButton btn_start = new JButton("Search for Goal");
    
    public static void main(String[] args) {
        
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setSize(Screensize + 5, Screensize + 80);
        app.setResizable(false);
        app.setLayout(null);
        
//define matrix:
        int matrix[][] = new int[matrixSize][matrixSize];
//Random Elements For -1000:
        matrix = createRandomelements(matrix, -1000, 5, matrixSize);
//Random Elements For -100:       
        matrix = createRandomelements(matrix, -100, 10, matrixSize);
//Random Elements For -10:
        matrix = createRandomelements(matrix, -10, 20, matrixSize);
//Random Element For 10000:
        matrix = createRandomelements(matrix, 10000, 1, matrixSize);        
//define Button matrix:
        JButton[][] btn_matrix = new JButton[matrixSize][matrixSize];
        btn_matrix = defineBtnMatrix(btn_matrix, matrixSize);
//define start Button:        
        btn_start.setFont(new Font("Tahoma", Font.BOLD, 13));
        btn_start.setForeground(Color.black);
        btn_start.setBounds(0, Screensize, Screensize/2, 50);
        app.add(btn_start);
        
        
//temp btn_matrix, temp matrix
        JButton[][] temp_btn_matrix;
        temp_btn_matrix = btn_matrix;
        int[][] temp_matrix;
        temp_matrix = matrix;
   
        btn_colorAndpoints.setFont(new Font("Tahoma", Font.BOLD, 13));
        btn_colorAndpoints.setForeground(Color.black);
        btn_colorAndpoints.setBounds(Screensize/2, Screensize, Screensize/2, 50);
        app.add(btn_colorAndpoints);
        btn_colorAndpoints.addActionListener(e->{
            show_ColorAndPoints(temp_matrix); 
        });
        
        btn_start.addActionListener(e->{
            searchMethod(temp_matrix, temp_btn_matrix, 0, 0, matrixSize);
        });
        for(int i = 0; i<matrixSize; i++){
            for(int j = 0; j<matrixSize; j++){
                int row = i;
                int col = j;
                btn_matrix[i][j].addActionListener(e->{
                    searchMethod(temp_matrix, temp_btn_matrix, row, col, matrixSize);
                });
            }
        }
    
        app.setVisible(true);  
    }
//Create Random Elements
    public static int[][] createRandomelements(int[][] matrix, int value, int n, int matrixSize){
        for(double i = 1; i<=matrixSize*matrixSize*n/64; i++){
            Random rnd = new Random();
            int rnd1 = rnd.nextInt(matrixSize);
            int rnd2 = rnd.nextInt(matrixSize);
            
            if(matrix[rnd1][rnd2]==0 && rnd1!=0 && rnd2!=0){
                
                matrix[rnd1][rnd2]=value;
            }
            else{
                
                while(matrix[rnd1][rnd2]!=0 || (rnd1==0 && rnd2==0)){
                    rnd1 = rnd.nextInt(matrixSize);
                    rnd2 = rnd.nextInt(matrixSize);
                }
                
                matrix[rnd1][rnd2]=value;
            }
        }
        
        return matrix;
    }    
    
//Create btn_matrix:
    public static JButton[][] defineBtnMatrix(JButton[][] btn_matrix, int matrixSize){
        for(int i = 0; i<matrixSize; i++){
            for(int j = 0; j<matrixSize; j++){
               
                btn_matrix[i][j] = new JButton();
                if(i == 0 && j ==0){
                    btn_matrix[i][j].setText("Start");
                }
                btn_matrix[i][j].setBackground(Color.CYAN);
                btn_matrix[i][j].setBounds(j*Screensize/matrixSize, i*Screensize/matrixSize, Screensize/matrixSize, Screensize/matrixSize);
                app.add(btn_matrix[i][j]);
            }   
        }
        
        
        return btn_matrix;
    }

//SearchMethod:
    public static void searchMethod(int[][] matrix, JButton[][] btn_matrix, int row, int col, int matrixSize){
        
        //Resize Of JFrame:
        app.setSize(Screensize + 200, Screensize + 80);
        
        //txtArea OF Movements:
        txta_Movements.setBounds(Screensize, 0, 200, Screensize/2+40);
        txta_Movements.setEditable(false);
        txta_Movements.setFont(new Font("Tahoma", Font.BOLD, 15));
        txta_Movements.setForeground(Color.BLUE);
        txta_Movements.setText("       [MOVEMENTS]");
        txta_Movements.setBackground(Color.white);
        app.add(txta_Movements);
        
        /*
        //txtArea Of Heuristics Of Agent:
        txta_Heuristics.setBounds(Screensize, 215, 250, 215);
        txta_Heuristics.setEditable(false);
        txta_Heuristics.setText("\t[HEURISTICS OF AGENT]");
        txta_Heuristics.setBackground(Color.white);
        app.add(txta_Heuristics);
        */
        
        //txtArea Of Points:
        txta_points.setBounds(Screensize, Screensize/2+40, 200, Screensize/2+10);
        txta_points.setEditable(false);
        txta_points.setFont(new Font("Tahoma", Font.BOLD, 15));
        txta_points.setForeground(Color.red);
        txta_points.setText("\t[POINTS]");
        txta_points.setBackground(Color.white);
        app.add(txta_points);
        int cost=0;
        
        //heuristics of value :
        int[][] heuristic_of_value = new int[matrixSize][matrixSize];
        for(int i = 0; i<matrixSize; i++){
            for(int j= 0; j<matrixSize; j++){
                heuristic_of_value[i][j] = -1;
            }
        }
        //heuristics of Colors:
        String[][] heuristic_of_colors = new String[matrixSize][matrixSize];
        for(int i = 0; i<matrixSize; i++){
            for(int j= 0; j<matrixSize; j++){
                heuristic_of_colors[i][j] = "_";
            }
        }
        
        //Move Matrix for showing end result by animation:
        int[][] MoveMatrix = new int[100000][2];
        for(int i = 0; i<100000; i++){
            for(int j = 0; j<2; j++){
                MoveMatrix[i][j] = -1;
            }
        }
        
        //Move Matrix for showing end result by animation:
        int[] costMatrix = new int[100000];
        for(int i = 0; i<100000; i++){
                costMatrix[i] = 0;
        }
        //Move Matrix for showing end result by animation:
        String[][] colorMatrix = new String[matrixSize][matrixSize];
        for(int i = 0; i<matrixSize; i++){
            for(int j = 0; j<matrixSize; j++){
                colorMatrix[i][j] = "";
            }
        }
        
     
//Search Algorithm:
        int row_of_this_step = row;
        int col_of_this_step = col;
        
if(matrix[row][col]==10000){
            MoveMatrix[0][0]=row;
            MoveMatrix[0][1]=col;
        }
else{
        //First move:[0:Up, 1:Down, 2:Right, 3:Left]
        Random rnd_move = new Random();
        String[] nMove_move_points = aroundPoints(row, col, matrix);
        String Move = nMove_move_points[1];
           
        ArrayList<Integer> rnd_new_move = new ArrayList();
        if(Move.contains("Up")){
            rnd_new_move.add(1);
        }
        if(Move.contains("Down")){
            rnd_new_move.add(2);
        }
        if(Move.contains("Left")){
            rnd_new_move.add(3);
        }
        if(Move.contains("Right")){
            rnd_new_move.add(4);
        }
        
        int index = rnd_move.nextInt(rnd_new_move.size());
        if(rnd_new_move.get(index) == 1){
            row = row-1;
            col = col;
        }
        if(rnd_new_move.get(index)==2){
            row = row+1;
            col = col;
        }
        if(rnd_new_move.get(index)==3){
            row = row;
            col = col-1;
        }
        if(rnd_new_move.get(index)==4){
            row = row;
            col = col+1;
        }
        int old_row = row_of_this_step;
        int old_col = col_of_this_step;
        heuristic_of_value[old_row][old_col] = matrix[old_row][old_col]; 
        heuristic_of_colors[old_row][old_col]= aroundPoints(old_row, old_col, matrix)[2];
        colorMatrix[0][0]=heuristic_of_colors[old_row][old_col];
        heuristic_of_value[row][col] = matrix[row][col]; 
        heuristic_of_colors[row][col]= aroundPoints(row, col, matrix)[2];
        colorMatrix[0][1]=heuristic_of_colors[row][col];
        MoveMatrix[0][0] = old_row;
        MoveMatrix[0][1] = old_col;
        MoveMatrix[1][0] = row;
        MoveMatrix[1][1] = col;
        
        
        cost=cost-50;
        cost=cost + heuristic_of_value[row][col];
        System.out.println("Cost:"+cost);
        costMatrix[1] = cost;
        
        int check_back[][] = new int[matrixSize][matrixSize];
        for(int i = 0; i<matrixSize; i++){
            for(int j = 0; j<matrixSize; j++){
                check_back[i][j]=-1;
            }
        }
        
        int check_visit[][] = new int[matrixSize][matrixSize];
        for(int i = 0; i<matrixSize; i++){
            for(int j = 0; j<matrixSize; j++){
                check_visit[i][j]=0;
            }
        }
        
        int step = 2;
        while(heuristic_of_colors[row][col].contains("Y") != true && matrix[row][col]!=10000){    
            
            row_of_this_step = row;
            col_of_this_step = col;
            heuristic_of_value = think(heuristic_of_value, heuristic_of_colors);
                
            
            String[] nmove_moves_points = aroundPoints(row, col, matrix);
            String nmove = nmove_moves_points[0];
            String moves = nmove_moves_points[1];
            String points = nmove_moves_points[2];
            int know_of_around = 0;
            int Up = -1, Down = -1, Right = -1, Left = -1;
            int Up_is_old_row = -1, Down_is_old_row = -1, Left_is_old_col = -1, Right_is_old_col = -1;
            Random rnd = new Random();
            
            ArrayList <Integer> next_row_col = new ArrayList();
            ArrayList <String> list_of_move = new ArrayList();
            ArrayList <String> list_of_point_than_old = new ArrayList();
            
            for(int i = 0; i<points.length(); i++){
                list_of_point_than_old.add(String.valueOf(points.charAt(i)));
            }
            if(matrix[old_row][old_col] == -10){
                list_of_point_than_old.remove("B");
            }
            else if(matrix[old_row][old_col] == -100){
                
                list_of_point_than_old.remove("O");
            }
            else if(matrix[old_row][old_col] == -1000){
                
                list_of_point_than_old.remove("R");
            }
            
            ArrayList<String> tmp_lMove = new ArrayList();
            
            if(moves.contains("Up")){
                list_of_move.add("Up");
                tmp_lMove.add("Up");
                if(heuristic_of_value[row-1][col]!=-1){
                    know_of_around++;
                }
                Up = row-1;
                if(old_row == Up){
                    list_of_move.remove("Up");
                    Up_is_old_row = 1;
                }
            }
            if(moves.contains("Down")){
                list_of_move.add("Down");
                tmp_lMove.add("Down");
                if(heuristic_of_value[row+1][col]!=-1){
                    know_of_around++;
                }
                Down = row+1;
                if(old_row == Down){
                    Down_is_old_row = 1;
                    list_of_move.remove("Down");
                }
            }
            if(moves.contains("Right")){
                list_of_move.add("Right");
                tmp_lMove.add("Right");
                if(heuristic_of_value[row][col+1]!=-1){
                    know_of_around++;
                }
                Right = col+1;
                if(old_col == Right){
                    Right_is_old_col = 1;
                    list_of_move.remove("Right");
                }
            }
            if(moves.contains("Left")){
                list_of_move.add("Left");
                tmp_lMove.add("Left");
                if(heuristic_of_value[row][col-1]!=-1){
                    know_of_around++;
                }
                Left = col-1;
                if(old_col == Left){
                    Left_is_old_col = 1;
                    list_of_move.remove("Left");
                }
            }
            
            //System.out.println("Up:"+Up+"dowm:"+Down+"Right:"+Right+"left:"+Left);
            
            System.out.println("size of list of move:"+list_of_move.size());
            System.out.println(list_of_move);
            //old = 0 or -10:
            if(matrix[old_row][old_col]==0||matrix[old_row][old_col]==-10){    
                //around nmove:2
                if(nmove.equals("2")){
                    
                    if(points.contains("O")||points.contains("R")){
                        if(check_back[row][col]==-1){
                            check_back[row][col]=1;
                            row = old_row;
                            col = old_col;
                        }
                        else{
                            if(list_of_move.get(0).equals("Up")){
                                row = Up;
                            }
                            if(list_of_move.get(0).equals("Down")){
                                row = Down;
                            }
                            if(list_of_move.get(0).equals("Right")){
                                col = Right;
                            }
                            if(list_of_move.get(0).equals("Left")){
                                col = Left;
                            }
                        }
                    }
                    else{
                        if(list_of_move.get(0).equals("Up")){
                            row = Up;
                        }
                        if(list_of_move.get(0).equals("Down")){
                            row = Down;
                        }
                        if(list_of_move.get(0).equals("Right")){
                            col = Right;
                        }
                        if(list_of_move.get(0).equals("Left")){
                            col = Left;
                        }
                    }
                }
                //around nmove:3
                else if(nmove.equals("3")){
                    //knowknow_of_around:1
                    if(know_of_around==1){
                        if(points.contains("O") || points.contains("R")){
                            if(check_back[row][col]==-1){
                                check_back[row][col]=1;
                                row = old_row;
                                col = old_col;
                            }
                            else{
                                next_row_col = rnd_walk(list_of_move, row_of_this_step, col_of_this_step, Up, Down, Right, Left);
                                row = next_row_col.get(0);
                                col = next_row_col.get(1);
                                
                            }
                        }
                        else{
                            next_row_col = rnd_walk(list_of_move, row_of_this_step, col_of_this_step, Up, Down, Right, Left);
                            row = next_row_col.get(0);
                            col = next_row_col.get(1);
                        }
                    }
                    //know_of_around:3
                    else if(know_of_around==3){

                        int danger=0;
                        for(int i = 0; i<list_of_point_than_old.size(); i++){
                            if(list_of_point_than_old.get(i).contains("R")||list_of_point_than_old.get(i).contains("O")){
                                danger++;
                            }
                        }
                        
                        if(danger == 2){
                            if(check_back[row][col]==-1){
                                
                                check_back[row][col]=1;
                                row = old_row;
                                col = old_col;
                            }
                            else{                                
                                next_row_col = rnd_walk(list_of_move, row_of_this_step, col_of_this_step, Up, Down, Right, Left);
                                row = next_row_col.get(0);
                                col = next_row_col.get(1);
                            }
                        }
                        else if(danger == 1){
                            if(Up!=-1){
                               if(heuristic_of_value[Up][col]<=-100){
                                   list_of_move.remove("Up");
                               } 
                            }
                            if(Down!=-1){
                               if(heuristic_of_value[Down][col]<=-100){
                                   list_of_move.remove("Down");
                               } 
                            }
                            if(Left!=-1){
                               if(heuristic_of_value[row][Left]<=-100){
                                   list_of_move.remove("Left");
                               } 
                            }
                            if(Right!=-1){
                               if(heuristic_of_value[row][Right]<=-100){
                                   list_of_move.remove("Rigth");
                               } 
                            }
                            
                            if(list_of_move.get(0).equals("Up")){
                                row = Up;
                            }
                            if(list_of_move.get(0).equals("Down")){
                                row = Down;
                            }
                            if(list_of_move.get(0).equals("Right")){
                                col = Right;
                            }
                            if(list_of_move.get(0).equals("Left")){
                                col = Left;
                            }
                        }
                        else if(danger == 0){
                            next_row_col = rnd_walk(list_of_move, row_of_this_step, col_of_this_step, Up, Down, Right, Left);
                            row = next_row_col.get(0);
                            col = next_row_col.get(1);
                        }
                    }
                }
                
                //around nmove:4
                else if(nmove.equals("4")){
                    //know_of_around:1
                    if(know_of_around==1){
                        
                        if(points.contains("O") || points.contains("R")){
                            if(check_back[row][col]==-1){    
                                check_back[row][col]=1;
                                row = old_row;
                                col = old_col;
                            }
                            else{
                                next_row_col = rnd_walk(list_of_move, row_of_this_step, col_of_this_step, Up, Down, Right, Left);
                                row = next_row_col.get(0);
                                col = next_row_col.get(1);
                            }
                        }
                        else{
                            next_row_col = rnd_walk(list_of_move, row_of_this_step, col_of_this_step, Up, Down, Right, Left);
                            row = next_row_col.get(0);
                            col = next_row_col.get(1);
                        }
                    }
                    //know_of_around:2
                    if(know_of_around==2){
                        int danger=0;
                        int hamsayeh_danger=0;
                        
                        if(Up!=-1)
                        if(heuristic_of_value[Up][col]!=-1 && Up!=old_row){
                            if(heuristic_of_value[Up][col]==-10){
                                list_of_point_than_old.remove("B");
                            }
                            if(heuristic_of_value[Up][col]==-100){
                                list_of_point_than_old.remove("O");
                                hamsayeh_danger++;
                            }
                            if(heuristic_of_value[Up][col]==-1000){
                                list_of_point_than_old.remove("R");
                                hamsayeh_danger++;
                            }
                        }
                        if(Down!=-1)
                        if(heuristic_of_value[Down][col]!=-1 && Down!=old_row){
                            if(heuristic_of_value[Down][col]==-10){
                                list_of_point_than_old.remove("B");
                            }
                            if(heuristic_of_value[Down][col]==-100){
                                list_of_point_than_old.remove("O");
                                hamsayeh_danger++;
                            }
                            if(heuristic_of_value[Down][col]==-1000){
                                list_of_point_than_old.remove("R");
                                hamsayeh_danger++;
                            }
                        }
                        if(Right!=-1)
                        if(heuristic_of_value[row][Right]!=-1 && Right!=old_col){
                            if(heuristic_of_value[row][Right]==-10){
                                list_of_point_than_old.remove("B");
                            }
                            if(heuristic_of_value[row][Right]==-100){
                                list_of_point_than_old.remove("O");
                                hamsayeh_danger++;
                            }
                            if(heuristic_of_value[row][Right]==-1000){
                                list_of_point_than_old.remove("R");
                                hamsayeh_danger++;
                            }
                        }
                        if(Left!=-1)
                        if(heuristic_of_value[row][Left]!=-1 && Left!=old_col){
                            if(heuristic_of_value[row][Left]==-10){
                                list_of_point_than_old.remove("B");
                            }
                            if(heuristic_of_value[row][Left]==-100){
                                list_of_point_than_old.remove("O");
                                hamsayeh_danger++;
                            }
                            if(heuristic_of_value[row][Left]==-1000){
                                list_of_point_than_old.remove("R");
                                hamsayeh_danger++;
                            }
                        }
                        
                        for(int i = 0; i<list_of_point_than_old.size(); i++){
                            if(list_of_point_than_old.get(i).equals("R")||list_of_point_than_old.get(i).equals("O")){
                                danger++;
                            }
                        }
                        
                        if(danger==2 || danger==1){
                            if(hamsayeh_danger==1){
                                if(check_back[row][col]==-1){
                                    check_back[row][col]=1;
                                    row = old_row;
                                    col = old_col;
                                }
                                // random boro be oon 2 khooneie ke nemidoni va ya oon khonie ke midoni boro ta too loop gir nakone
                                else{
                                    next_row_col = rnd_walk(list_of_move, row_of_this_step, col_of_this_step, Up, Down, Right, Left);
                                    row = next_row_col.get(0);
                                    col = next_row_col.get(1);
                                }
                            }
                            else if(hamsayeh_danger==0){
                                if(Up!=-1)
                                if(heuristic_of_value[Up][col_of_this_step]!=-1 && Up!=old_row){
                                    row = Up;
                                }
                                if(Down!=-1)
                                if(heuristic_of_value[Down][col_of_this_step]!=-1 && Down!=old_row){
                                    row = Down;
                                }
                                if(Right!=-1)
                                if(heuristic_of_value[row_of_this_step][Right]!=-1 && Right!=old_col){
                                    col = Right;
                                }
                                if(Left!=-1)
                                if(heuristic_of_value[row_of_this_step][Left]!=-1 && Left!=old_col){
                                    col = Left;
                                }
                            }
                        }
                        else if(danger==0){
                            if(Up!=-1)
                            if(heuristic_of_value[Up][col_of_this_step]!=-1&&Up!=old_row){
                                list_of_move.remove("Up");
                            }
                            if(Down!=-1)
                            if(heuristic_of_value[Down][col_of_this_step]!=-1&&Down!=old_row){
                                list_of_move.remove("Down");
                            }
                            if(Right!=-1)
                            if(heuristic_of_value[row_of_this_step][Right]!=-1&&Right!=old_col){
                                list_of_move.remove("Right");
                            }
                            if(Left!=-1)
                            if(heuristic_of_value[row_of_this_step][Left]!=-1&&Left!=old_col){
                                list_of_move.remove("Left");
                            }
                   
                            next_row_col = rnd_walk(list_of_move, row_of_this_step, col_of_this_step, Up, Down, Right, Left);
                            row = next_row_col.get(0);
                            col = next_row_col.get(1);
                        }
                    }
                    //know_of_around:4
                    else if(know_of_around==4){
                        int danger = 0;
                        for(int i = 0; i<list_of_point_than_old.size(); i++){
                            if(list_of_point_than_old.get(i).equals("O")||list_of_point_than_old.get(i).equals("R")){
                                danger++;
                            }
                        }
                        
                        if(danger==3){
                            if(check_back[row][col]==-1){
                                check_back[row][col]=1;
                                row = old_row;
                                col = old_col;
                            }
                            else{
                                next_row_col = rnd_walk(list_of_move, row_of_this_step, col_of_this_step, Up, Down, Right, Left);
                                row = next_row_col.get(0);
                                col = next_row_col.get(1);
                            }
                        }
                        else if(danger==2){
                            if(Up!=-1){
                                if(heuristic_of_value[Up][col]<=-100){
                                   list_of_move.remove("Up");
                                }
                            }
                            if(Down!=-1){
                               if(heuristic_of_value[Down][col]<=-100){
                                   list_of_move.remove("Down");
                               } 
                            }
                            if(Left!=-1){
                               if(heuristic_of_value[row][Left]<=-100){
                                   list_of_move.remove("Left");
                               } 
                            }
                            if(Right!=-1){
                               if(heuristic_of_value[row][Right]<=-100){
                                   list_of_move.remove("Rigth");
                               } 
                            }
                 
                            if(list_of_move.get(0).equals("Up")){
                                row = Up;
                            }
                            else if(list_of_move.get(0).equals("Down")){
                                row = Down;
                            }
                            else if(list_of_move.get(0).equals("Right")){
                                col = Right;
                            }
                            else if(list_of_move.get(0).equals("Left")){
                                col = Left;
                            }
                        }
                        else if(danger==1){
                            
                            if(Up!=-1){
                                if(heuristic_of_value[Up][col]<=-100){
                                   list_of_move.remove("Up");
                                }
                            }
                            if(Down!=-1){
                               if(heuristic_of_value[Down][col]<=-100){
                                   list_of_move.remove("Down");
                               } 
                            }
                            if(Left!=-1){
                               if(heuristic_of_value[row][Left]<=-100){
                                   list_of_move.remove("Left");
                               } 
                            }
                            if(Right!=-1){
                               if(heuristic_of_value[row][Right]<=-100){
                                   list_of_move.remove("Rigth");
                               } 
                            }
                            next_row_col = rnd_walk(list_of_move, row_of_this_step, col_of_this_step, Up, Down, Right, Left);
                            row = next_row_col.get(0);
                            col = next_row_col.get(1);
                        }
                        else if(danger==0){
                            next_row_col = rnd_walk(list_of_move, row_of_this_step, col_of_this_step, Up, Down, Right, Left);
                            row = next_row_col.get(0);
                            col = next_row_col.get(1);
                        }
                    }
                }
            }
            
            //[old = -100]
            else if(matrix[old_row][old_col]==-100){
                if(nmove.equals("2")){
                    
                    if(heuristic_of_colors[row][col].contains("R")){
                        if(check_back[row][col]==-1){
                            check_back[row][col]=1;
                            row = old_row;
                            col = old_col;
                        }
                        else{
                            if(list_of_move.get(0).equals("Up")){
                                row = Up;
                            }
                            else if(list_of_move.get(0).equals("Down")){
                                row = Down;
                            }
                            else if(list_of_move.get(0).equals("Right")){
                                col = Right;
                            }
                            else if(list_of_move.get(0).equals("Left")){
                                col = Left;
                            }
                        }
                    }
                    else{
                        if(list_of_move.get(0).equals("Up")){
                            row = Up;
                        }
                        if(list_of_move.get(0).equals("Down")){
                            row = Down;
                        }
                        if(list_of_move.get(0).equals("Right")){
                            col = Right;
                        }
                        if(list_of_move.get(0).equals("Left")){
                            col = Left;
                        }
                    }
                    
                }
                
                else if(nmove.equals("3")){
                    //know_of_around:1
                    if(know_of_around==1){
                        
                        if(points.contains("R")){
                            if(check_back[row][col]==-1){
                                check_back[row][col]=1;
                                row = old_row;
                                col = old_col;
                            }
                            else{
                                next_row_col = rnd_walk(list_of_move, row_of_this_step, col_of_this_step, Up, Down, Right, Left);
                                row = next_row_col.get(0);
                                col = next_row_col.get(1);
                            }
                        }
                        else{
                            next_row_col = rnd_walk(list_of_move, row_of_this_step, col_of_this_step, Up, Down, Right, Left);
                            row = next_row_col.get(0);
                            col = next_row_col.get(1);
                        }
                    }
                    
                    ////know_of_around:3
                    else if(know_of_around==3){
                        
                        int danger=0;
                        for(int i = 0; i<list_of_point_than_old.size(); i++){
                            if(list_of_point_than_old.get(i).contains("R")){
                                danger++;
                            }
                        }
                        
                        if(danger == 2){
                            if(check_back[row][col]==-1){
                                check_back[row][col]=1;
                                row = old_row;
                                col = old_col;
                            }
                            else{
                                next_row_col = rnd_walk(list_of_move, row_of_this_step, col_of_this_step, Up, Down, Right, Left);
                                row = next_row_col.get(0);
                                col = next_row_col.get(1);
                            }
                        }
                        else if(danger == 1){
                            if(Up!=-1){
                               if(heuristic_of_value[Up][col]==-1000){
                                   list_of_move.remove("Up");
                               } 
                            }
                            if(Down!=-1){
                               if(heuristic_of_value[Down][col]==-1000){
                                   list_of_move.remove("Down");
                               } 
                            }
                            if(Left!=-1){
                               if(heuristic_of_value[row][Left]==-1000){
                                   list_of_move.remove("Left");
                               } 
                            }
                            if(Right!=-1){
                               if(heuristic_of_value[row][Right]==-1000){
                                   list_of_move.remove("Rigth");
                               } 
                            }
                            
                            if(list_of_move.get(0).equals("Up")){
                                row = Up;
                            }
                            if(list_of_move.get(0).equals("Down")){
                                row = Down;
                            }
                            if(list_of_move.get(0).equals("Right")){
                                col = Right;
                            }
                            if(list_of_move.get(0).equals("Left")){
                                col = Left;
                            }
                        }
                        else if(danger == 0){
                            next_row_col = rnd_walk(list_of_move, row_of_this_step, col_of_this_step, Up, Down, Right, Left);
                            row = next_row_col.get(0);
                            col = next_row_col.get(1);
                        }
                    }
                }
                
                else if(nmove.equals("4")){
                    //know_of_around:1
                    if(know_of_around==1){
                        
                        if(heuristic_of_colors[row][col].contains("R")){
                            if(check_back[row][col]==-1){
                                check_back[row][col]=1;
                                row = old_row;
                                col = old_col;
                            }
                            else{
                                next_row_col = rnd_walk(list_of_move, row_of_this_step, col_of_this_step, Up, Down, Right, Left);
                                row = next_row_col.get(0);
                                col = next_row_col.get(1);
                            }
                        }
                        else{
                            next_row_col = rnd_walk(list_of_move, row_of_this_step, col_of_this_step, Up, Down, Right, Left);
                            row = next_row_col.get(0);
                            col = next_row_col.get(1);
                        }
                    }
                    
                    //know_of_around:2
                    else if(know_of_around==2){
                        
                        int danger=0;
                        int hamsayeh_danger=0;
                        if(Up!=-1)
                        if(heuristic_of_value[Up][col]!=-1 && Up!=old_row){
                            if(heuristic_of_value[Up][col]==-10){
                                list_of_point_than_old.remove("B");
                            }
                            if(heuristic_of_value[Up][col]==-100){
                                list_of_point_than_old.remove("O");
                            }
                            if(heuristic_of_value[Up][col]==-1000){
                                list_of_point_than_old.remove("R");
                                hamsayeh_danger++;
                            }
                        }
                        if(Down!=-1)
                        if(heuristic_of_value[Down][col]!=-1 && Down!=old_row){
                            if(heuristic_of_value[Down][col]==-10){
                                list_of_point_than_old.remove("B");
                            }
                            if(heuristic_of_value[Down][col]==-100){
                                list_of_point_than_old.remove("O");
                            }
                            if(heuristic_of_value[Down][col]==-1000){
                                list_of_point_than_old.remove("R");
                                hamsayeh_danger++;
                            }
                        }
                        if(Right!=-1)
                        if(heuristic_of_value[row][Right]!=-1 && Right!=old_col){
                            if(heuristic_of_value[row][Right]==-10){
                                list_of_point_than_old.remove("B");
                            }
                            if(heuristic_of_value[row][Right]==-100){
                                list_of_point_than_old.remove("O");
                            }
                            if(heuristic_of_value[row][Right]==-1000){
                                list_of_point_than_old.remove("R");
                                hamsayeh_danger++;
                            }
                        }
                        if(Left!=-1)
                        if(heuristic_of_value[row][Left]!=-1 && Left!=old_col){
                            if(heuristic_of_value[row][Left]==-10){
                                list_of_point_than_old.remove("B");
                            }
                            if(heuristic_of_value[row][Left]==-100){
                                list_of_point_than_old.remove("O");
                            }
                            if(heuristic_of_value[row][Left]==-1000){
                                list_of_point_than_old.remove("R");
                                hamsayeh_danger++;
                            }
                        }
                        
                        for(int i = 0; i<list_of_point_than_old.size(); i++){
                            if(list_of_point_than_old.get(i).equals("R")){
                                danger++;
                            }
                        }
                        
                        if(danger==2 || danger==1){
                            if(hamsayeh_danger==1){
                                if(check_back[row][col]==-1){
                                    check_back[row][col]=1;
                                    row = old_row;
                                    col = old_col;
                                }
                                // random boro be oon 2 khooneie ke nemidoni va ya oon khonie ke midoni boro ta too loop gir nakone
                                else{
                                    next_row_col = rnd_walk(list_of_move, row_of_this_step, col_of_this_step, Up, Down, Right, Left);
                                    row = next_row_col.get(0);
                                    col = next_row_col.get(1);
                                }
                            }
                            else if(hamsayeh_danger==0){
                                if(Up!=-1)
                                if(heuristic_of_value[Up][col_of_this_step]!=-1 && Up!=old_row){
                                    row = Up;
                                }
                                if(Down!=-1)
                                if(heuristic_of_value[Down][col_of_this_step]!=-1 && Down!=old_row){
                                    row = Down;
                                }
                                if(Right!=-1)
                                if(heuristic_of_value[row_of_this_step][Right]!=-1 && Right!=old_col){
                                    col = Right;
                                }
                                if(Left!=-1)
                                if(heuristic_of_value[row_of_this_step][Left]!=-1 && Left!=old_col){
                                    col = Left;
                                }
                            }
                        }
                        else if(danger==0){
                            
                            if(Up!=-1)
                            if(heuristic_of_value[Up][col]!=-1&&Up!=old_row){
                                list_of_move.remove("Up");
                            }
                            if(Down!=-1)
                            if(heuristic_of_value[Down][col]!=-1&&Down!=old_row){
                                list_of_move.remove("Down");
                            }
                            if(Right!=-1)
                            if(heuristic_of_value[row][Right]!=-1&&Right!=old_col){
                                list_of_move.remove("Right");
                            }
                            if(Left!=-1)
                            if(heuristic_of_value[row][Left]!=-1&&Left!=old_col){
                                list_of_move.remove("Left");
                            }
                   
                            next_row_col = rnd_walk(list_of_move, row_of_this_step, col_of_this_step, Up, Down, Right, Left);
                            row = next_row_col.get(0);
                            col = next_row_col.get(1);
                        }
                    }
                    
                    //know_of_around:4
                    else if(know_of_around==4){
                        
                        int danger = 0;
                        for(int i = 0; i<list_of_point_than_old.size(); i++){
                            if(list_of_point_than_old.get(i).equals("R")){
                                danger++;
                            }
                        }
                        if(danger==3){
                            if(check_back[row][col]==-1){
                                check_back[row][col]=1;
                                row = old_row;
                                col = old_col;
                            }
                            else{
                                next_row_col = rnd_walk(list_of_move, row_of_this_step, col_of_this_step, Up, Down, Right, Left);
                                row = next_row_col.get(0);
                                col = next_row_col.get(1);
                            }
                        }
                        else if(danger==2){
                            if(Up!=-1){
                                if(heuristic_of_value[Up][col]==-1000){
                                   list_of_move.remove("Up");
                                }
                            }
                            if(Down!=-1){
                               if(heuristic_of_value[Down][col]==-1000){
                                   list_of_move.remove("Down");
                               } 
                            }
                            if(Left!=-1){
                               if(heuristic_of_value[row][Left]==-1000){
                                   list_of_move.remove("Left");
                               } 
                            }
                            if(Right!=-1){
                               if(heuristic_of_value[row][Right]==-1000){
                                   list_of_move.remove("Rigth");
                               } 
                            }
                            
                            if(list_of_move.get(0).equals("Up")){
                                row = Up;   
                            }
                            else if(list_of_move.get(0).equals("Down")){
                                row = Down;
                            }
                            else if(list_of_move.get(0).equals("Right")){
                                col = Right;
                            }
                            else if(list_of_move.get(0).equals("Left")){
                                col = Left;
                            }
                        }
                        else if(danger==1){
                            if(Up!=-1){
                                if(heuristic_of_value[Up][col]==-1000){
                                   list_of_move.remove("Up");
                                }
                            }
                            if(Down!=-1){
                               if(heuristic_of_value[Down][col]==-1000){
                                   list_of_move.remove("Down");
                               } 
                            }
                            if(Left!=-1){
                               if(heuristic_of_value[row][Left]==-1000){
                                   list_of_move.remove("Left");
                               } 
                            }
                            if(Right!=-1){
                               if(heuristic_of_value[row][Right]==-1000){
                                   list_of_move.remove("Rigth");
                               } 
                            }
                            
                            next_row_col = rnd_walk(list_of_move, row_of_this_step, col_of_this_step, Up, Down, Right, Left);
                            row = next_row_col.get(0);
                            col = next_row_col.get(1);
                        }
                        else if(danger==0){
                            next_row_col = rnd_walk(list_of_move, row_of_this_step, col_of_this_step, Up, Down, Right, Left);
                            row = next_row_col.get(0);
                            col = next_row_col.get(1);
                        }
                    }
                }
            }
            //old = -1000
            else if(matrix[old_row][old_col]==-1000){
                if(nmove.equals("2")){
                    
                    if(list_of_move.get(0).equals("Up")){
                        row = Up;
                    }
                    if(list_of_move.get(0).equals("Down")){
                        row = Down;
                    }
                    if(list_of_move.get(0).equals("Left")){
                        col = Left;
                    }
                    if(list_of_move.get(0).equals("Right")){
                        col = Right;
                    }
                }
                
                else if(nmove.equals("3")){
                    //know_of_around:1
                    if(know_of_around==1){
                        next_row_col = rnd_walk(list_of_move, row_of_this_step, col_of_this_step, Up, Down, Right, Left);
                        row = next_row_col.get(0);
                        col = next_row_col.get(1);
                    }
                    
                    ////know_of_around:3
                    else if(know_of_around==3){
                     
                        int danger=0;
                        for(int i = 0; i<list_of_point_than_old.size(); i++){
                            if(list_of_point_than_old.get(i).contains("R")){
                                danger++;
                            }
                        }
                        
                        if(danger == 2){
                            next_row_col = rnd_walk(list_of_move, row_of_this_step, col_of_this_step, Up, Down, Right, Left);
                            row = next_row_col.get(0);
                            col = next_row_col.get(1);
                        }
                        else if(danger == 1){
                            if(Up!=-1){
                               if(heuristic_of_value[Up][col]==-1000){
                                   list_of_move.remove("Up");
                               } 
                            }
                            if(Down!=-1){
                               if(heuristic_of_value[Down][col]==-1000){
                                   list_of_move.remove("Down");
                               } 
                            }
                            if(Left!=-1){
                               if(heuristic_of_value[row][Left]==-1000){
                                   list_of_move.remove("Left");
                               } 
                            }
                            if(Right!=-1){
                               if(heuristic_of_value[row][Right]==-1000){
                                   list_of_move.remove("Rigth");
                               } 
                            }
                            
                            if(list_of_move.get(0).equals("Up")){
                                row = Up;
                            }
                            if(list_of_move.get(0).equals("Down")){
                                row = Down;
                            }
                            if(list_of_move.get(0).equals("Right")){
                                col = Right;
                            }
                            if(list_of_move.get(0).equals("Left")){
                                col = Left;
                            }
                        }
                        else if(danger == 0){
                            next_row_col = rnd_walk(list_of_move, row_of_this_step, col_of_this_step, Up, Down, Right, Left);
                            row = next_row_col.get(0);
                            col = next_row_col.get(1);
                        }
                    }
                }
                
                else if(nmove.equals("4")){
                    //know_of_around:1
                    if(know_of_around==1){
                        next_row_col = rnd_walk(list_of_move, row_of_this_step, col_of_this_step, Up, Down, Right, Left);
                        row = next_row_col.get(0);
                        col = next_row_col.get(1);
                    }
                    
                    //know_of_around:2
                    else if(know_of_around==2){
                        
                        int danger=0;
                        int hamsayeh_danger=0;
                        
                        if(Up!=-1)
                        if(heuristic_of_value[Up][col]!=-1 && Up!=old_row){
                            if(heuristic_of_value[Up][col]==-10){
                                list_of_point_than_old.remove("B");
                            }
                            if(heuristic_of_value[Up][col]==-100){
                                list_of_point_than_old.remove("O");
                            }
                            if(heuristic_of_value[Up][col]==-1000){
                                list_of_point_than_old.remove("R");
                                hamsayeh_danger++;
                            }
                        }
                        if(Down!=-1)
                        if(heuristic_of_value[Down][col]!=-1 && Down!=old_row){
                            if(heuristic_of_value[Down][col]==-10){
                                list_of_point_than_old.remove("B");
                            }
                            if(heuristic_of_value[Down][col]==-100){
                                list_of_point_than_old.remove("O");
                            }
                            if(heuristic_of_value[Down][col]==-1000){
                                list_of_point_than_old.remove("R");
                                hamsayeh_danger++;
                            }
                        }
                        if(Right!=-1)
                        if(heuristic_of_value[row][Right]!=-1 && Right!=old_col){
                            if(heuristic_of_value[row][Right]==-10){
                                list_of_point_than_old.remove("B");
                            }
                            if(heuristic_of_value[row][Right]==-100){
                                list_of_point_than_old.remove("O");
                            }
                            if(heuristic_of_value[row][Right]==-1000){
                                list_of_point_than_old.remove("R");
                                hamsayeh_danger++;
                            }
                        }
                        if(Left!=-1)
                        if(heuristic_of_value[row][Left]!=-1 && Left!=old_col){
                            if(heuristic_of_value[row][Left]==-10){
                                list_of_point_than_old.remove("B");
                            }
                            if(heuristic_of_value[row][Left]==-100){
                                list_of_point_than_old.remove("O");
                            }
                            if(heuristic_of_value[row][Left]==-1000){
                                list_of_point_than_old.remove("R");
                                hamsayeh_danger++;
                            }
                        }
                        
                        for(int i = 0; i<list_of_point_than_old.size(); i++){
                            if(list_of_point_than_old.get(i).equals("R")){
                                danger++;
                            }
                        }
                        
                        if(danger==2 || danger==1){
                            if(Up!=-1)
                            if(heuristic_of_value[Up][col_of_this_step]==-1000 && Up!=old_row){
                                row = Up;
                            }
                            if(Down!=-1)
                            if(heuristic_of_value[Down][col_of_this_step]==-1000 && Down!=old_row){
                                row = Down;
                            }
                            if(Left!=-1)
                            if(heuristic_of_value[row_of_this_step][Left]==-1000 && Left!=old_col){
                                col = Left;
                            }
                            if(Right!=-1)
                            if(heuristic_of_value[row_of_this_step][Right]==-1000 && Right!=old_col){
                                col = Right;
                            }
                        }
                        else if(danger==0){
                            
                            if(Up!=-1)
                            if(heuristic_of_value[Up][col_of_this_step]!=-1&&Up!=old_row){
                                list_of_move.remove("Up");
                            }
                            else if(Down!=-1)
                            if(heuristic_of_value[Down][col_of_this_step]!=-1&&Down!=old_row){
                                list_of_move.remove("Down");
                            }
                            else if(Right!=-1)
                            if(heuristic_of_value[row_of_this_step][Right]!=-1&&Right!=old_col){
                                list_of_move.remove("Right");
                            }
                            else if(Left!=-1)
                            if(heuristic_of_value[row_of_this_step][Left]!=-1&&Left!=old_col){
                                list_of_move.remove("Left");
                            }
                   
                            next_row_col = rnd_walk(list_of_move, row_of_this_step, col_of_this_step, Up, Down, Right, Left);
                            row = next_row_col.get(0);
                            col = next_row_col.get(1);
                        }
                    }
                    
                    //know_of_around:4
                    else if(know_of_around==4){
                        
                        int danger = 0;
                        for(int i = 0; i<list_of_point_than_old.size(); i++){
                            if(list_of_point_than_old.get(i).equals("R")){
                                danger++;
                            }
                        }
                        if(danger==3){
                            next_row_col = rnd_walk(list_of_move, row_of_this_step, col_of_this_step, Up, Down, Right, Left);
                            row = next_row_col.get(0);
                            col = next_row_col.get(1);
                        }
                        else if(danger==2){
                            if(Up!=-1){
                                if(heuristic_of_value[Up][col]==-1000){
                                   list_of_move.remove("Up");
                                }
                            }
                            if(Down!=-1){
                               if(heuristic_of_value[Down][col]==-1000){
                                   list_of_move.remove("Down");
                               } 
                            }
                            if(Left!=-1){
                               if(heuristic_of_value[row][Left]==-1000){
                                   list_of_move.remove("Left");
                               } 
                            }
                            if(Right!=-1){
                               if(heuristic_of_value[row][Right]==-1000){
                                   list_of_move.remove("Rigth");
                               } 
                            }
                            
                            if(list_of_move.get(0).equals("Up")){
                                row = Up;
                            }
                            else if(list_of_move.get(0).equals("Down")){
                                row = Down;
                            }
                            else if(list_of_move.get(0).equals("Right")){
                                col = Right;
                            }
                            else if(list_of_move.get(0).equals("Left")){
                                col = Left;
                            }
                        }
                        else if(danger==1){
                            
                            if(Up!=-1){
                                if(heuristic_of_value[Up][col]==-1000){
                                   list_of_move.remove("Up");
                                }
                            }
                            if(Down!=-1){
                               if(heuristic_of_value[Down][col]==-1000){
                                   list_of_move.remove("Down");
                               } 
                            }
                            if(Left!=-1){
                               if(heuristic_of_value[row][Left]==-1000){
                                   list_of_move.remove("Left");
                               } 
                            }
                            if(Right!=-1){
                               if(heuristic_of_value[row][Right]==-1000){
                                   list_of_move.remove("Rigth");
                               } 
                            }
                            
                            next_row_col = rnd_walk(list_of_move, row_of_this_step, col_of_this_step, Up, Down, Right, Left);
                            row = next_row_col.get(0);
                            col = next_row_col.get(1);
                        }
                        else if(danger==0){
                            next_row_col = rnd_walk(list_of_move, row_of_this_step, col_of_this_step, Up, Down, Right, Left);
                            row = next_row_col.get(0);
                            col = next_row_col.get(1);
                        }
                    }
                }
            }
            
            int truee = 0;
            if(row==row_of_this_step){
                truee++;
            }
            if(col == col_of_this_step){
                truee++;
            }
            System.out.print("");
            if(truee==0){
                System.out.println("***********************************************************************************\n"+row+", "+col+"***************************************************************");
                
            }
            
            if(check_visit[row][col]>=4 || (row==row_of_this_step&&col==col_of_this_step)){
                       
                if(row==Up){
                    tmp_lMove.remove("Up");
                }
                else if(row==Down){
                    tmp_lMove.remove("Down");
                }
                else if(col==Right){
                    tmp_lMove.remove("Right");
                }
                else if(col==Left){
                    tmp_lMove.remove("Left");
                }
                
                ArrayList<String> rndmove = new ArrayList();
               
                if(tmp_lMove.contains("Up")){
                    rndmove.add("1");
                }
                if(tmp_lMove.contains("Down")){
                    rndmove.add("2");
                }
                if(tmp_lMove.contains("Left")){
                    rndmove.add("3");
                }
                if(tmp_lMove.contains("Right")){
                    rndmove.add("4");
                }
                
                Random rndm = new Random();
                int i = rndm.nextInt(rndmove.size());
               
                if(rndmove.get(i).equals("1")){
                    row = Up;
                    col = col_of_this_step;
                }
                if(rndmove.get(i).equals("2")){
                    row = Down;
                    col = col_of_this_step;
                }
                if(rndmove.get(i).equals("3")){
                    col = Left;
                    row = row_of_this_step;
                }
                if(rndmove.get(i).equals("4")){
                    col = Right;
                    row = row_of_this_step;
                }
            }
            
            check_visit[row][col]++;
            
            //System.out.println("row : " + row + " col : " + col);
            cost = cost - 50;
            cost = cost + matrix[row][col];
            
            old_row = row_of_this_step;
            old_col = col_of_this_step;
            
            
            heuristic_of_value[old_row][old_col] = matrix[old_row][old_col]; 
            heuristic_of_colors[old_row][old_col]= aroundPoints(old_row, old_col, matrix)[2];
            heuristic_of_value[row][col] = matrix[row][col]; 
            heuristic_of_colors[row][col]= aroundPoints(row, col, matrix)[2];
            colorMatrix[old_row][old_col] = heuristic_of_colors[old_row][old_col]; 
            colorMatrix[row][col] = heuristic_of_colors[row][col];
            costMatrix[step]=cost;
            
            if(row_of_this_step==row&&col_of_this_step==col){
                System.out.println("***********************************************************************************\n"+row+", "+col+"***************************************************************");
                System.out.println("row , col :"+row+col);
            }
            
          
            MoveMatrix[step][0] = row;
            MoveMatrix[step][1] = col;
            
            /*for(int i = 0; i<check_back.length; i++){
                for(int j = 0; j<check_back.length; j++){
                    System.out.print(check_back[i][j]+"\t");
                }
                System.out.println();
            }
            
            for(int i = 0; i<matrixSize; i++){
                for(int j =0; j<matrixSize; j++){
                    System.out.print(heuristic_of_colors[i][j]+"\t");
                }
                System.out.println();
            }
            for(int i = 0; i<matrixSize; i++){
                for(int j =0; j<matrixSize; j++){
                    System.out.print(heuristic_of_value[i][j]+"\t");
                }
                System.out.println();
            }
            for(int i = 0; i<matrixSize; i++){
                for(int j =0; j<matrixSize; j++){
                    System.out.print(check_visit[i][j]+"\t");
                }
                System.out.println();
            }
            System.out.println("Cost : "+cost + "Step : "+step);
                    */
            step++;
        }

                
        System.out.println("Cost:"+cost+"\nStep:"+step);
        //end step:
        int result_find=0;
        if(matrix[row][col]!=10000){
            
            if(row-1>=0){
                if(row-1!=old_row){
                    if(matrix[row-1][col]==10000){
                        MoveMatrix[step][0] = row-1;
                        MoveMatrix[step][1] = col;
                        cost = cost - 50;
                        cost = cost + matrix[row-1][col];
                        costMatrix[step] = cost;
                        result_find=1;
                    }
                    else{
                        MoveMatrix[step][0] = row-1;
                        MoveMatrix[step][1] = col;
                        cost = cost - 50;
                        cost = cost + matrix[row-1][col];
                        costMatrix[step] = cost;
                        step++;
                        
                        MoveMatrix[step][0] = row;
                        MoveMatrix[step][1] = col;
                        cost = cost - 50;
                        cost = cost + matrix[row][col];
                        costMatrix[step] = cost;
                        step++;
                    }
                }
            }
            if(row+1<=matrixSize-1&&result_find==0){
                if(row+1!=old_row){
                    if(matrix[row+1][col]==10000){
                        MoveMatrix[step][0] = row+1;
                        MoveMatrix[step][1] = col;
                        cost = cost - 50;
                        cost = cost + matrix[row+1][col];
                        costMatrix[step] = cost;
                        result_find=1;
                    }
                    else{
                        MoveMatrix[step][0] = row+1;
                        MoveMatrix[step][1] = col;
                        cost = cost - 50;
                        cost = cost + matrix[row+1][col];
                        costMatrix[step] = cost;
                        step++;
                        
                        MoveMatrix[step][0] = row;
                        MoveMatrix[step][1] = col;
                        cost = cost - 50;
                        cost = cost + matrix[row][col];
                        costMatrix[step] = cost;
                        step++;
                    }
                }
            }
            if(col-1>=0&&result_find==0){
                if(col-1!=old_col){
                    if(matrix[row][col-1]==10000){
                        MoveMatrix[step][0] = row;
                        MoveMatrix[step][1] = col-1;
                        cost = cost - 50;
                        cost = cost + matrix[row][col-1];
                        costMatrix[step] = cost;
                        result_find=1;
                    }
                    else{
                        MoveMatrix[step][0] = row;
                        MoveMatrix[step][1] = col-1;
                        cost = cost - 50;
                        cost = cost + matrix[row][col-1];
                        costMatrix[step] = cost;
                        step++;
                        
                        MoveMatrix[step][0] = row;
                        MoveMatrix[step][1] = col;
                        cost = cost - 50;
                        cost = cost + matrix[row][col];
                        costMatrix[step] = cost;
                        step++;
                    }
                }
            }
            if(col+1<=matrixSize-1&&result_find==0){
                if(col+1!=old_col){
                    if(matrix[row][col+1]==10000){
                        MoveMatrix[step][0] = row;
                        MoveMatrix[step][1] = col+1;
                        cost = cost - 50;
                        cost = cost + matrix[row][col+1];
                        costMatrix[step] = cost;
                        result_find=1;
                    }
                    else{
                        MoveMatrix[step][0] = row;
                        MoveMatrix[step][1] = col+1;
                        cost = cost - 50;
                        cost = cost + matrix[row][col+1];
                        costMatrix[step] = cost;
                        step++;
                        
                        MoveMatrix[step][0] = row;
                        MoveMatrix[step][1] = col;
                        cost = cost - 50;
                        cost = cost + matrix[row][col];
                        costMatrix[step] = cost;
                    }
                }
            }
        }
}//end of else        
        
        
        show_JFrameMethod(MoveMatrix, colorMatrix, costMatrix, btn_matrix, matrix);
                
        
        System.out.print("\n");
        for(int i = 0; i<matrixSize; i++){
            for(int j =0; j<matrixSize; j++){
                System.out.print(heuristic_of_colors[i][j]+"\t");
            }
            System.out.println();
        }
        for(int i = 0; i<matrixSize; i++){
            for(int j =0; j<matrixSize; j++){
                System.out.print(heuristic_of_value[i][j]+"\t");
            }
            System.out.println();
        }
    }
//random walk:
    public static ArrayList<Integer> rnd_walk(ArrayList list_of_move, int row, int col, int Up, int Down, int Right, int Left){
        
        Random rnd = new Random();
        int chance = rnd.nextInt(list_of_move.size());
        if(list_of_move.get(chance).equals("Up")){
            row = Up;
        }
        if(list_of_move.get(chance).equals("Down")){
            row = Down;
        }
        if(list_of_move.get(chance).equals("Right")){
            col = Right;
        }
        if(list_of_move.get(chance).equals("Left")){
            col = Left;
        }
        ArrayList<Integer> next_row_col = new ArrayList();
        next_row_col.add(row);
        next_row_col.add(col);
        return next_row_col;
    }
    
    
//calculate around Point :    
    public static String[] aroundPoints(int row, int col, int[][] matrix){
        
        String str_Move="";
        String str_points="";
        String number_of_move="";
        
        //4:
        if(row != 0 && row != matrixSize-1 && col != 0 && col != matrixSize-1){
            //Up:
            str_points = str_points + UpPoint(row, col, matrix);
            //Down:
            str_points = str_points + DownPoint(row, col, matrix);
            //Right:
            str_points = str_points + RightPoint(row, col, matrix);
            //Left:
            str_points = str_points + LeftPoint(row, col, matrix);
             
            str_Move = "Up" + "Down" + "Right" + "Left";
            number_of_move = "4";
        }
        
        //3:
        else if(row == 0 && col !=0 && col != matrixSize-1){
            //Down:  
            str_points = str_points + DownPoint(row, col, matrix);
            //Right:
            str_points = str_points + RightPoint(row, col, matrix);
            //Left:
            str_points = str_points + LeftPoint(row, col, matrix);
            
            number_of_move = "3";
            str_Move = "Down" + "Right" + "Left";
        }
        
        else if(row == matrixSize-1 && col !=0 && col != matrixSize-1){
            //UP:
            str_points = str_points + UpPoint(row, col, matrix);
            //Right:
            str_points = str_points + RightPoint(row, col, matrix);
            //Left:
            str_points = str_points + LeftPoint(row, col, matrix);
            
            number_of_move = "3";
            str_Move = "Up" + "Right" + "Left";
        }
        
        else if(col == 0 && row != 0 && row != matrixSize-1){
            //UP:
            str_points = str_points + UpPoint(row, col, matrix);
            //Down:
            str_points = str_points + DownPoint(row, col, matrix);
            //Right:
            str_points = str_points + RightPoint(row, col, matrix);
                   
            number_of_move = "3";
            str_Move = "Up" + "Down" + "Right";
        }
        
        else if(col == matrixSize-1 && row != 0 && row != matrixSize-1){
            //UP:
            str_points = str_points + UpPoint(row, col, matrix);
            //Down:
            str_points = str_points + DownPoint(row, col, matrix);
            //Left:
            str_points = str_points + LeftPoint(row, col, matrix);
             
            number_of_move = "3";
            str_Move = "Up" + "Down" + "Left";
        }
        //2:
        else if(row == 0 && col == 0){
            //Down:     
            str_points = str_points + DownPoint(row, col, matrix);
            //Right:
            str_points = str_points + RightPoint(row, col, matrix);
                
            str_Move = "Down" + "Right";
            number_of_move = "2";
        }
        if(row == 0 && col == matrixSize-1){
            //Down:
            str_points = str_points + DownPoint(row, col, matrix);
            //Left:
            str_points = str_points + LeftPoint(row, col, matrix);
            
            str_Move = "Down" + "Left";
            number_of_move = "2";
        }
        else if(row == matrixSize-1 && col == 0){
            //UP:
            str_points = str_points + UpPoint(row, col, matrix);
            //Right:
            str_points = str_points + RightPoint(row, col, matrix);    
            
            str_Move = "Up" + "Right";
            number_of_move = "2";
        }
        else if(row == matrixSize-1 && col == matrixSize-1){
            //UP:
            str_points = str_points + UpPoint(row, col, matrix);
            //Left:
            str_points = str_points + LeftPoint(row, col, matrix);
             
            str_Move = "Up" + "Left";
            number_of_move = "2";
        }
        
        String[] result = new String[3];
        result[0] = number_of_move;
        result[1] = str_Move;
        result[2] = str_points;
        return result;
    }
    //UP:
    public static String UpPoint(int row, int col, int[][] matrix){
        String str_pint = "";
        switch(matrix[row-1][col]){
                case -1000:{
                    str_pint = "R";
                    break;
                }

                case -100:{
                    str_pint = "O";
                    break;
                }
                case -10:{
                    str_pint = "B";
                    break;
                }
                case 10000:{
                    str_pint = "Y";
                    break;
                }
                default:{
                        break;
                }
            }
        return str_pint; 
    }
    //Down:
    public static String DownPoint(int row, int col, int[][] matrix){
        String str_pint = "";
        switch(matrix[row+1][col]){
                case -1000:{
                    str_pint = "R";
                    break;
                }

                case -100:{
                    str_pint = "O";
                    break;
                }
                case -10:{
                    str_pint = "B";
                    break;
                }
                case 10000:{
                    str_pint = "Y";
                    break;
                }
                default:{
                        break;
                }
            }
        return str_pint; 
    }
    //Right:
    public static String RightPoint(int row, int col, int[][] matrix){
        String str_pint = "";
        switch(matrix[row][col+1]){
                case -1000:{
                    str_pint = "R";
                    break;
                }

                case -100:{
                    str_pint = "O";
                    break;
                }
                case -10:{
                    str_pint = "B";
                    break;
                }
                case 10000:{
                    str_pint = "Y";
                    break;
                }
                default:{
                        break;
                }
            }
        return str_pint; 
    }    
    //Left:
    public static String LeftPoint(int row, int col, int[][] matrix){
        String str_pint = "";
        switch(matrix[row][col-1]){
                case -1000:{
                    str_pint = "R";
                    break;
                }

                case -100:{
                    str_pint = "O";
                    break;
                }
                case -10:{
                    str_pint = "B";
                    break;
                }
                case 10000:{
                    str_pint = "Y";
                    break;
                }
                default:{
                        break;
                }
            }
        return str_pint; 
    }    
       
//show JFreame Method:
    public static void show_JFrameMethod(int[][] MoveMatrix,String[][]colorMatrix, int[]costMatrix,  JButton[][] btn_matrix, int[][] matrix){
        
        int first_row = MoveMatrix[0][0];
        int first_col = MoveMatrix[0][1];
        
        btn_matrix[first_row][first_col].setIcon((new ImageIcon(Class.class.getResource("/Agent.png"))));
        txta_points.setText(txta_points.getText()+"\n"+String.valueOf(costMatrix[0]));
        
        app.remove(btn_start);
        btn_colorAndpoints.setBounds(2*Screensize/3, Screensize, Screensize/3, 50);
        JButton btn_pause = new JButton("Pause");
        JButton btn_continue = new JButton("Continue");
        btn_pause.setFont(new Font("Tahoma", Font.BOLD, 18));
        btn_continue.setFont(new Font("Tahoma", Font.BOLD, 18));
        btn_pause.setForeground(Color.white);
        btn_continue.setForeground(Color.white);
        btn_pause.setBounds(0, Screensize, Screensize/3, 50);
        btn_continue.setBounds(Screensize/3, Screensize, Screensize/3, 50);
        btn_pause.setBackground(new Color(255, 0, 0));
        btn_continue.setBackground(new Color(0, 255, 0));
        app.add(btn_continue);
        app.add(btn_pause);
        
        while(MoveMatrix[count][0]!=-1){        
            int i = MoveMatrix[count][0];
            int j = MoveMatrix[count][1];
            int step = count;
            String cost = String.valueOf(costMatrix[count]);
            ActionListener al = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for(int n = 0; n<matrixSize; n++){
                        for(int m = 0; m<matrixSize; m++){
                            btn_matrix[n][m].setIcon(null);
                            btn_matrix[n][m].setBackground(Color.cyan);
                            txta_points.setText(null);
                            txta_points.setText("\t[POINTS]");
                        }
                    }
                    
                    if(matrix[i][j]==-10){
                        btn_matrix[i][j].setBackground(Color.blue);
                    }
                    if(matrix[i][j]==-100){
                        btn_matrix[i][j].setBackground(new Color(253, 77, 0));
                    }
                    if(matrix[i][j]==-1000){
                        btn_matrix[i][j].setBackground(Color.red);
                    }
                    if(matrix[i][j]==10000){
                        btn_matrix[i][j].setBackground(Color.yellow);
                        JOptionPane.showMessageDialog(null, "Found Goal in "+step+"Step"+" and "+cost+" Point");
                    }
                    btn_matrix[i][j].setText(colorMatrix[i][j]);
                    btn_matrix[i][j].setIcon((new ImageIcon(Class.class.getResource("/Agent.png"))));
                    btn_matrix[i][j].setHorizontalTextPosition(SwingConstants.CENTER);
                    btn_matrix[i][j].setVerticalTextPosition(SwingConstants.BOTTOM);
                    txta_points.setText(txta_points.getText()+"\n"+cost);
                    txta_Movements.setText(txta_Movements.getText()+"\n"+"[Step: "+step+"] row:"+i+",  col:"+j);
                }
            }; 
            
            Timer t = new Timer(count*3000, al);
            t.start();
            t.setRepeats(false);
            
            btn_pause.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    t.stop();
                }
            });
            
            btn_continue.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for(int n = 0; n<matrixSize; n++){
                        for(int m = 0; m<matrixSize; m++){
                            btn_matrix[n][m].setText(null);
                            txta_points.setText(null);
                            txta_Movements.setText(null);
                        }
                    }       
                    t.start();
                }
            });
            count++;
        }
    }
//show Color and Points:
    public static void show_ColorAndPoints(int[][] matrix){
        JFrame colorAndpoint = new JFrame("Color and Points Frame");
        int new_screenSize = 6*Screensize/7;
        colorAndpoint.setSize(new_screenSize+5, new_screenSize+27);
        colorAndpoint.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        colorAndpoint.setResizable(false);
        colorAndpoint.setLayout(null);
        colorAndpoint.setVisible(true);
        
        JButton[][] temp_btn_matrix = new JButton[matrixSize][matrixSize];
        defineBtnMatrix(temp_btn_matrix, matrixSize);
        for(int i = 0; i<matrixSize; i++){
            for(int j = 0; j<matrixSize; j++){
                temp_btn_matrix[i][j] = new JButton();
                if(i == 0 && j ==0){
                    temp_btn_matrix[i][j].setText("Start");
                }
                temp_btn_matrix[i][j].setBackground(Color.CYAN);
                temp_btn_matrix[i][j].setBounds(j*new_screenSize/matrixSize, i*new_screenSize/matrixSize, new_screenSize/matrixSize, new_screenSize/matrixSize);
                colorAndpoint.add(temp_btn_matrix[i][j]);
            }   
        }
        
        for(int i = 0; i<matrixSize; i++){
            for(int j = 0; j<matrixSize; j++){
                String point = aroundPoints(i, j, matrix)[2];
                temp_btn_matrix[i][j].setText(point);
                switch(matrix[i][j]){
                    case -1000:{
                        temp_btn_matrix[i][j].setBackground(Color.RED);
                        break;
                    }
                    case -100:{
                        temp_btn_matrix[i][j].setBackground(new Color(253, 77, 0));
                        break;
                    }
                    case -10:{
                        temp_btn_matrix[i][j].setBackground(Color.BLUE);
                        break;
                    }
                    case 10000:{
                        temp_btn_matrix[i][j].setBackground(Color.YELLOW);
                        break;
                    }
                    default:{
                        temp_btn_matrix[i][j].setBackground(Color.WHITE);
                        break;
                    }
                }
            }
        }
        
        for(int i = 0; i<matrixSize; i++){
            for(int j = 0; j<matrixSize; j++){
                temp_btn_matrix[i][j].setFont(new java.awt.Font("Arial", 1, 11));
            }
        }
    }
//Show Console Method:
    public static void show_consoleMethod(int[][] matrix, int matrixSize){
        int x1 = 0,x2 = 0,x3 = 0, x4 = 0;
        for(int i = 0; i<matrixSize; i++){
            for(int j = 0; j<matrixSize; j++){
                if(matrix[i][j] == -10){
                    x1++;
                }
                else if(matrix[i][j] == -100){
                    x2++;
                }
                else if(matrix[i][j] == -1000){
                    x3++;
                }
                else if(matrix[i][j] == +10000){
                    x4++;
                }
                System.out.print(matrix[i][j]);
                System.out.print("\t");
            }
            System.out.print("\n");
        }
        System.out.format("\n%d, %d, %d, %d", x1, x2, x3, x4);
        
    }
//Think of agent:
    public static int[][] think(int[][] heuristic_of_value, String[][] heuristic_of_colors){
        
        for(int i = 0; i<matrixSize; i++){
            for(int j = 0; j<matrixSize; j++){
                int Up_row = -1, Down_row = -1, Right_col = -1, left_col = -1;
                int knowState = 0;
                String[] nMove_Moves_Points = aroundPoints(i, j, heuristic_of_value);        
                if(i-1>=0){
                    if(heuristic_of_value[i-1][j]!=-1){
                       knowState++;
                    } 
                    Up_row = i-1;
                }
                if(j-1>=0){     
                    if(heuristic_of_value[i][j-1]!=-1){
                       knowState++;
                    } 
                    left_col = j-1;
                }
                if(i+1<=matrixSize-1){
                    if(heuristic_of_value[i+1][j]!=-1){
                        knowState++;
                    } 
                    Down_row = i+1;
                }
                if(j+1<=matrixSize-1){
                    if(heuristic_of_value[i][j+1]!=-1){
                       knowState++;
                    } 
                    Right_col = j+1;
                }
                //System.out.println("Up:" + Up_row +"Down:"+Down_row+"left:"+left_col+"right:"+Right_col);
                int nmove = nMove_Moves_Points[0].charAt(0)-48;
                
                if(nmove-knowState == 1){
                    String color = heuristic_of_colors[i][j];
                    char[] colors = color.toCharArray(); 
                    //System.out.println("0 : "+String.valueOf(colors));
                    if(Up_row != -1 && heuristic_of_value[Up_row][j]!=-1){
                        if(heuristic_of_value[Up_row][j]==-10){
                            for(int index = 0; index<colors.length; index++){
                                if(colors[index] == 'B'){
                                    colors[index] = '-';
                                    break;
                                }
                            }
                        }
                        if(heuristic_of_value[Up_row][j]==-100){
                            for(int index = 0; index<colors.length; index++){
                                if(colors[index] == 'O'){
                                    colors[index] = '-';
                                    break;
                                }
                            }
                        }
                        if(heuristic_of_value[Up_row][j]==-1000){
                            for(int index = 0; index<colors.length; index++){
                                if(colors[index] == 'R'){
                                    colors[index] = '-';
                                    break;
                                }
                            }
                        }
                        if(heuristic_of_value[Up_row][j]==10000){
                            for(int index = 0; index<colors.length; index++){
                                if(colors[index] == 'Y'){
                                    colors[index] = '-';
                                    break;
                                }
                            }
                        }
                    }
                    
                    //System.out.println("1 : " + String.valueOf(colors));
                    if(Down_row != -1 && heuristic_of_value[Down_row][j]!=-1){
                          if(heuristic_of_value[Down_row][j]==-10){
                            for(int index = 0; index<colors.length; index++){
                                if(colors[index] == 'B'){
                                    colors[index] = '-';
                                    break;
                                }
                            }
                        }
                        if(heuristic_of_value[Down_row][j]==-100){
                            for(int index = 0; index<colors.length; index++){
                                if(colors[index] == 'O'){
                                    colors[index] = '-';
                                    break;
                                }
                            }
                        }
                        if(heuristic_of_value[Down_row][j]==-1000){
                            for(int index = 0; index<colors.length; index++){
                                if(colors[index] == 'R'){
                                    colors[index] = '-';
                                    break;
                                }
                            }
                        }
                        if(heuristic_of_value[Down_row][j]==10000){
                            for(int index = 0; index<colors.length; index++){
                                if(colors[index] == 'Y'){
                                    colors[index] = '-';
                                    break;
                                }
                            }
                        }
                    }
                    
                    //System.out.println("2 : " + String.valueOf(colors));
                    if(Right_col!= -1 && heuristic_of_value[i][Right_col]!=-1){
                        if(heuristic_of_value[i][Right_col]==-10){
                            for(int index = 0; index<colors.length; index++){
                                if(colors[index] == 'B'){
                                    colors[index] = '-';
                                    break;
                                }
                            }
                        }
                        if(heuristic_of_value[i][Right_col]==-100){
                            for(int index = 0; index<colors.length; index++){
                                if(colors[index] == 'O'){
                                    colors[index] = '-';
                                    break;
                                }
                            }
                        }
                        if(heuristic_of_value[i][Right_col]==-1000){
                            for(int index = 0; index<colors.length; index++){
                                if(colors[index] == 'R'){
                                    colors[index] = '-';
                                    break;
                                }
                            }
                        }
                        if(heuristic_of_value[i][Right_col]==10000){
                            for(int index = 0; index<colors.length; index++){
                                if(colors[index] == 'Y'){
                                    colors[index] = '-';
                                    break;
                                }
                            }
                        }
                    }
                    
                    //System.out.println("3 : "+String.valueOf(colors));
                    if(left_col != -1 && heuristic_of_value[i][left_col]!=-1){
                        if(heuristic_of_value[i][left_col]==-10){
                            for(int index = 0; index<colors.length; index++){
                                if(colors[index] == 'B'){
                                    colors[index] = '-';
                                    break;
                                }
                            }
                        }
                        if(heuristic_of_value[i][left_col]==-100){
                            for(int index = 0; index<colors.length; index++){
                                if(colors[index] == 'O'){
                                    colors[index] = '-';
                                    break;
                                }
                            }
                        }
                        if(heuristic_of_value[i][left_col]==-1000){
                            for(int index = 0; index<colors.length; index++){
                                if(colors[index] == 'R'){
                                    colors[index] = '-';
                                    break;
                                }
                            }
                        }
                        if(heuristic_of_value[i][left_col]==10000){
                            for(int index = 0; index<colors.length; index++){
                                if(colors[index] == 'Y'){
                                    colors[index] = '-';
                                    break;
                                }
                            }
                        }
                    }
                    
                    //System.out.print("4 : "+String.valueOf(colors));
                    color = String.valueOf(colors);
                    //System.out.print("\n" + color);
                    if(Up_row>=0 && heuristic_of_value[Up_row][j]==-1){
                        if(color.contains("B")){
                            heuristic_of_value[Up_row][j] = -10;
                        }
                        else if(color.contains("O")){
                            heuristic_of_value[Up_row][j] = -100;
                        }
                        else if(color.contains("R")){
                            heuristic_of_value[Up_row][j] = -1000;
                        }
                        else if(color.contains("Y")){
                            heuristic_of_value[Up_row][j] = 10000;
                        }
                        else if(color.equals("----")||color.equals("---")||color.equals("--")||color.equals("-")||color.equals("")){
                            heuristic_of_value[Up_row][j] = 0;  
                        }
                        
                    }
                    else if(Down_row<matrixSize  && Down_row>0 && heuristic_of_value[Down_row][j]==-1){
                       
                        if(color.contains("B")){
                            heuristic_of_value[Down_row][j] = -10;
                        }
                        else if(color.contains("O")){
                            heuristic_of_value[Down_row][j] = -100;
                        }
                        else if(color.contains("R")){
                            heuristic_of_value[Down_row][j] = -1000;
                        }
                        else if(color.contains("Y")){
                            heuristic_of_value[Down_row][j] = 10000;
                        }
                        else if(color.equals("----")||color.equals("---")||color.equals("--")||color.equals("-")||color.equals("")){
                            heuristic_of_value[Down_row][j] = 0;
                        } 
                    }
                    else if(Right_col<matrixSize && Right_col>0 && heuristic_of_value[i][Right_col]==-1){
                        
                        if(color.contains("B")){
                            heuristic_of_value[i][Right_col] = -10;
                        }
                        else if(color.contains("O")){
                            heuristic_of_value[i][Right_col] = -100;
                        }
                        else if(color.contains("R")){
                            heuristic_of_value[i][Right_col] = -1000;
                        }
                        else if(color.contains("Y")){
                            heuristic_of_value[i][Right_col] = 10000;
                        }
                        else if(color.equals("----")||color.equals("---")||color.equals("--")||color.equals("-")||color.equals("")){
                            heuristic_of_value[i][Right_col] = 0;
                        } 
                    }
                    else if(left_col>=0 && heuristic_of_value[i][left_col]==-1){
                        
                        if(color.contains("B")){
                            heuristic_of_value[i][left_col] = -10;
                        }
                        else if(color.contains("O")){
                            heuristic_of_value[i][left_col] = -100;
                        }
                        else if(color.contains("R")){
                            heuristic_of_value[i][left_col] = -1000;
                        }
                        else if(color.contains("Y")){
                            heuristic_of_value[i][left_col] = 10000;
                        }
                        else if(color.equals("----")||color.equals("---")||color.equals("---")||color.equals("-")||color.equals("")){
                            heuristic_of_value[i][left_col] = 0;
                        }
                    }
                }
            }       
        }
        return heuristic_of_value;
    }
    
//sound:
    public static void sound(String path) {
        
        InputStream music = null ;
        try{
            music = new FileInputStream(new File(path));
            AudioStream audio = new AudioStream(music);
            AudioPlayer.player.start(audio);
        }catch(Exception e){}
            
    }
}