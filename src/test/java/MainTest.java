import stu.cn.ua.domain.Seller;
import stu.ua.cn.dao.AllObjectsDAO;
import stu.ua.cn.dao.HibernateDAOFactory;
import stu.cn.ua.domain.Product;
import org.junit.jupiter.api.*;


import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


class ProductDAOTest {
    private Product product1;
    private Product product2;
    private Product product3;


    @BeforeEach
    public void before() {
        // Create test products before starting test
        product1 = new Product("Test product name 1",
                "Test product description 1");
        product2 = new Product("Test product name 2",
                "Test product description 2");
        product3 = new Product("Test product name 3",
                "Test product description 3");
        HibernateDAOFactory.getInstance().getProductDAO().createProduct(product1);
        HibernateDAOFactory.getInstance().getProductDAO().updatedescription(product1);
        HibernateDAOFactory.getInstance().getProductDAO().createProduct(product2);
        HibernateDAOFactory.getInstance().getProductDAO().createProduct(product3);
    }


    @AfterEach
    public void after() {
        // Remove all products after completing test
        List<Product> productList = HibernateDAOFactory.getInstance()
                .getProductDAO().getAllProducts();
        for (Product product : productList) {
            HibernateDAOFactory.getInstance().getProductDAO().deleteProduct(product);
        }
    }


    @Test
    public void whenGetAllThenAllShouldBePresent() {
        // given
        // when
        List<Product> productList = HibernateDAOFactory.getInstance()
                .getProductDAO().getAllProducts();
        // then
        assertEquals(3, productList.size());
        for (Product product : productList) {
            Assertions.assertNotNull(product);
            Assertions.assertNotNull(product.getId());
            Assertions.assertNotNull(product.getName());
            Assertions.assertNotNull(product.getDescription());
        }
    }


    @Test
    public void whenAddProductThenItShoudBePresentInDb() throws Exception {
        // given
        Product product = new Product("Test product name",
                "Test product description");
        // when
        HibernateDAOFactory.getInstance().getProductDAO().createProduct(product);
        // then
        List<Product> productListAfter = HibernateDAOFactory.getInstance()
                .getProductDAO().getAllProducts();
        int afterSize = productListAfter.size();
        Assertions.assertNotNull(product.getId());
        assertEquals(4, afterSize);
    }


    @Test
    public void whenDeleteProductThenItShoudNotBePresentInDb() throws Exception {
        // given
        Product product = new Product("Test product name",
                "Test product description");
        HibernateDAOFactory.getInstance().getProductDAO().createProduct(product);
        // when
        HibernateDAOFactory.getInstance().getProductDAO().deleteProduct(product);
        // then
        List<Product> productListAfter = HibernateDAOFactory.getInstance()
                .getProductDAO().getAllProducts();
        assertEquals(3, productListAfter.size());
        for (Product productEnt : productListAfter) {
            assertNotEquals("Test product name", productEnt.getName());
            assertNotEquals("Test product description", productEnt.getDescription());
        }
    }


    @Test
    public void whenGetAllProductsByNameThenAllWithSuchNameShouldBePresent() {
        //given
        Product product1 = new Product("Specific product name",
                "Test product description1");
        Product product2 = new Product("Specific product name",
                "Test product description2");
        Product product3 = new Product("Specific product name",
                "Test product description3");
        HibernateDAOFactory.getInstance().getProductDAO().createProduct(product1);
        HibernateDAOFactory.getInstance().getProductDAO().createProduct(product2);
        HibernateDAOFactory.getInstance().getProductDAO().createProduct(product3);
        //when
        List<Product> productListByName = HibernateDAOFactory.getInstance()
                .getProductDAO().getProductsByName("Specific product name");
        //then
        assertEquals(3, productListByName.size());
        for (Product productEnt : productListByName) {
            assertEquals("Specific product name", productEnt.getName());
        }
    }

    @Test
    void whenAddNewProductAndSellerOneToMany() {
        // Создаем два продукта
        Product product1 = new Product("Specific product name", "Test product description1");
        Product product2 = new Product("Specific product name", "Test product description2");

        // Создаем продавца
        Seller seller = new Seller("Seller", "email");

        // Устанавливаем продавца для каждого продукта
        product1.setSeller(seller);
        product2.setSeller(seller);

        // Добавляем продукты к продавцу
        seller.addProduct(product1);
        seller.addProduct(product2);

        // Сохраняем продавца (и каскадно сохраняются продукты)
        AllObjectsDAO<Seller> sellerDAO = HibernateDAOFactory.getInstance().getSellerDAO();
        sellerDAO.createEntity(seller);

        sellerDAO.updateEntity(seller);
        // Проверяем, что у продавца есть два продукта
        Seller updatedSeller = sellerDAO.getEntityById(seller.getId());
        assertEquals(2, updatedSeller.getProducts().size());
        sellerDAO.deleteEntity(seller);
    }



}