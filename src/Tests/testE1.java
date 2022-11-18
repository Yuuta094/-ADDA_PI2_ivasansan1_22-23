package Tests;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import Tests.testE1.TResultD;
import ejercicio1.Ejercicio1;
import us.lsi.common.Pair;
import us.lsi.common.Trio;
import us.lsi.curvefitting.DataCurveFitting;
import utils.GraficosAjuste;
import utils.Resultados;
import utils.TipoAjuste;

public class testE1 {

	public static void main(String[] args) {
		
		generadatosTiempoEjecucion();
		muestraGraficas();
	}
	
	private static Integer nMin = 1; // n mínimo
	private static Integer nMaxRec = 60; // n máximo para el fibonacci recursivo sin memoria 
	private static Integer nMaxIter = 70; // n máximo para el fibonacci no exponencial
	private static Integer numSizes = 80; // número de problemas
	private static Integer numMediciones = 90; //10; // número de mediciones de tiempo de cada caso (número de experimentos)
												// para exponencial se puede reducir 
	private static Integer numIter = 120; //50; // número de iteraciones para cada medición de tiempo
											// para exponencial se puede reducir 
	private static Integer numIterWarmup = 2000; // número de iteraciones para warmup
	
	// Trios de métodos a probar con su tipo de ajuste y etiqueta para el nombre de los datos
	private static List<Trio<Function<Integer, Number>, TipoAjuste, String>> metodosBigInteger = 
			List.of(
				Trio.of(Ejercicio1::factorialBigIntRec, TipoAjuste.EXP, "factorial_BigInt_Rec"), 
				Trio.of(Ejercicio1::factorialBigIntItr, TipoAjuste.EXP, "factorial_BigInt_Itr")
			);
	
	private static List<Trio<Function<Integer, Number>, TipoAjuste, String>> metodosDouble = 
			List.of(
					Trio.of(Ejercicio1::factorialDoubleRec, TipoAjuste.EXP, "factorial_Double_Rec"), 
					Trio.of(Ejercicio1::factorialDoubleItr, TipoAjuste.EXP,"factorial_Double_Itr")
			);


	
	private static <E> void generadatosTiempoEjecucionMetodos(List<Trio<Function<E, Number>, TipoAjuste, String>> metodos) {
		
		for (int i=0; i<metodos.size(); i++) { 
			int numMax = nMaxIter;//i==0 ? nMaxRec : nMaxIter; 
			Boolean flagExp = false; //i==0 ? true : false;
			
			String datosalida = String.format("datos/Tiempos%s.csv",
					metodos.get(i).third());
			
			testTiemposEjecucion(nMin, numMax, 
						metodos.get(i).first(),
						datosalida,
						flagExp);
			}
		
	}
	
	public static void generadatosTiempoEjecucion() {
		
		generadatosTiempoEjecucionMetodos(metodosBigInteger);
		generadatosTiempoEjecucionMetodos(metodosDouble);
	}
	
	
	public static <E> void muestraGraficasMetodos(List<Trio<Function<E, Number>, TipoAjuste, String>> metodos, List<String> datosSalida, List<String> labels) {
		for (int i=0; i<metodos.size(); i++) { 
			
			String datosalida = String.format("datos/Tiempos%s.csv",
					metodos.get(i).third());
			datosSalida.add(datosalida);
			String label = metodos.get(i).third();
			System.out.println(label);

			TipoAjuste tipoAjuste = metodos.get(i).second();
			GraficosAjuste.show(datosalida, tipoAjuste, label);	
			
			// Obtener ajusteString para mostrarlo en gráfica combinada
			Pair<Function<Double, Double>, String> parCurve = GraficosAjuste.fitCurve(
					DataCurveFitting.points(datosalida), tipoAjuste);
			String ajusteString = parCurve.second();
			labels.add(String.format("%s     %s", label, ajusteString));
		}
	}
	
	public static void muestraGraficas() {
		List<String> datosSalida = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		
		muestraGraficasMetodos(metodosBigInteger, datosSalida, labels);
		muestraGraficasMetodos(metodosDouble, datosSalida, labels);
		
		GraficosAjuste.showCombined("Grafica", datosSalida, labels);
	}
	
	

	
	
	@SuppressWarnings("unchecked")
	public static <E> void testTiemposEjecucion(Integer nMin, Integer nMax,
			Function<E, Number> funcionFib,
			String ficheroTiempos,
			Boolean flagExp) {
		Map<Problema, Double> tiempos = new HashMap<Problema,Double>();
		Integer nMed = flagExp ? 1 : numMediciones; 
		for (int iter=0; iter<nMed; iter++) {
			for (int i=0; i<numSizes; i++) {
				Double r = Double.valueOf(nMax-nMin)/(numSizes-1);
				Integer tam = (Integer.MAX_VALUE/nMax > i) 
						? nMin + i*(nMax-nMin)/(numSizes-1)
						: nMin + (int) (r*i) ;
				Problema p = Problema.of(tam);
				System.out.println(tam);
				warmup(funcionFib, 10);
				Integer nIter = flagExp ? 5 : numIter;
				Number[] res = new Number[nIter];
				Long t0 = System.nanoTime();
				for (int z=0; z<nIter; z++) {
					res[z] = funcionFib.apply((E) tam);
				}
				Long t1 = System.nanoTime();
				actualizaTiempos(tiempos, p, Double.valueOf(t1-t0)/nIter);
			}
			
		}
		
	
		Resultados.toFile(tiempos.entrySet().stream()
				.map(x->TResultD.of(x.getKey().tam(), 
									x.getValue()))
				.map(TResultD::toString),
			ficheroTiempos, true);
		
	}
	
	private static void actualizaTiempos(Map<Problema, Double> tiempos, Problema p, double d) {
		if (!tiempos.containsKey(p)) {
			tiempos.put(p, d);
		} else if (tiempos.get(p) > d) {
				tiempos.put(p, d);
		}
	}
	
	
	private static <E> BigInteger warmup(Function<E, Number> fib, Integer n) {
		BigInteger res=BigInteger.ZERO;
		BigInteger z = BigInteger.ZERO; 
		for (int i=0; i<numIterWarmup; i++) {
			if (fib.apply((E) n).equals(z)) z.add(BigInteger.ONE);
		}
		res = z.equals(BigInteger.ONE)? z.add(BigInteger.ONE):z;
		return res;
	}
	

	record TResultD(Integer tam, Double t) {
		public static TResultD of(Integer tam, Double t){
			return new TResultD(tam, t);
		}
		
		public String toString() {
			return String.format("%d,%.0f", tam, t);
		}
	}
	

	record Problema(Integer tam) {
		public static Problema of(Integer tam){
			return new Problema(tam);
		}
	}
}
