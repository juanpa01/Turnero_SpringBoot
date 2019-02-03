/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.utp.isc.gia.turnero.ws.controller;

import co.edu.utp.isc.gia.turnero.services.TurnService;
import co.edu.utp.isc.gia.turnero.ws.dto.DisplayResponse;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author juan
 */

@RestController
@RequestMapping("api//display")
public class DisplayController {
    
    @Autowired
    TurnService turnService;
    
    
    /*
    @MessageMapping("/display")
    @SendTo("/topic/office")
    public DisplayResponse displayTurnAndAdviser(String name) throws Exception {
        Thread.sleep(1000); 
        return  DisplayResponse.builder()
                .name(name)
                .cubicle(" cubiculo 1")
                .build();
    }
*/
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<DisplayResponse>> displayTurns() {
        List<DisplayResponse> listDisplay = turnService.displayTurns();
        
        if (listDisplay.isEmpty()) {
            return  ResponseEntity.noContent().build();
        } else {
            return  ResponseEntity.status(HttpStatus.ACCEPTED).body(listDisplay);
        }
    }
    
}
