package ejercicio4;

import us.lsi.tiposrecursivos.BinaryTree;
import us.lsi.tiposrecursivos.BinaryTree.BEmpty;
import us.lsi.tiposrecursivos.BinaryTree.BLeaf;
import us.lsi.tiposrecursivos.BinaryTree.BTree;

public class ArbolBinarioE4 {

	/*
	 * 4. Dado un árbol binario de cadena de caracteres, diseñe un algoritmo que
	 * devuelva cierto si se cumple que, para todo nodo, el número total de vocales
	 * contenidas en el subárbol izquierdo es igual al del subárbol derecho.
	 * Proporcione una solución también para árboles n-arios.
	 * 
	 */

	public static Boolean ejercicio4_Binario(BinaryTree<String> tree) {
		return ejercicio4_Binario(tree, false);
	}

	public static Boolean ejercicio4_Binario(BinaryTree<String> tree, Boolean f) {

		return switch (tree) {
		case BEmpty<String> t -> f;
		case BLeaf<String> t -> f;
		case BTree<String> t -> {
					if (vocales(t.left().toString()) == vocales(t.right().toString())) {
						f = ejercicio4_Binario(t.left(), true);
						f = ejercicio4_Binario(t.right(), true);
					}
				yield f;
		}
		};
	}

	public static Integer vocales(String s) {
		Integer n = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == 'a' || s.charAt(i) == 'e' || s.charAt(i) == 'i' || s.charAt(i) == 'o'
					|| s.charAt(i) == 'u') {
				n++;
			}
		}
		return n;
	}
}