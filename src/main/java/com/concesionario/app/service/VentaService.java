package com.concesionario.app.service;

import com.concesionario.app.domain.Venta;
import com.concesionario.app.repository.VentaRepository;

import java.time.LocalDate;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Venta}.
 */
@Service
@Transactional
public class VentaService {

    private final Logger log = LoggerFactory.getLogger(VentaService.class);

    private final VentaRepository ventaRepository;

    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    /**
     * Save a venta.
     *
     * @param venta the entity to save.
     * @return the persisted entity.
     */
    public Venta save(Venta venta) {
        log.debug("Request to save Venta : {}", venta);
        venta.setFecha(LocalDate.now());
        return ventaRepository.save(venta);
    }

    /**
     * Partially update a venta.
     *
     * @param venta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Venta> partialUpdate(Venta venta) {
        log.debug("Request to partially update Venta : {}", venta);

        return ventaRepository
            .findById(venta.getId())
            .map(existingVenta -> {
                if (venta.getUnidades() != null) {
                    existingVenta.setUnidades(venta.getUnidades());
                }
                if (venta.getFecha() != null) {
                    existingVenta.setFecha(venta.getFecha());
                }
                if (venta.getDescuento() != null) {
                    existingVenta.setDescuento(venta.getDescuento());
                }
                if (venta.getPrecio_total() != null) {
                    existingVenta.setPrecio_total(venta.getPrecio_total());
                }

                return existingVenta;
            })
            .map(ventaRepository::save);
    }

    /**
     * Get all the ventas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Venta> findAll(Pageable pageable) {
        log.debug("Request to get all Ventas");
        return ventaRepository.findAll(pageable);
    }

    /**
     * Get one venta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Venta> findOne(Long id) {
        log.debug("Request to get Venta : {}", id);
        return ventaRepository.findById(id);
    }

    /**
     * Delete the venta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Venta : {}", id);
        ventaRepository.deleteById(id);
    }
}
