package Tests;

import java.util.List;
import us.lsi.common.Files2;
import us.lsi.common.Pair;
import us.lsi.tiposrecursivos.BinaryTree;
import us.lsi.tiposrecursivos.Tree;

public class testE3 {

	public static void main(String[] args) {
		
		//Test_3_Binario();
		Test_3_Nario();
	}
	
	public static void  Test_3_Binario() {
		String file = "datos/Ejercicio3DatosEntradaBinario.txt";
		
		List<Pair<BinaryTree<Character>, Character>>
		inputs = Files2.streamFromFile(file).map(linea->{
			String [] aux = linea.split("#");
			BinaryTree<Character> tree = BinaryTree.parse(aux[0], s->s.charAt(0));
			char c = aux[1].charAt(0);
			return Pair.of(tree, c);
			}).toList();
		
		for (Pair<BinaryTree<Character>, Character> par: inputs) {
			BinaryTree<Character> tree = par.first();
			Character ch = par.second();
			List<String> res  = ejercicio3.ArbolBinarioE3.ejercicio3_Binario(tree, ch);
			System.out.println("Binario: " + tree + "\t#"+ ch + "\tresultado: " + res);
		}
	}
	
	public static void  Test_3_Nario() {
		String file = "datos/Ejercicio3DatosEntradaNario.txt";
		
		List<Pair<Tree<Character>, Character>>
		inputs = Files2.streamFromFile(file).map(linea->{
			String [] aux = linea.split("#");
			Tree<Character> tree = Tree.parse(aux[0], s->s.charAt(0));
			char c = aux[1].charAt(0);
			return Pair.of(tree, c);
			}).toList();
		
		for (Pair<Tree<Character>, Character> par: inputs) {
			Tree<Character> tree = par.first();
			Character ch = par.second();
			List<String> res  = ejercicio3.ArbolNarioE3.ejercicio3_Nario(tree, ch);
			System.out.println("N-ario: " + tree + "\t#"+ ch + "\tresultado: " + res);
		}
	}
}