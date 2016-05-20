package thread;

public class SumMatrix implements Runnable{
    static String SumGlobal;
    static int row , col;
    static int matrix[][];
    int id;
    static int countProc;
    public static final Object eye = new Object();
    private int times;
    static int repCount;
    
    static{
        row = 7;
        col = 7;
        matrix = new int[row][col];
        for(int i = 0; i < row; ++i)
            for(int j = 0; j < col; j++){
                matrix[i][j] = (int)(Math.random()* 9) + 1;
                if((i == 0 && j < col) || (j == 0 || i == row) || (i == row - 1) && (j < col) || (j == col - 1) && (i < row))
                    matrix[i][j] = 0;
            }
    }
    
    
    
    SumMatrix(int _id, int _times) { id = _id; times = _times; }
    
    @Override
    public void run(){
        int[] tmp = new int[row];       
        for(int j = 0; j < times; j++){
            System.out.println("Thread [" + id + "] started calc");
            for(int i = 1; i < row - 1 ; ++i)
                tmp[i] = 
                        matrix[id - 1][i - 1] + 
                        matrix[id - 1][i] + 
                        matrix[id - 1][i + 1] + 
                        matrix[id][i - 1] +
                        matrix[id][i] + 
                        matrix[id][i + 1] + 
                        matrix[id + 1][i - 1] + 
                        matrix[id + 1][i] + 
                        matrix[id + 1][i + 1];
            countProc++;
            System.out.println("Thread [" + id + "] finished calc");
            if(countProc < row - 2){
                try{
                    synchronized(eye){
                        eye.wait();
                    }
                }catch(InterruptedException ex){}
            }
            else synchronized(eye){
                    eye.notifyAll();
                }
            System.out.println("Thread [" + id + "] started updating matrix (" + (j + 1) + ")");
            for(int i = 1; i < row - 1; i++)
                matrix[id][i] = tmp[i];
            System.out.println("Thread [" + id + "] finished updating matrix (" + (j + 1) + ")");
        
            if(repCount % (tmp.length - 2) != 0) {
                synchronized (eye) {
                    try {
                            eye.wait();
                    } catch (InterruptedException e) {}
                }
            } else {
                synchronized (eye) {
                    eye.notifyAll();
                }
            }
        }
    }
    
    public static void main(String args[]){
        for(int i = 0; i < row; i++)
            for(int j = 0; j < col; j++){
                System.out.print(matrix[i][j] + " ");
                if(j == row - 1)
                    System.out.println();
            }
        
        for(int i = 1; i < row - 1; i++)
            (new Thread(new SumMatrix(i,2))).start();
        try{
            Thread.sleep(1100);
        }catch(Exception e){}
        
        System.out.println("----------------------------------");
        for(int i = 0; i < row; i++)
            for(int j = 0; j < col; j++){
                System.out.print(matrix[i][j] + " ");
                if(j == row - 1)
                    System.out.println();
            }
    }
}
