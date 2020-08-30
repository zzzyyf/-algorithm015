# Week01学习笔记
## 作业：用新的API改写`Deque`的demo代码
```java
Deque<String> deque = new LinkedList<String>();

deque.addFirst("a");
deque.addFirst("b");
deque.addFirst("c");
System.out.println(deque);

String str = deque.peekFirst();
System.out.println(str);
System.out.println(deque);

while(deque.size()>0){
    System.out.println(deque.removeFirst());
}
System.out.println(deque);
```

## 作业：分析`Queue`的源代码
> 分析的代码版本为javase-13.0.2。
- 首先`Queue`是一个接口，其继承了`Collection`接口。
- 从概述中可以得知，`Queue`额外提供了一些**增、删、查**的操作，而且这些额外操作可以根据**错误时的行为**分为两类：一类抛出异常，另一类返回特殊值，有时是`null`有时是`false`。错误时返回特殊值的**插入**方法是特别针对**限制队列大小**的情况实现的，因为此时插入失败可能是经常性操作，没必要动用异常处理。
- 由于`poll()`约定在空队是返回`null`，不应向队列中插入`null`，而且一般也不允许。
- `Queue`不一定要求实现FIFO的队列，比如优先队列，但是每个`Queue`的实现都被要求必须说明其为元素排序的属性。
- `Queue`的实现类通常不会定义基于元素实现的`equals()`和`hashCode()`方法，而是基于identity（本体）的即`Object`实现的方法，因为基于元素实现的方法不会考虑队列的排序属性，从而不适用于队列。
    - 说人话就是如果两个元素内容完全一致，但是在队列中的位置不同，对于队列而言它们可能不应被认为是相同的元素。
- 常见的实现有：`PriorityQueue`，`BlockingQueue`，常见的子接口有`Deque`。
- 下面就从增、删、查三个方面来分析`Queue`的源码。

### 增
`add(E): boolean`
- 语焉不详地说把元素插入到队列，但是插入到什么位置呢？估计与队列的具体实现有关。
- 正常插入时返回`true`，插入失败时抛出异常：
    - 超出队列容量限制时抛出`IllegalStateException`
    - 不满足泛型限制时抛出`ClassCastException`
    - 队列的具体实现不支持插入`null`时抛出`NullPointerException`
    - 不满足队列的其他具体限制时抛出`IllegalArgumentException`

`offer(E): boolean`
- 这个方法就是`add()`针对队列容量限制的特别版，与`add()`的唯一区别是当插入元素超出队列容量限制时不抛出`IllegalStateException`，而是直接返回`false`。

### 查
`element(): E`
- 返回（不删除）**队首**元素。当队为空时抛出`NoSuchElementException`异常。

`peek(): E`
- `element()`方法的特别版，当队为空时不抛出异常而是返回`null`。

### 删
`remove(): E`
- 删除并返回**队首**元素。当队为空时抛出`NoSuchElementException`异常。

`poll(): E`
- `remove()`方法的特别版，当队为空时不抛出异常而是返回`null`。

## 作业：分析`PriorityQueue`的源代码
> 分析的代码版本为javase-13.0.2。
### 概述
- `PriorityQueue`是一个类，其继承了`AbstractQueue`类，实现了`Serializable`接口。`AbstractQueue`类顾名思义，提供了`Queue`接口的基本实现。
- `PriorityQueue`类不限制容量，基于Priority Heap（优先堆）实现。队列中的元素可以基于自然顺序排序，也可使用队列的构造函数中传入的`Comparator`来排序。
- `PriorityQueue`不允许插入`null`元素，同时若队列使用自然顺序排序，则不允许插入未实现`Comparable`接口的对象。
- 队首是基于特定顺序的最小者。若有多个元素为最小，则队首是其中的任一一个。可以使用`poll`, `remove`, `peek`, `element`等方法访问队首。
- `PriorityQueue`是基于数组实现的，因此有一个实际容量，默认初始容量为`11`。容量是由类自动管理的，其增长策略是未指定的。
- 本类及其迭代器实现了所有的`Collection`和`Iterator`接口的可选方法。`Iterator`和`Spliterator`分别由`iterator()`和`spliterator()`方法提供，它们均不保证以任何顺序进行遍历。如果需要按序遍历，可以使用`Arrays.sort(pq.toArray())`来使用数组遍历。
- 本实现不是同步的。当多线程中任一线程更改队列时，多线程不能采用并发访问。对并发有需求可以使用`java.util.concurrent.PriorityBlockingQueue`类。
- 本实现为入队和出队方法（`offer`, `poll`, `remove()`和`add`）提供`O(log(n))`的时间复杂度，为`remove(Object)`和`contains(Object)`方法提供线性时间复杂度，为`peek`, `element`和`size`等方法提供常数时间复杂度。

