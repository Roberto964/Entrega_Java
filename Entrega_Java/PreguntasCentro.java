package Entrega_Java;

import us.lsi.centro.*;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class PreguntasCentro {
	public Double promedioEdadProfesoresImperativo(String dni) {
		Centro c = Centro.of();
		Matriculas ms = c.matriculas();
		Profesores pr = c.profesores();
		Asignaciones as = c.asignaciones();
		Integer contador = 0;
		double edadProfesor = 0.;
		for (Matricula ma : ms.todas()) {
			if (ma.dni().equals(dni)) {// tengo la matricula de un alumno
				for (Asignacion asn : as.todas()) {
					if (ma.ida() == asn.ida() && ma.idg() == asn.idg()) {
						for (Profesor pro : pr.todos()) {
							if (asn.dni().equals(pro.dni())) {
								contador += 1;
								edadProfesor += pro.edad();

							}
						}
					}

				}
			}
		}
		return edadProfesor / contador;
	}

	public Double promedioEdadProfesoresFuncional(String dni) {
		Centro c = Centro.of();
		Matriculas ms = c.matriculas();
		Profesores pr = c.profesores();
		Asignaciones as = c.asignaciones();
		Set<String> AlumnosMatriculados = ms.todas().stream().filter(matricula -> matricula.dni().equals(dni))
				.map(matricula -> matricula.ida() + "-" + matricula.idg()).collect(Collectors.toSet());
		Set<String> AsignacionesDniProfesor = as.todas().stream()
				.filter(asignacion -> AlumnosMatriculados.contains(asignacion.ida() + "-" + asignacion.idg()))
				.map(Asignacion::dni).collect(Collectors.toSet());
		List<Profesor> numeroProfesoresAlumnos = pr.todos().stream()
				.filter(profesor -> AsignacionesDniProfesor.contains(profesor.dni())).collect(Collectors.toList());
		return numeroProfesoresAlumnos.stream().mapToDouble(profesor -> profesor.edad()).average().orElse(0.);
	}

	public Set<String> grupoMayorDiversidadEdadImperativo() {
	    Centro c = Centro.of();
	    Matriculas ms = c.matriculas();
	    Alumnos al = c.alumnos();
	    Grupos gr = c.grupos();

	    int maxDiferencia = -1;
	    Set<String> grupoMax = new HashSet<>();

	    for (Grupo grupo : gr.todos()) {
	        List<Alumno> alumnosDelGrupo = new ArrayList<>();

	        for (Matricula m : ms.todas()) {
	            if (m.ida().equals(grupo.ida()) && m.idg().equals(grupo.idg())) {
	                Alumno alumno = al.alumno(m.dni());
	                if (alumno != null) {
	                    alumnosDelGrupo.add(alumno);
	                }
	            }
	        }

	        if (!alumnosDelGrupo.isEmpty()) {
	            int minEdad = Integer.MAX_VALUE;
	            int maxEdad = Integer.MIN_VALUE;

	            for (Alumno a : alumnosDelGrupo) {
	                int edad = a.edad();
	                if (edad < minEdad) minEdad = edad;
	                if (edad > maxEdad) maxEdad = edad;
	            }

	            int diferencia = maxEdad - minEdad;

	            if (diferencia > maxDiferencia) {
	                maxDiferencia = diferencia;
	                grupoMax.clear();
	                grupoMax.add("ida: " + grupo.ida() + ", idg: " + grupo.idg());
	            }
	        }
	    }

	    return grupoMax;
	}


	public Grupo grupoMayorDiversidadEdadFuncional() {
		Centro c = Centro.of();
	    return c.grupos().todos().stream()
	        .map(gr -> {
	            List<Alumno> listaAlumnos = c.matriculas().todas().stream()
	                .filter(m -> m.ida().equals(gr.ida()) && m.idg().equals(gr.idg()))
	                .map(m -> c.alumnos().alumno(m.dni()))
	                .filter(Objects::nonNull)
	                .collect(Collectors.toList());

	            if (listaAlumnos.isEmpty()) {
	                return null;
	            }

	            int edadMinima = listaAlumnos.stream().mapToInt(Alumno::edad).min().orElse(Integer.MAX_VALUE);
	            int edadMaxima = listaAlumnos.stream().mapToInt(Alumno::edad).max().orElse(Integer.MIN_VALUE);
	            int diferenciaEdad = edadMaxima - edadMinima;

	            return new Object[] { gr, diferenciaEdad };
	        })
	        .filter(Objects::nonNull)
	        .max(Comparator.comparingInt(arr -> (int) arr[1]))
	        .map(arr -> (Grupo) arr[0])
	        .orElse(null);
	}


	public String alumnoMasMatriculasImperativo() {
		Centro c = Centro.of();
		Matriculas ms = c.matriculas();

		Map<String, Integer> contador = new HashMap<>();

		for (Matricula mt : ms.todas()) {
			String dni = mt.dni();
			Integer cuenta = contador.get(dni);
			if (cuenta == null) {
				contador.put(dni, 1);
			} else {
				contador.put(dni, cuenta + 1);
			}
		}

		String dniMax = null;
		int max = 0;

		for (Map.Entry<String, Integer> entry : contador.entrySet()) {
			if (entry.getValue() > max) {
				max = entry.getValue();
				dniMax = entry.getKey();
			}
		}

		return dniMax;
	}

	public String alumnoMasMatriculasFuncional() {
		Centro c = Centro.of();
		Matriculas ms = c.matriculas();
		Map<String, Long> MatriculaPorAlumno = ms.todas().stream().map(Matricula::dni)
				.collect(Collectors.groupingBy(dni -> dni, Collectors.counting()));// Contamos cuntas veces aparece un
																					// dni del alumno
		return MatriculaPorAlumno.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey)
				.orElse(null);// con ese dni del alumno tenemos q compararlo con el resto de dni, y sacamos el
								// mayor

	}

	public Map<String,Map<String,Integer>> rangosEdadPorAlumnoImperativo(String rangoStr) {
		Centro c = Centro.of();
        Map<String,Map<String,Integer>> rangosEdadAlumnos = new HashMap<>();
        String[] rangos = rangoStr.split(",");

        for (String r : rangos) {
            // r = r.replaceAll("\s+", "");
            if (!r.matches("\\d+-\\d+")) {
                throw new IllegalArgumentException("Formato incorrecto: " + r);
            }

            String[] valores = r.split("-");
            Integer min = Integer.parseInt(valores[0].strip());
            Integer max = Integer.parseInt(valores[1].strip());
            Map<String,Integer> alumnoEdad = new HashMap<>();

            for (Alumno a : c.alumnos().todos()) {
                if (a.edad() >= min && a.edad() <= max) {
                    alumnoEdad.put(a.nombre(), a.edad());
                }
            }
            rangosEdadAlumnos.put(r,alumnoEdad);
        }

        return rangosEdadAlumnos;
    }

	public Map<String, Map<String, Integer>> rangosEdadPorAlumnoFuncional(String rangoStr) {
		Centro c = Centro.of();
        return Arrays.stream(rangoStr.split(",")) // Convierte la cadena de rangos en un stream de Strings, separando por comas
            .peek(r -> {
                // r = r.replaceAll("\s+", ""); // Quita espacios en blanco
                if (!r.matches("\\d+-\\d+")) { // Verifica que el formato sea "número-número"
                    throw new IllegalArgumentException("Formato incorrecto: " + r);
                }
            })
            .collect(Collectors.toMap( // Convierte el rango "min-max" en un array de enteros [min, max]
                r -> r,
                r -> {
                    String[] valores = r.split("-");
                    int min = Integer.parseInt(valores[0].strip());
                    int max = Integer.parseInt(valores[1].strip());
                    return c.alumnos().todos().stream()
                        .filter(a -> a.edad() >= min && a.edad() <= max)
                        .collect(Collectors.toMap(
                                Alumno::nombre, 
                                Alumno::edad,
                                (a,b)->a // Evita nombres duplicados
                                ));
                    }
            ));
    }

	public String nombreProfesorMasGruposImperativo(Integer min, Integer max) {
		Centro c = Centro.of();
		Profesores pr = c.profesores();
		Asignaciones as = c.asignaciones();
		

		Map<String, Integer> contadorGrupos = new HashMap<>();

		if (min >= max) {
			throw new IllegalArgumentException("La edad mínima debe ser menor que la máxima");
		}
		for (Profesor prf : pr.todos()) {
			if (prf.edad() >= min && prf.edad() <= max) {// comprobamos q la edad del profesor está dentro del rango,
															// porque tiene q estar dentro del rg
				contadorGrupos.put(prf.dni(), 0);
			}
		}
		for (Asignacion asg : as.todas()) {
			String dniProf = asg.dni();
			if (contadorGrupos.containsKey(dniProf)) {
				contadorGrupos.put(dniProf, contadorGrupos.get(dniProf) + 1);
			}
		}
		String dniMax = null;
		int maxGrupos = -1;
		for (Map.Entry<String, Integer> entry : contadorGrupos.entrySet()) {
			if (entry.getValue() > maxGrupos) {
				maxGrupos = entry.getValue();
				dniMax = entry.getKey();
			}
		}

		if (dniMax == null)
			return null;

		Profesor profMax = pr.profesor(dniMax);
		return profMax.nombreCompleto();

	}

	public String nombreProfesorMasGruposFuncional(Integer min, Integer max) {
		    if (min >= max) {
		        throw new IllegalArgumentException("La edad mínima debe ser menor que la máxima");
		    }

		    Centro c = Centro.of();
		    Profesores pr = c.profesores();
		    Asignaciones as = c.asignaciones();

		    // Obtenemos los dnis de los profesores en el rango de edad
		    Set<String> dnisProfesoresFiltrados = pr.todos().stream()
		        .filter(p -> p.edad() >= min && p.edad() <= max)
		        .map(Profesor::dni)
		        .collect(Collectors.toSet());

		    // Contamos cuántas asignaciones tiene cada profesor filtrado
		    Map<String, Long> conteo = as.todas().stream()
		        .map(Asignacion::dni)
		        .filter(dnisProfesoresFiltrados::contains)
		        .collect(Collectors.groupingBy(dni -> dni, Collectors.counting()));

		    // Buscamos el dni con más asignaciones
		    return conteo.entrySet().stream()
		        .max(Map.Entry.comparingByValue())
		        .map(entry -> pr.profesor(entry.getKey()).nombreCompleto())
		        .orElse(null);
		}
	

	public List<String> nombresAlumnosMayorNotaImperativo(Integer n, LocalDateTime anio) {
		Centro c = Centro.of();
		Alumnos alums = c.alumnos();
		ArrayList<String> AlumnosTop = new ArrayList<>();

		if (n == null) {
			throw new IllegalArgumentException("La nota de los alumnos no puede ser null");
		}
		for (Alumno alum : alums.todos()) {
			if (alum.fechaDeNacimiento().isAfter(anio) && n < 10 && n > 1) {// pasamos la fecha de nacimiento
																						// a LocalDate y vemos si está
																						// despues de la fecha "a" y
																						// comporbamos q esta dentro del
																						// limite
				if (alum.nota() >= n) {
					AlumnosTop.add(alum.nombre());
				}
			}
		}
		return AlumnosTop;
	}

	public List<String> nombresAlumnosMayorNotaFuncional(Integer n, LocalDateTime anio) {
		Centro c = Centro.of();
		Alumnos alums = c.alumnos();
		if (n == null) {
			throw new IllegalArgumentException("La nota de los alumnos no puede ser null ");
		}
		if (n < 10 && n > 1) {
			throw new IllegalArgumentException("La nota de los alumnos debe estar entre 2 y 9 ");
		}

		return alums.todos().stream().filter(alumno -> alumno.fechaDeNacimiento().isAfter(anio))
				.filter(alumno -> alumno.nota() >= n).map(alumno -> alumno.nombre()).collect(Collectors.toList());
	}

	public static void main(String[] args) {
		PreguntasCentro pc = new PreguntasCentro();

        System.out.println(pc.promedioEdadProfesoresImperativo("72074089R"));
        System.out.println(pc.promedioEdadProfesoresFuncional("72074089R"));

        System.out.println(pc.grupoMayorDiversidadEdadImperativo());
        System.out.println(pc.grupoMayorDiversidadEdadFuncional());

        System.out.println(pc.alumnoMasMatriculasImperativo());
        System.out.println(pc.alumnoMasMatriculasFuncional());

        System.out.println(pc.rangosEdadPorAlumnoImperativo("20-23,24-26,26-28"));
        System.out.println(pc.rangosEdadPorAlumnoFuncional("20-23,24-26,26-28"));

        System.out.println(pc.nombreProfesorMasGruposImperativo(26,27));
        System.out.println(pc.nombreProfesorMasGruposFuncional(26,27));

        LocalDateTime anio = LocalDateTime.of(2003,1,1,0,0);
        System.out.println(pc.nombresAlumnosMayorNotaImperativo(4, anio));
        System.out.println(pc.nombresAlumnosMayorNotaFuncional(4, anio));

	}

}


