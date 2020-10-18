# Week07学习笔记
## Two-ended BFS模板
```java
// 初始化存储两端当前层内容的队列和全局查重用的Set
Deque<String> beginDeque = new ArrayDeque<>();
Deque<String> endDeque = new ArrayDeque<>();
Set<String> visited = new HashSet<>();

Deque<String> tmp;

// 初始化顶端和底端
beginDeque.addFirst(begin);
endDeque.addFirst(end);

while(!beginDeque.isEmpty() && !endDeque.isEmpty()){
    // 每次取状态少的一端扩展
    if(beginDeque.size()>endDeque.size()){
        Deque<String> deque = beginDeque;
        beginDeque = endDeque;
        endDeque = deque;
    }
    tmp = new ArrayDeque<>();

    // 扩展，扩展得到的内容暂存在tmp中
    while(!beginDeque.isEmpty()){
        String str = beginDeque.removeLast();
        // 对str进行扩展
        if(endDeque.contains(extendedStr)){
            // 相遇了，进行处理
        }else if(!visited.contains(extendedStr)){
            tmp.addFirst(extendedStr);
            visited.add(extendedStr);
        }
    }
    beginDeque = tmp;
}
```