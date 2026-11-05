
package com.retail.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.retail.inventory.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
