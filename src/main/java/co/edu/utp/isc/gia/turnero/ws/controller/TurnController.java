/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.utp.isc.gia.turnero.ws.controller;

import co.edu.utp.isc.gia.turnero.model.Turn;
import co.edu.utp.isc.gia.turnero.services.TurnService;
import co.edu.utp.isc.gia.turnero.ws.dto.TurnResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@RestController
@RequestMapping("api/turns")
public class TurnController {
    
    @Autowired
    private TurnService turnService;
    
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<TurnResponse>> listTurns(){
        
        List<TurnResponse> turns = turnService.listTurns();
        
        if (turns.isEmpty()) {
            return  ResponseEntity.noContent().build();
        } else {
            return  ResponseEntity.status(HttpStatus.ACCEPTED).body(turns);
        }
    }
    
   
    
    
    
}
