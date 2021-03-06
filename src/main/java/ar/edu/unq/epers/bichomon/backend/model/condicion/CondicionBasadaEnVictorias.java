package ar.edu.unq.epers.bichomon.backend.model.condicion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class CondicionBasadaEnVictorias extends Condicion {
    @Column
    private Integer victorias;

    public CondicionBasadaEnVictorias(Integer victorias) {
        this.victorias = victorias;
    }

    public CondicionBasadaEnVictorias() {
    }

    @Override
    public Boolean evaluar(Bicho bicho) {
        return bicho.getVictorias() > victorias;
    }
}
