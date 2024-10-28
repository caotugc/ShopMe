package com.shopme.admin.brand;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import com.shopme.common.entity.Brand;

@SpringBootTest
@AutoConfigureMockMvc
public class BrandControllerTest {

    @Autowired
    private BrandService brandService;

    @Autowired
    private BrandController brandController;

    @Test
    public void testListByPage() {
        int pageNum = 1; // Số trang muốn kiểm tra
        String sortField = "name"; // Trường để sắp xếp
        String sortDir = "asc"; // Định hướng sắp xếp
        String keyword = ""; // Từ khóa tìm kiếm

        // Gọi phương thức listByPage
        //String viewName = brandController.listByPage(pageNum, null, sortField, sortDir, keyword);

        // Kiểm tra rằng viewName trả về là chính xác
        //assertEquals("brands", viewName); // Thay "brands" bằng tên view thực tế nếu cần

        // Giả sử bạn đã tạo ra dữ liệu trong cơ sở dữ liệu để kiểm tra
        Page<Brand> page = brandService.listByPage(pageNum, sortField, sortDir, keyword);
        assertNotNull(page); // Kiểm tra rằng trang không null

        // In ra tổng số phần tử
        System.out.println("Total Elements: " + page.getTotalElements());
        assertEquals(54, page.getTotalElements()); // Thay expectedTotalElements bằng số dự kiến
    }
}
