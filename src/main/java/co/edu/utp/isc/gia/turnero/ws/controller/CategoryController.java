/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.utp.isc.gia.turnero.ws.controller;

import co.edu.utp.isc.gia.turnero.model.Category;
import co.edu.utp.isc.gia.turnero.model.Turn;
import co.edu.utp.isc.gia.turnero.services.CategoryService;
import co.edu.utp.isc.gia.turnero.services.TurnService;
import co.edu.utp.isc.gia.turnero.ws.dto.CategoryResponse;
import co.edu.utp.isc.gia.turnero.ws.dto.TurnResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author juan
 */
//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/categories")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private TurnService turnService;
    
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<CategoryResponse>> listCategories(){
        
        List<CategoryResponse> categories = categoryService.listCategories();
        
        if (categories.isEmpty()) {
            return  ResponseEntity.noContent().build();
        } else {
            return  ResponseEntity.status(HttpStatus.ACCEPTED).body(categories);
        }
    }
    
    @PostMapping
    @ResponseBody
    public ResponseEntity<CategoryResponse> insertCategory(@RequestBody Category category) {
        if (category == null) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        
        CategoryResponse newCategory = categoryService.insertCategory(category);
        
        if (newCategory == null) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return  ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
    }
    
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable("id") long id, 
                                                            @RequestBody Category category) {
        if (category == null) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        
        CategoryResponse updateCategory = categoryService.updateCategory(id, category);
        
        if (updateCategory != null) {
            return   ResponseEntity.status(HttpStatus.ACCEPTED).body(updateCategory);
        }
        else {
            return  ResponseEntity.status(HttpStatus.NO_CONTENT).body(updateCategory);
        }
    }
    
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity deleteCategory(@PathVariable("id") long id) {
        CategoryResponse deleteCategory = categoryService.deleteCategory(id);
        
        if (deleteCategory == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity(HttpStatus.ACCEPTED);
        }
    }
    
     @PostMapping("/{categoryId}/turns")
    @ResponseBody
    public ResponseEntity<TurnResponse> generateTurn(@PathVariable("categoryId") long categoryId) {

        TurnResponse newTurn = categoryService.generateTurn(categoryId);
        
        if (newTurn == null) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return  ResponseEntity.status(HttpStatus.CREATED).body(newTurn);
    }
    
}
