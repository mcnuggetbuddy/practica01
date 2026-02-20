/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica01.practica01.service;

import com.practica01.practica01.domain.Arbol;
import com.practica01.practica01.repository.ArbolRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
/**
 *
 * @author israelapuy
 */

@Service
public class ArbolService {

    private final ArbolRepository arbolRepository;
    private final FirebaseStorageService firebaseStorageService;

    public ArbolService(ArbolRepository arbolRepository,
            FirebaseStorageService firebaseStorageService) {
        this.arbolRepository = arbolRepository;
        this.firebaseStorageService = firebaseStorageService;
    }
    
    @Transactional(readOnly = true)
    public List<Arbol> getArboles() {
        return arbolRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Arbol> getArbol(Integer idArbol) {
        return arbolRepository.findById(idArbol);
    }
    
    @Transactional
    public void save(Arbol arbol, MultipartFile imagenFile) {
        arbol = arbolRepository.save(arbol);
        if (!imagenFile.isEmpty()) { //Si no está vacío... pasaron una imagen...            
            try {
                String rutaImagen = firebaseStorageService.uploadImage(
                        imagenFile, "arbol",
                        arbol.getIdArbol());
                arbol.setRutaImagen(rutaImagen);
                arbolRepository.save(arbol);
            } catch (IOException e) {
                throw new RuntimeException("Error al subir la imagen del árbol a Firebase", e);
            }
        }
    }
    
    @Transactional
    public void delete(Integer idArbol) {
        // Verifica si la categoría existe antes de intentar eliminarlo
        if (!arbolRepository.existsById(idArbol)) {
            // Lanza una excepción para indicar que el usuario no fue encontrado
            throw new IllegalArgumentException("El árbol con ID " + idArbol + " no existe.");
        }
        try {
            arbolRepository.deleteById(idArbol);
        } catch (DataIntegrityViolationException e) {
            // Lanza una nueva excepción para encapsular el problema de integridad de datos
            throw new IllegalStateException("No se puede eliminar el árbol. Tiene datos asociados.", e);
        }
    }
}
