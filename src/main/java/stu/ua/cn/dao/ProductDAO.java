package stu.ua.cn.dao;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import stu.cn.ua.domain.Product;

public class ProductDAO {
    private Session session;
    public ProductDAO(Session session) {
        this.session = session;
    }
    /**
     * This method create new entity
     */
    public Product createProduct(Product product) {
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(product);
        transaction.commit();
        return product;
    }
    public Product updatedescription(Product product) {
        Transaction transaction = session.beginTransaction();
        session.flush();
        session.refresh(product);
        product.setDescription(product.getDescription() + "new");
        transaction.commit();
        return product;
    }
    /**
     * This method update existing product
     */
    public Product updateProduct(Product product) {
        Transaction transaction = session.beginTransaction();
        session.merge(product);
        transaction.commit();
        return product;
    }
    /**
     * This method delete existing product
     */
    public void deleteProduct(Product product) {
        Transaction transaction = session.beginTransaction();
        session.delete(product);
        transaction.commit();
    }

    /**
     * This method remove entity by id
     */
    public void deleteProductById(Long productId) {
        Product product = (Product) session.get(Product.class, productId);
        deleteProduct(product);
    }
    /**
     * This method return all entities
     */
    public List<Product> getAllProducts() {
        SQLQuery query = session.createSQLQuery(
                "select * from product").addEntity(Product.class);
        return (List<Product>) query.list();
    }
    /**
     * This method return all products by name
     * This method is specific for Product domain object
     *instead of other methods from this class
     * All other methods can be moved to generic class and
     *can be used for other domain objects.
     */
    public List<Product> getProductsByName(String name) {
        Criteria criteria = session.createCriteria(Product.class)
                .add(Restrictions.eq("name", name));
        return criteria.list();
    }
}
