package Entrega_Java;

import java.io.IOException;

import us.lsi.biblioteca.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import us.lsi.bancos.Prestamos;
import us.lsi.biblioteca.Libro;
import us.lsi.biblioteca.Libros;
import us.lsi.centro.Titulo;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PreguntasBiblioteca {
	public Libro masVecesPrestadoImperativa() {
		
		Prestamos pr = Prestamos.of();
        Libro libroMasPrestado = null;
        Integer maxPrestamos = Integer.MIN_VALUE;

        for (Libro l : Libros.of().todos()) {
            Integer numPrestamos = 0;

            for (Prestamo p :pr.todos()) {//da error no se porque creo q es del "eclipse"
                if (l.isbn().equals(p.isbn())){
                    numPrestamos++;
                }
            }

            if (numPrestamos > maxPrestamos) {
                maxPrestamos = numPrestamos;
                libroMasPrestado = l;
            }
        }
        return libroMasPrestado;
    }

	   public Libro masVecesPrestadoFuncional() {
	        Prestamos prestamos = Prestamos.of();
	        Libros libros = Libros.of();

	        return libros.todos().stream()
	            .max(Comparator.comparingInt(
	                libro -> (int) prestamos.todos().stream()
	                    .filter(prestamo -> prestamo.isbn().equals(libro.isbn()))//no se porque no me lee prestamo si está en los atributos
	                    .count()
	            ))
	            .orElse(null);
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
	 public static void main(String[] args) {
	        PreguntasBiblioteca pb = new PreguntasBiblioteca();
	   
	        
	        Libro libroImperativo = pb.masVecesPrestadoImperativa();
	        System.out.println("Libro más veces prestado (Imperativo): " + (libroImperativo != null ? libroImperativo.titulo() : "No hay libros"));

	        Libro libroFuncional = pb.masVecesPrestadoFuncional();
	        System.out.println("Libro más veces prestado (Funcional): " + (libroFuncional != null ? libroFuncional.titulo() : "No hay libros"));

	        List<String> autores = List.of("Autor1", "Autor2"); 
	        Map<String, Set<String>> librosPorAutorImperativo = pb.librosPorAutorImperativo(Libros.of(), autores);
	        System.out.println("Libros por autor (Imperativo): " + librosPorAutorImperativo);
	     
	        Map<String, Set<String>> librosPorAutorFuncional = pb.librosPorAutorFuncional(Libros.of(), autores);
	        System.out.println("Libros por autor (Funcional): " + librosPorAutorFuncional);
	    }
}