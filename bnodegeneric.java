package com.example.project;
import java.util.*;
public class bnodegeneric<T extends Comparable <T>>{


    Vector<T> keys; 
    int MinDeg; 
    Vector<bnodegeneric<T>> children; 
    int num; 
    boolean isLeaf; 

    // Constructor
    public bnodegeneric(int deg,boolean isLeaf){

        this.MinDeg = deg;
        this.isLeaf = isLeaf;
        this.keys = new Vector<T>(); 
        this.children = new Vector<bnodegeneric<T>>();
        this.num = 0;
    }


    public int findKey(T key){

        int idx = 0;
        while (idx < num && keys.get(idx).compareTo(key) < 0)
            ++idx;
        return idx;
    }


    public void remove(T key){

        int idx = findKey(key);
        if (idx < num && keys.get(idx).compareTo(key) == 0){ 
            if (isLeaf) 
                removeFromLeaf(idx);
            else 
                removeFromNonLeaf(idx);
        }
        else{
            if (isLeaf){ 
                System.out.printf("The key %d is does not exist in the tree\n",key);
                return;
            }

            
            boolean flag = idx == num; 
            
            if (children.get(idx).num < MinDeg) 
                fill(idx);

            if (flag && idx > num)
                children.get(idx-1).remove(key);
            else
                children.get(idx).remove(key);
        }
    }

    public void removeFromLeaf(int idx){

        // Shift from idx
        for (int i = idx +1;i < num;++i)
            keys.set(i - 1, keys.get(i));
        num --;
    }

    public void removeFromNonLeaf(int idx){

        T key = keys.get(idx);
        if (children.get(idx).num >= MinDeg){
            T pred = getPred(idx);
            keys.set(idx, pred);
            children.get(idx).remove(pred);
        }
        else if (children.get(idx + 1).num >= MinDeg){
            T succ = getSucc(idx);
            keys.set(idx, succ);
            children.get(idx + 1).remove(succ);
        }
        else{
             merge(idx);
            children.get(idx).remove(key);
        }
    }

    public T getPred(int idx){ 
        bnodegeneric<T> cur = children.get(idx);
        while (!cur.isLeaf)
        	cur = cur.children.get(cur.num);
        return cur.keys.get(cur.num - 1);
    }

    public T getSucc(int idx){ // Subsequent nodes are found from the right subtree all the way to the left

        // Continue to move the leftmost node from children[idx+1] until it reaches the leaf node
        bnodegeneric<T> cur = children.get(idx);
        while (!cur.isLeaf)
            cur = cur.children.get(0);
        return cur.keys.get(0);
    }

    // Fill children[idx] with less than MinDeg keys
    public void fill(int idx){

        // If the previous child node has multiple MinDeg-1 keys, borrow from them
        if (idx != 0 && children.get(idx-1).num >= MinDeg)
            borrowFromPrev(idx);
        // The latter sub node has multiple MinDeg-1 keys, from which to borrow
        else if (idx != num && children.get(idx+1).num >= MinDeg)
            borrowFromNext(idx);
        else{
            // Merge children[idx] and its brothers
            // If children[idx] is the last child node
            // Then merge it with the previous child node or merge it with its next sibling
            if (idx != num)
                merge(idx);
            else
                merge(idx-1);
        }
    }

   
    public void borrowFromPrev(int idx){
        bnodegeneric<T> child = children.get(idx);
        bnodegeneric<T> sibling = children.get(idx + 1);
        
            child.keys.set(i+1, child.keys.elementAt(i));

        if (!child.isLeaf){ 
            for (int i = child.num; i >= 0; --i)
             child.children.set(i+1,child.children.elementAt(i));
        }

        
        child.keys.set(0, keys.elementAt(idx-1));
        if (!child.isLeaf) 
           child.children.set(0,sibling.children.elementAt(sibling.num));

        // Move the last key of sibling up to the last key of the current node
        keys.set(idx-1, sibling.keys.elementAt(sibling.num-1));
        child.num += 1;
        sibling.num -= 1;
    }

    // Symmetric with borowfromprev
    public void borrowFromNext(int idx){

      bnodegeneric<T> child = children.get(idx);
        bnodegeneric<T> sibling = children.get(idx + 1); 

          child.keys.set(child.num, keys.get(idx));

       if (!child.isLeaf)
        	child.children.set(child.num + 1, sibling.children.get(0));

        keys.set(idx, sibling.keys.get(0));
        for (int i = 1; i < sibling.num; ++i)
        	sibling.keys.set(i - 1, sibling.keys.get(i));

        if (!sibling.isLeaf){
            for (int i= 1; i <= sibling.num;++i)
            	sibling.children.set(i - 1, sibling.children.get(i));
        }
        child.num += 1;
        sibling.num -= 1;
    }

    // Merge childre[idx+1] into childre[idx]
    public void merge(int idx){

        bnodegeneric<T> child = children.get(idx);
        bnodegeneric<T> sibling = children.get(idx + 1);
    
        child.keys.set(MinDeg - 1, keys.get(idx));

        for (int i =0 ; i< sibling.num; ++i)
        	child.keys.set(i + MinDeg, sibling.keys.get(i));

        if (!child.isLeaf){
            for (int i = 0;i <= sibling.num; ++i)
            	child.children.set(i + MinDeg, sibling.children.get(i));
        }

        for (int i = idx+1; i<num; ++i)
        	keys.set(i - 1, keys.get(i));

        for (int i = idx+2;i<=num;++i)
        	children.set(i - 1, children.get(i));

        child.num += sibling.num + 1;
        num--;
    }
    public void splitChild(int i ,bnodegeneric<T> y){
        bnodegeneric <T> z = new bnodegeneric<T>(y.MinDeg,y.isLeaf);
        z.num = MinDeg - 1;

       
        for (int j = 0; j < MinDeg-1; j++)
        	z.keys.set(j, y.keys.get(j + MinDeg));
        if (!y.isLeaf){
            for (int j = 0; j < MinDeg; j++)
            	z.children.set(j, y.children.get(j + MinDeg));
        }
        y.num = MinDeg-1;

        
        for (int j = num; j >= i+1; j--)
            children.set(j + 1, children.get(j));
        
        children.set(i + 1, z);

       
        for (int j = num-1;j >= i;j--)
        	keys.set(j + 1, keys.get(j));
        keys.set(i, y.keys.get(MinDeg - 1));

        num = num + 1;
    }



    public bnodegeneric <T>search(T key){
        int i = 0;
        while (i < num && (key.compareTo(keys.get(i)) > 0))
            i++;

        if (keys.get(i).compareTo(key) == 0)
            return this;
        if (isLeaf)
            return null;
        return children.get(i).search(key);
    }
}
