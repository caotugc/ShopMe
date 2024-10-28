package com.shopme.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.shopme.common.entity.Category;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryServiceTests {
	@MockBean
	private CategoryRepository repo;
	
	@InjectMocks
	private CategoryService service;
	
	@Test
	public void testCheckUniqueInNewModeReturnDuplicateName() {
		Integer id = null;
		String name = "Computers";
		String alias = "abc";
		
		Category newCategory = new Category(id, name, alias);
		
		Mockito.when(repo.findByName(name)).thenReturn(newCategory);
		Mockito.when(repo.findByAlias(alias)).thenReturn(null);
		String result = service.checkUnique(id, name, alias);
		assertThat(result).isEqualTo("Duplicated name");
	}
	
	@Test
	public void testCheckUniqueInNewModeReturnDuplicateAlias() {
		Integer id = null;
		String name = "HGHG";
		String alias = "iphone";
		
		Category newCategory = new Category(id, name, alias);
		
		Mockito.when(repo.findByName(name)).thenReturn(null);
		Mockito.when(repo.findByAlias(alias)).thenReturn(newCategory);
		String result = service.checkUnique(id, name, alias);
		assertThat(result).isEqualTo("Duplicated alias");
	}
	
	@Test
	public void testCheckUniqueInNewModeReturnOk() {
		Integer id = null;
		String name = "HGHG";
		String alias = "iphone";
		
		Mockito.when(repo.findByName(name)).thenReturn(null);
		Mockito.when(repo.findByAlias(alias)).thenReturn(null);
		String result = service.checkUnique(id, name, alias);
		assertThat(result).isEqualTo("OK");
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnDuplicateName() {
		Integer id = 1;
		String name = "Computers";
		String alias = "a";
		
		Category newCategory = new Category(2, name, alias);
		
		Mockito.when(repo.findByName(name)).thenReturn(newCategory);
		Mockito.when(repo.findByAlias(alias)).thenReturn(null);
		String result = service.checkUnique(id, name, alias);
		assertThat(result).isEqualTo("Duplicated name");
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnDuplicateAlias() {
		Integer id = 1;
		String name = "HGHG";
		String alias = "iphone";
		
		Category newCategory = new Category(2, name, alias);
		
		Mockito.when(repo.findByName(name)).thenReturn(null);
		Mockito.when(repo.findByAlias(alias)).thenReturn(newCategory);
		String result = service.checkUnique(id, name, alias);
		assertThat(result).isEqualTo("Duplicated alias");
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnOk() {
		Integer id = 1;
		String name = "HGHG";
		String alias = "computers";
			
		Mockito.when(repo.findByName(name)).thenReturn(null);
		Mockito.when(repo.findByAlias(alias)).thenReturn(null);
		String result = service.checkUnique(id, name, alias);
		
		assertThat(result).isEqualTo("OK");
	}
}
