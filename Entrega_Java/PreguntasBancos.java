package Entrega_Java;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import us.lsi.bancos.Banco;
import us.lsi.bancos.Cuenta;
import us.lsi.bancos.Cuentas;

/*
 * Implemente el método valorTotalPrestamos que obtenga para cada cliente del banco
de edad inferior a e, de tipo entero, el valor total de sus préstamos que tengan un valor
comprendido entre a y b, ambos de tipo Double, y concedidos con fecha posterior a f, de
tipo LocalDate. Implemente las siguientes restricciones:

• La edad e debe ser un entero superior a 18
• Los valores a y b deben ser positivos
• a debe ser menor que b
 */
public class PreguntasBancos {
	public Map<String, Double> valorTotalPrestamosImperativo(Integer e, Double a, Double b, LocalDate f) {
		
		Banco bk=Banco.of();
		Cuentas cts = bk.cuentas();
		Map<String, Double> ValorPrestamos = new HashMap<>();
		
		if(e<18) {
			throw  new IllegalArgumentException("La edad e debe ser un entero superior a 18");
		}
		if(a<0 && b<0) {
			throw  new IllegalArgumentException("Los valores a y b deben ser positivos");
		}
		if(a>b) {
			throw  new IllegalArgumentException(" a debe ser menor que b");
		}
	
		for(Cuenta ct:cts.todas()) {
			if(ct) {}
		}
	}

	public Map<String, Double> valorTotalPrestamosFuncional(Integer e, Double a, Double b, LocalDate f) {
		return null;
	}

}