### 成员
- 一个序列化使用的`UID`
- 数组的默认初始容量，值为`11`
- 用数组实现的平衡二叉小顶堆`queue`，`queue[n]`的左右子分别为`queue[2*n+1]`和`queue[2*(n+1)]`。注意其用`transient`修饰，意味着堆内容无法被序列化？稍后再看具体方法代码。
- 元素计数器`int size`
- 使用的`Comparator`
- `modCount`，看到后面的实现是一个简单检测并发修改用的计数器。

### 构造函数
- 默认无参构造函数，使用上述默认初始容量`11`和无`Comparator`构造队列。
- 根据有无初始容量和有无`Comparator`的组合有一系列构造函数，最后的实现是检测初始容量是否<1，然后新建实现堆的数组，再为比较器赋值。
- 可以从`Collection`中构造队列，如果是`SortedSet`或者另一个`PriorityQueue`，可以直接继承其顺序，否则使用元素的自然顺序，即`Comparable`的实现。对于以上两个具体的`Collection`实现还有重载方法。

### 辅助方法
- `init***`：根据上述构造函数的描述，对于`SortedSet`和`PriorityQueue`，获取其比较器、元素（若元素为空则新建一个`Object[1]`数组确保非空）和大小。对于一般的`Collection`，除了上述操作外，由于其没有指定顺序，还会进行一个`heapify()`操作，其时间复杂度为`O(size)`。
- `grow(int)`：在最小所需容量、容量+2和容量\*2中选择一个进行扩充。当最小所需容量最大时，以其为准，否则当当前容量<64时选择+2，不然选择\*2。
- `heapify()`：有`Comparator`时，对堆的非叶节点按照完全二叉树的逆序调用`siftDownUsingComparator()`；否则对堆的非叶节点按照完全二叉树的逆序调用`siftDownComparable()`。
- `siftDown()`, `siftDownComparable()`, `siftDownUsingComparator()`都是当发生删除时对新根执行可能的下移的操作，使用迭代实现。当待插入位置为非叶节点（小于n/2）时，找出左右子中的最小者，若待插入元素大于最小者，则将最小者上移到待插入位置，然后待插入位置变为空出的位置，继续迭代直到待插入位置变为叶节点，此时直接插入即可。
- `siftUp()`, `siftUpComparable()`, `siftUpUsingComparator()`都是发生插入时对新结点执行可能的上移的操作，同样使用迭代实现。当待插入位置不为根（>0）时，用(n-1)/2获取父节点坐标，当新结点**大于**父节点时，将父节点下移到待插入位置，待插入位置变为父节点的位置，继续迭代直到待插入位置变为根节点或新结点小于等于父节点，直接插入即可。
- `bulkRemove(Predicate)`：
    - 首先记录下当前`modCount`，然后遍历到首个满足条件的元素处，此时若未发现满足条件的元素则返回`false`，若`modCount`被修改了则抛出`CME`(`ConcurrentModificationException`)。
    - 把首个满足条件的元素下标记为`beg`，调用`nBits()`对`beg`及之后的元素构造一个映射`long[] deathRow`。由于`beg`处满足条件，把`deathRow[0]`置1。
    - 遍历`beg`之后的元素，若其满足条件则调用`setBit()`把映射的对应位置1。
    - 再次检查`modCount`，若被修改则抛出`CME`。
    - 用`w`记录不满足条件的元素数量，同时把满足条件的元素覆盖掉，具体操作为先把`w`初始化为`beg`，然后遍历`beg`之后的元素，调用`isClear()`判断其映射对应位，为0时用其覆盖掉`w`处的元素，然后`w++`。这一步很像之前做过的[移动零](https://leetcode-cn.com/problems/move-zeroes/)的思路。
    - 把`w`及之后的元素置空，然后`heapify()`。
    - 主要的工作是把满足条件的元素删去，然后把剩余元素组织为堆。那为什么需要那么一个映射呢？
- `nBits()`：就一行代码`return new long[((n-1) >> 6) + 1)];`，根据给定大小设置一个用`long`的一位映射一个元素的数组，比如`bits[0] = 3`就说明原数组的第0和第1个元素为`true`。
- `setBit()`：就一行代码`bits[i >> 6] |= 1L << i;`，设置对应位置的`bit`位为1，比如给定`3`就设置`bits[0] |= 8`，即把第3+1位设为1.
- `isClear()`：就一行代码`return (bits[i >> 6] & (1L << i)) == 0;`，判断对应位置的bit是否为0.比如给定`2`就判断`bits[0]`的第2+1位是否为0。
- `removeAt(int)`：移除指定位置的元素，当移除完全二叉树的最后一个叶节点时直接删除，否则把最后一个叶节点填入空缺。填入时有两种情况：叶节点大于等于/小于父节点。不知道是基于什么考虑，代码实现并没有直接比较，而是默认叶节点大于等于父节点，对应的是先默认执行`siftDown()`方法，然后若没有移动则再执行`siftUp()`方法。由于该方法还在`Iterator`的`remove()`方法中被调用，为了`Iterator`遍历时不丢失元素，该方法还会在最后一个叶节点被移动到i之前时（即`siftUp()`执行后）返回移动后的节点。

### 队列操作
#### 增
- `add(E)`, `offer(E)`：插入元素，`add`的实现是`offer`的套壳。`offer`的实现中，先对元素进行判空，然后调用`grow`进行可能的扩容操作，然后调用`siftUp(int, E)`进行堆的插入操作，最后更新容量。注意二者都只返回`true`或者抛出异常，因为其不是限定容量的队列。

#### 删
- `remove(E)`：若队列中存在`equals()`判断相等的元素，则移除之，方法是先调用`indexOf(E)`遍历找到对应的下标，然后调用`removeAt(int)`进行移除。发生移除则返回`true`，未找到则返回`false`。可以看出是`O(n)`的时间复杂度。
- `removeAll(Collection)`, `retainAll(Collection)`, `removeIf(Predicate)`：都是借助`bulkRemove()`方法实现的
- `poll()`：取出`queue[0]`，取出最后一个叶节点`queue[--size]`并将原位置置空，然后根据是否有`Comparator`调用对应的`siftDown(int, E, Object[], int)`方法整理堆
- `clear()`：清空所有元素，不改变数组大小

#### 查
- `peek(): E`
- `contains(E)`：调用`indexOf(E)`遍历获取对应元素的下标，可以看出是`O(n)`的时间复杂度。

### `Iterator`相关
- `forEach(Consumer)`：有点像`map()`，此处不允许对数组进行更改。

### 序列化实现 
- `writeObject(ObjectOutputStream)`：先写入除数组元素外的其他成员，然后另外写入数组的大小为`Math.max(2, size+1)`（可能是为了兼容旧版本jdk，总之这个数据没啥用），最后遍历数组写入所有元素。
- `readObject(ObjectInputStream)`：先读取除数组元素外的其他成员，然后另外读取并丢弃数组的大小，然后用读取的size新建数组并读入所有元素，最后执行`heapify()`。

## 学习总结
- 之前参加了￥9.9的7天算法训练营，所以第一周的大部分内容之前都学过了，本周重点放在刷题和看题解上。
- 本周开始使用五毒神掌方式刷题，总体上感觉还是挺不错的，但是有些比较复杂的题目题解也不能在短时间内看懂，多刷几遍感觉也是囫囵吞枣，这时候效率和理解程度应该怎么平衡呢？
- 截止目前已经刷了接近25道题，有易有难，感觉刷过的题基于艾宾浩斯曲线复习之后记忆还是挺清楚的。
- 感觉边界条件和测试很重要，有时甚至会影响整个题目的思路，这方面还有待加强。