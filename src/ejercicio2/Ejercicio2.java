package ejercicio2;

import java.util.List;

import us.lsi.common.IntPair;
import us.lsi.common.List2;

public class Ejercicio2 {
	
	public static Void quicksort(List<Integer> l, Integer umbral) {
		if (l.size() <= umbral) {
			ordenaInsercion(l);
		} else {
			IntPair ab = bandera(l);
			quicksort(l.subList(0, ab.first()), umbral);
			quicksort(l.subList(ab.second(), l.size()), umbral);
		}
		return null;
	}

	private static void ordenaInsercion(List<Integer> l) {
		int i = 1, j;

		while (i < l.size()) {
			Integer elem = l.get(i);
			if (elem < l.get(i - 1)) {
				j = i - 1;
				while (j >= 0) {
					if (j == 0 || elem >= l.get(j - 1)) {
						l.remove(i);
						l.add(j, elem);
						j = 0;
					}
					j--;
				}
			}
			i++;
		}
	}

	private static IntPair bandera(List<Integer> l) {
		Integer pivote = l.get(0);
		int a = 0;
		int b = 0;
		int c = l.size();

		while (b < c) {
			Integer i = l.get(b);
			if (i < pivote) {
				List2.intercambia(l, a, b);
				a++;
				b++;
			} else if (i > pivote) {
				List2.intercambia(l, b, c - 1);
				c--;
			} else {
				b++;
			}
		}
		return IntPair.of(a, b);
	}

}
