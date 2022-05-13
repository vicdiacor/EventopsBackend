package com.eventops.service;

import com.eventops.repository.EventosRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.eventops.model.Evento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventosService {

	@Autowired
    private EventosRepository repository;
    
    
	public List<Evento> findAll(){
        return repository.findAll();
    }

    public List<Evento> findPublicNotFinishedEvents(){
        LocalDateTime fechaActual= LocalDateTime.now();
       

        return repository.findPublicNotFinishedEvents(fechaActual);
    }
    public List<Evento> findMisEventos(Long user_id){
       

        return repository.findMisEventos(user_id);
    }

	public Optional<Evento> findById(Long id){ 
        return repository.findById(id);
    }

    public Evento save(Evento evento){ 
        return repository.save(evento);
    }

    

    public void deleteById(Long id){ 
        repository.deleteById(id);
    }
    
    
}
