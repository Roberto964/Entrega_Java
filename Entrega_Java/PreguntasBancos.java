package Entrega_Java;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import us.lsi.bancos.*;
import us.lsi.ejemplos_b1_tipos.Persona;

public class PreguntasBancos {


   

    public Map<Persona,List<Double>> valorTotalPrestamosImperativo(Integer e, Double a, Double b, LocalDate f) {
    	 Banco bk = Banco.of();
        if (e <= 18 ) {
            throw new IllegalArgumentException("La edad debe ser superior a 18 años");
        }
        if (a < 0||  b < 0 || a >= b) {
            throw new IllegalArgumentException("a debe ser menor que b (ambos positivos)");
        }

        Map<Persona,List<Double>> valorTotalPrestamosCliente = new HashMap<>();

        for (Persona pe : bk.personas().todos()) {
            List<Double> valores = new ArrayList<>();

            if (pe.edad() < e) {
                for (Prestamo pr : bk.prestamos().todos()) {
                    if (pe.dni().equals(pr.dniCliente()) && pr.cantidad() > a && pr.cantidad() < b && pr.fechaComienzo().isBefore(f)){
                        valores.add(pr.cantidad());
                    }
                }
            }
            if (!valores.isEmpty()) {
                valorTotalPrestamosCliente.put(pe, valores);
            }
        }

        return valorTotalPrestamosCliente;
    }
    public Map<Persona,List<Double>> valorTotalPrestamosFuncional(Integer e, Double a, Double b, LocalDate f) {
    	 Banco bk = Banco.of();
        return bk.personas().todos().stream()
            .filter(pr -> pr.edad() < e)
            .collect(Collectors.toMap(
                pr -> pr,
                pe -> bk.prestamos().todos().stream()
                    .filter(pr -> pe.dni().equals(pr.dniCliente()) 
                            && pr.cantidad() > a && pr.cantidad() < b 
                            && pr.fechaComienzo().isBefore(f))
                    .map(Prestamo::cantidad)
                    .collect(Collectors.toList())
            ))
            .entrySet().stream()
            .filter(entry -> !entry.getValue().isEmpty()) // Quita a las personsa q no tienen pestamos
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));  

    }


    public static void main(String[] args) {
        PreguntasBancos pb = new PreguntasBancos();

        LocalDate date = LocalDate.of(2018, 1, 1);
        System.out.println(pb.valorTotalPrestamosImperativo(12, 4578., 90000., date));
        System.out.println(pb.valorTotalPrestamosFuncional(21, 7887., 90000., date));
    }
}
