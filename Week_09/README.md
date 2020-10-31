# Week09学习笔记
## [不同路径2](https://leetcode-cn.com/problems/unique-paths-ii/)的状态转移方程
记`n(i, j)`为从网格的第i行第j列到右下角的路径数，则`n(i, j)`=
- 0, 若该格是障碍物
- `n(i+1, j)`, 若j=最后一列
- `n(i, j+1)`, 若i=最后一行
- `n(i+1, j) + n(i, j+1)`, 其他