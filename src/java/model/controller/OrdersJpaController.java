/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.controller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Customer;
import model.Payment;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import model.Orderdetail;
import model.Orders;
import model.controller.exceptions.NonexistentEntityException;
import model.controller.exceptions.RollbackFailureException;

/**
 *
 * @author JewlyTwin
 */
public class OrdersJpaController implements Serializable {

    public OrdersJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Orders orders) throws RollbackFailureException, Exception {
        if (orders.getPaymentList() == null) {
            orders.setPaymentList(new ArrayList<Payment>());
        }
        if (orders.getOrderdetailList() == null) {
            orders.setOrderdetailList(new ArrayList<Orderdetail>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Customer customerid = orders.getCustomerid();
            if (customerid != null) {
                customerid = em.getReference(customerid.getClass(), customerid.getCustomerid());
                orders.setCustomerid(customerid);
            }
            List<Payment> attachedPaymentList = new ArrayList<Payment>();
            for (Payment paymentListPaymentToAttach : orders.getPaymentList()) {
                paymentListPaymentToAttach = em.getReference(paymentListPaymentToAttach.getClass(), paymentListPaymentToAttach.getPaymentid());
                attachedPaymentList.add(paymentListPaymentToAttach);
            }
            orders.setPaymentList(attachedPaymentList);
            List<Orderdetail> attachedOrderdetailList = new ArrayList<Orderdetail>();
            for (Orderdetail orderdetailListOrderdetailToAttach : orders.getOrderdetailList()) {
                orderdetailListOrderdetailToAttach = em.getReference(orderdetailListOrderdetailToAttach.getClass(), orderdetailListOrderdetailToAttach.getOrderdetailid());
                attachedOrderdetailList.add(orderdetailListOrderdetailToAttach);
            }
            orders.setOrderdetailList(attachedOrderdetailList);
            em.persist(orders);
            if (customerid != null) {
                customerid.getOrdersList().add(orders);
                customerid = em.merge(customerid);
            }
            for (Payment paymentListPayment : orders.getPaymentList()) {
                Orders oldOrderidOfPaymentListPayment = paymentListPayment.getOrderid();
                paymentListPayment.setOrderid(orders);
                paymentListPayment = em.merge(paymentListPayment);
                if (oldOrderidOfPaymentListPayment != null) {
                    oldOrderidOfPaymentListPayment.getPaymentList().remove(paymentListPayment);
                    oldOrderidOfPaymentListPayment = em.merge(oldOrderidOfPaymentListPayment);
                }
            }
            for (Orderdetail orderdetailListOrderdetail : orders.getOrderdetailList()) {
                Orders oldOrderidOfOrderdetailListOrderdetail = orderdetailListOrderdetail.getOrderid();
                orderdetailListOrderdetail.setOrderid(orders);
                orderdetailListOrderdetail = em.merge(orderdetailListOrderdetail);
                if (oldOrderidOfOrderdetailListOrderdetail != null) {
                    oldOrderidOfOrderdetailListOrderdetail.getOrderdetailList().remove(orderdetailListOrderdetail);
                    oldOrderidOfOrderdetailListOrderdetail = em.merge(oldOrderidOfOrderdetailListOrderdetail);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Orders orders) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Orders persistentOrders = em.find(Orders.class, orders.getOrderid());
            Customer customeridOld = persistentOrders.getCustomerid();
            Customer customeridNew = orders.getCustomerid();
            List<Payment> paymentListOld = persistentOrders.getPaymentList();
            List<Payment> paymentListNew = orders.getPaymentList();
            List<Orderdetail> orderdetailListOld = persistentOrders.getOrderdetailList();
            List<Orderdetail> orderdetailListNew = orders.getOrderdetailList();
            if (customeridNew != null) {
                customeridNew = em.getReference(customeridNew.getClass(), customeridNew.getCustomerid());
                orders.setCustomerid(customeridNew);
            }
            List<Payment> attachedPaymentListNew = new ArrayList<Payment>();
            for (Payment paymentListNewPaymentToAttach : paymentListNew) {
                paymentListNewPaymentToAttach = em.getReference(paymentListNewPaymentToAttach.getClass(), paymentListNewPaymentToAttach.getPaymentid());
                attachedPaymentListNew.add(paymentListNewPaymentToAttach);
            }
            paymentListNew = attachedPaymentListNew;
            orders.setPaymentList(paymentListNew);
            List<Orderdetail> attachedOrderdetailListNew = new ArrayList<Orderdetail>();
            for (Orderdetail orderdetailListNewOrderdetailToAttach : orderdetailListNew) {
                orderdetailListNewOrderdetailToAttach = em.getReference(orderdetailListNewOrderdetailToAttach.getClass(), orderdetailListNewOrderdetailToAttach.getOrderdetailid());
                attachedOrderdetailListNew.add(orderdetailListNewOrderdetailToAttach);
            }
            orderdetailListNew = attachedOrderdetailListNew;
            orders.setOrderdetailList(orderdetailListNew);
            orders = em.merge(orders);
            if (customeridOld != null && !customeridOld.equals(customeridNew)) {
                customeridOld.getOrdersList().remove(orders);
                customeridOld = em.merge(customeridOld);
            }
            if (customeridNew != null && !customeridNew.equals(customeridOld)) {
                customeridNew.getOrdersList().add(orders);
                customeridNew = em.merge(customeridNew);
            }
            for (Payment paymentListOldPayment : paymentListOld) {
                if (!paymentListNew.contains(paymentListOldPayment)) {
                    paymentListOldPayment.setOrderid(null);
                    paymentListOldPayment = em.merge(paymentListOldPayment);
                }
            }
            for (Payment paymentListNewPayment : paymentListNew) {
                if (!paymentListOld.contains(paymentListNewPayment)) {
                    Orders oldOrderidOfPaymentListNewPayment = paymentListNewPayment.getOrderid();
                    paymentListNewPayment.setOrderid(orders);
                    paymentListNewPayment = em.merge(paymentListNewPayment);
                    if (oldOrderidOfPaymentListNewPayment != null && !oldOrderidOfPaymentListNewPayment.equals(orders)) {
                        oldOrderidOfPaymentListNewPayment.getPaymentList().remove(paymentListNewPayment);
                        oldOrderidOfPaymentListNewPayment = em.merge(oldOrderidOfPaymentListNewPayment);
                    }
                }
            }
            for (Orderdetail orderdetailListOldOrderdetail : orderdetailListOld) {
                if (!orderdetailListNew.contains(orderdetailListOldOrderdetail)) {
                    orderdetailListOldOrderdetail.setOrderid(null);
                    orderdetailListOldOrderdetail = em.merge(orderdetailListOldOrderdetail);
                }
            }
            for (Orderdetail orderdetailListNewOrderdetail : orderdetailListNew) {
                if (!orderdetailListOld.contains(orderdetailListNewOrderdetail)) {
                    Orders oldOrderidOfOrderdetailListNewOrderdetail = orderdetailListNewOrderdetail.getOrderid();
                    orderdetailListNewOrderdetail.setOrderid(orders);
                    orderdetailListNewOrderdetail = em.merge(orderdetailListNewOrderdetail);
                    if (oldOrderidOfOrderdetailListNewOrderdetail != null && !oldOrderidOfOrderdetailListNewOrderdetail.equals(orders)) {
                        oldOrderidOfOrderdetailListNewOrderdetail.getOrderdetailList().remove(orderdetailListNewOrderdetail);
                        oldOrderidOfOrderdetailListNewOrderdetail = em.merge(oldOrderidOfOrderdetailListNewOrderdetail);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = orders.getOrderid();
                if (findOrders(id) == null) {
                    throw new NonexistentEntityException("The orders with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Orders orders;
            try {
                orders = em.getReference(Orders.class, id);
                orders.getOrderid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The orders with id " + id + " no longer exists.", enfe);
            }
            Customer customerid = orders.getCustomerid();
            if (customerid != null) {
                customerid.getOrdersList().remove(orders);
                customerid = em.merge(customerid);
            }
            List<Payment> paymentList = orders.getPaymentList();
            for (Payment paymentListPayment : paymentList) {
                paymentListPayment.setOrderid(null);
                paymentListPayment = em.merge(paymentListPayment);
            }
            List<Orderdetail> orderdetailList = orders.getOrderdetailList();
            for (Orderdetail orderdetailListOrderdetail : orderdetailList) {
                orderdetailListOrderdetail.setOrderid(null);
                orderdetailListOrderdetail = em.merge(orderdetailListOrderdetail);
            }
            em.remove(orders);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Orders> findOrdersEntities() {
        return findOrdersEntities(true, -1, -1);
    }

    public List<Orders> findOrdersEntities(int maxResults, int firstResult) {
        return findOrdersEntities(false, maxResults, firstResult);
    }

    private List<Orders> findOrdersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Orders.class));
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

    public Orders findOrders(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Orders.class, id);
        } finally {
            em.close();
        }
    }

    public int getOrdersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Orders> rt = cq.from(Orders.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
