# Week02学习笔记
## `HashMap`代码分析
> 分析的代码版本为JavaSE-13.0.2. 本次仅分析与核心接口(put, get)相关的代码，至于扩容、桶结构的变化等留到以后分析。
### 概述
- `HashMap`类继承了`AbstractMap`类，实现了`Map`、`Cloneable`、`Serializable`接口。
- `HashMap`是hash表的Map实现，其实现了`Map`的所有可选方法。它与`Hashtable`大致一致，除了`Hashtable`是`synchronized`的并且不允许`null`值和key。
- `HashMap`有许多意思容易混淆的术语：
    - 桶：桶是Hash函数映射到的具体存储位置，因此可能发生碰撞。一个桶存储一个或多个`Entry`，可能为列表或树结构。
    - `Entry`：一个`Entry`中包含一个Key和一个Value。
- 影响`HashMap`性能的主要有两个因素：初始容量和装填因子(load factor)。容量是桶的大小（比如用数组存放键的话就是数组的大小），装填因子是桶已用空间/桶的总空间，是一个阈值，经验值为0.75，当超过阈值后桶就会进行扩容，扩充为大约原容量的两倍。
    - 较大的装填因子空间开销更小，但是查找时的时间开销更大（在包括`get`和`put`的大多数操作中都会有体现）。
    - 在设置初始容量时，需要考虑应存放的Entry数量及其装填因子，以尽量减少扩容操作。
    - 遍历`HashMap`的时间与`HashMap`的“容量”+“大小”（键值对的数量）成正比，因此不要设置过大的初始容量或过小的装填因子。
- 若很多键的`hashCode`一致，则本类可能会使用`Comparable`接口的性质来辅助操作。

### 成员
- `int TREEIFY_THRESHOLD = 8`：当某个桶有大于等于8个键结点时，把该桶的结构由列表转换为树
- `int UNTREEIFY_THRESHOLD = 6`：当某个桶变为小于等于6个键结点时，把该桶的结构由树转换为列表
- `int MIN_TREEIFY_CAPACITY = 64`：当桶数量超过64时才考虑将桶结构转换为树。
- `class Node<K, V> implements Map.Entry<K, V>`：桶结点的基本实现类，除了存储键值对外，还存储hash值和链表中的下一结点的引用。
- `transient Node<K, V>[] table`：存储桶结点的数组
- `transient Set<Map.Entry<K, V>> entrySet`：
- `transient int size`：键值对的计数
- `float loadFactor`：装填因子

### 方法
#### 工具方法
- `hash(Object key): int`：当`key`不为空时，计算hash为`(h=key.hashCode()) ^ (h>>>16)`，把hashCode的高位传播至低位。因为`HashMap`采用`hash & (table.length-1)`的方式来进行映射，虽然hash可以有32位，但是由于`table`长度有限，实际上参与映射的只有低位，因此需要把高位的信息掺入低位中，这样也能减少碰撞。其实可以多次异或，但是基于性能和效果的平衡考虑此处只进行了一次异或。
- `getNode(int hash, Object key): Node<K,V>`：
    - 先检查`table`是否存在及是否为空，然后检查首个元素是否为空
    - 然后检查按照hash访问的首元素是否为所需的元素。具体是双保险（为什么？），检查hash相等且首元素的key就是给定的key或者二者`equals()`判定一致，此处先用`==`检查可能是利用短路计算来加速。
    - 然后再根据桶结构的不同使用不同的方式查找。若桶是树结构，则调用`first.getTreeNode(hash, key)`查找对应的值。
    - 若不是树结构，则通过next域遍历桶内的其他节点，依然是通过上述的方法寻找所需的元素。
- `putVal(int hash, Object key, Object value, boolean onlyIfAbsent, boolean evict): V`：
    - 若`table`为空或长为0则调用`resize()`
    - 若通过hash访问的位置为空，则直接根据参数构造一个新的`Node`存入即可。
    - 否则用一个引用`e`记录待操作的`Node`。
        - 若该位置下节点的key也相同，则直接把原节点记作`e`。
        - 若原结点是一棵树，则调用`putTreeVal`进行放置，并且把返回值记作`e`。
        - 否则（桶为链表时发生碰撞）遍历`Node`，若找到`key`相同的结点，则记为`e`；若遍历到链尾也没有找到`key`相同的结点，则在链尾构造一个新的`Node`，若此时算上桶中的元素链长≥`TREEIFY_THRESHOLD`，则调用`treeifyBin(table, hash)`方法把桶转换为树。
        - 之后检查`e`，若`e`不为空（什么时候会为空？），则用一个引用`oldValue`记录旧值。当`!onlyIfAbsent`或`oldValue==null`时，替换`e`的值为新值。然后调用`afterNodeAccess`，并返回旧值。
    - 对于hash访问的位置为空时，此时对表进行了“结构性”修改，`modCount++`。若此时`++size`大于容量\*装填因子则调用`resize()`。然后调用`afterNodeInsertion(evict)`方法，最后返回`null`。

#### 接口方法
- `get(Object key): V`：调用`getNode(hash, key)`方法获取对应的值，hash用`hash(key)`来计算。找不到则返回`null`。注意返回`null`时有可能存在键，只是其值为`null`。
- `containsKey(Object key): boolean`：调用`getNode(hash, key)`方法，返回返回值是否为`null`，因此效率与`get`基本差不多。注意返回`false`时有可能存在键，只是其值为`null`。
- `put(K key, V value): V`：调用`putVal()`方法进行存放，其返回值为返回值。