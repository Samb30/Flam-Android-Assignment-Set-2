package Q1_Nqueens;

import java.util.*;

public class Q1 
{
    public static List<List<String>> func(int n) 
    {
        char[][] board = new char[n][n];
        int[] lr = new int[n];
        int[] ud = new int[2 * n - 1];
        int[] ld = new int[2 * n - 1];
        List<List<String>> r = new ArrayList<>();

        for (char[] row : board)
        {
            Arrays.fill(row, '.');
        }

        solve(0, r, lr, board, ld, ud);

        return r;
    }

    static void solve(int col, List<List<String>> r, int[] lr, char[][] board, int[] ld, int[] ud) 
    {
        if (col == board.length) 
        {
            r.add(construct(board));
            return;
        }

        for (int row = 0; row < board.length; row++) 
        {
            if (lr[row] == 0 && ld[row + col] == 0 && ud[board.length - 1 + col - row] == 0) 
            {
                board[row][col] = 'Q';
                lr[row] = 1;
                ld[row + col] = 1;
                ud[board.length - 1 + col - row] = 1;

                solve(col + 1, r, lr, board, ld, ud);

                board[row][col] = '.';
                lr[row] = 0;
                ld[row + col] = 0;
                ud[board.length - 1 + col - row] = 0;
            }
        }
    }

    static List<String> construct(char[][] board) 
    {
        List<String> res = new ArrayList<>();
        
        for (char[] row : board)
        {
            res.add(new String(row));
        }
        
        return res;
    }

    public static void main(String[] args) 
    {
        int n = 4; // this value can be changed to test other inputs

        List<List<String>> ans = func(n);

        for (int i = 0; i < ans.size(); i++) 
        {
            List<String> board = ans.get(i);

            for (int j = 0; j < board.size(); j++) 
            {
                System.out.print("\"" + board.get(j) + "\"");
                if (j < board.size() - 1)
                {
                    System.out.print(",");
                }
            }

            if (i < ans.size() - 1)
            {
                System.out.print("\n");
            }
        }
    }
}