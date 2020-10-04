/*
 * @lc app=leetcode.cn id=236 lang=java
 *
 * [236] 二叉树的最近公共祖先
 */

// @lc code=start
// Definition for a binary tree node.
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode(int x) { val = x; }
}

class Solution {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        // 三种情况：
        // 1. p和q分别在一个节点的左右子树，比如遍历左、右子树找到了p和q则返回root
        // 2. q在p的某个子树，即p为root且某子树找到q则返回root
        // 3. p在q的某个子树，即q为root且某子树找到p则返回root

        // 基线条件+本层处理
        /*
        // 对应一个在另一个的子树的情况
        if(root.val==p.val || root.val==q.val && (l!=null || r!=null)) return root;
        // 其余情况
        if(root.val==p.val) return p;
        if(root.val==q.val) return q;
        */
        if(root==null || root.val==p.val || root.val == q.val) return root;
    
        TreeNode l = lowestCommonAncestor(root.left, p, q);
        TreeNode r = lowestCommonAncestor(root.right, p, q);

        if((l==p || l==q) && (r==p || r==q)) return root;
        // 传递底层的结果
        return l==null ? r : l;
    }
}
// @lc code=end