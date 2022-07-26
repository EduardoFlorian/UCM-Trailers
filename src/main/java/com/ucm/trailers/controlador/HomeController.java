package com.ucm.trailers.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ucm.trailers.modelo.Pelicula;
import com.ucm.trailers.repositorio.PeliculaRepository;

@Controller
@RequestMapping("")
public class HomeController {
	
	@Autowired
	private PeliculaRepository repoPeli;
	
	//VISTA CON ULTIMOS 3 TRAILERS
	@GetMapping({"/", ""})
	public ModelAndView InicioConUltimosTrailers() {
		
		List<Pelicula> ultimosTrailers = repoPeli.findAll(PageRequest.of(0, 4,Sort.by("fechaEstreno").descending())).toList();
		
		return new ModelAndView("inicio-principal").addObject("ultimosTrailers",ultimosTrailers);
	
	}
	
	//VISTA CON TODOS LOS TRAILERS
	@GetMapping("/peliculas/full")
	public ModelAndView listarTodasLasPeliculas(@PageableDefault(sort = "fechaEstreno",direction = Sort.Direction.DESC, size=8) Pageable pageable) {
		
		Page<Pelicula> peliculas = repoPeli.findAll(pageable);
		
		return new ModelAndView("peliculas-totales").addObject("peliculasTotales",peliculas);
	
	}
	
	//VISTA CON LA INFO (DETALLE) DE CADA PELICULA
	@GetMapping("/peliculas/{id}")
	public ModelAndView mostrarDetallesDePelicula(@PathVariable Integer id) {
		Pelicula pelicula = repoPeli.getById(id);
		
		return new ModelAndView("pelicula-detalle").addObject("peliculaDetalle",pelicula);
	}
}
