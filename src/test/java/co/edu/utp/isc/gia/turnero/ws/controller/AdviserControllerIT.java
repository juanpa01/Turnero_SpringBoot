/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.utp.isc.gia.turnero.ws.controller;

import co.edu.utp.isc.gia.turnero.model.Adviser;
import co.edu.utp.isc.gia.turnero.model.Category;
import co.edu.utp.isc.gia.turnero.model.Turn;
import co.edu.utp.isc.gia.turnero.repository.AdviserRepository;
import co.edu.utp.isc.gia.turnero.repository.CategoryRepository;
import co.edu.utp.isc.gia.turnero.repository.TurnRepository;
import co.edu.utp.isc.gia.turnero.ws.dto.NextTurnResponse;
import java.time.LocalDateTime;
import java.util.List;
import junit.framework.Assert;
import lombok.extern.java.Log;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author juan
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
@Log
public class AdviserControllerIT {
    
    @Autowired
    private AdviserRepository adviserRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private TurnRepository turnRepository;
    
     @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    @Transactional
    public void testNextTurn() {
        Adviser adviser = this.adviserRepository.save(new Adviser("Juan", "1"));
       
          Category category = this.categoryRepository.save(new Category("A", 5, 1));
          Category categoryB = this.categoryRepository.save(new Category("B", 3, 1));
      
         Turn turn = this.turnRepository.save(new Turn("A1", "listado"));
        turn.setPriority(category.getPriority());
        turn.setAdviser(adviser);
        turn.setCreated(LocalDateTime.now());
        turn.setCategory(category);
        this.turnRepository.save(turn);
        
        Turn turn2 = this.turnRepository.save(new Turn("B1", "listado"));
        turn2.setPriority(categoryB.getPriority());
        turn2.setAdviser(adviser);
        turn2.setCreated(LocalDateTime.now());
        turn2.setCategory(categoryB);
        this.turnRepository.save(turn2);
        
        long id = adviser.getId();
        
        ResponseEntity<NextTurnResponse> result = restTemplate.exchange(
                String.format("http://localhost:8080/api/advisers/%d/nextTurn", id),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<NextTurnResponse>() {}
        );
        
        turnRepository.deleteAll();
        adviserRepository.deleteAll();
        categoryRepository.deleteAll();
        
        
        Assert.assertEquals(HttpStatus.ACCEPTED, result.getStatusCode() );
        
        
    }
}
