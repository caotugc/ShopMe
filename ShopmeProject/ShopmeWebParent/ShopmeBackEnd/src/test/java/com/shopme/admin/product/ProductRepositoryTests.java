package com.shopme.admin.product;

import static org.assertj.core.api.Assertions.assertThat;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class ProductRepositoryTests {
    @Autowired
    private ProductRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateProduct() {
        Brand brand = entityManager.find(Brand.class, 38);
        Category category = entityManager.find(Category.class, 8);
        Product product = new Product();
        product.setName("Dell Inspiron 300");
        product.setAlias("Dell Inspiron 300");
        product.setShortDescription("Good laptop Dell");
        product.setFullDescription("Full description: This is a good Dell's laptop");
        product.setBrand(brand);
        product.setCategory(category);
        product.setPrice(4560);
        product.setCost(6200);
        product.setEnabled(true);
        product.setInStock(true);
        product.setCreateTime(new Date());
        product.setUpdateTime(new Date());

        Product savedproduct = repo.save(product);

        assert savedproduct.getId() != null;
    }

    @Test
    public void testListAll() {
        Iterable<Product> products = repo.findAll();
        products.forEach(System.out::println);
    }

    @Test
    public void testFindById() {
        Integer id = 2;
        Product product = repo.findById(id).get();
        assertNotNull(product);
    }

    @Test
    public void testUpdate() {
        Integer id = 2;
        Product product = repo.findById(id).get();
        product.setPrice(8560);
        repo.save(product);

        Product updatedProduct = entityManager.find(Product.class, id);
        assert updatedProduct.getPrice() == 8560;
    }
    @Test
    public void testDelete() {
        Integer id = 2;
        repo.deleteById(id);
        Optional<Product> product = repo.findById(id);
        assert product.isEmpty();
    }

    @Test
    public void testSaveProductWithImages() {
        Integer id = 1;
        Product product = repo.findById(id).get();

        product.setMainImage("main_image.png");
        product.addExtraImage("extra_image1.png");
        product.addExtraImage("extra_image2.png");
        product.addExtraImage("extra_image3.png");

        Product savedProduct =  repo.save(product);
        assertThat(savedProduct.getImages().size()).isEqualTo(3);

    }

    @Test
    public void testSaveProductWithDetails() {
        Integer productId = 4;
        Product product = repo.findById(productId).get();

        product.addDetail("Device memory", "128Gb");
        product.addDetail("CPU model", "i7 Gen 12th");

        Product savedProduct =  repo.save(product);
        assertThat(savedProduct.getDetails().size()).isEqualTo(2);
    }
}
