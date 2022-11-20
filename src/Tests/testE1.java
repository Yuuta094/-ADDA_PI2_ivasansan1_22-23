package Tests;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import ejercicio1.Ejercicio1;
import us.lsi.common.Pair;
import us.lsi.common.Trio;
import us.lsi.curvefitting.DataCurveFitting;
import utils.GraficosAjuste;
import utils.Resultados;
import utils.TipoAjuste;

public class testE1 {

	public static void main(String[] args) {
		System.out.println("--------------- PROBAMOS QUE LOS MÉTODOS FUNCIONEN CORRECTAMENTE -------");
		System.out.println(Ejercicio1.factorialDoubleItr(5));
		System.out.println(Ejercicio1.factorialDoubleRec(5));

		System.out.println(Ejercicio1.factorialBigIntItr(5));
		System.out.println(Ejercicio1.factorialBigIntRec(5));

		System.out.println("\n--------------- GENERAMOS LOS .csv Y REPRESENTAMOS	");
		// generadatosTiempoEjecucion();
		// generadatosTiempoEjecucion2();
		muestraGraficas();
	}

	// Definimos parámetros para los valores
	private static Integer nMin = 1; // n mínimo para el cálculo del factorial
	private static Integer nMax = 10000; // n máximo para el cálculo del factorial CON DOUBLE private static Integer
											// nMax2 = 5000; // n máximo para el cálculo del factorial CON BIGINT
											// private static Integer numSizes = 200; // número de problemas (número de
											// factoriales distintos a calcular)
	private static Integer nMax2 = 500; // n máximo para el cálculo del factorial CON BIGINT
	private static Integer numSizes = 60; // número de problemas (número de factoriales distintos a calcular)
	private static Integer numMediciones = 10; // número de mediciones de tiempo de cada caso (número de experimentos)
	private static Integer numIter = 50; // número de iteraciones para cada medición de tiempo
	private static Integer numIterWarmup = 1000; // número de iteraciones para warmup

	// Trios de métodos a probar con su tipo de ajuste y etiqueta para el nombre de
	// los datos
	// Trios para Double
	private static List<Trio<Function<Integer, Double>, TipoAjuste, String>> metodos = List.of(
			Trio.of(Ejercicio1::factorialDoubleRec, TipoAjuste.EXP, "factorialDoubleRec"),
			Trio.of(Ejercicio1::factorialDoubleItr, TipoAjuste.EXP, "factorialDoubleItr"));
	// Trios para BigInteger
	private static List<Trio<Function<Integer, BigInteger>, TipoAjuste, String>> metodos2 = List.of(
			Trio.of(Ejercicio1::factorialBigIntRec, TipoAjuste.EXP, "factorialBigIntRec"),
			Trio.of(Ejercicio1::factorialBigIntItr, TipoAjuste.EXP, "factorialBigIntItr"));

	// Generamos datos Double
	public static void generadatosTiempoEjecucion() {
		for (Trio<Function<Integer, Double>, TipoAjuste, String> element : metodos) {
			String datosalida = String.format("datos/Tiempos%s.csv", element.third());
			testTiemposEjecucion(nMin, nMax,

					element.first(), datosalida);
		}
	}

	// Generamos datos BigInteger
	public static void generadatosTiempoEjecucion2() {
		for (Trio<Function<Integer, BigInteger>, TipoAjuste, String> element : metodos2) {
			String datosalida = String.format("datos/Tiempos%s.csv", element.third());

			testTiemposEjecucion2(nMin, nMax2, element.first(), datosalida);
		}
	}

	public static void testTiemposEjecucion(Integer nMin, Integer nMax, Function<Integer, Double> funcion,
			String datosalida) {

		// Hacemos un Map con el tamaño del problema como clave y el tiempo calculado
		// como valor
		Map<Problema, Double> tiempos = new HashMap<>();

		// Iteramos las veces especificadas para sacar los menores tiempos
		for (int iter = 0; iter < numMediciones; iter++) {
//			Pasamos por todos los posibles tamaños
			for (int i = 0; i < numSizes; i++) {
				double r = (nMax - nMin) / (numSizes - 1);
				Integer tam = (Integer.MAX_VALUE / nMax > i) ? nMin + i * (nMax - nMin) / (numSizes - 1)
						: nMin + (int) (r * i);
//			Obtenemos el tamaño del problema actual
				Problema p = Problema.of(tam);

//			Hacemos warmup
				warmup(funcion, 10);

//			Obtenemos el tiempo empleado en cada iteracion y lo guardamos en un array
				Double[] res = new Double[numIter];
				Long t0 = System.nanoTime();
				for (int z = 0; z < numIter; z++) {
					res[z] = funcion.apply(tam);
				}
				Long t1 = System.nanoTime();
//			Actualizamos los tiempos del Map con el nuevo tiempo calculado para el tamaño en cuestión
				actualizaTiempos(tiempos, p, Double.valueOf((t1 - t0) / numIter));
			}
		}

		// Guardamos los resultados en un fichero
		Resultados.toFile(tiempos.entrySet().stream()
				.map(tiempo -> TamTiempo.of(tiempo.getKey().tam(), tiempo.getValue())).map(TamTiempo::toString),
				datosalida, true);
	}

	// Lo mismo pero para BigInteger, en vez de Double
	public static void testTiemposEjecucion2(Integer nMin, Integer nMax, Function<Integer, BigInteger> funcion,
			String datosalida) {

		Map<Problema, Double> tiempos = new HashMap<>();

		for (int iter = 0; iter < numMediciones; iter++) {
			for (int i = 0; i < numSizes; i++) {

				double r = (nMax - nMin) / (numSizes - 1);
				Integer tam = (Integer.MAX_VALUE / nMax > i) ? nMin + i * (nMax - nMin) / (numSizes - 1)
						: nMin + (int) (r * i);
				Problema p = Problema.of(tam);
				warmup2(funcion, 10);
				BigInteger[] res = new BigInteger[numIter];
				Long t0 = System.nanoTime();
				for (int z = 0; z < numIter; z++) {
					res[z] = funcion.apply(tam);
				}
				Long t1 = System.nanoTime();
				actualizaTiempos(tiempos, p, Double.valueOf((t1 - t0) / numIter));
			}
		}

		Resultados.toFile(tiempos.entrySet().stream()
				.map(tiempo -> TamTiempo.of(tiempo.getKey().tam(), tiempo.getValue())).map(TamTiempo::toString),
				datosalida, true);
	}

	// Record para almacenar el tamaño del problema en cada caso
	private record Problema(Integer tam) {
		public static Problema of(Integer tam) {
			return new Problema(tam);
		}
	}

	// Funciones para warmup
	private static void warmup(Function<Integer, Double> fun, Integer n) {
		for (int i = 0; i < numIterWarmup; i++) {
			fun.apply(n);
		}
	}

	private static void warmup2(Function<Integer, BigInteger> fun, Integer n) {
		for (int i = 0; i < numIterWarmup; i++) {
			fun.apply(n);
		}
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
		// Lo mismo para los métodos BigInteger
		for (int i = 0; i < metodos2.size(); i++) {
			String datosalida = String.format("datos/Tiempos%s.csv", metodos2.get(i).third());
			datosSalida.add(datosalida);
			String label = metodos2.get(i).third();
			System.out.println(label);
			TipoAjuste tipoAjuste = metodos2.get(i).second();
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
