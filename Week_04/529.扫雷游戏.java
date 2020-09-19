import java.util.*;

class Solution {
    public char[][] updateBoard(char[][] board, int[] click) {
        // 挖出M则改为X
        // 挖出E则改为B，并递归地把其相邻的E都改为B
            // E改为B时，检索其周围M的数量，若有M则改为M的数量
        int x=click[1], y=click[0];
        if(board[y][x]=='M'){
            board[y][x]='X';
        }else{
            recReveal(board, x, y);
        }
        return board;
    }

    private void recReveal(char[][] board, int x, int y){
        // 对应坐标不是E时直接返回
        if(board[y][x]!='E') return;
        // 确定该坐标的内容
        board[y][x] = getMineCountAround(board, x, y);
        // 递归地揭露周围的方块
        if(board[y][x]!='B') return;
        for(int i=Math.max(0, x-1); i>=0 && i<board[0].length && i<=x+1; i++){
            for(int j=Math.max(0, y-1); j>=0 && j<board.length && j<=y+1; j++){
                if(i==x && j==y) continue;
                recReveal(board, i, j);
            }
        }
    }

    private char getMineCountAround(char[][] board, int x, int y){
        char cnt = 0;
        for(int i=Math.max(0, x-1); i>=0 && i<board[0].length && i<=x+1; i++){
            for(int j=Math.max(0, y-1); j>=0 && j<board.length && j<=y+1; j++){
                if(i==x && j==y) continue;
                if(board[j][i]=='M') cnt++;
            }
        }
        if(cnt>0) return (char)('0'+cnt);
        return 'B';
    }
}