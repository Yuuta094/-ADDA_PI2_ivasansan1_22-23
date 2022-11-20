package Tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import ejercicio2.Ejercicio2;
import us.lsi.common.Files2;
import us.lsi.common.List2;
import us.lsi.common.Pair;
import us.lsi.common.Trio;
import us.lsi.curvefitting.DataCurveFitting;
import utils.FicherosListas;
import utils.GraficosAjuste;
import utils.Resultados;
import utils.TipoAjuste;
import utils.FicherosListas.PropiedadesListas;

public class testE2 {

	public static void main(String[] args) {

		System.out.println("--------------- PROBAMOS QUE LOS MÉTODOS FUNCIONEN CORRECTAMENTE -------");
		List<Integer> l = new ArrayList<>();
		l.add(5);
		l.add(2);
		l.add(9);
		l.add(5);
		l.add(3);
		l.add(7);
		List<Integer> l2 = new ArrayList<>(l);
		System.out.println("Lista original: " + l);

		Ejercicio2.quicksort(l, 3);
		System.out.println("Lista ordenada con bandera (umbral pequeño): " + l);

		Ejercicio2.quicksort(l2, 14);
		System.out.println("Lista ordenada sin bandera (umbral grande): " + l2);

		System.out.println("\n--------------- GENERAMOS LOS .csv Y REPRESENTAMOS--------------------");
		generadatosTiempoEjecucion();
		muestraGraficas();
	}

	// Definimos parámetros para los valores de las listas y las mediciones
	private static Integer sizeMin = 100; // tamaño mínimo de lista
	private static Integer sizeMax = 300; // 50000; // tamaño máximo de lista 
	

	private static Integer maxValue = 100;
	private static Integer minValue = 0;
	private static Integer numSizes = 30; // 110; // número de tamaños de listas
	private static Integer numListPerSize = 1; // PopiedadesListas lo requiere, pero no nos afecta ni al generar ni al
												// ordenar
	private static Integer numCasesPerList = 1; // igual que lo anterior
	private static Random rr = new Random(System.nanoTime()); // inicializamos una vez y compratimos el valor

	private static List<Integer> umbrales = List.of(4, 25, 100, 500);

	private static Integer numMediciones = 5; // número de mediciones de tiempo de cada caso (número de experimentos)
	private static Integer numIter = 50; // número de iteraciones para cada medición de tiempo
	private static Integer numIterWarmup = 1000; // número de iteraciones para warmup

	private static List<Trio<BiFunction<List<Integer>, Integer, Void>, TipoAjuste, String>> metodos = List.of(
			Trio.of(Ejercicio2::quicksort, TipoAjuste.NLOGN_0, "Quicksort_Umbral4"),
			Trio.of(Ejercicio2::quicksort, TipoAjuste.NLOGN_0, "Quicksort_Umbral25"),
			Trio.of(Ejercicio2::quicksort, TipoAjuste.NLOGN_0, "Quicksort_Umbral100"),
			Trio.of(Ejercicio2::quicksort, TipoAjuste.NLOGN_0, "Quicksort_Umbral500"));

	private static void generadatosTiempoEjecucion() {

		for (Trio<BiFunction<List<Integer>, Integer, Void>, TipoAjuste, String> metodo : metodos) {
			String datosalida = String.format("datos/Tiempos%s.csv", metodo.third());

			testTiemposEjecucion(sizeMin, sizeMax, umbrales.get(metodos.indexOf(metodo)), metodo.first(),
					datosalida);
		}
	}

