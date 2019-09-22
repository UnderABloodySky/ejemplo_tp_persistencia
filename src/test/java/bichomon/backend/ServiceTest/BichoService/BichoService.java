package bichomon.backend.ServiceTest.BichoService;

import ar.edu.unq.epers.bichomon.backend.dao.impl.HibernateBichoDao;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import ar.edu.unq.epers.bichomon.backend.model.especie.TipoBicho;
import ar.edu.unq.epers.bichomon.backend.model.Service;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BichoService {

    private Bicho bicho;
    private Especie especie;
    private Service service;

    @Before
    public void prepare() {
        especie = new Especie("fuego", TipoBicho.AIRE);
        bicho = new Bicho(especie, "alfredo");
        service = new Service();
        service.setBichoDao(new HibernateBichoDao());

    }
    @Test
    public void test01_NoSePuedeRecuperarUnBichoQueNoPerteneceALaBaseDeDatos(){
        this.service.guardarBicho(bicho);
        Bicho bichoRecuperado = this.service.recuperarBicho(bicho.getId());
        Assert.assertEquals(bichoRecuperado.getNombre(), bicho.getNombre());
    }

    @Test
    public void test002_(){

    }


}
