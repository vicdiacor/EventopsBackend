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
@RequestMapping("/eventos")
public class EventosController {


    @Autowired
    private final EventosService eventosService;

    
    @Autowired
    private final LikeService likeService;

     
    @Autowired
    private final ClientRepository clientRepository;



    public EventosController(EventosService eventosService,LikeService likeService,ClientRepository clientRepository) {
        this.eventosService = eventosService;
        this.likeService = likeService;
        this.clientRepository = clientRepository;
        
     
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/publicNotFinishedSinLikes/")
    public ResponseEntity getPublicNotFinishedEvents() {
        
           
            List<Evento> events = eventosService.findPublicNotFinishedEvents();
            return ResponseEntity.ok(events);
      
        
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/numLikesEventosDisponibles/")
    public ResponseEntity findNumLikesEventosDisponibles() {
        
               
        Map<Long,Long> response = new HashMap<>();
        List<String> errores = new ArrayList<String>();

        response = likeService.findNumLikesEvents();
       
        return ResponseEntity.ok(response);
        
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/misEventosSinLikes/{userId}")
    public ResponseEntity getMisEventosSinLikes(@PathVariable String userId) {
        
           
        Map<String,Object> response = new HashMap<>();
        List<String> errores = new ArrayList<String>();

        Long userIdLong = Long.parseLong(userId);
        Client cliente = clientRepository.findById(userIdLong).get();
        
        if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) || cliente.getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())){
            
            
            List<Evento> events = eventosService.findMisEventos(userIdLong);
            return ResponseEntity.ok(events);
        }else{
            errores.add("No puedes editar los eventos de otro usuario");
            response.put("errores", errores);
            return ResponseEntity.badRequest().body(response);
        } 

      
        
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/publicNotFinished/{userId}")
    public ResponseEntity getPublicNotFinishedEventsUserLikes(@PathVariable String userId) {
        Map<String,Object> response = new HashMap<>();
        List<String> errores = new ArrayList<String>();

        Long userIdLong = Long.parseLong(userId);
        Client cliente = clientRepository.findById(userIdLong).get();
        
        if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) || cliente.getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())){
            
            Map<Long,Object> res = new HashMap<>(); // {idEvento: {EVENTO: X, LIKE:X}}
            List<Evento> events = eventosService.findPublicNotFinishedEvents();

            for (Evento evento : events){
                
                Map<String,Object> eventoAndLikeMap = new HashMap<>(); // {EVENTO: X, LIKE:X}
                eventoAndLikeMap.put("evento", evento);
                
                Optional<Like> like= likeService.findByUsuarioYEvento(cliente.getId(), evento.getId());
                if(like.isEmpty()){                    
                    eventoAndLikeMap.put("like", null);
                }else{
                    eventoAndLikeMap.put("like", like.get());

                }
                res.put(evento.getId(), eventoAndLikeMap);
                
            }
            return ResponseEntity.ok(res);
        }else{
            errores.add("No tienes acceso a los likes de otro usuario");
            response.put("errores", errores);
            return ResponseEntity.badRequest().body(response);
        } 


      
        
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/misEventosAndLikes/{userId}")
    public ResponseEntity getMisEventosAndLikes(@PathVariable String userId) {
        Map<String,Object> response = new HashMap<>();
        List<String> errores = new ArrayList<String>();

        Long userIdLong = Long.parseLong(userId);
        Client cliente = clientRepository.findById(userIdLong).get();
        
        if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) || cliente.getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())){
            
            Map<Long,Object> res = new HashMap<>(); // {idEvento: {EVENTO: X, LIKE:X}}
            List<Evento> events = eventosService.findMisEventos(userIdLong);

            for (Evento evento : events){
                
                Map<String,Object> eventoAndLikeMap = new HashMap<>(); // {EVENTO: X, LIKE:X}
                eventoAndLikeMap.put("evento", evento);
                
                Optional<Like> like= likeService.findByUsuarioYEvento(cliente.getId(), evento.getId());
                if(like.isEmpty()){                    
                    eventoAndLikeMap.put("like", null);
                }else{
                    eventoAndLikeMap.put("like", like.get());

                }
                res.put(evento.getId(), eventoAndLikeMap);
                
            }
            return ResponseEntity.ok(res);
        }else{
            errores.add("No tienes acceso a los likes de otro usuario");
            response.put("errores", errores);
            return ResponseEntity.badRequest().body(response);
        } 


      
        
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/{eventoId}")
    public ResponseEntity getEvento(@PathVariable String eventoId) {
        Map<String,Object> response = new HashMap<>();
        List<String> errores = new ArrayList<String>();

        Long eventoIdLong = Long.parseLong(eventoId);
        Optional<Evento> eventoOptional = eventosService.findById(eventoIdLong);
        if(eventoOptional.isEmpty()){

            errores.add("No puedes acceder a un evento que no existe");
            response.put("errores", errores);
            return ResponseEntity.badRequest().body(response);

        }else{
            Evento evento = eventoOptional.get();

            if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) || evento.getUsuario().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())){
            
                return ResponseEntity.ok(evento);
            }else{
                errores.add("No puedes editar el evento de otro usuario");
                response.put("errores", errores);
                return ResponseEntity.badRequest().body(response);
            } 
        }
        


      
        
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @SuppressWarnings("rawtypes")
	@PostMapping()
    public ResponseEntity createEvento(@RequestBody Evento evento) throws URISyntaxException {
        List<String> errores = new ArrayList<>();
        Map<String,Object> response = new HashMap<>();
        
        Evento savedEvento = eventosService.save(evento);
        return ResponseEntity.created(new URI("/eventos/" + savedEvento.getId())).body(savedEvento);
    	
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteEvento(@PathVariable Long id) {
        Optional<Evento> optionalEvent = eventosService.findById(id);
        Map<String,Object> response = new HashMap<>();
        List<String> errores = new ArrayList<>();

        if (optionalEvent.isPresent()){
            Evento evento= optionalEvent.get();
            String clienteEmail = evento.getUsuario().getEmail();

            if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) || clienteEmail.equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())){
                likeService.deleteAllLikesOfEvent(id);
                eventosService.deleteById(id);
                return ResponseEntity.ok().build();
            }else{
                errores.add("No puedes eliminar un evento que no es tuyo");
                response.put("errores",errores);
                return ResponseEntity.badRequest().body(response);
            
            }
            
           
        }else{
           
            errores.add("No puedes eliminar un evento que no existe");
			response.put("errores",errores);
			return ResponseEntity.badRequest().body(response);
        }
    }

    @SuppressWarnings("rawtypes")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @PutMapping("/{id}")
    public ResponseEntity updateEvento(@PathVariable Long id, @RequestBody Evento evento) {
    	Map<String,Object> response = new HashMap<>();
    	List<String> errores = new ArrayList<String>();
        Optional<Evento> optionalEvento= eventosService.findById(id);
        if (optionalEvento.isEmpty()){
             
  			errores.add("No puedes editar un evento que no existe");            
            response.put("errores", errores);
            return ResponseEntity.badRequest().body(response);
        }else{
            if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ||evento.getUsuario().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())){
                    Evento eventoDB= optionalEvento.get();
                    eventoDB.setAforoMaximo(evento.getAforoMaximo());
                    eventoDB.setCiudad(evento.getCiudad());
                    eventoDB.setDescripcion(evento.getDescripcion());
                    eventoDB.setFechaFin(evento.getFechaFin());
                    eventoDB.setFechaInicio(evento.getFechaInicio());
                    eventoDB.setMostrar(evento.getMostrar());
                    eventoDB.setNombre(evento.getNombre());
                    eventoDB.setPrecio(evento.getPrecio());
                    eventoDB = eventosService.save(eventoDB);
                    return ResponseEntity.ok(eventoDB);
            }else{
                
                errores.add("No puedes editar un evento que no es de tu propiedad");            
                response.put("errores", errores);
                return ResponseEntity.badRequest().body(response);
            }
        }
    	
    	
    }


    
}

