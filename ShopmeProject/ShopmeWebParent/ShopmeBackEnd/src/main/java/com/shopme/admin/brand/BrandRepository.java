package com.shopme.admin.brand;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.common.entity.Brand;

import java.util.List;

public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer>, CrudRepository<Brand, Integer> {
	public Long countById(Integer id);
	
	public Brand findByName(String name);
	
	@Query ("SELECT b FROM Brand b WHERE b.name LIKE %?1%")
	public Page<Brand> findByKey(String keyword, Pageable pageable);

	@Query("SELECT NEW Brand(b.id, b.name) FROM Brand b order by b.name ASC")
	public List<Brand> findAll();
}
