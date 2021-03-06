package bichomon.backend.model.duelo;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.duelo.Duelo;
import ar.edu.unq.epers.bichomon.backend.model.duelo.ResultadoCombate;
import ar.edu.unq.epers.bichomon.backend.model.entrenador.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.entrenador.Nivel;
import ar.edu.unq.epers.bichomon.backend.model.entrenador.ProveedorDeNiveles;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import ar.edu.unq.epers.bichomon.backend.model.especie.TipoBicho;
import ar.edu.unq.epers.bichomon.backend.model.historialDeCampeones.FichaDeCampeon;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Dojo;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Guarderia;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.UbicacionIncorrectaException;
import bichomon.backend.factory.Factory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DueloTest {
    Entrenador retador;
    Entrenador campeon;
    Bicho bicho;
    LocalDate fechaInicio;
    Especie reptilmon;
    Especie largartomon;
    ProveedorDeNiveles proveedor;
    List niveles = new ArrayList<Nivel>();
    Nivel nivel;
    List fichas = new ArrayList<FichaDeCampeon>();
    FichaDeCampeon ficha;
    Bicho bicho1;
    Dojo dojo;
    Duelo duelo;
    @Before
    public void setup(){
        reptilmon = Factory.especieSinEvolucion("Reptilmon", TipoBicho.TIERRA);
        largartomon = new Especie("Lagartomon", TipoBicho.TIERRA, reptilmon, largartomon);
        bicho = new Bicho(reptilmon, "helloworld");
        bicho1 = new Bicho(largartomon, "blabla");
        bicho.setEnergia(1000.00);
        bicho1.setEnergia(1000.00);
        nivel = new Nivel(1, 1, 99);
        niveles.add(nivel);
        proveedor = Factory.proveedorDeNiveles(niveles);
        campeon = new Entrenador("campeon", new Dojo("Guarderia"), proveedor);
        bicho.setEntrenador(campeon);

        retador = new Entrenador("Entrenador", new Dojo("Guarderia"), proveedor);
        bicho1.setEntrenador(retador);

        fechaInicio = LocalDate.now();
        ficha = new FichaDeCampeon(campeon, bicho, fechaInicio, dojo);

        dojo = new Dojo("prueba");
        dojo.declararCampeones(campeon, bicho);
        duelo = new Duelo(bicho1, dojo);

    }
    @Test
    public void alPelearLaListaDeAtaquesTieneAlMenosUnAtaque(){
        duelo.pelear();
        Assert.assertTrue(duelo.cantidadDeAtaques() >0);
    }
    @Test
    public void luegoDeUnaPeleaUnBichoTieneMenosEnergiaQueAntes(){
        duelo.pelear();
        Assert.assertTrue(bicho.getEnergiaPorDuelo() < 1000.00);
    }
    @Test
    public void luegoDeUnDueloElObjetoResultadoNoTieneAtributosNulosYLaListDeAtaquesTieneElementos(){
        ResultadoCombate resultado = duelo.pelear();
        Assert.assertFalse(resultado.getAtaques().isEmpty());

    }
    @Test
    public void elResultadoDeUnDueloTieneunBichoYEntrenadorCampeon(){
        ResultadoCombate resultado = duelo.pelear();
        Assert.assertNotNull(resultado.getBichoCampeon());
        Assert.assertNotNull(resultado.getNombreDeEntrenadorCampeon());
    }
    @Test
    public void luegoDeUnDueloElBichoCampeonAunTieneEnergia(){
        ResultadoCombate resultado = duelo.pelear();
        Assert.assertTrue(resultado.getBichoCampeon().puedeSeguir());
    }

    @Test(expected = UbicacionIncorrectaException.class)
    public void unEntrenadorEnUnaUbicacionQueNoEsDojoNoPuedeEstarEnDuelo(){
        retador.ubicarseEn(new Guarderia("Guarderia"));
        retador.duelo(bicho);
    }

    @Test
    public void elBichoCampeonDeUnDueloAumentaSuCantidadDeVictorias() {
        ResultadoCombate resultado = duelo.pelear();
        Assert.assertEquals(new Integer(1), resultado.getBichoCampeon().getVictorias());
    }
}
