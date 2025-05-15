package Entrega_Java;

import java.time.LocalDate;
import us.lsi.centro.*;
import java.util.List;
import java.util.Map;
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
		Set<String> AlumnosMatriculados = ms.todas().stream().filter(matricula->matricula.dni().equals(dni)).map(matricula->matricula.ida()+"-"+matricula.idg()).collect(Collectors.toSet());
		Set<String> AsignacionesDniProfesor= as.todas().stream().filter(asignacion->AlumnosMatriculados.contains(asignacion.ida()+"-"+asignacion.idg())).map(Asignacion::dni).collect(Collectors.toSet());
		 List<Profesor> numeroProfesoresAlumnos = pr.todos().stream().filter(profesor->AsignacionesDniProfesor.contains(profesor.dni())).collect(Collectors.toList());
		 return numeroProfesoresAlumnos.stream().mapToDouble(profesor->profesor.edad()).average().orElse(0.);
	}

	public Set<String> grupoMayorDiversidadEdadImperativo() {
		return null;
	}

	public Set<String> grupoMayorDiversidadEdadFuncional() {
		return null;
	}

	public String alumnoMasMatriculasImperativo() {
		return null;
	}

	public String alumnoMasMatriculasFuncional() {
		return null;
	}

	public Map<String, String> rangosEdadPorAlumnoImperativo() {
		return null;
	}

	public Map<String, String> rangosEdadPorAlumnoFuncional() {
		return null;
	}

	public String nombreProfesorMasGruposImperativo(Integer min, Integer max) {
		return null;
	}

	public String nombreProfesorMasGruposFuncional(Integer min, Integer max) {
		return null;
	}

	public List<String> nombresAlumnosMayorNotaImperativo(Integer n, LocalDate a) {
		return null;
	}

	public List<String> nombresAlumnosMayorNotaFuncional(Integer n, LocalDate a) {
		return null;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
