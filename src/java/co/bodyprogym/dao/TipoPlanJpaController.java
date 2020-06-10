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
import co.bodyprogym.entity.PlanDeportista;
import co.bodyprogym.entity.TipoPlan;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author usuario
 */
public class TipoPlanJpaController implements Serializable {

    public TipoPlanJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoPlan tipoPlan) throws PreexistingEntityException, Exception {
        if (tipoPlan.getPlanDeportistaCollection() == null) {
            tipoPlan.setPlanDeportistaCollection(new ArrayList<PlanDeportista>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<PlanDeportista> attachedPlanDeportistaCollection = new ArrayList<PlanDeportista>();
            for (PlanDeportista planDeportistaCollectionPlanDeportistaToAttach : tipoPlan.getPlanDeportistaCollection()) {
                planDeportistaCollectionPlanDeportistaToAttach = em.getReference(planDeportistaCollectionPlanDeportistaToAttach.getClass(), planDeportistaCollectionPlanDeportistaToAttach.getIdentificador());
                attachedPlanDeportistaCollection.add(planDeportistaCollectionPlanDeportistaToAttach);
            }
            tipoPlan.setPlanDeportistaCollection(attachedPlanDeportistaCollection);
            em.persist(tipoPlan);
            for (PlanDeportista planDeportistaCollectionPlanDeportista : tipoPlan.getPlanDeportistaCollection()) {
                TipoPlan oldTipoPlanOfPlanDeportistaCollectionPlanDeportista = planDeportistaCollectionPlanDeportista.getTipoPlan();
                planDeportistaCollectionPlanDeportista.setTipoPlan(tipoPlan);
                planDeportistaCollectionPlanDeportista = em.merge(planDeportistaCollectionPlanDeportista);
                if (oldTipoPlanOfPlanDeportistaCollectionPlanDeportista != null) {
                    oldTipoPlanOfPlanDeportistaCollectionPlanDeportista.getPlanDeportistaCollection().remove(planDeportistaCollectionPlanDeportista);
                    oldTipoPlanOfPlanDeportistaCollectionPlanDeportista = em.merge(oldTipoPlanOfPlanDeportistaCollectionPlanDeportista);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTipoPlan(tipoPlan.getIdentificador()) != null) {
                throw new PreexistingEntityException("TipoPlan " + tipoPlan + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoPlan tipoPlan) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoPlan persistentTipoPlan = em.find(TipoPlan.class, tipoPlan.getIdentificador());
            Collection<PlanDeportista> planDeportistaCollectionOld = persistentTipoPlan.getPlanDeportistaCollection();
            Collection<PlanDeportista> planDeportistaCollectionNew = tipoPlan.getPlanDeportistaCollection();
            Collection<PlanDeportista> attachedPlanDeportistaCollectionNew = new ArrayList<PlanDeportista>();
            for (PlanDeportista planDeportistaCollectionNewPlanDeportistaToAttach : planDeportistaCollectionNew) {
                planDeportistaCollectionNewPlanDeportistaToAttach = em.getReference(planDeportistaCollectionNewPlanDeportistaToAttach.getClass(), planDeportistaCollectionNewPlanDeportistaToAttach.getIdentificador());
                attachedPlanDeportistaCollectionNew.add(planDeportistaCollectionNewPlanDeportistaToAttach);
            }
            planDeportistaCollectionNew = attachedPlanDeportistaCollectionNew;
            tipoPlan.setPlanDeportistaCollection(planDeportistaCollectionNew);
            tipoPlan = em.merge(tipoPlan);
            for (PlanDeportista planDeportistaCollectionOldPlanDeportista : planDeportistaCollectionOld) {
                if (!planDeportistaCollectionNew.contains(planDeportistaCollectionOldPlanDeportista)) {
                    planDeportistaCollectionOldPlanDeportista.setTipoPlan(null);
                    planDeportistaCollectionOldPlanDeportista = em.merge(planDeportistaCollectionOldPlanDeportista);
                }
            }
            for (PlanDeportista planDeportistaCollectionNewPlanDeportista : planDeportistaCollectionNew) {
                if (!planDeportistaCollectionOld.contains(planDeportistaCollectionNewPlanDeportista)) {
                    TipoPlan oldTipoPlanOfPlanDeportistaCollectionNewPlanDeportista = planDeportistaCollectionNewPlanDeportista.getTipoPlan();
                    planDeportistaCollectionNewPlanDeportista.setTipoPlan(tipoPlan);
                    planDeportistaCollectionNewPlanDeportista = em.merge(planDeportistaCollectionNewPlanDeportista);
                    if (oldTipoPlanOfPlanDeportistaCollectionNewPlanDeportista != null && !oldTipoPlanOfPlanDeportistaCollectionNewPlanDeportista.equals(tipoPlan)) {
                        oldTipoPlanOfPlanDeportistaCollectionNewPlanDeportista.getPlanDeportistaCollection().remove(planDeportistaCollectionNewPlanDeportista);
                        oldTipoPlanOfPlanDeportistaCollectionNewPlanDeportista = em.merge(oldTipoPlanOfPlanDeportistaCollectionNewPlanDeportista);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoPlan.getIdentificador();
                if (findTipoPlan(id) == null) {
                    throw new NonexistentEntityException("The tipoPlan with id " + id + " no longer exists.");
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
            TipoPlan tipoPlan;
            try {
                tipoPlan = em.getReference(TipoPlan.class, id);
                tipoPlan.getIdentificador();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoPlan with id " + id + " no longer exists.", enfe);
            }
            Collection<PlanDeportista> planDeportistaCollection = tipoPlan.getPlanDeportistaCollection();
            for (PlanDeportista planDeportistaCollectionPlanDeportista : planDeportistaCollection) {
                planDeportistaCollectionPlanDeportista.setTipoPlan(null);
                planDeportistaCollectionPlanDeportista = em.merge(planDeportistaCollectionPlanDeportista);
            }
            em.remove(tipoPlan);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoPlan> findTipoPlanEntities() {
        return findTipoPlanEntities(true, -1, -1);
    }

    public List<TipoPlan> findTipoPlanEntities(int maxResults, int firstResult) {
        return findTipoPlanEntities(false, maxResults, firstResult);
    }

    private List<TipoPlan> findTipoPlanEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoPlan.class));
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

    public TipoPlan findTipoPlan(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoPlan.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoPlanCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoPlan> rt = cq.from(TipoPlan.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
