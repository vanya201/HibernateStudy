package stu.ua.cn.dao;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class AllObjectsDAO<T> {
    private Session session;
    private Class<T> entityClass;

    public AllObjectsDAO(Session session, Class<T> entityClass) {
        this.session = session;
        this.entityClass = entityClass;
    }

    /**
     * This method creates a new entity.
     */
    public T createEntity(T obj) {
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(obj);

        transaction.commit();
        return obj;
    }

    /**
     * This method updates an existing entity.
     */
    public T updateEntity(T entity) {
        Transaction transaction = session.beginTransaction();
        session.merge(entity);
        transaction.commit();
        return entity;
    }

    /**
     * This method deletes an existing entity.
     */
    public void deleteEntity(T entity) {
        Transaction transaction = session.beginTransaction();
        session.delete(entity);
        transaction.commit();
    }

    /**
     * This method removes an entity by id.
     */
    public void deleteEntityById(Long id) {
        T entity = (T) session.get(entityClass, id);
        deleteEntity(entity);
    }
    public T getEntityById(Long id) {
        T entity = (T) session.get(entityClass, id);
        return entity;
    }
    /**
     * This method returns all entities.
     */
    public List<T> getAllEntities() {
        Criteria criteria = session.createCriteria(entityClass);
        return criteria.list();  // Возвращает все сущности типа T
    }

    /**
     * This method returns entities by a specific field value.
     */
    public List<T> getEntitiesByField(String fieldName, Object value) {
        Criteria criteria = session.createCriteria(entityClass)
                .add(Restrictions.eq(fieldName, value));
        return criteria.list();
    }
}
