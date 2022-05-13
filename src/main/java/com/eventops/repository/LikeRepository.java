package com.eventops.repository;


import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.eventops.model.Evento;
import com.eventops.model.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {

    
    @Query(value = "SELECT  * FROM likes WHERE user_id = :id", nativeQuery=true)
    public List<Like> findByUser(@Param("id") Long id) throws DataAccessException;


    @Query(value = "SELECT  * FROM likes WHERE user_id = :idUsuario and evento_id = :idEvento", nativeQuery=true)
    public List<Like> findByEventoYUsuario(@Param("idUsuario") Long idUsuario,@Param("idEvento") Long idEvento) throws DataAccessException;

    @Query(value = "SELECT  evento_id ,COUNT(*) FROM (likes l LEFT JOIN eventos e ON e.mostrar=1 and e.fecha_fin >= :fechaActual and l.evento_id = e.id) GROUP BY evento_id", nativeQuery=true)
    public List<List<Long>> findNumLikesOfEventosDisponibles(@Param("fechaActual") LocalDateTime fechaActual) throws DataAccessException;
     
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM likes WHERE evento_id = :idEvento", nativeQuery=true)
    public void deleteAllLikesOfEvent(@Param("idEvento") Long idEvento) throws DataAccessException;

 
}
