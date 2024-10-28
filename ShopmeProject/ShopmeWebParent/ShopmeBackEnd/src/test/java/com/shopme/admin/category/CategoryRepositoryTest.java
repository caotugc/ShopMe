package com.shopme.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import com.shopme.common.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Category;

import jakarta.transaction.Transactional;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTest {
	@Autowired
	private CategoryRepository repoCat;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateRootCategory() {
		Category category = new Category("Electronics");
		Category savedCategory =  repoCat.save(category);
		assertThat(savedCategory.getId()).isGreaterThan(0);
	}
	
	@Test
	@Transactional
	public void testCreateSubCategory() {
		Category parent = new Category(12);
		Category new1 = new Category("iphone", parent);
		try {
		    repoCat.saveAll(List.of(new1));
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	@Test
	public void testGetCategory() {
		Category category = repoCat.findById(1).get();
		System.out.println(category.getName());
		
		Set<Category> children = category.getChildren();
		
		for(Category sub : children) {
			System.out.println(sub.getName());
		}
		assertThat(children.size()).isGreaterThan(0);
	}
	
	@Test
	public void testPrintHierarchicalCategories() {
		Iterable<Category> categories = repoCat.findAll();
		
		for (Category category : categories) {
			if(category.getParent() == null) {
				System.out.println(category.getName());
				Set<Category> children = category.getChildren();
				
				for (Category subCategory : children) {
					System.out.println("--" + subCategory.getName());
					printChildren(subCategory, 1);
				}
			}
		}
	}
	
	private void printChildren(Category parent, int sublevel) {
		int newSublevel = sublevel + 1;
		Set<Category> children = parent.getChildren();
		for (Category subCategory : children) {
			for(int i = 0; i < newSublevel; i++) {
				System.out.print("--");
			}
			System.out.println(subCategory.getName());
			printChildren(subCategory, newSublevel);
		}
	}
	
	@Test
	public void testListRootCategories() {
		List<Category> rootCategories = repoCat.findRootCategories(Sort.by("name").ascending());
		rootCategories.forEach(cat -> System.out.println(cat.getName()));
	}
	
	@Test
	public void testFindByName() {
		String name = "Computers";
		Category category = repoCat.findByName(name);
		assertThat(category.getName()).isEqualTo(name);
		assertThat(category).isNotNull();
	}
	
	@Test
	public void testFindByAlias() {
		String alias = "iphone";
		Category category = repoCat.findByAlias(alias);
		assertThat(category.getName()).isEqualTo(alias);
		assertThat(category).isNotNull();
	}
}
