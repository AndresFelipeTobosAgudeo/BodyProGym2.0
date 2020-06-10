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
import co.bodyprogym.entity.TipoPlan;
import co.bodyprogym.entity.Deportista;
import co.bodyprogym.entity.PlanDeportista;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author usuario
 */
public class PlanDeportistaJpaController implements Serializable {

    public PlanDeportistaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PlanDeportista planDeportista) throws PreexistingEntityException, Exception {
        if (planDeportista.getDeportistaCollection() == null) {
            planDeportista.setDeportistaCollection(new ArrayList<Deportista>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoPlan tipoPlan = planDeportista.getTipoPlan();
            if (tipoPlan != null) {
                tipoPlan = em.getReference(tipoPlan.getClass(), tipoPlan.getIdentificador());
                planDeportista.setTipoPlan(tipoPlan);
            }
            Collection<Deportista> attachedDeportistaCollection = new ArrayList<Deportista>();
            for (Deportista deportistaCollectionDeportistaToAttach : planDeportista.getDeportistaCollection()) {
                deportistaCollectionDeportistaToAttach = em.getReference(deportistaCollectionDeportistaToAttach.getClass(), deportistaCollectionDeportistaToAttach.getCedula());
                attachedDeportistaCollection.add(deportistaCollectionDeportistaToAttach);
            }
            planDeportista.setDeportistaCollection(attachedDeportistaCollection);
            em.persist(planDeportista);
            if (tipoPlan != null) {
                tipoPlan.getPlanDeportistaCollection().add(planDeportista);
                tipoPlan = em.merge(tipoPlan);
            }
            for (Deportista deportistaCollectionDeportista : planDeportista.getDeportistaCollection()) {
                PlanDeportista oldPlanOfDeportistaCollectionDeportista = deportistaCollectionDeportista.getPlan();
                deportistaCollectionDeportista.setPlan(planDeportista);
                deportistaCollectionDeportista = em.merge(deportistaCollectionDeportista);
                if (oldPlanOfDeportistaCollectionDeportista != null) {
                    oldPlanOfDeportistaCollectionDeportista.getDeportistaCollection().remove(deportistaCollectionDeportista);
                    oldPlanOfDeportistaCollectionDeportista = em.merge(oldPlanOfDeportistaCollectionDeportista);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPlanDeportista(planDeportista.getIdentificador()) != null) {
                throw new PreexistingEntityException("PlanDeportista " + planDeportista + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PlanDeportista planDeportista) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PlanDeportista persistentPlanDeportista = em.find(PlanDeportista.class, planDeportista.getIdentificador());
            TipoPlan tipoPlanOld = persistentPlanDeportista.getTipoPlan();
            TipoPlan tipoPlanNew = planDeportista.getTipoPlan();
            Collection<Deportista> deportistaCollectionOld = persistentPlanDeportista.getDeportistaCollection();
            Collection<Deportista> deportistaCollectionNew = planDeportista.getDeportistaCollection();
            if (tipoPlanNew != null) {
                tipoPlanNew = em.getReference(tipoPlanNew.getClass(), tipoPlanNew.getIdentificador());
                planDeportista.setTipoPlan(tipoPlanNew);
            }
            Collection<Deportista> attachedDeportistaCollectionNew = new ArrayList<Deportista>();
            for (Deportista deportistaCollectionNewDeportistaToAttach : deportistaCollectionNew) {
                deportistaCollectionNewDeportistaToAttach = em.getReference(deportistaCollectionNewDeportistaToAttach.getClass(), deportistaCollectionNewDeportistaToAttach.getCedula());
                attachedDeportistaCollectionNew.add(deportistaCollectionNewDeportistaToAttach);
            }
            deportistaCollectionNew = attachedDeportistaCollectionNew;
            planDeportista.setDeportistaCollection(deportistaCollectionNew);
            planDeportista = em.merge(planDeportista);
            if (tipoPlanOld != null && !tipoPlanOld.equals(tipoPlanNew)) {
                tipoPlanOld.getPlanDeportistaCollection().remove(planDeportista);
                tipoPlanOld = em.merge(tipoPlanOld);
            }
            if (tipoPlanNew != null && !tipoPlanNew.equals(tipoPlanOld)) {
                tipoPlanNew.getPlanDeportistaCollection().add(planDeportista);
                tipoPlanNew = em.merge(tipoPlanNew);
            }
            for (Deportista deportistaCollectionOldDeportista : deportistaCollectionOld) {
                if (!deportistaCollectionNew.contains(deportistaCollectionOldDeportista)) {
                    deportistaCollectionOldDeportista.setPlan(null);
                    deportistaCollectionOldDeportista = em.merge(deportistaCollectionOldDeportista);
                }
            }
            for (Deportista deportistaCollectionNewDeportista : deportistaCollectionNew) {
                if (!deportistaCollectionOld.contains(deportistaCollectionNewDeportista)) {
                    PlanDeportista oldPlanOfDeportistaCollectionNewDeportista = deportistaCollectionNewDeportista.getPlan();
                    deportistaCollectionNewDeportista.setPlan(planDeportista);
                    deportistaCollectionNewDeportista = em.merge(deportistaCollectionNewDeportista);
                    if (oldPlanOfDeportistaCollectionNewDeportista != null && !oldPlanOfDeportistaCollectionNewDeportista.equals(planDeportista)) {
                        oldPlanOfDeportistaCollectionNewDeportista.getDeportistaCollection().remove(deportistaCollectionNewDeportista);
                        oldPlanOfDeportistaCollectionNewDeportista = em.merge(oldPlanOfDeportistaCollectionNewDeportista);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = planDeportista.getIdentificador();
                if (findPlanDeportista(id) == null) {
                    throw new NonexistentEntityException("The planDeportista with id " + id + " no longer exists.");
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
            PlanDeportista planDeportista;
            try {
                planDeportista = em.getReference(PlanDeportista.class, id);
                planDeportista.getIdentificador();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The planDeportista with id " + id + " no longer exists.", enfe);
            }
            TipoPlan tipoPlan = planDeportista.getTipoPlan();
            if (tipoPlan != null) {
                tipoPlan.getPlanDeportistaCollection().remove(planDeportista);
                tipoPlan = em.merge(tipoPlan);
            }
            Collection<Deportista> deportistaCollection = planDeportista.getDeportistaCollection();
            for (Deportista deportistaCollectionDeportista : deportistaCollection) {
                deportistaCollectionDeportista.setPlan(null);
                deportistaCollectionDeportista = em.merge(deportistaCollectionDeportista);
            }
            em.remove(planDeportista);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PlanDeportista> findPlanDeportistaEntities() {
        return findPlanDeportistaEntities(true, -1, -1);
    }

    public List<PlanDeportista> findPlanDeportistaEntities(int maxResults, int firstResult) {
        return findPlanDeportistaEntities(false, maxResults, firstResult);
    }

    private List<PlanDeportista> findPlanDeportistaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PlanDeportista.class));
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

    public PlanDeportista findPlanDeportista(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PlanDeportista.class, id);
        } finally {
            em.close();
        }
    }

    public int getPlanDeportistaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PlanDeportista> rt = cq.from(PlanDeportista.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
