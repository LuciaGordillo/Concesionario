package com.concesionario.app.service;

import com.concesionario.app.domain.Coche;
import com.concesionario.app.repository.CocheRepository;
import com.concesionario.app.repository.specifications.CocheSpecification;

import java.util.List;
import java.util.Optional;

import org.hibernate.internal.FilterHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Coche}.
 */
@Service
@Transactional
public class CocheService {

    private final Logger log = LoggerFactory.getLogger(CocheService.class);

    private final CocheRepository cocheRepository;

    public CocheService(CocheRepository cocheRepository) {
        this.cocheRepository = cocheRepository;
    }

    /**
     * Save a coche.
     *
     * @param coche the entity to save.
     * @return the persisted entity.
     */
    public Coche save(Coche coche) {
        log.debug("Request to save Coche : {}", coche);
        return cocheRepository.save(coche);
    }

    /**
     * Partially update a coche.
     *
     * @param coche the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Coche> partialUpdate(Coche coche) {
        log.debug("Request to partially update Coche : {}", coche);

        return cocheRepository
            .findById(coche.getId())
            .map(existingCoche -> {
                if (coche.getColor() != null) {
                    existingCoche.setColor(coche.getColor());
                }
                if (coche.getMatricula() != null) {
                    existingCoche.setMatricula(coche.getMatricula());
                }
                if (coche.getPotencia() != null) {
                    existingCoche.setPotencia(coche.getPotencia());
                }
                if (coche.getPrecio() != null) {
                    existingCoche.setPrecio(coche.getPrecio());
                }
                if (coche.getPuertas() != null) {
                    existingCoche.setPuertas(coche.getPuertas());
                }
                if (coche.getCombustible() != null) {
                    existingCoche.setCombustible(coche.getCombustible());
                }
                if (coche.getVendido() != null) {
                    existingCoche.setVendido(coche.getVendido());
                }

                return existingCoche;
            })
            .map(cocheRepository::save);
    }

    /**
     * Get all the coches.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Coche> findAll(Pageable pageable) {
        log.debug("Request to get all Coches");
        return cocheRepository.findAll(pageable);
    }
  

    /**
     * Get all the coches with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Coche> findAllWithEagerRelationships(Pageable pageable) {
        return cocheRepository.findAllWithEagerRelationships(pageable);
    }
    /*Get all the coches with vendido=true*/
    public Page<Coche> devolverTrue(Pageable pageable){
        return cocheRepository.findByVendidoTrue(pageable);
    }
        /*Get all the coches with vendido=false*/

    public Page<Coche> devolverFalse(Pageable pageable){
        return cocheRepository.findByVendidoFalse(pageable);
    }
    /**
     * Get one coche by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Coche> findOne(Long id) {
        log.debug("Request to get Coche : {}", id);
        return cocheRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the coche by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Coche : {}", id);
        cocheRepository.deleteById(id);
    }
    public List<Coche> buscarcolor(String color){
        log.debug("Llega a consulta color");
        return cocheRepository.findByColorContaining(color);
    }
    public List<Coche> buscarfiltro(String filtro){
        log.debug("Llega a consulta filtro");
       return cocheRepository.findAll(CocheSpecification.busquedaCoche(filtro));
    }
}
