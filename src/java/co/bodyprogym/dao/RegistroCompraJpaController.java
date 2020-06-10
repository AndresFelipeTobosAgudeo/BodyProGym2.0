/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.bodyprogym.dao;

import co.bodyprogym.dao.exceptions.NonexistentEntityException;
import co.bodyprogym.dao.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.bodyprogym.entity.Deportista;
import co.bodyprogym.entity.RegistroCompra;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author usuario
 */
public class RegistroCompraJpaController implements Serializable {

    public RegistroCompraJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RegistroCompra registroCompra) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Deportista cedulaDeportista = registroCompra.getCedulaDeportista();
            if (cedulaDeportista != null) {
                cedulaDeportista = em.getReference(cedulaDeportista.getClass(), cedulaDeportista.getCedula());
                registroCompra.setCedulaDeportista(cedulaDeportista);
            }
            em.persist(registroCompra);
            if (cedulaDeportista != null) {
                cedulaDeportista.getRegistroCompraCollection().add(registroCompra);
                cedulaDeportista = em.merge(cedulaDeportista);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRegistroCompra(registroCompra.getIdentificador()) != null) {
                throw new PreexistingEntityException("RegistroCompra " + registroCompra + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RegistroCompra registroCompra) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RegistroCompra persistentRegistroCompra = em.find(RegistroCompra.class, registroCompra.getIdentificador());
            Deportista cedulaDeportistaOld = persistentRegistroCompra.getCedulaDeportista();
            Deportista cedulaDeportistaNew = registroCompra.getCedulaDeportista();
            if (cedulaDeportistaNew != null) {
                cedulaDeportistaNew = em.getReference(cedulaDeportistaNew.getClass(), cedulaDeportistaNew.getCedula());
                registroCompra.setCedulaDeportista(cedulaDeportistaNew);
            }
            registroCompra = em.merge(registroCompra);
            if (cedulaDeportistaOld != null && !cedulaDeportistaOld.equals(cedulaDeportistaNew)) {
                cedulaDeportistaOld.getRegistroCompraCollection().remove(registroCompra);
                cedulaDeportistaOld = em.merge(cedulaDeportistaOld);
            }
            if (cedulaDeportistaNew != null && !cedulaDeportistaNew.equals(cedulaDeportistaOld)) {
                cedulaDeportistaNew.getRegistroCompraCollection().add(registroCompra);
                cedulaDeportistaNew = em.merge(cedulaDeportistaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = registroCompra.getIdentificador();
                if (findRegistroCompra(id) == null) {
                    throw new NonexistentEntityException("The registroCompra with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RegistroCompra registroCompra;
            try {
                registroCompra = em.getReference(RegistroCompra.class, id);
                registroCompra.getIdentificador();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The registroCompra with id " + id + " no longer exists.", enfe);
            }
            Deportista cedulaDeportista = registroCompra.getCedulaDeportista();
            if (cedulaDeportista != null) {
                cedulaDeportista.getRegistroCompraCollection().remove(registroCompra);
                cedulaDeportista = em.merge(cedulaDeportista);
            }
            em.remove(registroCompra);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RegistroCompra> findRegistroCompraEntities() {
        return findRegistroCompraEntities(true, -1, -1);
    }

    public List<RegistroCompra> findRegistroCompraEntities(int maxResults, int firstResult) {
        return findRegistroCompraEntities(false, maxResults, firstResult);
    }

    private List<RegistroCompra> findRegistroCompraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RegistroCompra.class));
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

    public RegistroCompra findRegistroCompra(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RegistroCompra.class, id);
        } finally {
            em.close();
        }
    }

    public int getRegistroCompraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RegistroCompra> rt = cq.from(RegistroCompra.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