	private static void testTiemposEjecucion(Integer nMin, Integer nMax, Integer umbral,
			BiFunction<List<Integer>, Integer, Void> funcion, String fichero) {

		// Hacemos un Map con el tamaño del problema como clave y el tiempo calculado
		// como valor
		Map<Problema, Double> tiempos = new HashMap<>();

		for (int iter = 0; iter < numMediciones; iter++) {
			for (int i = 0; i < numSizes; i++) {
				double r = (nMax - nMin) / (numSizes - 1);
				Integer tam = (Integer.MAX_VALUE / nMax > i) ? nMin + i * (nMax - nMin) / (numSizes - 1)
						: nMin + (int) (r * i);
//			Obtenemos el tamaño del problema actual
				Problema p = Problema.of(tam);

//			Generamos una lista aleatoria del tamaño actual
				PropiedadesListas prop = PropiedadesListas.of(sizeMin, sizeMax, numSizes, minValue, maxValue,
						numListPerSize, numCasesPerList, rr);
				List<Integer> List = generaListaEnteros(tam, prop);

//			Hacemos warmup
				warmup(funcion, List, umbral);

//			Obtenemos el tiempo empleado en cada iteracion y lo guardamos en un array
				Void[] res = new Void[numIter];
				Long t0 = System.nanoTime();
				for (int z = 0; z < numIter; z++) {
					List<Integer> listaMedicion = new ArrayList<>(List);
					res[z] = funcion.apply(listaMedicion, umbral);
				}
				Long t1 = System.nanoTime();
//			Actualizamos los tiempos del Map con el nuevo tiempo calculado para el tamaño en cuestión
				actualizaTiempos(tiempos, p, Double.valueOf((t1 - t0) / numIter));
			}
		}

		Resultados.toFile(tiempos.entrySet().stream()
				.map(tiempo -> TamTiempo.of(tiempo.getKey().tam(), tiempo.getValue())).map(TamTiempo::toString),
				fichero, true);
	}

	// Record para almacenar el tamaño del problema en cada caso
	private record Problema(Integer tam) {
		public static Problema of(Integer tam) {
			return new Problema(tam);
		}
	}

	// Función para warmup
	private static void warmup(BiFunction<List<Integer>, Integer, Void> fun, List<Integer> l, Integer umbral) {
		for (int i = 0; i < numIterWarmup; i++) {
			List<Integer> warmUpList = new ArrayList<>(l);
			fun.apply(warmUpList, umbral);
		}
	}

	// Función para creación de listas aleatorias
	public static List<Integer> generaListaEnteros(Integer sizeList, PropiedadesListas p) {
		List<Integer> ls = new ArrayList<>();
		for (int i = 0; i < sizeList; i++) {
			ls.add(p.minValue() + p.rr().nextInt(p.maxValue() - p.minValue()));
		}
		return ls;
	}

	// Función para actualziar los tiempos de cada problema (nos vamos quedando con
	// el menor)
	private static void actualizaTiempos(Map<Problema, Double> tiempos, Problema p, Double tiempo) {
		if (!tiempos.containsKey(p) || tiempos.get(p) > tiempo) {
			tiempos.put(p, tiempo);
		}
	}

	// Record para facilitar el guardado de datos en fichero
	private record TamTiempo(Integer tam, Double t) {
		public static TamTiempo of(Integer tam, Double t) {
			return new TamTiempo(tam, t);
		}

		@Override
		public String toString() {
			return String.format("%d,%.0f", tam, t);
		}
	}

	public static void muestraGraficas() {

		List<String> datosSalida = new ArrayList<>();
		List<String> labels = new ArrayList<>();

		// Empezamos con los métodos Double
		for (int i = 0; i < metodos.size(); i++) {
//			Obtenemos el nombre de cada fichero y lo guardamos en una lista, al igual que la labe y el TipoAjuste para mostrar la gráfica
			String datosalida = String.format("datos/Tiempos%s.csv", metodos.get(i).third());
			datosSalida.add(datosalida);
			String label = metodos.get(i).third();
			System.out.println(label);
			TipoAjuste tipoAjuste = metodos.get(i).second();
			GraficosAjuste.show(datosalida, tipoAjuste, label);

			// Obtener ajusteString para mostrarlo en gráfica combinada
			Pair<Function<Double, Double>, String> parCurve = GraficosAjuste
					.fitCurve(DataCurveFitting.points(datosalida), tipoAjuste);
			String ajusteString = parCurve.second();
			labels.add(String.format("%s	%s", label, ajusteString));
		}

		// Mostramos la gráfica combinada
		GraficosAjuste.showCombined("Factorial", datosSalida, labels);
	}

}
