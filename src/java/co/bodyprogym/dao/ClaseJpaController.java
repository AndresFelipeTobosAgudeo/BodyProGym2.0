/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.bodyprogym.dao;

import co.bodyprogym.dao.exceptions.NonexistentEntityException;
import co.bodyprogym.dao.exceptions.PreexistingEntityException;
import co.bodyprogym.entity.Clase;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import co.bodyprogym.entity.Instructor;
import co.bodyprogym.entity.Deportista;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author usuario
 */
public class ClaseJpaController implements Serializable {

    public ClaseJpaController() {
        this.emf = Persistence.createEntityManagerFactory("BodyProGymPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Clase clase) throws PreexistingEntityException, Exception {
        if (clase.getDeportistaCollection() == null) {
            clase.setDeportistaCollection(new ArrayList<Deportista>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Instructor insCedula = clase.getInsCedula();
            if (insCedula != null) {
                insCedula = em.getReference(insCedula.getClass(), insCedula.getCedula());
                clase.setInsCedula(insCedula);
            }
            Collection<Deportista> attachedDeportistaCollection = new ArrayList<Deportista>();
            for (Deportista deportistaCollectionDeportistaToAttach : clase.getDeportistaCollection()) {
                deportistaCollectionDeportistaToAttach = em.getReference(deportistaCollectionDeportistaToAttach.getClass(), deportistaCollectionDeportistaToAttach.getCedula());
                attachedDeportistaCollection.add(deportistaCollectionDeportistaToAttach);
            }
            clase.setDeportistaCollection(attachedDeportistaCollection);
            em.persist(clase);
            if (insCedula != null) {
                insCedula.getClaseCollection().add(clase);
                insCedula = em.merge(insCedula);
            }
            for (Deportista deportistaCollectionDeportista : clase.getDeportistaCollection()) {
                deportistaCollectionDeportista.getClaseCollection().add(clase);
                deportistaCollectionDeportista = em.merge(deportistaCollectionDeportista);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findClase(clase.getIdentificador()) != null) {
                throw new PreexistingEntityException("Clase " + clase + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Clase clase) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Clase persistentClase = em.find(Clase.class, clase.getIdentificador());
            Instructor insCedulaOld = persistentClase.getInsCedula();
            Instructor insCedulaNew = clase.getInsCedula();
            Collection<Deportista> deportistaCollectionOld = persistentClase.getDeportistaCollection();
            Collection<Deportista> deportistaCollectionNew = clase.getDeportistaCollection();
            if (insCedulaNew != null) {
                insCedulaNew = em.getReference(insCedulaNew.getClass(), insCedulaNew.getCedula());
                clase.setInsCedula(insCedulaNew);
            }
            Collection<Deportista> attachedDeportistaCollectionNew = new ArrayList<Deportista>();
            for (Deportista deportistaCollectionNewDeportistaToAttach : deportistaCollectionNew) {
                deportistaCollectionNewDeportistaToAttach = em.getReference(deportistaCollectionNewDeportistaToAttach.getClass(), deportistaCollectionNewDeportistaToAttach.getCedula());
                attachedDeportistaCollectionNew.add(deportistaCollectionNewDeportistaToAttach);
            }
            deportistaCollectionNew = attachedDeportistaCollectionNew;
            clase.setDeportistaCollection(deportistaCollectionNew);
            clase = em.merge(clase);
            if (insCedulaOld != null && !insCedulaOld.equals(insCedulaNew)) {
                insCedulaOld.getClaseCollection().remove(clase);
                insCedulaOld = em.merge(insCedulaOld);
            }
            if (insCedulaNew != null && !insCedulaNew.equals(insCedulaOld)) {
                insCedulaNew.getClaseCollection().add(clase);
                insCedulaNew = em.merge(insCedulaNew);
            }
            for (Deportista deportistaCollectionOldDeportista : deportistaCollectionOld) {
                if (!deportistaCollectionNew.contains(deportistaCollectionOldDeportista)) {
                    deportistaCollectionOldDeportista.getClaseCollection().remove(clase);
                    deportistaCollectionOldDeportista = em.merge(deportistaCollectionOldDeportista);
                }
            }
            for (Deportista deportistaCollectionNewDeportista : deportistaCollectionNew) {
                if (!deportistaCollectionOld.contains(deportistaCollectionNewDeportista)) {
                    deportistaCollectionNewDeportista.getClaseCollection().add(clase);
                    deportistaCollectionNewDeportista = em.merge(deportistaCollectionNewDeportista);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = clase.getIdentificador();
                if (findClase(id) == null) {
                    throw new NonexistentEntityException("The clase with id " + id + " no longer exists.");
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
            Clase clase;
            try {
                clase = em.getReference(Clase.class, id);
                clase.getIdentificador();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clase with id " + id + " no longer exists.", enfe);
            }
            Instructor insCedula = clase.getInsCedula();
            if (insCedula != null) {
                insCedula.getClaseCollection().remove(clase);
                insCedula = em.merge(insCedula);
            }
            Collection<Deportista> deportistaCollection = clase.getDeportistaCollection();
            for (Deportista deportistaCollectionDeportista : deportistaCollection) {
                deportistaCollectionDeportista.getClaseCollection().remove(clase);
                deportistaCollectionDeportista = em.merge(deportistaCollectionDeportista);
            }
            em.remove(clase);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Clase> findClaseEntities() {
        return findClaseEntities(true, -1, -1);
    }

    public List<Clase> findClaseEntities(int maxResults, int firstResult) {
        return findClaseEntities(false, maxResults, firstResult);
    }

    private List<Clase> findClaseEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Clase.class));
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

    public Clase findClase(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Clase.class, id);
        } finally {
            em.close();
        }
    }

    public int getClaseCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Clase> rt = cq.from(Clase.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
