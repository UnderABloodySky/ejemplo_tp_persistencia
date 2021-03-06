package ar.edu.unq.epers.bichomon.backend.jdbc.dao.impl;

import ar.edu.unq.epers.bichomon.backend.jdbc.dao.EntrenadorDao;
import ar.edu.unq.epers.bichomon.backend.jdbc.service.runner.TransactionRunner;
import ar.edu.unq.epers.bichomon.backend.model.entrenador.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;


public class HibernateEntrenadorDao extends HibernateDAO<Entrenador> implements EntrenadorDao {


    public HibernateEntrenadorDao() {
        super(Entrenador.class);
    }

    @Override
    public Entrenador recuperar(String nombre_entrenador) {
        Session session = TransactionRunner.getCurrentSession();
        return session.get(Entrenador.class, nombre_entrenador);
    }

    @Override
    public List<Entrenador> recuperarTodosEnUbicacion(String ubicacion) {


        Session session = TransactionRunner.getCurrentSession();
        String hql = "select entrenador from Entrenador entrenador where ubicacionActual.nombre = :ubicacion ";
        Query<Entrenador> query = session.createQuery(hql);
        query.setParameter("ubicacion", ubicacion);

        return query.getResultList();
    }

    @Override
    public List<Entrenador> recuperarCampeones() {
        Session session = TransactionRunner.getCurrentSession();

        String hql = "select f.campeon from FichaDeCampeon f "
                + "where f.fechaFin is null "
                + "order by fechaInicio asc";

        Query<Entrenador> query = session.createQuery(hql,  Entrenador.class);
        query.setMaxResults(10);

        return query.getResultList();
    }

    @Override
    public void actualizarUbicacion(Entrenador entrenador, Ubicacion ubicacionRecuperada) {

        Session session = TransactionRunner.getCurrentSession();
        session.saveOrUpdate(entrenador);

    }

    @Override
    public List<Entrenador> recuperarLideres() {
        Session session = TransactionRunner.getCurrentSession();

        String hql = "select e from Entrenador e inner join e.bichos b " +
                "group by e order by sum(b.energia) desc";

        Query<Entrenador> query = session.createQuery(hql, Entrenador.class);
        query.setMaxResults(10);

        return query.getResultList();
    }
}
