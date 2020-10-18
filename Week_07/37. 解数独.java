class Solution {
    char[][] board;
    boolean[][] rows = new boolean[9][9], cols = new boolean[9][9], grids = new boolean[9][9];
    public void solveSudoku(char[][] board) {
        this.board = board;
        // 首先要利用已有的数据，没必要把它当做一般情况对待
        for(int x=0; x<9; x++){
            for(int y=0; y<9; y++){
                if(board[y][x]!='.'){
                    int n = board[y][x]-'1';
                    rows[y][n] = cols[x][n] = grids[y/3*3+x/3][n] = true;
                }
            }
        }
        dfs(0, 0);
    }

    private boolean dfs(int x, int y){
        if(x==0 && y==board.length) return true;
        if(board[y][x]!='.'){
            if(x==board[0].length-1){
                if(dfs(0, y+1)) return true;
            }else{
                if(dfs(x+1, y)) return true;
            }
            return false;
        }
        // 尝试填入1-9中可填的数
        for(int i=0; i<9; i++){
            if(rows[y][i] || cols[x][i] || grids[y/3*3+x/3][i]) continue;
            rows[y][i] = cols[x][i] = grids[y/3*3+x/3][i] = true;
            board[y][x] = (char)('1'+i);
            if(x==board[0].length-1){
                if(dfs(0, y+1)) return true;
            }else{
                if(dfs(x+1, y)) return true;
            }
            rows[y][i] = cols[x][i] = grids[y/3*3+x/3][i] = false;
            board[y][x] = '.';
        }
        return false;
    }
}