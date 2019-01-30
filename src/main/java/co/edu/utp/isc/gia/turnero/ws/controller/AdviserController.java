/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.utp.isc.gia.turnero.ws.controller;

import co.edu.utp.isc.gia.turnero.services.AdviserService;
import co.edu.utp.isc.gia.turnero.services.TurnService;
import co.edu.utp.isc.gia.turnero.ws.dto.DisplayResponse;
import co.edu.utp.isc.gia.turnero.ws.dto.NextTurnResponse;
import co.edu.utp.isc.gia.turnero.ws.dto.TurnResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author juan
 */
@RestController
@RequestMapping("advisers")
public class AdviserController {
    
    @Autowired
    AdviserService  adviserService;
    
    
    @GetMapping("/{adviserId}/nextTurn")
    @ResponseBody
    public ResponseEntity<NextTurnResponse>  nextTurn(@PathVariable("adviserId") long adviserId) {
        NextTurnResponse turn = adviserService.nextTurn(adviserId);
        
        if (turn == null) {
            return  ResponseEntity.noContent().build();
        } else {
            return  ResponseEntity.status(HttpStatus.ACCEPTED).body(turn);
        }
    }
    
    @GetMapping("/{adviserId}/reCall")
    @ResponseBody
    public ResponseEntity<List<DisplayResponse>> reCall(@PathVariable("adviserId") long adviserId) {
        List<DisplayResponse> listDisplay = adviserService.reCall(adviserId);
        if (listDisplay.isEmpty()) {
            return  ResponseEntity.noContent().build();
        } else {
            return  ResponseEntity.status(HttpStatus.ACCEPTED).body(listDisplay);
        }
    }
    
    @GetMapping("/{adviserId}/reCallLost")
    @ResponseBody
    public ResponseEntity<List<DisplayResponse>> reCallLost(@PathVariable("adviserId") long adviserId) {
        List<DisplayResponse> display = adviserService.reCallLost(adviserId);
        if (display == null) {
            return  ResponseEntity.noContent().build();
        } else {
            return  ResponseEntity.status(HttpStatus.ACCEPTED).body(display);
        }
    }
    
    @GetMapping("/{adviserId}/endTurn")
    @ResponseBody
    public ResponseEntity<NextTurnResponse> endTurn(@PathVariable("adviserId") long adviserId) {
        NextTurnResponse endTurn = adviserService.endTurn(adviserId);
        if (endTurn == null) {
            return  ResponseEntity.noContent().build();
        } else {
            return  ResponseEntity.status(HttpStatus.ACCEPTED).body(endTurn);
        }
    }
 
}
