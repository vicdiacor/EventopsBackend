package com.eventops.repository;


import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

import com.eventops.model.Evento;

public interface EventosRepository extends JpaRepository<Evento, Long> {

    @Query(value = "SELECT  * FROM eventos  WHERE mostrar=1 and fecha_fin >= :fechaActual", nativeQuery=true)
    public List<Evento> findPublicNotFinishedEvents( @Param("fechaActual") LocalDateTime fechaActual) throws DataAccessException;

    @Query(value = "SELECT  * FROM eventos  WHERE user_id = :userId", nativeQuery=true)
    public List<Evento> findMisEventos( @Param("userId") Long userId) throws DataAccessException;



}
