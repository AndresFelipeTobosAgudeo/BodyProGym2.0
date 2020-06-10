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
import co.bodyprogym.entity.Usuario;
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
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController() {
        this.emf = Persistence.createEntityManagerFactory("BodyProGymPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws PreexistingEntityException, Exception {
        if (usuario.getDeportistaCollection() == null) {
            usuario.setDeportistaCollection(new ArrayList<Deportista>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Deportista> attachedDeportistaCollection = new ArrayList<Deportista>();
            for (Deportista deportistaCollectionDeportistaToAttach : usuario.getDeportistaCollection()) {
                deportistaCollectionDeportistaToAttach = em.getReference(deportistaCollectionDeportistaToAttach.getClass(), deportistaCollectionDeportistaToAttach.getCedula());
                attachedDeportistaCollection.add(deportistaCollectionDeportistaToAttach);
            }
            usuario.setDeportistaCollection(attachedDeportistaCollection);
            em.persist(usuario);
            for (Deportista deportistaCollectionDeportista : usuario.getDeportistaCollection()) {
                Usuario oldUsuarioOfDeportistaCollectionDeportista = deportistaCollectionDeportista.getUsuario();
                deportistaCollectionDeportista.setUsuario(usuario);
                deportistaCollectionDeportista = em.merge(deportistaCollectionDeportista);
                if (oldUsuarioOfDeportistaCollectionDeportista != null) {
                    oldUsuarioOfDeportistaCollectionDeportista.getDeportistaCollection().remove(deportistaCollectionDeportista);
                    oldUsuarioOfDeportistaCollectionDeportista = em.merge(oldUsuarioOfDeportistaCollectionDeportista);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuario(usuario.getCedulamod()) != null) {
                throw new PreexistingEntityException("Usuario " + usuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getCedulamod());
            Collection<Deportista> deportistaCollectionOld = persistentUsuario.getDeportistaCollection();
            Collection<Deportista> deportistaCollectionNew = usuario.getDeportistaCollection();
            Collection<Deportista> attachedDeportistaCollectionNew = new ArrayList<Deportista>();
            for (Deportista deportistaCollectionNewDeportistaToAttach : deportistaCollectionNew) {
                deportistaCollectionNewDeportistaToAttach = em.getReference(deportistaCollectionNewDeportistaToAttach.getClass(), deportistaCollectionNewDeportistaToAttach.getCedula());
                attachedDeportistaCollectionNew.add(deportistaCollectionNewDeportistaToAttach);
            }
            deportistaCollectionNew = attachedDeportistaCollectionNew;
            usuario.setDeportistaCollection(deportistaCollectionNew);
            usuario = em.merge(usuario);
            for (Deportista deportistaCollectionOldDeportista : deportistaCollectionOld) {
                if (!deportistaCollectionNew.contains(deportistaCollectionOldDeportista)) {
                    deportistaCollectionOldDeportista.setUsuario(null);
                    deportistaCollectionOldDeportista = em.merge(deportistaCollectionOldDeportista);
                }
            }
            for (Deportista deportistaCollectionNewDeportista : deportistaCollectionNew) {
                if (!deportistaCollectionOld.contains(deportistaCollectionNewDeportista)) {
                    Usuario oldUsuarioOfDeportistaCollectionNewDeportista = deportistaCollectionNewDeportista.getUsuario();
                    deportistaCollectionNewDeportista.setUsuario(usuario);
                    deportistaCollectionNewDeportista = em.merge(deportistaCollectionNewDeportista);
                    if (oldUsuarioOfDeportistaCollectionNewDeportista != null && !oldUsuarioOfDeportistaCollectionNewDeportista.equals(usuario)) {
                        oldUsuarioOfDeportistaCollectionNewDeportista.getDeportistaCollection().remove(deportistaCollectionNewDeportista);
                        oldUsuarioOfDeportistaCollectionNewDeportista = em.merge(oldUsuarioOfDeportistaCollectionNewDeportista);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = usuario.getCedulamod();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getCedulamod();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            Collection<Deportista> deportistaCollection = usuario.getDeportistaCollection();
            for (Deportista deportistaCollectionDeportista : deportistaCollection) {
                deportistaCollectionDeportista.setUsuario(null);
                deportistaCollectionDeportista = em.merge(deportistaCollectionDeportista);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
