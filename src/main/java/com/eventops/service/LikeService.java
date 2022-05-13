package com.eventops.service;


import com.eventops.repository.LikeRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.eventops.model.Like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

	@Autowired
    private LikeRepository repository;
    
    
	public List<Like> findByUser(Long user_id){
        return repository.findByUser(user_id);
    }

    public Boolean usuarioHaDadoLikeAlEvento(Long user_id, Long evento_id){
        
        List<Like> likes= repository.findByEventoYUsuario(user_id, evento_id);
        if (likes.isEmpty()){
            return false;
        }else{
            return true;
        }

    }
	public Optional<Like> findById(Long id){ 
        return repository.findById(id);
    }

    public Optional<Like> findByUsuarioYEvento(Long user_id, Long evento_id){ //Retorna el like si lo hay, y si no null
        Optional<Like> res = Optional.empty();

        List<Like> likes= repository.findByEventoYUsuario(user_id, evento_id);
        if (likes.isEmpty()){
            return res;
        }else{
            Like likeEncontrado = likes.get(0);
            return Optional.of(likeEncontrado);
        }

    }

    public Map<Long,Long> findNumLikesEvents(){ 
        LocalDateTime fechaActual= LocalDateTime.now();
        Map<Long,Long> res = new HashMap<>();
        List<List<Long>> eventosNumLikes= repository.findNumLikesOfEventosDisponibles(fechaActual);
        for (List<Long> tupla: eventosNumLikes){
            res.put(tupla.get(0), tupla.get(1));
        }
        return res;
    }


    public Like save(Like like){ 
        return repository.save(like);
    }

    public void deleteById(Long id){ 
        repository.deleteById(id);
    }

    public void deleteAllLikesOfEvent(Long idEvento){
        repository.deleteAllLikesOfEvent(idEvento);
    }

  
}
