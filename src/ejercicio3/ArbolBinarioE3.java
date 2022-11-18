package ejercicio3;

import java.util.List;

import us.lsi.common.List2;
import us.lsi.tiposrecursivos.BinaryTree;

import us.lsi.tiposrecursivos.BinaryTree.BEmpty;
import us.lsi.tiposrecursivos.BinaryTree.BLeaf;
import us.lsi.tiposrecursivos.BinaryTree.BTree;

public class ArbolBinarioE3 {

	
/*
	3. Dados un �rbol binario de caracteres y un car�cter, dise�e un algoritmo que devuelva
una lista con todas las cadenas que se forman desde la ra�z a una hoja no vac�a, excluyendo
aquellas cadenas que contengan dicho car�cter. Proporcione una soluci�n tambi�n para
�rboles n-arios.

 */
	

public static List<String> ejercicio3_Binario(BinaryTree<Character> tree, char c) {
	return ejercicio3_Binario(tree, c, List2.empty(), "");
}

public static List<String> ejercicio3_Binario(BinaryTree<Character> tree, char c, List<String> ls, String cadena) {

	return switch (tree) {
	case BEmpty<Character> t -> ls;
	case BLeaf<Character> t -> {
		Character letra = t.label();					  // Obeter el la letra de ese nivel
		cadena = cadena + letra; 						 // Guardar esa letra en una cadena
		if (!cadena.contains(String.valueOf(c))) { 		// Si pasa las restricciones....
			ls.add(cadena); 								// Guarda esa convinaci�n en la lista
		}
		yield ls;
	}
	case BTree<Character> t -> {
		Character letra = t.label();
		cadena = cadena + letra;
		if (!cadena.contains(String.valueOf(c))) {
			ls = ejercicio3_Binario(t.left(), c, ls, cadena);   // Obetenemos el arbol izquierdo
			ls = ejercicio3_Binario(t.right(), c, ls, cadena); // Obetenemos el arbol derecho
		}
		yield ls;
	}
	};
	}
}