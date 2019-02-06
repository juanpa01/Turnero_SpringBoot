/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.utp.isc.gia.turnero.ws.controller;

import co.edu.utp.isc.gia.turnero.model.Adviser;
import co.edu.utp.isc.gia.turnero.repository.AdviserRepository;
import co.edu.utp.isc.gia.turnero.services.AdviserService;
import co.edu.utp.isc.gia.turnero.ws.dto.DisplayResponse;
import co.edu.utp.isc.gia.turnero.ws.dto.EndTurnResponse;
import co.edu.utp.isc.gia.turnero.ws.dto.AverageResponse;
import co.edu.utp.isc.gia.turnero.ws.dto.NextTurnResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author juan
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AdviserControllerTest {
    
    @Mock
    private AdviserService adviserService;
    
    @InjectMocks
    private AdviserController adviserController;
    
    @Autowired
    private AdviserRepository adviserRepository;
    
    @Test
    public void testNextTurnAccept() {
        this.adviserRepository.save(new Adviser("Juan", "1"));
        Adviser adviser = this.adviserRepository.findAll().get(0);
        long id = adviser.getId();
        given(adviserService.nextTurn(id)).willReturn( NextTurnResponse.builder()
                                                       .name("A1")
                                                       .stateTurn("listado")
                                                       .timeTurn(LocalDateTime.now())
                                                       .created(LocalDateTime.now())
                                                       .build()
                                                        );
        ResponseEntity<NextTurnResponse> turn = this.adviserController.nextTurn(id);
        
        assertEquals(turn.getStatusCode(), HttpStatus.ACCEPTED);
    }
    
    @Test
    public void testNextTurnNoFound() {
    this.adviserRepository.save(new Adviser("Juan", "1"));
        Adviser adviser = this.adviserRepository.findAll().get(0);
        long id = adviser.getId();
        given(adviserService.nextTurn(id)).willReturn(null);
        ResponseEntity<NextTurnResponse> turn = this.adviserController.nextTurn(id);
        
        assertEquals(turn.getStatusCode(), HttpStatus.NOT_FOUND);
    }
    
    @Test
    public void testReCallAceept() {
        this.adviserRepository.save(new Adviser("Juan", "1"));
        Adviser adviser = this.adviserRepository.findAll().get(0);
        long id = adviser.getId();
        given(adviserService.reCall(id)).willReturn( DisplayResponse.builder()
                                                    .name("A1")
                                                    .adviserId(id)
                                                    .build()
                                                    );
        ResponseEntity<DisplayResponse> turn = this.adviserController.reCall(id);
        
        assertEquals(turn.getStatusCode(), HttpStatus.ACCEPTED);
    }
    
    @Test
    public void testReCallNoContent() {
        this.adviserRepository.save(new Adviser("Juan", "1"));
        Adviser adviser = this.adviserRepository.findAll().get(0);
        long id = adviser.getId();
        given(adviserService.reCall(id)).willReturn( null );
        ResponseEntity<DisplayResponse> turn = this.adviserController.reCall(id);
        
        assertEquals(turn.getStatusCode(), HttpStatus.NO_CONTENT);
    }
    /*
    @Test
    public void testReCallLostAceept() {
        this.adviserRepository.save(new Adviser("Juan", "1"));
        Adviser adviser = this.adviserRepository.findAll().get(0);
        long id = adviser.getId();
        given(adviserService.reCallLost(id)).willReturn( DisplayResponse.builder()
                                                    .name("A1")
                                                    .adviserId(id)
                                                    .build()
                                                    );
        ResponseEntity<DisplayResponse> turn = this.adviserController.reCallLost(id);
        
        assertEquals(turn.getStatusCode(), HttpStatus.ACCEPTED);
    }
    
    @Test
    public void testReCallLostNoContent() {
        this.adviserRepository.save(new Adviser("Juan", "1"));
        Adviser adviser = this.adviserRepository.findAll().get(0);
        long id = adviser.getId();
        given(adviserService.reCallLost(id)).willReturn( null );
        ResponseEntity<DisplayResponse> turn = this.adviserController.reCallLost(id);
        
        assertEquals(turn.getStatusCode(), HttpStatus.NO_CONTENT);
    }
    */
    
    @Test
    public void testEndTurnAceept() {
        this.adviserRepository.save(new Adviser("Juan", "1"));
        Adviser adviser = this.adviserRepository.findAll().get(0);
        long id = adviser.getId();
        given(adviserService.endTurn(id)).willReturn( EndTurnResponse.builder()
                                                       .name("A1")
                                                       .stateTurn("listado")
                                                       .timeTurn(LocalDateTime.now())
                                                       .created(LocalDateTime.now())
                                                       .build()
                                                    );
        ResponseEntity<EndTurnResponse> turn = this.adviserController.endTurn(id);
        
        assertEquals(turn.getStatusCode(), HttpStatus.ACCEPTED);
    }
    
    @Test
    public void testEndTurnNoContent() {
        this.adviserRepository.save(new Adviser("Juan", "1"));
        Adviser adviser = this.adviserRepository.findAll().get(0);
        long id = adviser.getId();
        given(adviserService.endTurn(id)).willReturn( null );
        ResponseEntity<EndTurnResponse> turn = this.adviserController.endTurn(id);
        
        assertEquals(turn.getStatusCode(), HttpStatus.NOT_FOUND);
    }
    /*
    @Test
    public void testLostAceept() {
        this.adviserRepository.save(new Adviser("Juan", "1"));
        Adviser adviser = this.adviserRepository.findAll().get(0);
        long id = adviser.getId();
        given(adviserService.lost(id)).willReturn( Arrays.asList(
                DisplayResponse.builder()
                .name("A1")
                .adviserId(id)
                .build()
        ));
        
        ResponseEntity<List<DisplayResponse>> list = this.adviserController.lost(id);
        
        assertEquals(list.getStatusCode(), HttpStatus.ACCEPTED);
    }
    
    @Test
    public void testLostNoContent() {
        this.adviserRepository.save(new Adviser("Juan", "1"));
        Adviser adviser = this.adviserRepository.findAll().get(0);
        long id = adviser.getId();
        given(adviserService.lost(id)).willReturn( new ArrayList<>());
        
        ResponseEntity<List<DisplayResponse>> list = this.adviserController.lost(id);
        
        assertEquals(list.getStatusCode(), HttpStatus.NO_CONTENT);
    }
    */
    @Test
    public void testListReportAceept() {
        this.adviserRepository.save(new Adviser("Juan", "1"));
        Adviser adviser = this.adviserRepository.findAll().get(0);
        long id = adviser.getId();
        given(adviserService.listReport(id)).willReturn( Arrays.asList(
                DisplayResponse.builder()
                .name("A1")
                .adviserId(id)
                .build()
        ));
        
        ResponseEntity<List<DisplayResponse>> list = this.adviserController.listReport(id);
        
        assertEquals(list.getStatusCode(), HttpStatus.ACCEPTED);
    }
    
    @Test
    public void testListReportNoContent() {
        this.adviserRepository.save(new Adviser("Juan", "1"));
        Adviser adviser = this.adviserRepository.findAll().get(0);
        long id = adviser.getId();
        given(adviserService.listReport(id)).willReturn( new ArrayList<>());
        
        ResponseEntity<List<DisplayResponse>> list = this.adviserController.listReport(id);
        
        assertEquals(list.getStatusCode(), HttpStatus.NO_CONTENT);
    }
    
    @Test
    public void averageAceept() {
        this.adviserRepository.save(new Adviser("Juan", "1"));
        Adviser adviser = this.adviserRepository.findAll().get(0);
        long id = adviser.getId();
        given(adviserService.average(id)).willReturn( AverageResponse.builder()
                                                       .minutes(43)
                                                       .build()
                                                    );
        ResponseEntity<AverageResponse> turn = this.adviserController.average(id);
        
        assertEquals(turn.getStatusCode(), HttpStatus.ACCEPTED);
    }
    
    @Test
    public void averageNoContent() {
        this.adviserRepository.save(new Adviser("Juan", "1"));
        Adviser adviser = this.adviserRepository.findAll().get(0);
        long id = adviser.getId();
        given(adviserService.average(id)).willReturn( null );
        ResponseEntity<AverageResponse> turn = this.adviserController.average(id);
        
        assertEquals(turn.getStatusCode(), HttpStatus.NO_CONTENT);
    }
    
    
}
