/*
 * @lc app=leetcode.cn id=641 lang=java
 *
 * [641] 设计循环双端队列
 */

// @lc code=start
class MyCircularDeque {
    private int[] content;
    private int head;
    private int tail;
    private int size;
    /** Initialize your data structure here. Set the size of the deque to be k. */
    public MyCircularDeque(int k) {
        this.content = new int[k];
        this.head = 0;
        this.tail = 0;
        this.size = 0;
    }
    
    /** Adds an item at the front of Deque. Return true if the operation is successful. */
    public boolean insertFront(int value) {
        if(this.isFull()) return false;
        if(!this.isEmpty()) head = (head+1)%content.length;
        content[head] = value;
        size++;
        return true;
    }
    
    /** Adds an item at the rear of Deque. Return true if the operation is successful. */
    public boolean insertLast(int value) {
        if(this.isFull()) return false;
        if(!this.isEmpty()) tail = (tail-1+content.length)%content.length;
        content[tail] = value;
        size++;
        return true;
    }
    
    /** Deletes an item from the front of Deque. Return true if the operation is successful. */
    public boolean deleteFront() {
        if(this.isEmpty()) return false;
        if(size!=1) head = (head-1+content.length)%content.length;
        size--;
        return true;
    }
    
    /** Deletes an item from the rear of Deque. Return true if the operation is successful. */
    public boolean deleteLast() {
        if(this.isEmpty()) return false;
        if(size!=1) tail = (tail+1)%content.length;
        size--;
        return true;
    }
    
    /** Get the front item from the deque. */
    public int getFront() {
        if(this.isEmpty()) return -1;
        return content[head];
    }
    
    /** Get the last item from the deque. */
    public int getRear() {
        if(this.isEmpty()) return -1;
        return content[tail];
    }
    
    /** Checks whether the circular deque is empty or not. */
    public boolean isEmpty() {
        if(this.size==0) return true;
        return false;
    }
    
    /** Checks whether the circular deque is full or not. */
    public boolean isFull() {
        if(this.size==content.length) return true;
        return false;
    }
}

/**
 * Your MyCircularDeque object will be instantiated and called as such:
 * MyCircularDeque obj = new MyCircularDeque(k);
 * boolean param_1 = obj.insertFront(value);
 * boolean param_2 = obj.insertLast(value);
 * boolean param_3 = obj.deleteFront();
 * boolean param_4 = obj.deleteLast();
 * int param_5 = obj.getFront();
 * int param_6 = obj.getRear();
 * boolean param_7 = obj.isEmpty();
 * boolean param_8 = obj.isFull();
 */
// @lc code=end

