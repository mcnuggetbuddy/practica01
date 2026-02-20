/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica01.practica01.controller;

import com.practica01.practica01.domain.Arbol;
import com.practica01.practica01.service.ArbolService;
import jakarta.validation.Valid;
import java.util.Locale;
import java.util.Optional;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/**
 *
 * @author israelapuy
 */

@Controller
@RequestMapping("/arbol")
public class ArbolController {
    
    private final ArbolService arbolService;
    private final MessageSource messageSource;
    
    public ArbolController(ArbolService arbolService, MessageSource messageSource){
        this.arbolService = arbolService;
        this.messageSource = messageSource;
    }
    
     @GetMapping("/listado")
     public String listado(Model model) {
         model.addAttribute("arboles", arbolService.getArboles());
         model.addAttribute("arbol", new Arbol());
         model.addAttribute("totalArboles", arbolService.getArboles().size());
         return "/arbol/listado";
     }
     
     @PostMapping("/guardar")
    public String guardar(@Valid Arbol arbol,
            BindingResult result,
            @RequestParam MultipartFile imagenFile,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "/arbol/listado"; // o el formulario
        }

        arbolService.save(arbol, imagenFile);
        redirectAttributes.addFlashAttribute(
                "todoOk",
                messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault())
        );
        return "redirect:/arbol/listado";
    }
    
    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idArbol, RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String detalle = "mensaje.eliminado";
        try {
            arbolService.delete(idArbol);
        } catch (IllegalArgumentException e) {
            titulo = "error"; // Captura la excepción de argumento inválido para el mensaje de "no existe"
            detalle = "arbol.error01";
        } catch (IllegalStateException e) {
            titulo = "error"; // Captura la excepción de estado ilegal para el mensaje de "datos asociados"
            detalle = "arbol.error02";
        } catch (Exception e) {
            titulo = "error";  // Captura cualquier otra excepción inesperada
            detalle = "arbol.error03";
        }
        redirectAttributes.addFlashAttribute(titulo, messageSource.getMessage(detalle, null, Locale.getDefault()));
        return "redirect:/arbol/listado";
    }
    
    @GetMapping("/modificar/{idArbol}")
    public String modificar(@PathVariable("idArbol") Integer idArbol, Model model, RedirectAttributes redirectAttributes) {
        Optional<Arbol> arbolOpt = arbolService.getArbol(idArbol);
        if (arbolOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("arbol.error01", null, Locale.getDefault()));
            return "redirect:/arbol/listado";
        }
        model.addAttribute("arbol", arbolOpt.get());
        return "/arbol/modifica";
    }
}
