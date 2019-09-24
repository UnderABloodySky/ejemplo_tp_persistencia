package bichomon.backend.model.ubicacion;

import static org.junit.Assert.*;

import ar.edu.unq.epers.bichomon.backend.model.LimitePokemon;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.entrenador.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.entrenador.Nivel;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Pueblo;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;
import org.junit.Before;
import org.junit.Test;

public class PuebloTest extends UbicacionTest {

    private Pueblo pueblo;
    private Nivel nivel;

    @Before
    public void setUp() {
        nivel = new Nivel(10, 1000, 1000);
        entrenador = new Entrenador("El Entrenador", null, nivel, null);
        pueblo = new Pueblo("El Pueblo");
    }

    @Test
    public void un_pueblo_no_puede_dejar_abandonar_a_un_entrenador() {
        assertFalse(pueblo.puedeDejarAbandonar(entrenador));
    }

    @Test
    public void se_lanza_una_excepcion_si_el_entrenador_intenta_abandonar_un_bichomon_en_un_dojo() throws LimitePokemon {
        Bicho bichoAAbandonar = nuevoBicho("Bicho");
        entrenador.capturarBichomon(bichoAAbandonar, 10);
        try {
            pueblo.recibirAbandonado(entrenador, bichoAAbandonar);
            fail();
        } catch (RuntimeException e) {
            assertEquals(Ubicacion.ERROR_ABANDONO, e.getMessage());
        }
    }

    @Test
    public void un_pueblo_recien_creado_no_tiene_especies_posibles_de_bichomones_para_capturar() {
        assertTrue(pueblo.especiesPosibles().isEmpty());
    }

    @Test
    public void se_agrega_una_especie_posible_a_un_pueblo_vacio() {
        Especie especie = nuevaEspecie("Especie");
        pueblo.agregarEspecie(especie, 10);

        assertFalse(pueblo.especiesPosibles().isEmpty());
    }

    @Test
    public void no_se_puede_agregar_una_especie_posible_si_sobrepasa_la_probabilidad_total_de_todas_las_especies_de_aparecer() {
        Especie especie = nuevaEspecie("Especie");

        try {
            pueblo.agregarEspecie(especie, 101);
        } catch (RuntimeException e) {
            assertEquals(Pueblo.ERROR_EXCESO_ESPECIES, e.getMessage());
        }
    }

    //TODO: Testear bichomonPara(Entrenador entrenador)
}
