/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.utp.isc.gia.turnero.services;

import co.edu.utp.isc.gia.turnero.model.Category;
import co.edu.utp.isc.gia.turnero.model.Turn;
import co.edu.utp.isc.gia.turnero.repository.CategoryRepository;
import co.edu.utp.isc.gia.turnero.repository.TurnRepository;
import co.edu.utp.isc.gia.turnero.ws.dto.CategoryResponse;
import co.edu.utp.isc.gia.turnero.ws.dto.TurnResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author juan
 */
@Service
public class CategoryService {
    
    @Autowired 
    private CategoryRepository categoryRepository;
    
    @Autowired
    private TurnRepository turnRepository;
    
    public List<CategoryResponse> listCategories(){
        List<CategoryResponse> res = new ArrayList<>(); 
        List<Category> list = categoryRepository.findAll();
        
        for (Category category : list) {
            CategoryResponse categoryResponse = CategoryResponse.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .priority(category.getPriority())
                    .cont(category.getCont())
                    .turns(category.getTurns().size())
                    .build();
            
            res.add(categoryResponse);        
        }
        return res;
    }
    
    public CategoryResponse insertCategory(Category category) {
        Category newCategory = null;
        
        Optional<Category> u = categoryRepository.findById(category.getId());
        
        if (u.isPresent() && u.get() != null) {
            return null;
        }
        
        newCategory = categoryRepository.save(category);
        
        if (newCategory != null) {
            CategoryResponse categoryResponse = CategoryResponse.builder()
                    .id(newCategory.getId())
                    .name(newCategory.getName())
                    .priority(newCategory.getPriority())
                    .cont(newCategory.getCont())
                    .build();
            return categoryResponse;
        }
        return null;
    }
    
    public CategoryResponse updateCategory(long id, Category category) {
       Category updateCategory = null;
        
        Optional<Category> u = categoryRepository.findById(id);
        
        if (u.isPresent() && u.get() != null) {
            return null;
        }
        updateCategory = categoryRepository.save(category);
        
         if (updateCategory != null) {
            CategoryResponse categoryResponse = CategoryResponse.builder()
                    .name(updateCategory.getName())
                    .priority(updateCategory.getPriority())
                    .build();
            return categoryResponse;
        }
        return null;
    }
    
    public CategoryResponse deleteCategory(long id){
        Category deleteCategory = categoryRepository.getOne(id);
         if (deleteCategory == null) {
            return null;
        }
         
         CategoryResponse categoryResponse = CategoryResponse.builder()
                    .name(deleteCategory.getName())
                    .priority(deleteCategory.getPriority())
                    .build();
         
         categoryRepository.delete(deleteCategory);
         
         return categoryResponse;
    }
    
    public TurnResponse generateTurn(long categoryId, Turn turn) {
        Turn newTurn = null;
        String nameTurn;
        int cont;
        Optional<Turn> u = turnRepository.findById(turn.getId());
       Category category = null;
        
        if (categoryRepository.existsById(categoryId) ) {
             category = categoryRepository.getOne(categoryId);
        } else {
            return null;
        }
        
        if (u.isPresent() && u.get() != null) {
            return null;
        }
        
        cont = category.getCont();
        cont++;
        nameTurn = category.getName() + cont;
        category.setCont(cont);
       
        turn.setName(nameTurn);
        turn.setStateTurn("listado");
        turn.setCategory(category);
        
        categoryRepository.save(category);
        newTurn = turnRepository.save(turn);
        
        if (newTurn != null) {
            TurnResponse turnResponse = TurnResponse.builder()
                    .id(newTurn.getId())
                    .name(newTurn.getName())
                    .stateTurn(newTurn.getStateTurn())
                    .category(newTurn.getCategory().getId())
                    .build();
            return turnResponse;
        }
        return null;
    }
    
}
