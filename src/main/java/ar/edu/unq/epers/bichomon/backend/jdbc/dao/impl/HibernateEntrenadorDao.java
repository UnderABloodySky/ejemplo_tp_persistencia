package ar.edu.unq.epers.bichomon.backend.jdbc.dao.impl;

import ar.edu.unq.epers.bichomon.backend.jdbc.dao.EntrenadorDao;
import ar.edu.unq.epers.bichomon.backend.jdbc.dao.impl.HibernateDAO;
import ar.edu.unq.epers.bichomon.backend.jdbc.service.runner.TransactionRunner;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.entrenador.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;


public class HibernateEntrenadorDao extends HibernateDAO<Entrenador> implements EntrenadorDao {


    public HibernateEntrenadorDao() { super(Entrenador.class); }

    @Override
    public Entrenador recuperar(String nombre_entrenador) {
        Session session = TransactionRunner.getCurrentSession();
        return session.get(Entrenador.class, nombre_entrenador);
    }

    @Override
    public List<Bicho> recuperarEspeciesMasPopulares() {
        Session session = TransactionRunner.getCurrentSession();
        String hqlQuery = "select b.especie from Bicho b " +
                "group by b.especie order by count(b.especie.cantidadBichos) ASC";

        Query<Bicho> query = session.createQuery(hqlQuery);
        query.setMaxResults(2);
        return query.getResultList();
    }


}
