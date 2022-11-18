package ejercicio3;

import java.util.List;

import us.lsi.common.List2;
import us.lsi.tiposrecursivos.Tree;
import us.lsi.tiposrecursivos.Tree.TEmpty;
import us.lsi.tiposrecursivos.Tree.TLeaf;
import us.lsi.tiposrecursivos.Tree.TNary;

public class ArbolNarioE3 {

	/*
	3. Dados un árbol binario de caracteres y un carácter, diseñe un algoritmo que devuelva
una lista con todas las cadenas que se forman desde la raíz a una hoja no vacía, excluyendo
aquellas cadenas que contengan dicho carácter. Proporcione una solución también para
árboles n-arios.

 */
	

public static List<String> ejercicio3_Nario(Tree<Character> tree, char c) {
	return ejercicio3_Nario(tree, c, List2.empty(), "");
}

public static List<String> ejercicio3_Nario(Tree<Character> tree, char c, List<String> ls, String cadena) {

	return switch (tree) {
	case TEmpty<Character> t -> ls;
	case TLeaf<Character> t -> {
		Character letra = t.label();					  // Obeter el la letra de ese nivel
		cadena = cadena + letra; 						 // Guardar esa letra en una cadena
		if (!cadena.contains(String.valueOf(c))) { 		// Si pasa las restricciones....
			ls.add(cadena); 								// Guarda esa convinación en la lista
		}
		yield ls;
	}
	case TNary<Character> t -> {
		Character letra = t.label(); 
		String aux = cadena + letra;                 // Me hace falta un String aux porque "cadena" debe ser una variable final
		if (!cadena.contains(String.valueOf(c))) {
			t.elements().forEach(child -> 
			ejercicio3_Nario(child, c, ls, aux));
		}
		yield ls;
	}
	};
 }	
}
