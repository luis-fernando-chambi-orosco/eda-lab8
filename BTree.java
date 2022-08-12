package com.example.project;

public class BTree<T extends Comparable<? super T>> {
    public boolean add(T value) {
    	if(search(value) == true){
    		return false;
    	}
}
    public void remove( T value) {
       if (root == null){
            System.out.println("arbol vacio");
            return;
        }

        root.remove(value);

        if (root.num == 0){ 
           
            if (root.isLeaf)
                root = null;
            else
                root = root.children.elementAt(0);
        }
    }

    public boolean search(T value) {
        return root == null ? null : root.search(value);
    }

    public int size() {
        return size(root);
    }
}
