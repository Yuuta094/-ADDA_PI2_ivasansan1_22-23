package ejercicio4;

import java.util.List;

import us.lsi.tiposrecursivos.Tree;
import us.lsi.tiposrecursivos.Tree.TEmpty;
import us.lsi.tiposrecursivos.Tree.TLeaf;
import us.lsi.tiposrecursivos.Tree.TNary;

public class ArbolNarioE4 {

	public static Boolean ejercicio4_Nario(Tree<String> tree) {
		return ejercicio4_Nario(tree, false);
	}

	public static Boolean ejercicio4_Nario(Tree<String> tree, Boolean f) {

		return switch (tree) {
		case TEmpty<String> t -> f;
		case TLeaf<String> t -> f;
		case TNary<String> t -> {
			String palabra = t.label().toString();       //la palabra a analizar
			Integer SumVocales = vocales(palabra);		 //las vocales de esa palabra
			Integer TotalVocales = 0;					 //Total de vocales
			List<Tree<String>> child = t.elements();     //Hijos del arbol
			
			if (t.height() == 1) {						 //Si el arbol solo tiene raiz e hijos A(B,C)
				for (Tree<String> raiz : t.elements()) {
					SumVocales = vocales(palabra) + SumVocales;
					TotalVocales = vocales(t.elements().toString());
					if (TotalVocales == SumVocales) {
						f = ejercicio4_Nario(raiz, true);
					}
				}
			}
			for (Tree<String> hijos : child) {           //En caso de que tenga raiz, nodo e hijos A(B(C,D),E(F,G))
				SumVocales = vocales(palabra) + SumVocales;
				TotalVocales = vocales(hijos.elements().toString());  
				if (TotalVocales == SumVocales) {
					f = ejercicio4_Nario(hijos, true);
				}
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