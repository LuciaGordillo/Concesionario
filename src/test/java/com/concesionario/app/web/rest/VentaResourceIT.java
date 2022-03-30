package com.concesionario.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.concesionario.app.IntegrationTest;
import com.concesionario.app.domain.Venta;
import com.concesionario.app.repository.VentaRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link VentaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VentaResourceIT {

    private static final Integer DEFAULT_UNIDADES = 1;
    private static final Integer UPDATED_UNIDADES = 2;

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_DESCUENTO = 1D;
    private static final Double UPDATED_DESCUENTO = 2D;

    private static final Double DEFAULT_PRECIO_TOTAL = 1D;
    private static final Double UPDATED_PRECIO_TOTAL = 2D;

    private static final String ENTITY_API_URL = "/api/ventas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVentaMockMvc;

    private Venta venta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Venta createEntity(EntityManager em) {
        Venta venta = new Venta()
            .unidades(DEFAULT_UNIDADES)
            .fecha(DEFAULT_FECHA)
            .descuento(DEFAULT_DESCUENTO)
            .precio_total(DEFAULT_PRECIO_TOTAL);
        return venta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Venta createUpdatedEntity(EntityManager em) {
        Venta venta = new Venta()
            .unidades(UPDATED_UNIDADES)
            .fecha(UPDATED_FECHA)
            .descuento(UPDATED_DESCUENTO)
            .precio_total(UPDATED_PRECIO_TOTAL);
        return venta;
    }

    @BeforeEach
    public void initTest() {
        venta = createEntity(em);
    }

    @Test
    @Transactional
    void createVenta() throws Exception {
        int databaseSizeBeforeCreate = ventaRepository.findAll().size();
        // Create the Venta
        restVentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(venta)))
            .andExpect(status().isCreated());

        // Validate the Venta in the database
        List<Venta> ventaList = ventaRepository.findAll();
        assertThat(ventaList).hasSize(databaseSizeBeforeCreate + 1);
        Venta testVenta = ventaList.get(ventaList.size() - 1);
        assertThat(testVenta.getUnidades()).isEqualTo(DEFAULT_UNIDADES);
        assertThat(testVenta.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testVenta.getDescuento()).isEqualTo(DEFAULT_DESCUENTO);
        assertThat(testVenta.getPrecio_total()).isEqualTo(DEFAULT_PRECIO_TOTAL);
    }

    @Test
    @Transactional
    void createVentaWithExistingId() throws Exception {
        // Create the Venta with an existing ID
        venta.setId(1L);

        int databaseSizeBeforeCreate = ventaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(venta)))
            .andExpect(status().isBadRequest());

        // Validate the Venta in the database
        List<Venta> ventaList = ventaRepository.findAll();
        assertThat(ventaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUnidadesIsRequired() throws Exception {
        int databaseSizeBeforeTest = ventaRepository.findAll().size();
        // set the field null
        venta.setUnidades(null);

        // Create the Venta, which fails.

        restVentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(venta)))
            .andExpect(status().isBadRequest());

        List<Venta> ventaList = ventaRepository.findAll();
        assertThat(ventaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = ventaRepository.findAll().size();
        // set the field null
        venta.setFecha(null);

        // Create the Venta, which fails.

        restVentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(venta)))
            .andExpect(status().isBadRequest());

        List<Venta> ventaList = ventaRepository.findAll();
        assertThat(ventaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecio_totalIsRequired() throws Exception {
        int databaseSizeBeforeTest = ventaRepository.findAll().size();
        // set the field null
        venta.setPrecio_total(null);

        // Create the Venta, which fails.

        restVentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(venta)))
            .andExpect(status().isBadRequest());

        List<Venta> ventaList = ventaRepository.findAll();
        assertThat(ventaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVentas() throws Exception {
        // Initialize the database
        ventaRepository.saveAndFlush(venta);

        // Get all the ventaList
        restVentaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(venta.getId().intValue())))
            .andExpect(jsonPath("$.[*].unidades").value(hasItem(DEFAULT_UNIDADES)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].descuento").value(hasItem(DEFAULT_DESCUENTO.doubleValue())))
            .andExpect(jsonPath("$.[*].precio_total").value(hasItem(DEFAULT_PRECIO_TOTAL.doubleValue())));
    }

    @Test
    @Transactional
    void getVenta() throws Exception {
        // Initialize the database
        ventaRepository.saveAndFlush(venta);

        // Get the venta
        restVentaMockMvc
            .perform(get(ENTITY_API_URL_ID, venta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(venta.getId().intValue()))
            .andExpect(jsonPath("$.unidades").value(DEFAULT_UNIDADES))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.descuento").value(DEFAULT_DESCUENTO.doubleValue()))
            .andExpect(jsonPath("$.precio_total").value(DEFAULT_PRECIO_TOTAL.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingVenta() throws Exception {
        // Get the venta
        restVentaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVenta() throws Exception {
        // Initialize the database
        ventaRepository.saveAndFlush(venta);

        int databaseSizeBeforeUpdate = ventaRepository.findAll().size();

        // Update the venta
        Venta updatedVenta = ventaRepository.findById(venta.getId()).get();
        // Disconnect from session so that the updates on updatedVenta are not directly saved in db
        em.detach(updatedVenta);
        updatedVenta.unidades(UPDATED_UNIDADES).fecha(UPDATED_FECHA).descuento(UPDATED_DESCUENTO).precio_total(UPDATED_PRECIO_TOTAL);

        restVentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVenta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVenta))
            )
            .andExpect(status().isOk());

        // Validate the Venta in the database
        List<Venta> ventaList = ventaRepository.findAll();
        assertThat(ventaList).hasSize(databaseSizeBeforeUpdate);
        Venta testVenta = ventaList.get(ventaList.size() - 1);
        assertThat(testVenta.getUnidades()).isEqualTo(UPDATED_UNIDADES);
        assertThat(testVenta.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testVenta.getDescuento()).isEqualTo(UPDATED_DESCUENTO);
        assertThat(testVenta.getPrecio_total()).isEqualTo(UPDATED_PRECIO_TOTAL);
    }

    @Test
    @Transactional
    void putNonExistingVenta() throws Exception {
        int databaseSizeBeforeUpdate = ventaRepository.findAll().size();
        venta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, venta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(venta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Venta in the database
        List<Venta> ventaList = ventaRepository.findAll();
        assertThat(ventaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVenta() throws Exception {
        int databaseSizeBeforeUpdate = ventaRepository.findAll().size();
        venta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(venta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Venta in the database
        List<Venta> ventaList = ventaRepository.findAll();
        assertThat(ventaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVenta() throws Exception {
        int databaseSizeBeforeUpdate = ventaRepository.findAll().size();
        venta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVentaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(venta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Venta in the database
        List<Venta> ventaList = ventaRepository.findAll();
        assertThat(ventaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVentaWithPatch() throws Exception {
        // Initialize the database
        ventaRepository.saveAndFlush(venta);

        int databaseSizeBeforeUpdate = ventaRepository.findAll().size();

        // Update the venta using partial update
        Venta partialUpdatedVenta = new Venta();
        partialUpdatedVenta.setId(venta.getId());

        partialUpdatedVenta.fecha(UPDATED_FECHA).precio_total(UPDATED_PRECIO_TOTAL);

        restVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVenta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVenta))
            )
            .andExpect(status().isOk());

        // Validate the Venta in the database
        List<Venta> ventaList = ventaRepository.findAll();
        assertThat(ventaList).hasSize(databaseSizeBeforeUpdate);
        Venta testVenta = ventaList.get(ventaList.size() - 1);
        assertThat(testVenta.getUnidades()).isEqualTo(DEFAULT_UNIDADES);
        assertThat(testVenta.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testVenta.getDescuento()).isEqualTo(DEFAULT_DESCUENTO);
        assertThat(testVenta.getPrecio_total()).isEqualTo(UPDATED_PRECIO_TOTAL);
    }

    @Test
    @Transactional
    void fullUpdateVentaWithPatch() throws Exception {
        // Initialize the database
        ventaRepository.saveAndFlush(venta);

        int databaseSizeBeforeUpdate = ventaRepository.findAll().size();

        // Update the venta using partial update
        Venta partialUpdatedVenta = new Venta();
        partialUpdatedVenta.setId(venta.getId());

        partialUpdatedVenta.unidades(UPDATED_UNIDADES).fecha(UPDATED_FECHA).descuento(UPDATED_DESCUENTO).precio_total(UPDATED_PRECIO_TOTAL);

        restVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVenta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVenta))
            )
            .andExpect(status().isOk());

        // Validate the Venta in the database
        List<Venta> ventaList = ventaRepository.findAll();
        assertThat(ventaList).hasSize(databaseSizeBeforeUpdate);
        Venta testVenta = ventaList.get(ventaList.size() - 1);
        assertThat(testVenta.getUnidades()).isEqualTo(UPDATED_UNIDADES);
        assertThat(testVenta.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testVenta.getDescuento()).isEqualTo(UPDATED_DESCUENTO);
        assertThat(testVenta.getPrecio_total()).isEqualTo(UPDATED_PRECIO_TOTAL);
    }

    @Test
    @Transactional
    void patchNonExistingVenta() throws Exception {
        int databaseSizeBeforeUpdate = ventaRepository.findAll().size();
        venta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, venta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(venta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Venta in the database
        List<Venta> ventaList = ventaRepository.findAll();
        assertThat(ventaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVenta() throws Exception {
        int databaseSizeBeforeUpdate = ventaRepository.findAll().size();
        venta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(venta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Venta in the database
        List<Venta> ventaList = ventaRepository.findAll();
        assertThat(ventaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVenta() throws Exception {
        int databaseSizeBeforeUpdate = ventaRepository.findAll().size();
        venta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVentaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(venta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Venta in the database
        List<Venta> ventaList = ventaRepository.findAll();
        assertThat(ventaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVenta() throws Exception {
        // Initialize the database
        ventaRepository.saveAndFlush(venta);

        int databaseSizeBeforeDelete = ventaRepository.findAll().size();

        // Delete the venta
        restVentaMockMvc
            .perform(delete(ENTITY_API_URL_ID, venta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Venta> ventaList = ventaRepository.findAll();
        assertThat(ventaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
