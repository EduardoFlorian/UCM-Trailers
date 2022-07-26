package com.ucm.trailers.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="tb_genero")
public class Genero {
	
	@Id
	@Column(name="id_gen")
	private Integer id;
	
	@Column(name="tit_gen")
	private String titulo;
}
