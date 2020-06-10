/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.bodyprogym.dao;

import co.bodyprogym.dao.exceptions.NonexistentEntityException;
import co.bodyprogym.dao.exceptions.PreexistingEntityException;
import co.bodyprogym.entity.Asistente;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author usuario
 */
public class AsistenteJpaController implements Serializable {

    public AsistenteJpaController() {
        this.emf = Persistence.createEntityManagerFactory("Externa2");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Asistente asistente) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(asistente);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAsistente(asistente.getCedulamod()) != null) {
                throw new PreexistingEntityException("Asistente " + asistente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Asistente asistente) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            asistente = em.merge(asistente);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = asistente.getCedulamod();
                if (findAsistente(id) == null) {
                    throw new NonexistentEntityException("The asistente with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Asistente asistente;
            try {
                asistente = em.getReference(Asistente.class, id);
                asistente.getCedulamod();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The asistente with id " + id + " no longer exists.", enfe);
            }
            em.remove(asistente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Asistente> findAsistenteEntities() {
        return findAsistenteEntities(true, -1, -1);
    }

    public List<Asistente> findAsistenteEntities(int maxResults, int firstResult) {
        return findAsistenteEntities(false, maxResults, firstResult);
    }

    private List<Asistente> findAsistenteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Asistente.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Asistente findAsistente(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Asistente.class, id);
        } finally {
            em.close();
        }
    }

    public int getAsistenteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Asistente> rt = cq.from(Asistente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
