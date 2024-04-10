package com.example.demo.deal;

import com.google.common.collect.Lists;

import java.util.*;

/**
 * 类的描述
 *
 * @author hujiping
 * @date 2024/3/1 3:49 PM
 */
public class Compute {

    public static void main(String[] args) {

        // 字符串最长回文子串
        String text = "abaaba";
//        System.out.println(computeStr(text));
        
        // top k
        int[] nums = {1, 3, 1, 5, 2, 3};
//        System.out.println(computeTopK(nums,3));
        
        // 爬梯子
//        System.out.println(computeDp(5));
        
        // 合并数组
        int[] nums1 = {0,1,2,3,4,5};
        int[] nums2 = {1000000,1000001,1000002};
//        System.out.println(Arrays.toString(merge(nums1, 3, 4, nums2)));
        
        // 二叉树的最大深度
        TreeNode root = new TreeNode(1);
//        System.out.println(computeHigh(root));
        
        // 二叉树的左视图
        TreeNode tree = new TreeNode(1);
        tree.left = new TreeNode(2);
        tree.right = new TreeNode(3);
        tree.left.left = new TreeNode(4);
        tree.left.right = new TreeNode(5);

//        System.out.println(leftView(tree));
        
        //
        Deque<Integer> deque = new LinkedList<>();
//        deque.add(1); // 向链表末尾添加
        deque.push(1); // 向链表头部添加
        deque.pop(); // 拿出链表头部数据
        
        
        // 删除链表中第n个节点后返回首节点
        ListNode head = new ListNode(1);
        ListNode headNode = deleteNodeAtPositionOfN(head, 3);
        
        // 合并两个有序链表后返回首节点
        ListNode listNode1 = new ListNode(1);
        ListNode listNode2 = new ListNode(1);
        ListNode newHeadNode = mergeListNode(listNode1, listNode2);

        // 选择排序
        selectSort(new int[] {1,2,3});
        // 冒泡排序
        bubbleSort(new int[] {1,2,3});
        // 快排
        quickSort(new int[] {1,2,3});
    }

    private static void quickSort(int[] array) {
        
    }

    private static void bubbleSort(int[] array) {
        int len = array.length;
        for (int i = 0; i < len - 1; i ++) {
            for (int j = i + 1; j < len - 1; j ++) {
                if(array[j + 1] < array[j]){
                    int tmp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = tmp;
                }
            }
        }
    }

    private static void selectSort(int[] array) {
        int len = array.length;
        for (int i = 0; i < len - 1; i ++) {
            int min = i;
            for (int j = i + 1; j < len; j ++) {
                if(array[j] < array[min]){
                    min = j;
                }
            }
            int tmp = array[i];
            array[i] = array[min];
            array[min] = tmp;
        }
    }

    private static ListNode mergeListNode(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode current = dummy.next;
        
        while(l1 != null && l2 != null){
            if(l1.value < l2.value){
                current.next = l1;
                l1 = l1.next;
            }else {
                current.next = l2;
                l2 = l2.next;
            }
            current = current.next;
        }
        if(l1 != null){
            current.next = l1;
        }
        if(l2 != null){
            current.next = l2;
        }
        return dummy.next;
    }

    private static ListNode deleteNodeAtPositionOfN(ListNode head, int n) {
        ListNode result = new ListNode(0);
        result.next = head;

        ListNode current = head;
        ListNode pre = result;
        int position = 1;
        
        while (current != null) {
            if(position == n){
                pre.next = current.next;
                break;
            }
            position ++;
            pre = current;
            current = current.next;
        }
        
        return result.next;
    }

    static class ListNode {
        private int value;
        private ListNode next;

        public ListNode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public ListNode getNext() {
            return next;
        }

        public void setNext(ListNode next) {
            this.next = next;
        }
    }

    private static void leftView(TreeNode root) {
        if (root == null) {
            return;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                if (i == 0) {
                    System.out.println(node.value + " ");
                }
                if (node.left != null) {
                    queue.add(node.left);
                }
                if (node.right != null) {
                    queue.add(node.right);
                }
            }
        }
    }

    private static int computeHigh(TreeNode node) {
        if(node == null){
            return 0;
        }
        return Math.max(computeHigh(node.left), computeHigh(node.right)) + 1;
    }

    static class TreeNode {
        private int value;
        private TreeNode left;
        private TreeNode right;
        public TreeNode(int value) {
            this.value = value;
        }
    }
    
    public static int computeDp(int n){
        int[] dp = new int[n+1];
        dp[1] = 1;
        dp[2] = 2;
        for (int i = 3; i< dp.length; i++) {
            dp[i] = dp[i-1] + dp[i-2];
        }
        return dp[n];
    }

    public static int[] merge(int[] nums1, int m, int n, int[] nums2) {
        int startLen = m;
        int endLen = nums1.length - n - 1;
        int newArrayLen = startLen + nums2.length + endLen;
        int[] newArray = new int[newArrayLen];
        List<Integer> newList = new ArrayList<>();
        for (int num : nums1) {
            if(newList.size() >= m){
                break;
            }
            newList.add(num);
        }
        for (int num : nums2) {
            newList.add(num);
        }
        for(int i = n+1; i < nums1.length; i++) {
            newList.add(nums1[i]);
        }
        for(int i = 0; i < newList.size(); i++) {
            newArray[i] = newList.get(i);
        }
        return newArray;
    }
    
    private static List<Integer> computeTopK(int[] nums, int k) {
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>();
        for (int num : nums) {
            if(priorityQueue.contains(num)){
                continue;
            }
            priorityQueue.add(num);
            if(priorityQueue.size() > k){
                priorityQueue.poll();
            }
        }
        List<Integer> list = new ArrayList<>();
        while (!priorityQueue.isEmpty()){
            list.add(priorityQueue.poll());
        }
        return Lists.reverse(list);
    }
    

    private static String computeStr(String text) {
        int index = 0;
        int maxLength = 0;
        for (int i = 0; i < text.length(); i ++){
            int len1 = expandAroundCenter(text, i - 1, i + 1);
            int len2 = expandAroundCenter(text, i, i + 1);
            int max = Math.max(len1, len2);
            if(max > maxLength){
                index = i;
                maxLength = max;
            }
        }
        if(maxLength % 2 == 0){
            return text.substring(index + 1 - maxLength / 2, index + maxLength / 2 + 1);
        }
        return text.substring(index - maxLength / 2, index + maxLength / 2 + 1);
    }

    private static int expandAroundCenter(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        return right - left - 1;
    }
}
