public class prueba {
	public static void main(String[] args) {
BTree<Integer> a = new BTree<Integer>();

        a.add(1);
        a.add(2);
        a.add(16);
        a.add(54);
        a.add(17);
        a.add(69);
        a.add(9);

        System.out.println("Arbol:");
        a.size();
        System.out.println("Eliminando elementos");
        a.remove(54);
        a.remove(69);
        System.out.println("Arbol después de haber eliminado:");
        a.size();
}
}