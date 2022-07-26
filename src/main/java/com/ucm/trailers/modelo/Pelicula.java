package com.ucm.trailers.modelo;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Entity
@Data
@Table(name="tb_pelicula")
public class Pelicula {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_peli")
	private Integer id;
	
	@Column(name="titulo_peli")
	@NotBlank
	private String titulo;
	
	@Column(length = 500,name="sinopsis_peli")
	@NotBlank
	private String sinopsis;
	
	@Column(name="fec_estreno_peli")
	@NotNull
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate fechaEstreno;
	
	@Column(name="url_trailer_peli")
	@NotBlank
	private String urlYoutubeTrailer;
	
	@Column(name="ruta_portada_peli")
	private String rutaPortada;
	
	
	//Constuccion de la tabla con la relacion de muchos a muchos entre pelicula y genero
	@NotEmpty
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="tb_genero_pelicula", 
	joinColumns = @JoinColumn(name="id_peli"), inverseJoinColumns = @JoinColumn(name="id_gen"))
	private List<Genero> generos;
	
	
	//Esta anotacion hace que este dato sea temporal, es decir no se va guardar en la base de datos
	//este atributo. Donde se va almacenar este dato es en la propiedad sotrge.location del archivo properties
	@Transient
	private MultipartFile portada;
	
}
