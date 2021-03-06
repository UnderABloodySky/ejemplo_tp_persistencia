package ar.edu.unq.epers.bichomon.backend.model.ubicacion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.duelo.ResultadoCombate;
import ar.edu.unq.epers.bichomon.backend.model.entrenador.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Entity
public class Pueblo extends Ubicacion {
    @OneToMany(cascade = CascadeType.ALL)
    public List<ProbabilidadEspecie> especiesYProbabilidades = new ArrayList<>();
    public static String ERROR_EXCESO_ESPECIES = "No se puede agregar esa especie al pueblo";

    public Pueblo(String nombre) {
        super(nombre);
    }

    public Pueblo() {
    }

    @Override
    public void recibirAbandonado(Entrenador entrenador, Bicho bichoAAbandonar) {
        throw new ErrorAbandonoImposible();
    }

    @Override
    public Bicho bichomonPara(Entrenador entrenador) {

        Random randomGenerator = new Random();
        ProbabilidadEspecie probabilidadEspecieEncontrada = especiesYProbabilidades.stream()
                .filter(probabilidadEspecie -> probabilidadEspecie.probabilidad > randomGenerator.nextInt(101))
                .findFirst().get();

        return new Bicho(probabilidadEspecieEncontrada.especie);
    }

    public List<Especie> especiesPosibles() {
        return new ArrayList<>(especiesYProbabilidades.stream().map(probabilidadEspecie -> probabilidadEspecie.especie).collect(Collectors.toList()));
    }

    public void agregarEspecie(Especie especie, Integer probabilidadDeAparecer) {
        chequearProbabilidadesTotales(probabilidadDeAparecer);
        especiesYProbabilidades.add(new ProbabilidadEspecie(especie, probabilidadDeAparecer));
    }

    @Override
    public ResultadoCombate comenzarDuelo(Bicho bicho) {
        throw new UbicacionIncorrectaException();
    }

    private void chequearProbabilidadesTotales(Integer probabilidadDeAparecer) {
        if (probabilidadTotalesDeAparicion() + probabilidadDeAparecer > 100) {
            throw new ProbabilidadEnPuebloError(Pueblo.ERROR_EXCESO_ESPECIES);
        }
    }

    private Integer probabilidadTotalesDeAparicion() {
        return especiesYProbabilidades.stream().mapToInt(probabilidadEspecie -> probabilidadEspecie.probabilidad).sum();
    }
}
