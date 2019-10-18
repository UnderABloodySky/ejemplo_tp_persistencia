package ar.edu.unq.epers.bichomon.backend.jdbc.dao.impl;

import ar.edu.unq.epers.bichomon.backend.jdbc.dao.UbicacionDao;
import ar.edu.unq.epers.bichomon.backend.jdbc.service.runner.TransactionRunner;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.historialDeCampeones.FichaDeCampeon;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Dojo;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Guarderia;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;


public class HibernateUbicacionDao extends HibernateDAO<Ubicacion> implements UbicacionDao {

    public HibernateUbicacionDao() {
        super(Ubicacion.class);
    }

    @Override
    public Ubicacion recuperar(String nombre) {
        Session session = TransactionRunner.getCurrentSession();
        return session.get(Ubicacion.class, nombre);
    }

    @Override
    public Dojo recuperarDojo(String dojo) {
        Session session = TransactionRunner.getCurrentSession();
        return session.get(Dojo.class, dojo);
    }

    @Override
    public Integer recuperarIdCampeonHistoricoEnDojo(String dojo) {
        Session session = TransactionRunner.getCurrentSession();
        String hql = "select f.bichoCampeon from FichaDeCampeon f where f.dojo.nombre = :nombre " +
                "order by case when f.fechaFin is not null then days(f.fechaFin) - days(f.fechaInicio) " +
                "else days(current_date) - days(f.fechaInicio) end";

        Query<Bicho> query = session.createQuery(hql, Bicho.class);
        query.setParameter("nombre", dojo);

        return query.getSingleResult().getId();





       // Session session = TransactionRunner.getCurrentSession();
       // String hql = "select d.fichas from Dojo d where d.nombre = :nombre";
//
       // Query<FichaDeCampeon> query = session.createQuery(hql,FichaDeCampeon.class);
       // query.setParameter("nombre", dojo);
//
       // List<FichaDeCampeon> campeones = query.getResultList();
       // FichaDeCampeon fichaCampeon = new FichaDeCampeon(null, null, LocalDate.now(), null);
       // for (FichaDeCampeon ficha : campeones) {
       //     if (ficha.duracionComoCampeon() > fichaCampeon.duracionComoCampeon()) {
       //         fichaCampeon = ficha;
       //     }
       // }
       // return fichaCampeon.getBichoCampeon().getId();





    }
    @Override
    public Guarderia recuperarGuarderia(String nombre_guarderia) {
        Session session = TransactionRunner.getCurrentSession();
        return session.get(Guarderia.class, nombre_guarderia);
    }


}
