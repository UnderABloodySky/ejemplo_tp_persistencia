package bichomon.backend.ServiceTest.BichoService;

import ar.edu.unq.epers.bichomon.backend.jdbc.dao.BichoDao;
import ar.edu.unq.epers.bichomon.backend.jdbc.dao.impl.HibernateBichoDao;
import ar.edu.unq.epers.bichomon.backend.jdbc.dao.impl.HibernateEntrenadorDao;
import ar.edu.unq.epers.bichomon.backend.jdbc.service.bicho.BichoServiceImpl;
import ar.edu.unq.epers.bichomon.backend.jdbc.service.bicho.ErrorBichoNoPerteneceAEntrenador;
import ar.edu.unq.epers.bichomon.backend.jdbc.service.runner.TransactionRunner;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.entrenador.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.entrenador.Nivel;
import ar.edu.unq.epers.bichomon.backend.model.entrenador.ProveedorDeNiveles;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import ar.edu.unq.epers.bichomon.backend.model.especie.TipoBicho;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.*;
import bichomon.backend.factory.Factory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;

public class BichoServiceTest {

    Guarderia guarderia;
    Nivel nivel;
    ProveedorDeNiveles proveedor;
    Entrenador entrenador;
    Especie especie;
    Especie reptilmon;
    Bicho bicho;
    BichoServiceImpl bichoService;
    BichoDao bichoDao;
    HibernateEntrenadorDao entrenadorDao;
    Pueblo pueblo;
    Dojo dojo;

    @Before
    public void setUp(){
        guarderia = new Guarderia("guarderia");
        nivel = new Nivel(2, 1,99);
        List niveles = new ArrayList<Nivel>();
        niveles.add(nivel);
        proveedor = new ProveedorDeNiveles(niveles);
        reptilmon = new Especie("reptilmon", TipoBicho.TIERRA);
        especie = new Especie("especiemon", TipoBicho.TIERRA, reptilmon, especie);
        bicho = new Bicho(especie);
        entrenador = new Entrenador("ASH", null ,proveedor);
        bichoService = Mockito.spy(new BichoServiceImpl());
        bichoDao = new HibernateBichoDao();
        entrenadorDao = new HibernateEntrenadorDao();
        pueblo = new Pueblo("Pueblo");
        dojo = new Dojo("Dojo");

        bichoService.setBichoDao(bichoDao);
        bichoService.setEntrenadorDao(entrenadorDao);
    }

    @After
    public void cleanUp() {
        TransactionRunner.run(() -> {
            Factory.hibernateEntrenadorDAO().borrarTodo();
        });
    }

    @Test
    public void se_busca_un_bicho_por_nombre_de_entrenador() {
        pueblo.agregarEspecie(especie, 100);
        entrenador.moverseA(pueblo);
        bichoService.guardarEntrenador(entrenador);

        ResultadoDeBusqueda resultadoDeBusqueda = Mockito.mock(ResultadoDeBusqueda.class);
        Mockito.when(resultadoDeBusqueda.busquedaExitosa()).thenReturn(true);
        doReturn(resultadoDeBusqueda).when(bichoService).getBusqueda();

        Bicho bichoRecuperado = bichoService.buscar(entrenador.getNombre());

        assertEquals(bichoRecuperado.getEspecie().getNombre(), bicho.getEspecie().getNombre());
    }

    @Test(expected = ErrorBichoNoPerteneceAEntrenador.class)
    public void seLanzaUnErrorSiElBichoBuscadoNoPerteneceAlEntrenador() {
        entrenador.moverseA(dojo);

        bichoService.guardarEntrenador(entrenador);
        bichoService.guardarBicho(bicho);

        bichoService.puedeEvolucionar(entrenador.getNombre(), bicho.getId());
    }

    @Test(expected = ErrorAbandonoImposible.class)
    public void unEntrenadorNoPuedeAbandonarASuBichoEnUnDojo() {
        entrenador.moverseA(dojo);
        entrenador.capturarBichomon(bicho, 10);

        bichoService.guardarEntrenador(entrenador);

        bichoService.abandonar(entrenador.getNombre(), bicho.getId());
    }

    @Test(expected = ErrorAbandonoImposible.class)
    public void unEntrenadorNoPuedeAbandonarASuBichoEnUnPueblo() {
        entrenador.moverseA(pueblo);
        entrenador.capturarBichomon(bicho, 10);

        bichoService.guardarEntrenador(entrenador);

        bichoService.abandonar(entrenador.getNombre(), bicho.getId());
    }

    @Test(expected = ErrorAbandonoImposible.class)
    public void unEntrenadorNoPuedeAbandonarASuBichoEnUnaGuarderiaSiEsElUnicoQueTiene() {
        entrenador.moverseA(guarderia);
        entrenador.capturarBichomon(bicho, 10);

        bichoService.guardarEntrenador(entrenador);

        bichoService.abandonar(entrenador.getNombre(), bicho.getId());
    }

    @Test
    public void unEntrenadorPuedeAbandonarASuBichoEnUnaGuarderiaSiTieneOtro() {
        entrenador.moverseA(guarderia);
        entrenador.capturarBichomon(bicho, 10);
        Bicho otroBicho = Factory.bicho(especie);
        entrenador.capturarBichomon(otroBicho, 10);

        bichoService.guardarEntrenador(entrenador);

        bichoService.abandonar(entrenador.getNombre(), bicho.getId());

        Entrenador entrenadorRecuperado = TransactionRunner.run(() -> entrenadorDao.recuperar(entrenador.getNombre()));
        Guarderia guarderiaRecuperada = (Guarderia)entrenadorRecuperado.getUbicacionActual();

        assertEquals(1, guarderiaRecuperada.getBichosAbandonados().size());
    }

    @Test
    public void siUnBichoNoCumpleLaCondicionDeEvolucionNoPuedeEvolucionar(){
        bicho.setEnergia(1d);
        especie.setCondicionDeEvolucion(Factory.condicionBasadaEnEnergia(3));
        entrenador.capturarBichomon(bicho, 10);
        bichoService.guardarEntrenador(entrenador);

        assertFalse(bichoService.puedeEvolucionar(entrenador.getNombre(), bicho.getId()));
    }

    @Test
    public void unBichoSabeSiPuedeEvolucionarONo(){
        bicho.setEnergia(10d);
        especie.setCondicionDeEvolucion(Factory.condicionBasadaEnEnergia(3));
        entrenador.capturarBichomon(bicho, 10);

        bichoService.guardarEntrenador(entrenador);

        assertTrue(bichoService.puedeEvolucionar(entrenador.getNombre(), bicho.getId()));
    }

    @Test
    public void unBichoSiCumpleLaCondicionDeEvolucionPuedeEvolucionar(){
        bicho.setEnergia(10d);
        especie.setCondicionDeEvolucion(Factory.condicionBasadaEnEnergia(3));
        entrenador.capturarBichomon(bicho, 10);

        bichoService.guardarEntrenador(entrenador);
        bicho = bichoService.evolucionar(entrenador.getNombre(), bicho.getId());

        assertEquals(reptilmon.getNombre(), bicho.getEspecie().getNombre());
    }
}


