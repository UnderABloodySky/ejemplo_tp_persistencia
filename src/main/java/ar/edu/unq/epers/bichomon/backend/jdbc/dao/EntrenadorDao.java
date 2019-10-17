package ar.edu.unq.epers.bichomon.backend.jdbc.dao;

import ar.edu.unq.epers.bichomon.backend.model.entrenador.Entrenador;

import java.util.List;

public interface EntrenadorDao {

    Entrenador recuperar(String nombre_entrenador);
    void guardar(Entrenador entrenador);
    List<Entrenador> recuperarCampeones();
    List<Entrenador> recuperarLideres();
}