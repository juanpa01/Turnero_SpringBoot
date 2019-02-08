/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.utp.isc.gia.turnero.services;

import co.edu.utp.isc.gia.turnero.model.Adviser;
import co.edu.utp.isc.gia.turnero.model.Category;
import co.edu.utp.isc.gia.turnero.model.Turn;
import co.edu.utp.isc.gia.turnero.repository.AdviserRepository;
import co.edu.utp.isc.gia.turnero.repository.CategoryRepository;
import co.edu.utp.isc.gia.turnero.repository.TurnRepository;
import co.edu.utp.isc.gia.turnero.ws.dto.NextTurnResponse;
import co.edu.utp.isc.gia.turnero.ws.dto.DisplayResponse;
import co.edu.utp.isc.gia.turnero.ws.dto.EndTurnResponse;
import co.edu.utp.isc.gia.turnero.ws.dto.AverageResponse;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author juan
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AdviserServiceTest {
    
    @Autowired
    private AdviserRepository adviserRepository;
    
    @Autowired
    private AdviserService adviserService;
    
    @Autowired
    private TurnRepository turnRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private CategoryService categoryService;
    
    @Test
    public void nextTurnCase1() {
        this.adviserRepository.save(new Adviser("Juan", "1"));
        Adviser adviser = this.adviserRepository.findAll().get(0);
        long id = adviser.getId();
        NextTurnResponse res = this.adviserService.nextTurn(id);
        adviserRepository.deleteAll();
        turnRepository.deleteAll();
        assertNull(res);
    }
    
    @Test
    public void nextTurnCase2() {
        this.adviserRepository.save(new Adviser("Juan", "1"));
        Adviser adviser = this.adviserRepository.findAll().get(0);
        long id = adviser.getId();
        Turn turn =this.turnRepository.save(new Turn("A1", "llamando"));
        turn.setAdviser(adviser);
        this.turnRepository.save(turn);
        
        NextTurnResponse res = this.adviserService.nextTurn(id);
        adviserRepository.deleteAll();
        turnRepository.deleteAll();
        assertNull(res);
    }
    
    @Test
    public void nextTurnCase3() {
        Adviser adviser = this.adviserRepository.save(new Adviser("Juan", "1"));
        long id = adviser.getId();
        
        Category category =this.categoryRepository.save(new Category("A", 1,3));
          
        Turn turn = this.turnRepository.save(new Turn("A1", "listado"));
        turn.setCreated(LocalDateTime.now());
        turn.setCategory(category);
        this.turnRepository.save(turn);
        
        Turn turn2 = this.turnRepository.save(new Turn("A2", "llamando"));
        turn2.setAdviser(adviser);
        turn2.setCategory(category);
        turn2.setCreated(LocalDateTime.now());
        turn2.setUpdated(LocalDateTime.now());
        this.turnRepository.save(turn2);
        
        NextTurnResponse res = this.adviserService.nextTurn(id);
        adviserRepository.deleteAll();
        turnRepository.deleteAll();
        categoryRepository.deleteAll();
        assertNotNull(res);
    }
    
    @Test
    public void nextTurnCase4() {
        Adviser adviser = this.adviserRepository.save(new Adviser("Juan", "1"));
        long id = adviser.getId();
        
        Category category =this.categoryRepository.save(new Category("A", 1,3));
          
        Turn turn = this.turnRepository.save(new Turn("A1", "listado"));
        turn.setCreated(LocalDateTime.now());
        turn.setCategory(category);
        this.turnRepository.save(turn);
        
        NextTurnResponse res = this.adviserService.nextTurn(id);
        adviserRepository.deleteAll();
        turnRepository.deleteAll();
        categoryRepository.deleteAll();
        assertNotNull(res);
    }
    
    @Test
    public void reCallNull() {
        Adviser adviser = this.adviserRepository.save(new Adviser("Juan", "1"));
        long id = adviser.getId();
        
        DisplayResponse res = this.adviserService.reCall(id);
        adviserRepository.deleteAll();
         assertNull(res);
    }
    
   /* @Test
    public void reCallNotNull() {
        Adviser adviser = this.adviserRepository.save(new Adviser("Juan", "1"));
        long id = adviser.getId();
        
         Category category =this.categoryRepository.save(new Category("A", "alta"));
        
        Turn turn2 = this.turnRepository.save(new Turn("A2", "llamando"));
        turn2.setAdviser(adviser);
        turn2.setCategory(category);
        turn2.setCreated(LocalDateTime.now());
        turn2.setUpdated(LocalDateTime.now());
        this.turnRepository.save(turn2);
        
        DisplayResponse res = this.adviserService.reCall(id);
        adviserRepository.deleteAll();
        turnRepository.deleteAll();
        categoryRepository.deleteAll();
        assertNotNull(res);
    }
    
    @Test
    public void reCallLostNullCase1() {
        Adviser adviser = this.adviserRepository.save(new Adviser("Juan", "1"));
        long id = adviser.getId();
        
        DisplayResponse res = this.adviserService.reCallLost(id);
        adviserRepository.deleteAll();
         assertNull(res);
    }
    
     @Test
    public void reCallLostNullCase2() {
        Adviser adviser = this.adviserRepository.save(new Adviser("Juan", "1"));
        long id = adviser.getId();
        
        Category category =this.categoryRepository.save(new Category("A", "alta"));
        
        Turn turn2 = this.turnRepository.save(new Turn("A2", "llamando"));
        turn2.setAdviser(adviser);
        turn2.setCategory(category);
        turn2.setCreated(LocalDateTime.now());
        turn2.setUpdated(LocalDateTime.now());
        this.turnRepository.save(turn2);
        
        isplayResponse res = this.adviserService.reCallLost(id);
        adviserRepository.deleteAll();
        turnRepository.deleteAll();
        categoryRepository.deleteAll();
         assertNull(res);
    }
    
    @Test
    public void reCallLostNotNullCase1() {
        Adviser adviser = this.adviserRepository.save(new Adviser("Juan", "1"));
        long id = adviser.getId();
        
        Category category =this.categoryRepository.save(new Category("A", "alta"));
        
        Turn turn2 = this.turnRepository.save(new Turn("A2", "perdido"));
        turn2.setAdviser(adviser);
        turn2.setCategory(category);
        turn2.setCreated(LocalDateTime.now());
        turn2.setUpdated(LocalDateTime.now());
        this.turnRepository.save(turn2);
        
        DisplayResponse res = this.adviserService.reCallLost(id);
        adviserRepository.deleteAll();
        turnRepository.deleteAll();
        categoryRepository.deleteAll();
         assertNotNull(res);
    }
    
    @Test
    public void reCallLostNotNullCase2() {
        Adviser adviser = this.adviserRepository.save(new Adviser("Juan", "1"));
        long id = adviser.getId();
        
        Category category =this.categoryRepository.save(new Category("A", "alta"));
        
        Turn turn = this.turnRepository.save(new Turn("A2", "llamando"));
        turn.setAdviser(adviser);
        turn.setCategory(category);
        turn.setCreated(LocalDateTime.now());
        turn.setUpdated(LocalDateTime.now());
        this.turnRepository.save(turn);
        
        Turn turn2 = this.turnRepository.save(new Turn("A2", "perdido"));
        turn2.setAdviser(adviser);
        turn2.setCategory(category);
        turn2.setCreated(LocalDateTime.now());
        turn2.setUpdated(LocalDateTime.now());
        this.turnRepository.save(turn2);
        
        DisplayResponse res = this.adviserService.reCallLost(id);
        adviserRepository.deleteAll();
        turnRepository.deleteAll();
        categoryRepository.deleteAll();
         assertNotNull(res);
    }
    */
    
    @Test
    public void enTurnNull() {
        Adviser adviser = this.adviserRepository.save(new Adviser("Juan", "1"));
        long id = adviser.getId();
        
        EndTurnResponse res = this.adviserService.endTurn(id);
        adviserRepository.deleteAll();
         assertNull(res);
    }
    
     @Test
     public void enTurnNotNull() {
        Adviser adviser = this.adviserRepository.save(new Adviser("Juan", "1"));
        long id = adviser.getId();
        
        Category category =this.categoryRepository.save(new Category("A", 1,3));
        
        Turn turn = this.turnRepository.save(new Turn("A1", "llamando"));
        turn.setAdviser(adviser);
        turn.setCategory(category);
        turn.setTimeTurn(LocalDateTime.now());
        turn.setCreated(LocalDateTime.now());
        turn.setUpdated(LocalDateTime.now());
        turn.setFinalTime(LocalTime.now());
        this.turnRepository.save(turn);
        
        EndTurnResponse res = this.adviserService.endTurn(id);
        adviserRepository.deleteAll();
        turnRepository.deleteAll();
        categoryRepository.deleteAll();
         assertNotNull(res);
    }
     /*
     @Test
     public void lostEmpty() {
         Adviser adviser = this.adviserRepository.save(new Adviser("Juan", "1"));
        long id = adviser.getId();
        
        List<DisplayResponse> res = this.adviserService.lost(id);
        List<DisplayResponse> empty = new ArrayList<>();
        adviserRepository.deleteAll();
         assertEquals(empty, res);
     }
     
      @Test
     public void lostNotEmpty() {
         Adviser adviser = this.adviserRepository.save(new Adviser("Juan", "1"));
        long id = adviser.getId();
        
        Category category =this.categoryRepository.save(new Category("A", "alta"));
        
        Turn turn = this.turnRepository.save(new Turn("A2", "perdido"));
        turn.setAdviser(adviser);
        turn.setCategory(category);
        turn.setCreated(LocalDateTime.now());
        turn.setUpdated(LocalDateTime.now());
        this.turnRepository.save(turn);
        
        List<DisplayResponse> res = this.adviserService.lost(id);
        adviserRepository.deleteAll();
        turnRepository.deleteAll();
        categoryRepository.deleteAll();
         assertFalse(res.isEmpty());
     }
     */
     @Test
     public void listreportEmpty() {
         Adviser adviser = this.adviserRepository.save(new Adviser("Juan", "1"));
        long id = adviser.getId();
        
        List<DisplayResponse> res = this.adviserService.listReport(id);
        List<DisplayResponse> empty = new ArrayList<>();
        adviserRepository.deleteAll();
         assertEquals(empty, res);
     }
     
     @Test
     public void listReportNotEmpty() {
         Adviser adviser = this.adviserRepository.save(new Adviser("Juan", "1"));
        long id = adviser.getId();
        
        Category category =this.categoryRepository.save(new Category("A", 1, 3));
        
        Turn turn = this.turnRepository.save(new Turn("A1", "terminado"));
        turn.setAdviser(adviser);
        turn.setCategory(category);
        turn.setTimeTurn(LocalDateTime.now());
        turn.setCreated(LocalDateTime.now());
        turn.setUpdated(LocalDateTime.now());
        turn.setFinalTime(LocalTime.now());
        this.turnRepository.save(turn);
        
        List<DisplayResponse> res = this.adviserService.listReport(id);
        adviserRepository.deleteAll();
        turnRepository.deleteAll();
        categoryRepository.deleteAll();
         assertFalse(res.isEmpty());
     }
     
     @Test
    public void averageNull() {
        Adviser adviser = this.adviserRepository.save(new Adviser("Juan", "1"));
        long id = adviser.getId();
        
        AverageResponse res = this.adviserService.average(id);
        adviserRepository.deleteAll();
         assertNull(res);
    }
    
    @Test
    public void averageNotNull() {
        Adviser adviser = this.adviserRepository.save(new Adviser("Juan", "1"));
        long id = adviser.getId();
        
        Category category =this.categoryRepository.save(new Category("A", 1,3));
        
        Turn turn = this.turnRepository.save(new Turn("A1", "terminado"));
        turn.setAdviser(adviser);
        turn.setCategory(category);
        turn.setTimeTurn(LocalDateTime.now());
        turn.setCreated(LocalDateTime.now());
        turn.setUpdated(LocalDateTime.now());
        turn.setFinalTime(LocalTime.now());
        this.turnRepository.save(turn);
        
        AverageResponse res = this.adviserService.average(id);
        adviserRepository.deleteAll();
        turnRepository.deleteAll();
        categoryRepository.deleteAll();
         assertNotNull(res);
    }
    /*
    @Test
    public void generateListTurn() {
        Category categoryA = this.categoryRepository.save( new Category( "A", 2, 2));
        Category categoryB = this.categoryRepository.save( new Category( "B", 1, 1));
        
        Turn turnA1 = this.turnRepository.save(new Turn("A1", "listado"));
        turnA1.setCreated(LocalDateTime.now());
        turnA1.setCategory(categoryA);
        turnA1.setPriority(categoryA.getPriority());
        this.turnRepository.save(turnA1);
        
        Turn turnA2 = this.turnRepository.save(new Turn("A2", "listado"));
        turnA2.setCreated(LocalDateTime.now());
        turnA2.setCategory(categoryA);
        turnA2.setPriority(categoryA.getPriority());
        this.turnRepository.save(turnA2);
        
        Turn turnA3 = this.turnRepository.save(new Turn("A3", "listado"));
        turnA3.setCreated(LocalDateTime.now());
        turnA3.setCategory(categoryA);
        turnA3.setPriority(categoryA.getPriority());
        this.turnRepository.save(turnA3);
        
        Turn turnB1 = this.turnRepository.save(new Turn("B1", "listado"));
        turnB1.setCreated(LocalDateTime.now());
        turnB1.setCategory(categoryB);
        turnB1.setPriority(categoryA.getPriority());
        this.turnRepository.save(turnB1);
        
        Turn turnB2 = this.turnRepository.save(new Turn("B2", "listado"));
        turnB2.setCreated(LocalDateTime.now());
        turnB2.setCategory(categoryB);
        turnB2.setPriority(categoryA.getPriority());
        this.turnRepository.save(turnB2);
        
        Turn turnB3 = this.turnRepository.save(new Turn("B3", "listado"));
        turnB3.setCreated(LocalDateTime.now());
        turnB3.setCategory(categoryB);
        turnB3.setPriority(categoryA.getPriority());
        this.turnRepository.save(turnB3);
        
        List<Turn> general = Arrays.asList(turnB1, turnA1, turnA2, turnB2, turnA3, turnB3);
        
        List<Turn> res = this.adviserService.generateListTurn();
        assertNotNull(res);
        assertEquals(general, res);
    }*/
    
    /*
    @Test
    public void generateList(){
        List <Integer> general = Arrays.asList(2,2,2,2,3,3,3,4,4,2,2,2,2,3,3,3,4,4,3,3,4,4,4,4);
        
        List<Integer> res = this.adviserService.generateList(4);
        assertNotNull(res);
        assertEquals(general, res);
    }
    */
}
