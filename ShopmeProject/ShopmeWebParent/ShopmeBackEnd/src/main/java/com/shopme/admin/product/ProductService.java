package com.shopme.admin.product;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Product;
import com.shopme.common.exception.ProductNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ProductService {
    public static final int PRODUCTS_PER_PAGE = 4;
    @Autowired
    private ProductRepository repo;

    public List<Product> findAll() {
        return (List<Product>) repo.findAll();
    }

    public Page<Product> listByPage(int pageNum, String sortField, String sortDir, String keyword,
                                    Integer categoryId) {

        // Kiểm tra và xử lý sortDir
        Sort sort = Sort.by(sortField);
        sort = (sortDir != null && sortDir.equals("asc")) ? sort.ascending() : sort.descending();

        // Đảm bảo pageNum hợp lệ
        pageNum = (pageNum < 1) ? 1 : pageNum;

        // Tạo Pageable
        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE, sort);

        if (keyword != null && !keyword.isEmpty()) {
            if (categoryId != null && categoryId > 0) {
                String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
                return repo.searchInCategoryId(categoryId, categoryIdMatch, keyword, pageable);
            }
            return repo.findAll(keyword, pageable);
        }
        if (categoryId != null && categoryId > 0) {
            String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
            return repo.findAllInCategory(categoryId, categoryIdMatch, pageable);
        }
        return repo.findAll(pageable);
    }

    public Product save(Product product) {
        if(product.getId() == null) {
            product.setCreateTime(new Date());
        }
        if(product.getAlias() == null || product.getAlias().isEmpty()) {
            String defautAlias = product.getName().replaceAll(" ", "-");
            product.setAlias(defautAlias);
        } else {
            product.setAlias(product.getAlias().replaceAll(" ", "-"));
        }
        product.setUpdateTime(new Date());
        return repo.save(product);
    }

    public void saveProductPrice(Product productInForm) {
        Product productInDB = repo.findById(productInForm.getId()).get();
        productInDB.setPrice(productInForm.getPrice());
        productInDB.setCost(productInForm.getCost());
        productInDB.setDiscountPercent(productInForm.getDiscountPercent());

        repo.save(productInDB);
    }

    public String checkUnique (Integer id, String name) {
        boolean isCreatingNew = (id==null || id == 0);
        Product productByName = repo.findByName(name);

        if (isCreatingNew) {
            if (productByName != null) return "Duplicate";
        } else {
            if (productByName != null && productByName.getId() != id) {
                return "Duplicate";
            }
        }
        return "OK";
    }

    public void updateProductEnabledStatus(Integer id, Boolean enabled) {
        repo.updateEnabledStatus(id, enabled);
    }

    public void delete(Integer id) throws ProductNotFoundException {
        Long countId = repo.countById(id);
        if (countId == null || countId == 0) {
            throw new ProductNotFoundException("Could not find any product with ID " + id);
        }
        repo.deleteById(id);
    }

    public Product get(Integer id) throws ProductNotFoundException {
        try {
            return repo.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new ProductNotFoundException("Could not find any product with ID " + id);
        }
    }
}
