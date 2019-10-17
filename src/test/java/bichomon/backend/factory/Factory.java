package bichomon.backend.factory;

import ar.edu.unq.epers.bichomon.backend.jdbc.dao.impl.HibernateBichoDao;
import ar.edu.unq.epers.bichomon.backend.jdbc.dao.impl.HibernateEntrenadorDao;
import ar.edu.unq.epers.bichomon.backend.jdbc.service.bicho.BichoServiceImp;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.condicion.CondicionBasadaEnEnergia;
import ar.edu.unq.epers.bichomon.backend.model.entrenador.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.entrenador.Nivel;
import ar.edu.unq.epers.bichomon.backend.model.entrenador.ProveedorDeNiveles;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import ar.edu.unq.epers.bichomon.backend.model.especie.TipoBicho;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Dojo;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Guarderia;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Pueblo;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;

import java.util.List;

public class Factory {

    public Guarderia guarderia(String nombre) {
        return new Guarderia(nombre);
    }

    public ProveedorDeNiveles proveedorDeNiveles(List niveles) {
        return new ProveedorDeNiveles(niveles);
    }

    public Especie especieSinEvolucion(String nombre, TipoBicho tipoBicho) {
        return new Especie(nombre, tipoBicho);
    }

    public Especie especieConEvolucionYRaiz(String nombre, TipoBicho tipoBicho, Especie evolucionEspecie, Especie raizEspecie) {
        return new Especie(nombre, tipoBicho, evolucionEspecie, raizEspecie);
    }

    public Bicho bicho(Especie especie) {
        return new Bicho(especie);
    }

    public Entrenador entrenador(String nombre, Ubicacion ubicacion, ProveedorDeNiveles proveedor) {
        return new Entrenador(nombre, ubicacion, proveedor);
    }

    public HibernateBichoDao hibernateBichoDAO() {
        return new HibernateBichoDao();
    }

    public HibernateEntrenadorDao hibernateEntrenadorDAO() {
        return new HibernateEntrenadorDao();
    }

    public Pueblo pueblo() {
        return new Pueblo("Pueblo");
    }

    public Dojo dojo() {
        return new Dojo("Dojo");
    }

    public Nivel nivel(int numeroNivel, int experienciaMinima, int experienciaMaxima) {
        return new Nivel(numeroNivel, experienciaMinima, experienciaMaxima);
    }

    public CondicionBasadaEnEnergia condicionBasadaEnEnergia(int cantidadDeEnergia) {
        return new CondicionBasadaEnEnergia(cantidadDeEnergia);
    }

    public BichoServiceImp bichoServiceImpl() {
        return new BichoServiceImp();
    }
}
