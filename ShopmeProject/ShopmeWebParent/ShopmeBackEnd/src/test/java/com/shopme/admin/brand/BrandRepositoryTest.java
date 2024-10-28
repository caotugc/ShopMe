package com.shopme.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class BrandRepositoryTest {
	@Autowired
	private BrandRepository repo;
	
//	@Autowired
//    private BrandController brandController;
//
//	@Autowired
//    private BrandService brandService;
	
	@Test
	public void testCreateBrand() {
		Category laptops = new Category(6);
		Brand acer = new Brand("Acer");
		acer.getCategories().add(laptops);
		
		Brand savedBrand = repo.save(acer);
		
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateBrand2() {
		Category cellphones = new Category(4);
		Category tablets = new Category(7);
		
		Brand apple = new Brand("Apple");
		apple.getCategories().add(cellphones);
		apple.getCategories().add(tablets);
		
		Brand savedBrand = repo.save(apple);
		
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateBrand3() {		
		Brand samsung = new Brand("Samsung");
		samsung.getCategories().add(new Category(29));
		samsung.getCategories().add(new Category(24));
		
		Brand savedBrand = repo.save(samsung);
		
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testFindAll() {
		Iterable<Brand> brands = repo.findAll();
		brands.forEach(System.out::println);
		assertThat(brands).isNotEmpty();
	}
	
	@Test
	public void testGetById() {
		Brand brand = repo.findById(3).get();
		assertThat(brand.getName()).isEqualTo("Acer");
	}
	
	@Test
	public void testUpdateName() {
		String newName = "Samsung Electronics";
		Brand samsung = repo.findById(5).get();
		samsung.setName(newName);
		Brand savedBrand = repo.save(samsung);
		assertThat(savedBrand.getName()).isEqualTo(newName);
	}
	
	@Test
	public void testDelete() {
		Integer id = 3;
		repo.deleteById(3);
		Optional<Brand> result = repo.findById(id);
		assertThat(result.isEmpty());
	}

// Đoạn test này bị xung đột cho test findAll khi cập nhật mới đoạn repository @Query("SELECT NEW Brand(b.id, b.name) FROM Brand b order by b.name ASC")
//	@Test
//    public void testListByPage() {
//        int pageNum = 1; // Số trang muốn kiểm tra
//        String sortField = "name"; // Trường để sắp xếp
//        String sortDir = "asc"; // Định hướng sắp xếp
//        String keyword = ""; // Từ khóa tìm kiếm
//
//        // Gọi phương thức listByPage
//        //String viewName = brandController.listByPage(pageNum, null, sortField, sortDir, keyword);
//
//        // Kiểm tra rằng viewName trả về là chính xác
//        assertEquals("brands", viewName); // Thay "brands" bằng tên view thực tế nếu cần
//
//        // Giả sử bạn đã tạo ra dữ liệu trong cơ sở dữ liệu để kiểm tra
//        Page<Brand> page = brandService.listByPage(pageNum, sortField, sortDir, keyword);
//        assertNotNull(page); // Kiểm tra rằng trang không null
//
//        // In ra tổng số phần tử
//        System.out.println("Total Elements: " + page.getTotalElements());
//
//    }
}
