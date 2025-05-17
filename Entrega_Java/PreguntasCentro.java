package Entrega_Java;

import java.time.LocalDate;
import java.time.LocalDateTime;

import us.lsi.centro.*;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
							if (asn.dni() == pro.dni()) {
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


	public Set<String> grupoMayorDiversidadEdadFuncional() {
	    Centro centro = Centro.of();
	    List<Grupo> grupos = (List<Grupo>) centro.grupos().todos();
	    List<Matricula> matriculas = (List<Matricula>) centro.matriculas().todas();
	    Alumnos alumnos = centro.alumnos();

	    
	    return grupos.stream()
	        .map(grupo -> {
	            
	            List<Alumno> alumnosDelGrupo = matriculas.stream()
	                .filter(m -> m.ida().equals(grupo.ida()) && m.idg().equals(grupo.idg()))
	                .map(m -> alumnos.alumno(m.dni()))
	                .filter(Objects::nonNull)
	                .toList();

	            if (alumnosDelGrupo.isEmpty()) return null;

	            
	            int minEdad = alumnosDelGrupo.stream().mapToInt(Alumno::edad).min().orElse(0);
	            int maxEdad = alumnosDelGrupo.stream().mapToInt(Alumno::edad).max().orElse(0);
	            int diferencia = maxEdad - minEdad;

	            
	            return new AbstractMap.SimpleEntry<>(grupo, diferencia);
	        })
	        .filter(Objects::nonNull) 
	        .max(Comparator.comparingInt(Map.Entry::getValue)) 
	        .map(entry -> {
	            Grupo g = entry.getKey();
	            return Set.of("ida: " + g.ida() + ", idg: " + g.idg());
	        })
	        .orElse(Set.of());
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

	public Map<String, String> rangosEdadPorAlumnoImperativo() {
		return null;
	}

	public Map<String, String> rangosEdadPorAlumnoFuncional() {
		return null;
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
		return null;
	}

	public List<String> nombresAlumnosMayorNotaImperativo(Integer n, LocalDate a) {
		Centro c = Centro.of();
		Alumnos alums = c.alumnos();
		ArrayList<String> AlumnosTop = new ArrayList<>();

		if (n == null) {
			throw new IllegalArgumentException("La nota de los alumnos no puede ser null");
		}
		for (Alumno alum : alums.todos()) {
			if (alum.fechaDeNacimiento().toLocalDate().isAfter(a) && n < 10 && n > 1) {// pasamos la fecha de nacimiento
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

	public List<String> nombresAlumnosMayorNotaFuncional(Integer n, LocalDate a) {
		Centro c = Centro.of();
		Alumnos alums = c.alumnos();
		if (n == null) {
			throw new IllegalArgumentException("La nota de los alumnos no puede ser null ");
		}
		if (n < 10 && n > 1) {
			throw new IllegalArgumentException("La nota de los alumnos debe estar entre 2 y 9 ");
		}

		return alums.todos().stream().filter(alumno -> alumno.fechaDeNacimiento().toLocalDate().isAfter(a))
				.filter(alumno -> alumno.nota() >= n).map(alumno -> alumno.nombre()).collect(Collectors.toList());
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
