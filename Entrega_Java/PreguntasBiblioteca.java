package Entrega_Java;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import us.lsi.biblioteca.Libros;

import java.util.stream.Collectors;
import java.util.stream.Stream;


public class PreguntasBiblioteca {
	public Map<String,Integer> masVecesPrestadoImperativo() {return null;}
	public Map<String,Integer> masVecesPrestadoFuncional() {return null;}
	
	
	Map <String, Set<String>> librosPorAutorImperativo(Libros libros, List<String> nombres){return null;}
	Map <String, Set<String>> librosPorAutorFuncional(Libros libros, List<String> nombres){
		return libros.todos().stream().collect(Collectors.groupingBy(Libros::librosDeAutor,lbr->))
	}

}