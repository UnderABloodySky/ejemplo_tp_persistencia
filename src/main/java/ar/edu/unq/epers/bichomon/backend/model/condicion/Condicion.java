package ar.edu.unq.epers.bichomon.backend.model.condicion;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Condicion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public abstract Boolean evaluar(Bicho bicho);

    public int getId() {
        return id;
    }
}
