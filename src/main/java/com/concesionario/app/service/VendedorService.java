package com.concesionario.app.service;

import com.concesionario.app.domain.Vendedor;
import com.concesionario.app.repository.VendedorRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Vendedor}.
 */
@Service
@Transactional
public class VendedorService {

    private final Logger log = LoggerFactory.getLogger(VendedorService.class);

    private final VendedorRepository vendedorRepository;

    public VendedorService(VendedorRepository vendedorRepository) {
        this.vendedorRepository = vendedorRepository;
    }

    /**
     * Save a vendedor.
     *
     * @param vendedor the entity to save.
     * @return the persisted entity.
     */
    public Vendedor save(Vendedor vendedor) {
        log.debug("Request to save Vendedor : {}", vendedor);
        return vendedorRepository.save(vendedor);
    }

    /**
     * Partially update a vendedor.
     *
     * @param vendedor the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Vendedor> partialUpdate(Vendedor vendedor) {
        log.debug("Request to partially update Vendedor : {}", vendedor);

        return vendedorRepository
            .findById(vendedor.getId())
            .map(existingVendedor -> {
                if (vendedor.getDni() != null) {
                    existingVendedor.setDni(vendedor.getDni());
                }
                if (vendedor.getNombre() != null) {
                    existingVendedor.setNombre(vendedor.getNombre());
                }
                if (vendedor.getDireccion() != null) {
                    existingVendedor.setDireccion(vendedor.getDireccion());
                }
                if (vendedor.getTelefono() != null) {
                    existingVendedor.setTelefono(vendedor.getTelefono());
                }

                return existingVendedor;
            })
            .map(vendedorRepository::save);
    }

    /**
     * Get all the vendedors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Vendedor> findAll(Pageable pageable) {
        log.debug("Request to get all Vendedors");
        return vendedorRepository.findAll(pageable);
    }

    /**
     * Get one vendedor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Vendedor> findOne(Long id) {
        log.debug("Request to get Vendedor : {}", id);
        return vendedorRepository.findById(id);
    }

    /**
     * Delete the vendedor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Vendedor : {}", id);
        vendedorRepository.deleteById(id);
    }
}
