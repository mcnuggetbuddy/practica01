/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica01.practica01.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 *
 * @author israelapuy
 */

@Data
@Entity
@Table(name = "arbol")
public class Arbol implements Serializable{
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_arbol")
    private int idArbol;

    @NotBlank
    @Size(max = 100)
    @Column(name = "nombre_comun", nullable = false)
    private String nombreComun;

    @Size(max = 100)
    @Column(name = "tipo_flor")
    private String tipoFlor;

    @Column(name = "dureza_madera")
    private Integer durezaMadera;

    @Digits(integer = 3, fraction = 2)
    @Column(name = "altura_promedio")
    private BigDecimal alturaPromedio;

    @Size(max = 255)
    @Column(name = "ruta_imagen")
    private String rutaImagen;
}
