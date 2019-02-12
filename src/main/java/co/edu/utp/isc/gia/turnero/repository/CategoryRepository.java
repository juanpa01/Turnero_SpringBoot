/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.utp.isc.gia.turnero.repository;

import co.edu.utp.isc.gia.turnero.model.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author juan
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>  {

    public List<Category> findAllByOrderByPriority();
    
}
