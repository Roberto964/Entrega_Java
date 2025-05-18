package Entrega_Java;

import us.lsi.aeropuerto.*;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


	public class PreguntasAeropuertos {

	    public String ciudadAeropuertoMayorFacturacion(LocalDateTime a, LocalDateTime b)  {
	        if (a.isAfter(b) || a.plusDays(1).isAfter(b)) {
	            throw new IllegalArgumentException("Las fechas deben estar en orden y tener más de un día de diferencia.");
	        }

	        Map<String, Double> facturacionPorCiudad = new HashMap<>();
	        Vuelos vuelos = Vuelos.of();
	        VuelosProgramados vuelosProgramados = VuelosProgramados.of();
	        Aeropuertos aeropuertos = Aeropuertos.of();

	        for (Vuelo vuelo : vuelos.todas()) {
	            LocalDateTime fechaVuelo = vuelo.fecha();
	            if (!fechaVuelo.isBefore(a) && !fechaVuelo.isAfter(b)) {
	                Optional<VueloProgramado> vp = vuelosProgramados.vuelo(vuelo.codigoVueloProgramado());
	                String codigoDestino = vp.codigoDestino();
	                Optional<String> ciudadDestino = aeropuertos.ciudadDeAeropuerto(codigoDestino);
	                double precio = vp.precio().doubleValue();

	                facturacionPorCiudad.put(
	                    ciudadDestino,
	                    facturacionPorCiudad.getOrDefault(ciudadDestino, 0.0) + precio
	                );
	            }
	        }

	        String ciudadMayorFacturacion = null;
	        double maxFacturacion = 0.0;

	        for (Map.Entry<String, Double> entry : facturacionPorCiudad.entrySet()) {
	            if (entry.getValue() > maxFacturacion) {
	                maxFacturacion = entry.getValue();
	                ciudadMayorFacturacion = entry.getKey();
	            }
	        }

	        return ciudadMayorFacturacion;
	    }

	    public String ciudadAeropuertoMayorFacturacionFuncional(LocalDateTime a, LocalDateTime b) {
	        if (a.isAfter(b) || a.plusDays(1).isAfter(b)) {
	            throw new IllegalArgumentException("Las fechas deben estar en orden y tener más de un día de diferencia.");
	        }

	        Vuelos vuelos = Vuelos.of();
	        VuelosProgramados vuelosProgramados = VuelosProgramados.of();
	        Aeropuertos aeropuertos = Aeropuertos.of();

	        return vuelos.todas().stream()
	            .filter(v -> !v.fecha().isBefore(a) && !v.fecha().isAfter(b))
	            .filter(v -> vuelosProgramados.vuelo(v.codigoVueloProgramado()) != null)
	            .collect(Collectors.groupingBy(
	                v -> {
	                    String codigoDestino = vuelosProgramados.vuelo(v.codigoVueloProgramado()).codigoDestino();
	                    return aeropuertos.ciudadDeAeropuerto(codigoDestino);
	                },
	                Collectors.summingDouble(v -> {
	                    VueloProgramado vp = vuelosProgramados.vuelo(v.codigoVueloProgramado());
	                    return vp.precio() * v.numPasajeros();
	                })
	            ))
	            .entrySet().stream()
	            .max(Comparator.comparingDouble(Map.Entry::getValue))
	            .map(Map.Entry::getKey)
	            .toString();
	    }
	    
	    public static void main(String[] args) {
	        PreguntasAeropuertos pa = new PreguntasAeropuertos();

	        LocalDateTime start = LocalDateTime.of(2025, 5, 1, 0, 0);
	        LocalDateTime end = LocalDateTime.of(2025, 5, 10, 23, 59);

	        try {
	            System.out.println("Ciudad mayor facturación (imperativo):");
	            String ciudadImp = pa.ciudadAeropuertoMayorFacturacion(start, end);
	            System.out.println(ciudadImp);
	        } catch (Exception e) {
	            System.out.println("Error: " + e.getMessage());
	        }

	        try {
	            System.out.println("Ciudad mayor facturación (funcional):");
	            String ciudadFunc = pa.ciudadAeropuertoMayorFacturacionFuncional(start, end);
	            System.out.println(ciudadFunc);
	        } catch (Exception e) {
	            System.out.println("Error: " + e.getMessage());
	        }

	        try {
	            System.out.println("Probando fechas inválidas:");
	            pa.ciudadAeropuertoMayorFacturacionFuncional(end, start);
	        } catch (IllegalArgumentException e) {
	            System.out.println("Capturada excepción: " + e.getMessage());
	        }
	    }
}
	
	


