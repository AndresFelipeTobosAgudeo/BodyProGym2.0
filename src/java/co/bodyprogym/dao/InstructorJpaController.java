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
import co.bodyprogym.entity.Clase;
import co.bodyprogym.entity.Instructor;
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
public class InstructorJpaController implements Serializable {

    public InstructorJpaController() {
        this.emf = Persistence.createEntityManagerFactory("BodyProGymPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Instructor instructor) throws PreexistingEntityException, Exception {
        if (instructor.getClaseCollection() == null) {
            instructor.setClaseCollection(new ArrayList<Clase>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Clase> attachedClaseCollection = new ArrayList<Clase>();
            for (Clase claseCollectionClaseToAttach : instructor.getClaseCollection()) {
                claseCollectionClaseToAttach = em.getReference(claseCollectionClaseToAttach.getClass(), claseCollectionClaseToAttach.getIdentificador());
                attachedClaseCollection.add(claseCollectionClaseToAttach);
            }
            instructor.setClaseCollection(attachedClaseCollection);
            em.persist(instructor);
            for (Clase claseCollectionClase : instructor.getClaseCollection()) {
                Instructor oldInsCedulaOfClaseCollectionClase = claseCollectionClase.getInsCedula();
                claseCollectionClase.setInsCedula(instructor);
                claseCollectionClase = em.merge(claseCollectionClase);
                if (oldInsCedulaOfClaseCollectionClase != null) {
                    oldInsCedulaOfClaseCollectionClase.getClaseCollection().remove(claseCollectionClase);
                    oldInsCedulaOfClaseCollectionClase = em.merge(oldInsCedulaOfClaseCollectionClase);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findInstructor(instructor.getCedula()) != null) {
                throw new PreexistingEntityException("Instructor " + instructor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Instructor instructor) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Instructor persistentInstructor = em.find(Instructor.class, instructor.getCedula());
            Collection<Clase> claseCollectionOld = persistentInstructor.getClaseCollection();
            Collection<Clase> claseCollectionNew = instructor.getClaseCollection();
            Collection<Clase> attachedClaseCollectionNew = new ArrayList<Clase>();
            for (Clase claseCollectionNewClaseToAttach : claseCollectionNew) {
                claseCollectionNewClaseToAttach = em.getReference(claseCollectionNewClaseToAttach.getClass(), claseCollectionNewClaseToAttach.getIdentificador());
                attachedClaseCollectionNew.add(claseCollectionNewClaseToAttach);
            }
            claseCollectionNew = attachedClaseCollectionNew;
            instructor.setClaseCollection(claseCollectionNew);
            instructor = em.merge(instructor);
            for (Clase claseCollectionOldClase : claseCollectionOld) {
                if (!claseCollectionNew.contains(claseCollectionOldClase)) {
                    claseCollectionOldClase.setInsCedula(null);
                    claseCollectionOldClase = em.merge(claseCollectionOldClase);
                }
            }
            for (Clase claseCollectionNewClase : claseCollectionNew) {
                if (!claseCollectionOld.contains(claseCollectionNewClase)) {
                    Instructor oldInsCedulaOfClaseCollectionNewClase = claseCollectionNewClase.getInsCedula();
                    claseCollectionNewClase.setInsCedula(instructor);
                    claseCollectionNewClase = em.merge(claseCollectionNewClase);
                    if (oldInsCedulaOfClaseCollectionNewClase != null && !oldInsCedulaOfClaseCollectionNewClase.equals(instructor)) {
                        oldInsCedulaOfClaseCollectionNewClase.getClaseCollection().remove(claseCollectionNewClase);
                        oldInsCedulaOfClaseCollectionNewClase = em.merge(oldInsCedulaOfClaseCollectionNewClase);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = instructor.getCedula();
                if (findInstructor(id) == null) {
                    throw new NonexistentEntityException("The instructor with id " + id + " no longer exists.");
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
            Instructor instructor;
            try {
                instructor = em.getReference(Instructor.class, id);
                instructor.getCedula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The instructor with id " + id + " no longer exists.", enfe);
            }
            Collection<Clase> claseCollection = instructor.getClaseCollection();
            for (Clase claseCollectionClase : claseCollection) {
                claseCollectionClase.setInsCedula(null);
                claseCollectionClase = em.merge(claseCollectionClase);
            }
            em.remove(instructor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Instructor> findInstructorEntities() {
        return findInstructorEntities(true, -1, -1);
    }

    public List<Instructor> findInstructorEntities(int maxResults, int firstResult) {
        return findInstructorEntities(false, maxResults, firstResult);
    }

    private List<Instructor> findInstructorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Instructor.class));
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

    public Instructor findInstructor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Instructor.class, id);
        } finally {
            em.close();
        }
    }

    public int getInstructorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Instructor> rt = cq.from(Instructor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
