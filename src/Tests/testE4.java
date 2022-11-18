package Tests;

import java.util.List;

import us.lsi.common.Files2;
import us.lsi.tiposrecursivos.BinaryTree;
import us.lsi.tiposrecursivos.Tree;

public class testE4 {

	public static void main(String[] args) {
		//Test_4_Binario();
		Test_4_Nario();
		}
		
		public static void  Test_4_Binario() {
			String file = "datos/Ejercicio4DatosEntradaBinario.txt";
			
			
			List<BinaryTree<String>> inputs = Files2
					.streamFromFile(file)
					.map(linea-> BinaryTree.parse(linea, s-> s.toString()))
					.toList();
			
			for (BinaryTree<String>tree: inputs) {
				Boolean res  = ejercicio4.ArbolBinarioE4.ejercicio4_Binario(tree);
				System.out.println("Binario: " + tree + "\tresultado: " + res);
			}
		}
		
		public static void  Test_4_Nario() {
			String file = "datos/Ejercicio4DatosEntradaNario.txt";
			
			
			List<Tree<String>> inputs = Files2
					.streamFromFile(file)
					.map(linea-> Tree.parse(linea, s-> s.toString()))
					.toList();
			
			for (Tree<String>tree: inputs) {
				Boolean res  = ejercicio4.ArbolNarioE4.ejercicio4_Nario(tree);
				System.out.println("N-Ario: " + tree + "\tresultado: " + res);
			}
		}
	}
