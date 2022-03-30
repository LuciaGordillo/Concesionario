package com.concesionario.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.concesionario.app.IntegrationTest;
import com.concesionario.app.domain.Vendedor;
import com.concesionario.app.repository.VendedorRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VendedorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VendedorResourceIT {

    private static final String DEFAULT_DNI = "AAAAAAAAAA";
    private static final String UPDATED_DNI = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final Integer DEFAULT_TELEFONO = 1;
    private static final Integer UPDATED_TELEFONO = 2;

    private static final String ENTITY_API_URL = "/api/vendedors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VendedorRepository vendedorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVendedorMockMvc;

    private Vendedor vendedor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vendedor createEntity(EntityManager em) {
        Vendedor vendedor = new Vendedor().dni(DEFAULT_DNI).nombre(DEFAULT_NOMBRE).direccion(DEFAULT_DIRECCION).telefono(DEFAULT_TELEFONO);
        return vendedor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vendedor createUpdatedEntity(EntityManager em) {
        Vendedor vendedor = new Vendedor().dni(UPDATED_DNI).nombre(UPDATED_NOMBRE).direccion(UPDATED_DIRECCION).telefono(UPDATED_TELEFONO);
        return vendedor;
    }

    @BeforeEach
    public void initTest() {
        vendedor = createEntity(em);
    }

    @Test
    @Transactional
    void createVendedor() throws Exception {
        int databaseSizeBeforeCreate = vendedorRepository.findAll().size();
        // Create the Vendedor
        restVendedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendedor)))
            .andExpect(status().isCreated());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeCreate + 1);
        Vendedor testVendedor = vendedorList.get(vendedorList.size() - 1);
        assertThat(testVendedor.getDni()).isEqualTo(DEFAULT_DNI);
        assertThat(testVendedor.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testVendedor.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testVendedor.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
    }

    @Test
    @Transactional
    void createVendedorWithExistingId() throws Exception {
        // Create the Vendedor with an existing ID
        vendedor.setId(1L);

        int databaseSizeBeforeCreate = vendedorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVendedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendedor)))
            .andExpect(status().isBadRequest());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDniIsRequired() throws Exception {
        int databaseSizeBeforeTest = vendedorRepository.findAll().size();
        // set the field null
        vendedor.setDni(null);

        // Create the Vendedor, which fails.

        restVendedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendedor)))
            .andExpect(status().isBadRequest());

        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelefonoIsRequired() throws Exception {
        int databaseSizeBeforeTest = vendedorRepository.findAll().size();
        // set the field null
        vendedor.setTelefono(null);

        // Create the Vendedor, which fails.

        restVendedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendedor)))
            .andExpect(status().isBadRequest());

        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVendedors() throws Exception {
        // Initialize the database
        vendedorRepository.saveAndFlush(vendedor);

        // Get all the vendedorList
        restVendedorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vendedor.getId().intValue())))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)));
    }

    @Test
    @Transactional
    void getVendedor() throws Exception {
        // Initialize the database
        vendedorRepository.saveAndFlush(vendedor);

        // Get the vendedor
        restVendedorMockMvc
            .perform(get(ENTITY_API_URL_ID, vendedor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vendedor.getId().intValue()))
            .andExpect(jsonPath("$.dni").value(DEFAULT_DNI))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO));
    }

    @Test
    @Transactional
    void getNonExistingVendedor() throws Exception {
        // Get the vendedor
        restVendedorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVendedor() throws Exception {
        // Initialize the database
        vendedorRepository.saveAndFlush(vendedor);

        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();

        // Update the vendedor
        Vendedor updatedVendedor = vendedorRepository.findById(vendedor.getId()).get();
        // Disconnect from session so that the updates on updatedVendedor are not directly saved in db
        em.detach(updatedVendedor);
        updatedVendedor.dni(UPDATED_DNI).nombre(UPDATED_NOMBRE).direccion(UPDATED_DIRECCION).telefono(UPDATED_TELEFONO);

        restVendedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVendedor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVendedor))
            )
            .andExpect(status().isOk());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
        Vendedor testVendedor = vendedorList.get(vendedorList.size() - 1);
        assertThat(testVendedor.getDni()).isEqualTo(UPDATED_DNI);
        assertThat(testVendedor.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testVendedor.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testVendedor.getTelefono()).isEqualTo(UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void putNonExistingVendedor() throws Exception {
        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();
        vendedor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVendedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vendedor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vendedor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVendedor() throws Exception {
        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();
        vendedor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vendedor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVendedor() throws Exception {
        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();
        vendedor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendedorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendedor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVendedorWithPatch() throws Exception {
        // Initialize the database
        vendedorRepository.saveAndFlush(vendedor);

        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();

        // Update the vendedor using partial update
        Vendedor partialUpdatedVendedor = new Vendedor();
        partialUpdatedVendedor.setId(vendedor.getId());

        partialUpdatedVendedor.dni(UPDATED_DNI);

        restVendedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVendedor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVendedor))
            )
            .andExpect(status().isOk());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
        Vendedor testVendedor = vendedorList.get(vendedorList.size() - 1);
        assertThat(testVendedor.getDni()).isEqualTo(UPDATED_DNI);
        assertThat(testVendedor.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testVendedor.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testVendedor.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
    }

    @Test
    @Transactional
    void fullUpdateVendedorWithPatch() throws Exception {
        // Initialize the database
        vendedorRepository.saveAndFlush(vendedor);

        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();

        // Update the vendedor using partial update
        Vendedor partialUpdatedVendedor = new Vendedor();
        partialUpdatedVendedor.setId(vendedor.getId());

        partialUpdatedVendedor.dni(UPDATED_DNI).nombre(UPDATED_NOMBRE).direccion(UPDATED_DIRECCION).telefono(UPDATED_TELEFONO);

        restVendedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVendedor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVendedor))
            )
            .andExpect(status().isOk());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
        Vendedor testVendedor = vendedorList.get(vendedorList.size() - 1);
        assertThat(testVendedor.getDni()).isEqualTo(UPDATED_DNI);
        assertThat(testVendedor.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testVendedor.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testVendedor.getTelefono()).isEqualTo(UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void patchNonExistingVendedor() throws Exception {
        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();
        vendedor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVendedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vendedor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vendedor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVendedor() throws Exception {
        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();
        vendedor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vendedor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVendedor() throws Exception {
        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();
        vendedor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendedorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vendedor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVendedor() throws Exception {
        // Initialize the database
        vendedorRepository.saveAndFlush(vendedor);

        int databaseSizeBeforeDelete = vendedorRepository.findAll().size();

        // Delete the vendedor
        restVendedorMockMvc
            .perform(delete(ENTITY_API_URL_ID, vendedor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
