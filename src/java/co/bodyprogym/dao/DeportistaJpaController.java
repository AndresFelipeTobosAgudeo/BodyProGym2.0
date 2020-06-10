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
import co.bodyprogym.entity.Usuario;
import co.bodyprogym.entity.Clase;
import co.bodyprogym.entity.Deportista;
import java.util.ArrayList;
import java.util.Collection;
import co.bodyprogym.entity.RegistroCompra;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author usuario
 */
public class DeportistaJpaController implements Serializable {

    public DeportistaJpaController() {
        this.emf = Persistence.createEntityManagerFactory("BodyProGymPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Deportista deportista) throws PreexistingEntityException, Exception {
        if (deportista.getClaseCollection() == null) {
            deportista.setClaseCollection(new ArrayList<Clase>());
        }
        if (deportista.getRegistroCompraCollection() == null) {
            deportista.setRegistroCompraCollection(new ArrayList<RegistroCompra>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PlanDeportista plan = deportista.getPlan();
            if (plan != null) {
                plan = em.getReference(plan.getClass(), plan.getIdentificador());
                deportista.setPlan(plan);
            }
            Usuario usuario = deportista.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getCedulamod());
                deportista.setUsuario(usuario);
            }
            Collection<Clase> attachedClaseCollection = new ArrayList<Clase>();
            for (Clase claseCollectionClaseToAttach : deportista.getClaseCollection()) {
                claseCollectionClaseToAttach = em.getReference(claseCollectionClaseToAttach.getClass(), claseCollectionClaseToAttach.getIdentificador());
                attachedClaseCollection.add(claseCollectionClaseToAttach);
            }
            deportista.setClaseCollection(attachedClaseCollection);
            Collection<RegistroCompra> attachedRegistroCompraCollection = new ArrayList<RegistroCompra>();
            for (RegistroCompra registroCompraCollectionRegistroCompraToAttach : deportista.getRegistroCompraCollection()) {
                registroCompraCollectionRegistroCompraToAttach = em.getReference(registroCompraCollectionRegistroCompraToAttach.getClass(), registroCompraCollectionRegistroCompraToAttach.getIdentificador());
                attachedRegistroCompraCollection.add(registroCompraCollectionRegistroCompraToAttach);
            }
            deportista.setRegistroCompraCollection(attachedRegistroCompraCollection);
            em.persist(deportista);
            if (plan != null) {
                plan.getDeportistaCollection().add(deportista);
                plan = em.merge(plan);
            }
            if (usuario != null) {
                usuario.getDeportistaCollection().add(deportista);
                usuario = em.merge(usuario);
            }
            for (Clase claseCollectionClase : deportista.getClaseCollection()) {
                claseCollectionClase.getDeportistaCollection().add(deportista);
                claseCollectionClase = em.merge(claseCollectionClase);
            }
            for (RegistroCompra registroCompraCollectionRegistroCompra : deportista.getRegistroCompraCollection()) {
                Deportista oldCedulaDeportistaOfRegistroCompraCollectionRegistroCompra = registroCompraCollectionRegistroCompra.getCedulaDeportista();
                registroCompraCollectionRegistroCompra.setCedulaDeportista(deportista);
                registroCompraCollectionRegistroCompra = em.merge(registroCompraCollectionRegistroCompra);
                if (oldCedulaDeportistaOfRegistroCompraCollectionRegistroCompra != null) {
                    oldCedulaDeportistaOfRegistroCompraCollectionRegistroCompra.getRegistroCompraCollection().remove(registroCompraCollectionRegistroCompra);
                    oldCedulaDeportistaOfRegistroCompraCollectionRegistroCompra = em.merge(oldCedulaDeportistaOfRegistroCompraCollectionRegistroCompra);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDeportista(deportista.getCedula()) != null) {
                throw new PreexistingEntityException("Deportista " + deportista + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Deportista deportista) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Deportista persistentDeportista = em.find(Deportista.class, deportista.getCedula());
            PlanDeportista planOld = persistentDeportista.getPlan();
            PlanDeportista planNew = deportista.getPlan();
            Usuario usuarioOld = persistentDeportista.getUsuario();
            Usuario usuarioNew = deportista.getUsuario();
            Collection<Clase> claseCollectionOld = persistentDeportista.getClaseCollection();
            Collection<Clase> claseCollectionNew = deportista.getClaseCollection();
            Collection<RegistroCompra> registroCompraCollectionOld = persistentDeportista.getRegistroCompraCollection();
            Collection<RegistroCompra> registroCompraCollectionNew = deportista.getRegistroCompraCollection();
            if (planNew != null) {
                planNew = em.getReference(planNew.getClass(), planNew.getIdentificador());
                deportista.setPlan(planNew);
            }
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getCedulamod());
                deportista.setUsuario(usuarioNew);
            }
            Collection<Clase> attachedClaseCollectionNew = new ArrayList<Clase>();
            for (Clase claseCollectionNewClaseToAttach : claseCollectionNew) {
                claseCollectionNewClaseToAttach = em.getReference(claseCollectionNewClaseToAttach.getClass(), claseCollectionNewClaseToAttach.getIdentificador());
                attachedClaseCollectionNew.add(claseCollectionNewClaseToAttach);
            }
            claseCollectionNew = attachedClaseCollectionNew;
            deportista.setClaseCollection(claseCollectionNew);
            Collection<RegistroCompra> attachedRegistroCompraCollectionNew = new ArrayList<RegistroCompra>();
            for (RegistroCompra registroCompraCollectionNewRegistroCompraToAttach : registroCompraCollectionNew) {
                registroCompraCollectionNewRegistroCompraToAttach = em.getReference(registroCompraCollectionNewRegistroCompraToAttach.getClass(), registroCompraCollectionNewRegistroCompraToAttach.getIdentificador());
                attachedRegistroCompraCollectionNew.add(registroCompraCollectionNewRegistroCompraToAttach);
            }
            registroCompraCollectionNew = attachedRegistroCompraCollectionNew;
            deportista.setRegistroCompraCollection(registroCompraCollectionNew);
            deportista = em.merge(deportista);
            if (planOld != null && !planOld.equals(planNew)) {
                planOld.getDeportistaCollection().remove(deportista);
                planOld = em.merge(planOld);
            }
            if (planNew != null && !planNew.equals(planOld)) {
                planNew.getDeportistaCollection().add(deportista);
                planNew = em.merge(planNew);
            }
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getDeportistaCollection().remove(deportista);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getDeportistaCollection().add(deportista);
                usuarioNew = em.merge(usuarioNew);
            }
            for (Clase claseCollectionOldClase : claseCollectionOld) {
                if (!claseCollectionNew.contains(claseCollectionOldClase)) {
                    claseCollectionOldClase.getDeportistaCollection().remove(deportista);
                    claseCollectionOldClase = em.merge(claseCollectionOldClase);
                }
            }
            for (Clase claseCollectionNewClase : claseCollectionNew) {
                if (!claseCollectionOld.contains(claseCollectionNewClase)) {
                    claseCollectionNewClase.getDeportistaCollection().add(deportista);
                    claseCollectionNewClase = em.merge(claseCollectionNewClase);
                }
            }
            for (RegistroCompra registroCompraCollectionOldRegistroCompra : registroCompraCollectionOld) {
                if (!registroCompraCollectionNew.contains(registroCompraCollectionOldRegistroCompra)) {
                    registroCompraCollectionOldRegistroCompra.setCedulaDeportista(null);
                    registroCompraCollectionOldRegistroCompra = em.merge(registroCompraCollectionOldRegistroCompra);
                }
            }
            for (RegistroCompra registroCompraCollectionNewRegistroCompra : registroCompraCollectionNew) {
                if (!registroCompraCollectionOld.contains(registroCompraCollectionNewRegistroCompra)) {
                    Deportista oldCedulaDeportistaOfRegistroCompraCollectionNewRegistroCompra = registroCompraCollectionNewRegistroCompra.getCedulaDeportista();
                    registroCompraCollectionNewRegistroCompra.setCedulaDeportista(deportista);
                    registroCompraCollectionNewRegistroCompra = em.merge(registroCompraCollectionNewRegistroCompra);
                    if (oldCedulaDeportistaOfRegistroCompraCollectionNewRegistroCompra != null && !oldCedulaDeportistaOfRegistroCompraCollectionNewRegistroCompra.equals(deportista)) {
                        oldCedulaDeportistaOfRegistroCompraCollectionNewRegistroCompra.getRegistroCompraCollection().remove(registroCompraCollectionNewRegistroCompra);
                        oldCedulaDeportistaOfRegistroCompraCollectionNewRegistroCompra = em.merge(oldCedulaDeportistaOfRegistroCompraCollectionNewRegistroCompra);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = deportista.getCedula();
                if (findDeportista(id) == null) {
                    throw new NonexistentEntityException("The deportista with id " + id + " no longer exists.");
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
            Deportista deportista;
            try {
                deportista = em.getReference(Deportista.class, id);
                deportista.getCedula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The deportista with id " + id + " no longer exists.", enfe);
            }
            PlanDeportista plan = deportista.getPlan();
            if (plan != null) {
                plan.getDeportistaCollection().remove(deportista);
                plan = em.merge(plan);
            }
            Usuario usuario = deportista.getUsuario();
            if (usuario != null) {
                usuario.getDeportistaCollection().remove(deportista);
                usuario = em.merge(usuario);
            }
            Collection<Clase> claseCollection = deportista.getClaseCollection();
            for (Clase claseCollectionClase : claseCollection) {
                claseCollectionClase.getDeportistaCollection().remove(deportista);
                claseCollectionClase = em.merge(claseCollectionClase);
            }
            Collection<RegistroCompra> registroCompraCollection = deportista.getRegistroCompraCollection();
            for (RegistroCompra registroCompraCollectionRegistroCompra : registroCompraCollection) {
                registroCompraCollectionRegistroCompra.setCedulaDeportista(null);
                registroCompraCollectionRegistroCompra = em.merge(registroCompraCollectionRegistroCompra);
            }
            em.remove(deportista);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Deportista> findDeportistaEntities() {
        return findDeportistaEntities(true, -1, -1);
    }

    public List<Deportista> findDeportistaEntities(int maxResults, int firstResult) {
        return findDeportistaEntities(false, maxResults, firstResult);
    }

    private List<Deportista> findDeportistaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Deportista.class));
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

    public Deportista findDeportista(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Deportista.class, id);
        } finally {
            em.close();
        }
    }

    public int getDeportistaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Deportista> rt = cq.from(Deportista.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
