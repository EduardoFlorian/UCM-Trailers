package com.ucm.trailers.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ucm.trailers.modelo.Genero;
import com.ucm.trailers.modelo.Pelicula;
import com.ucm.trailers.repositorio.GeneroRepository;
import com.ucm.trailers.repositorio.PeliculaRepository;
import com.ucm.trailers.servicio.AlmacenServicioImpl;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private PeliculaRepository repoPeli;
	
	@Autowired
	private GeneroRepository repoGenero;
	
	@Autowired
	private AlmacenServicioImpl servicioAlmacen;
	
	//Para la paginacion de las peliculas
	//@PageableDefault(sort="titulo", size=5)Pageable pageable ===> En esta parte basicamente lo que
	//estamos pasando como parametro es el como se va estar paginando nuestra pagina, es decir
	//se ordenara por titulo (De la A a la Z) y cada pagina mostrara 5 peliculas como maximo. Y todo
	//eso lo casteamos en un Pageable
	@GetMapping({"/", ""})
	public ModelAndView verPaginaDeInicio(@PageableDefault(sort="fechaEstreno", direction = Sort.Direction.DESC, size=5)Pageable pageable) {
		
		//Creamos una Page (Pagina) con un listado que contenga todo el listado de peliculas 
		//y al metodo findAll le pasamos el atributo pageable para que sepa como ordenarse
		Page<Pelicula> peliculas = repoPeli.findAll(pageable);

		
		//Retornamos a la vista index y basicamente le mandamos un artibuto con las peliculas y su formato de paginacion
		return new ModelAndView("admin/index").addObject("peliculasListado", peliculas);
	}
	
	//INICIO REGISTRAR PELICULA
	
	@GetMapping("/peliculas/nuevo")
	public ModelAndView mostrarFormularioDeNuevaPelicula() {
		
		//Listados de generos ordenados por su titulo
		List<Genero> generoslista = repoGenero.findAll(Sort.by("titulo"));
		
		return new ModelAndView("admin/nueva-pelicula").addObject("pelicula", new Pelicula())
													   .addObject("generos", generoslista);
	}
	
	@PostMapping("/peliculas/nuevo")
	public ModelAndView registrarPelicula(@Validated Pelicula pelicula, BindingResult result) {
		
		if(result.hasErrors() || pelicula.getPortada().isEmpty()) {
			if(pelicula.getPortada().isEmpty()) {
				result.rejectValue("portada", "MultipartNotEmpty");
			}
			
			List<Genero> generoslista = repoGenero.findAll(Sort.by("titulo"));
			return new ModelAndView("admin/nueva-pelicula").addObject("pelicula", pelicula)
					   									   .addObject("generos", generoslista);
		}
		
		String rutaPortada = servicioAlmacen.almacenarArchivo(pelicula.getPortada());
		pelicula.setRutaPortada(rutaPortada);
		
		repoPeli.save(pelicula);
		
		return new ModelAndView("redirect:/admin");
	}
	
	//FIN REGISTRAR PELICULA
	
	
	//INICIO ACTUALIZAR PELICULA
	@GetMapping("/peliculas/editar/{id}")
	public ModelAndView EditarPelicula(@PathVariable Integer id) {
		
		//Obtner pelicula por id
		Pelicula peliculaEncontrada = repoPeli.getById(id);
		
		//Listados de generos ordenados por su titulo
		List<Genero> generoslista = repoGenero.findAll(Sort.by("titulo"));
		
		return new ModelAndView("admin/editar-pelicula").addObject("pelicula", peliculaEncontrada)
													   .addObject("generos", generoslista);
	}
	
	@PostMapping("/peliculas/editar/{id}")
	public ModelAndView RegitrarActualizacionPelicula(@PathVariable Integer id, @Validated Pelicula pelicula, BindingResult result) {
		
		if(result.hasErrors() || pelicula.getPortada().isEmpty()) {
			if(pelicula.getPortada().isEmpty()) {
				result.rejectValue("portada", "MultipartNotEmpty");
			}
			//Listados de generos ordenados por su titulo
			List<Genero> generoslista = repoGenero.findAll(Sort.by("titulo"));
			
			return new ModelAndView("admin/editar-pelicula").addObject("pelicula", pelicula)
					   .addObject("generos", generoslista);
		}
		
		//Obtner pelicula por id
		Pelicula peliculaEncontrada = repoPeli.getById(id);
		
		peliculaEncontrada.setTitulo(pelicula.getTitulo());
		peliculaEncontrada.setSinopsis(pelicula.getSinopsis());
		peliculaEncontrada.setFechaEstreno(pelicula.getFechaEstreno());
		peliculaEncontrada.setUrlYoutubeTrailer(pelicula.getUrlYoutubeTrailer());
		peliculaEncontrada.setGeneros(pelicula.getGeneros());
		
		if(!pelicula.getPortada().isEmpty()) {
			servicioAlmacen.eliminarArchivo(peliculaEncontrada.getRutaPortada());
			String nuevaRutaPortada = servicioAlmacen.almacenarArchivo(pelicula.getPortada());
			peliculaEncontrada.setRutaPortada(nuevaRutaPortada);
		}
		
		repoPeli.save(peliculaEncontrada);
		
		return new ModelAndView("redirect:/admin");
		
	}
	
	@PostMapping("/peliculas/eliminar/{id}")
	public String eliminarPelicula(@PathVariable Integer id) {
		//Encontramos la pelicula a eliminar
		Pelicula peliculaEncontrada = repoPeli.getById(id);
		
		//La eliminamos 
		repoPeli.delete(peliculaEncontrada);
		
		//Eliminamos tambien el archivo imagen q le pertenecia a dicha pelicula eliminada
		//de la carpeta en donde se guardan las imagenes
		servicioAlmacen.eliminarArchivo(peliculaEncontrada.getRutaPortada());
		
		return "redirect:/admin";
	}

}
