package ar.edu.unq.epers.bichomon.backend.model.especie;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.condicion.Condicion;

import javax.persistence.*;

/**
 * Representa una {@link Especie} de bicho.
 * 
 * @author Charly Backend
 */
@Entity
public class Especie {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column
	private String nombre;
	@Column
	private int altura;
	@Column
	private int peso;

	@Column
	private TipoBicho tipo;
	@Column
	private Integer energiaInicial;
	@Column
	private String urlFoto;
	@Column
	private Integer cantidadBichos;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Especie evolucionDeEspecie;

    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private Condicion condicionDeEvolucion;

	public Especie(String nombre, TipoBicho tipo) {
		this.nombre = nombre;
		this.tipo = tipo;
	}

	public Especie(String nombre, TipoBicho tipoBicho, Especie evolucionDeEspecie) {
		this.nombre = nombre;
		this.tipo = tipoBicho;
		this.evolucionDeEspecie = evolucionDeEspecie;
	}

	public Especie(Integer id, String nombre, TipoBicho tipo) {
		this.id = id;
		this.nombre = nombre;
		this.tipo = tipo;
	}

	public Especie() {
	}

    public void setCondicionDeEvolucion(Condicion condicion) {
        this.condicionDeEvolucion = condicion;
    }

    /**
	 * @return el nombre de la especie (por ejemplo: Perromon)
	 */
	public String getNombre() {
		return this.nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	/**
	 * @return la altura de todos los bichos de esta especie
	 */
	public int getAltura() {
		return this.altura;
	}
	public void setAltura(int altura) {
		this.altura = altura;
	}
	
	/**
	 * @return el peso de todos los bichos de esta especie
	 */
	public int getPeso() {
		return this.peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}
	
	/**
	 * @return una url que apunta al un recurso imagen el cual será
	 * utilizado para mostrar un thumbnail del bichomon por el frontend.
	 */
	public String getUrlFoto() {
		return this.urlFoto;
	}
	public void setUrlFoto(String urlFoto) {
		this.urlFoto = urlFoto;
	}
	
	/**
	 * @return la cantidad de energia de poder iniciales para los bichos
	 * de esta especie.
	 */
	public int getEnergiaInicial() {
		return this.energiaInicial;
	}
	public void setEnergiaIncial(int energiaInicial) {
		this.energiaInicial = energiaInicial;
	}

	/**
	 * @return el tipo de todos los bichos de esta especie
	 */
	public TipoBicho getTipo() {
		return this.tipo;
	}
	public void setTipo(TipoBicho tipo) {
		this.tipo = tipo;
	}
	
	/**
	 * @return la cantidad de bichos que se han creado para esta
	 * especie.
	 */
	public int getCantidadBichos() {
		return this.cantidadBichos;
	}
	public void setCantidadBichos(int i) {
		this.cantidadBichos = i;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public Bicho crearBicho(String nombreBicho){
		this.cantidadBichos++;
		return new Bicho(this, nombreBicho);
	}

    public Especie getEvolucionDeEspecie() {
        return evolucionDeEspecie;
    }

    public Boolean evaluarEvolucion(Bicho bicho) {
        return condicionDeEvolucion.evaluar(bicho);
    }
}