package com.eventops.web;

import java.net.URI;
import java.net.URISyntaxException;


import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;

import java.util.Optional;
import java.util.Set;

import java.util.Map;


import com.eventops.model.Client;

import com.eventops.model.Evento;
import com.eventops.model.Like;
import com.eventops.model.Role;
import com.eventops.repository.ClientRepository;
import com.eventops.repository.EventosRepository;
import com.eventops.repository.LikeRepository;
import com.eventops.repository.RoleRepository;
import com.eventops.service.EventosService;
import com.eventops.service.LikeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import antlr.debug.Event;

// @CrossOrigin(origins = "*",methods = {RequestMethod.GET,RequestMethod.PUT,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.HEAD}, maxAge = 1800)
@RestController
@RequestMapping("/likes")
public class LikeController {


    @Autowired
    private final LikeService likeService;
    
    @Autowired
    private final ClientRepository clientRepository;



    public LikeController(LikeService likeService,ClientRepository clientRepository) {
        this.likeService = likeService;
        this.clientRepository = clientRepository;
        
     
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/findByUser/{id}")
    public ResponseEntity getLikeByUser(@PathVariable String id) {
        Map<String,Object> response = new HashMap<>();
        List<String> errores = new ArrayList<String>();

        Long idLong= Long.parseLong(id);

        Client cliente = clientRepository.findById(idLong).get();
        
        if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) || cliente.getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())){
            List<Like> likes = likeService.findByUser(idLong);
            Map<Long,Like> mapaLikes = new HashMap<>(); // Mapa de (Id: LIKE)
            for (Like like:likes){
                mapaLikes.put(like.getId(), like);
            }
            return ResponseEntity.ok(mapaLikes);
        }else{
            errores.add("No tienes acceso a los likes de otro usuario");
            response.put("errores", errores);
            return ResponseEntity.badRequest().body(response);
        } 
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @SuppressWarnings("rawtypes")
	@PostMapping()
    public ResponseEntity createLike(@RequestBody Like like) throws URISyntaxException {
        List<String> errores = new ArrayList<>();
        Map<String,Object> response = new HashMap<>();
        String emailUser= like.getUsuario().getEmail();

        if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) || emailUser.equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())){
            if(likeService.usuarioHaDadoLikeAlEvento(like.getUsuario().getId(), like.getEvento().getId())){

                errores.add("No puedes estar interesado si previamente ya lo estabas, espera a que se actualice el sistema");
                response.put("errores", errores);
                return ResponseEntity.badRequest().body(response);
            }else{
                Like savedLike = likeService.save(like);
                return ResponseEntity.created(new URI("/likes/" + savedLike.getId())).body(savedLike);
            }
            
        }else{
            errores.add("No puedes dar like en nombre de otro usuario");
            response.put("errores", errores);
            return ResponseEntity.badRequest().body(response);
        } 
    	
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	@DeleteMapping("/{id}")
    public ResponseEntity quitarLike(@PathVariable Long id) throws URISyntaxException {
        List<String> errores = new ArrayList<>();
        Map<String,Object> response = new HashMap<>();
        Optional<Like> likeAlmacenado = likeService.findById(id);
        if(likeAlmacenado.isPresent()){
            Like like = likeAlmacenado.get();
            String emailUser= like.getUsuario().getEmail();

            if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) || emailUser.equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())){
                
               
                    likeService.deleteById(id);
                    return ResponseEntity.ok().build();
                
            }else{
                errores.add("No puedes eliminar un like de otro usuario");
                response.put("errores", errores);
                return ResponseEntity.badRequest().body(response);
            } 
        }else{

            errores.add("El like que intentas eliminar no existe. Espere a que se actualice el sistema");
            response.put("errores", errores);
            return ResponseEntity.badRequest().body(response);
        }

        
    	
    }

 


    
}

