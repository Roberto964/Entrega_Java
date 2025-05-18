package Entrega_Java;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import us.lsi.bancos.Prestamo;
import us.lsi.biblioteca.Libro;
import us.lsi.biblioteca.Libros;
import us.lsi.centro.Titulo;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PreguntasBiblioteca {
	public Map<String, Integer> masVecesPrestadoImperativo() {
		Map<String, Integer> conteoPrestamos = new HashMap<>();

	    // Contar préstamos por ISBN
	    for (Prestamo prestamo : prestamos.of().todos()) {
	        conteoPrestamos.put(
	            prestamo.isbn(),
	            conteoPrestamos.getOrDefault(prestamo.isbn(), 0) + 1
	        );
	    }

	    // Encontrar el ISBN con más préstamos
	    String isbnMax = null;
	    int maxVeces = 0;

	    for (Map.Entry<String, Integer> entry : conteoPrestamos.entrySet()) {
	        if (entry.getValue() > maxVeces) {
	            isbnMax = entry.getKey();
	            maxVeces = entry.getValue();
	        }
	    }

	    if (isbnMax == null) return null; // No hay préstamos

	    Libro libro = libros.libro(isbnMax);
	    return new LibroPrestado(libro, maxVeces);
	}
	

	public Map<String, Integer> masVecesPrestadoFuncional() {
		return null;
	}

	Map<String, Set<String>> librosPorAutorImperativo(Libros libros, List<String> nombres) {

		Map<String, Set<String>> librosDeAutor = new HashMap<>();// lista vacia

		if (nombres == null) {
			throw new IllegalArgumentException("La lista de nobres de autores no puede estar vacia");// comprobamos q no
																										// es null
		}

		for (Libro lb : libros.todos()) {
			if (nombres.contains(lb.autor())) {
				String Autor = lb.autor();
				String Titulo = lb.titulo();

				if (!librosDeAutor.containsKey(Autor)) {// si no está el autor crea un set vacio y va añadendo
					librosDeAutor.put(Autor, new HashSet<>());
				}
				librosDeAutor.get(Autor).add(Titulo);
			}
		}
		return librosDeAutor;

	}

	Map<String, Set<String>> librosPorAutorFuncional(Libros libros, List<String> nombres) {
		return libros.todos().stream().filter(libro -> nombres == null || nombres.contains(libro.titulo())).collect(
				Collectors.groupingBy(libro -> libro.autor(), Collectors.mapping(Libro::titulo, Collectors.toSet())));
	}

}