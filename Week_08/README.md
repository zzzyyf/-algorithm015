# Week08学习笔记
## 选择排序
```java
public void selectionSort(int[] nums){
    for(int i=0; i<nums.length; i++){
        // 从剩余元素中找到最小值
        int minIndex = i;
        for(int j=i;j<nums.length; j++){
            if(nums[j]<nums[minIndex]){
                minIndex = j;
            }
        }
        // 把最小值交换到i处
        int tmp = nums[i];
        nums[i] = nums[minIndex];
        nums[minIndex] = tmp;
    }
}

```
## 插入排序
```java
public void insertSort(int[] nums) {
    int i = 1, j, temp;
    // 缩小未排序区域
    for (; i < nums.length; i++) {
        // 在未排序区域中选择第一个值
        temp = nums[i];
        for (j = i - 1; j >= 0 && nums[j] > temp; j--) {
            // 稳定排序要求此处只有大于才向后移动
            nums[j + 1] = nums[j];
        }
        // 当前j不满足条件，后一个j才满足，循环结束时的状态不能直接拿来用
        nums[j + 1] = temp;
    }
}
```

## 冒泡排序
```java
public void bubbleSort(int[] nums){
    for(int i=0; i<nums.length; i++){
        // 从末尾开始逐个把当前最小值交换到开头
        for(int j=nums.length-2; j>=i; j--){
            if(nums[j]>nums[j+1]){
                int tmp = nums[j];
                nums[j] = nums[j+1];
                nums[j+1] = tmp;
            }
        }
    }
}

```