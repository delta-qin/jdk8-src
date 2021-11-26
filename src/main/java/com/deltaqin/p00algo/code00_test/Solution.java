package com.deltaqin.p00algo.code00_test;

class Solution {


    public int[] getLeastNumbers(int[] arr, int k) {

        if(arr == null || k <= 0) return new int[0];

        Heap h = new Heap(k+1);

        for(int item : arr){
            h.push(item);
            if(h.size() == k+1){
                h.pop();
            }
        }

        int[] res = new int[k];
        int i =0;
        while(h.size() > 0) {
            res[i] = h.pop();
            i++;
        }
        return res;

    }
}

class Heap{
    private int n = 0;
    private int[] arr = null;

    public Heap(int k){
        arr = new int[k];
    }

    public int size(){
        return n;
    }

    public void  sink(int index){
        int j = 0;
        int t = arr[index];

        while((j = 2*index+1) < n) {
            // 找子节点最大的那个
            if(j+1 < n && arr[j] < arr[j+1]){
                j++;
            }
            if(arr[j] > t) {
                arr[index] = arr[j];
                index = j;
            } else {
                break;
            }
        }
        arr[index] = t;
    }

    public void up(int index) {
        int j = 0;
        int t = arr[index];
        while(index > 0) {
            j = (index-1) >> 1;
            if(arr[j] < t) {
                arr[index] = arr[j];
                index = j;
            } else {
                break;
            }
        }
        arr[index] = t;
    }

    public void push(int x){
        arr[n++] = x;
        up(n-1);
    }

    public int pop() {
        int res = arr[0];
        arr[0] = arr[--n];
        sink(0);
        return res;
    }
}
