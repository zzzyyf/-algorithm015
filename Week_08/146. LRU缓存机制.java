import java.util.*;

class LRUCache {
    Map<Integer, DLinkedNode> map;
    DLinkedNode front, rear;
    final int capacity;
    int size;

    public LRUCache(int capacity) {
        map = new HashMap<>();
        this.capacity = capacity;
        front = new DLinkedNode();
        rear = new DLinkedNode();
        front.next = rear;
        rear.prev = front;
        front.prev = rear.next = null;
    }
    
    public int get(int key) {
        DLinkedNode node = map.get(key);
        if(node!=null) {
            // 更新其为最近使用，即挪到front之后
            node.prev.next = node.next;
            node.next.prev = node.prev;
            add(node);
            return node.value;
        }
        return -1;
    }
    
    public void put(int key, int value) {
        // 先检查是否存在
        // 存在则重新排序，挪到front之后，这一步已经包含在get中了
        if(this.get(key)==-1){
            // 不存在时，若缓存已满，则删除rear前的结点。然后把新结点挪到front之后
            DLinkedNode node = new DLinkedNode(key, value);
            map.put(key, node);
            if(size==capacity){
                map.remove(rear.prev.key);
                rear.prev = rear.prev.prev;
                rear.prev.next = rear;
                size--;
            }
            add(node);
            size++;
        }else{
            DLinkedNode node = map.get(key);
            node.value = value;
        }
    }

    private void add(DLinkedNode node){
        node.prev = front;
        node.next = front.next;
        front.next.prev = node;
        front.next = node;
    }
}

class DLinkedNode{
    DLinkedNode prev;
    DLinkedNode next;
    int key;
    int value;

    public DLinkedNode(){};

    public DLinkedNode(int key, int val){
        this.key = key;
        value = val;
    }
}

/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */