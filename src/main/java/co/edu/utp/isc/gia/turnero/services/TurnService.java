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
import co.edu.utp.isc.gia.turnero.ws.dto.DisplayResponse;
import co.edu.utp.isc.gia.turnero.ws.dto.TurnResponse;
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
public class TurnService {
    
    @Autowired
    private TurnRepository turnRepository;
    
    public List<TurnResponse> listTurns(){
        List<TurnResponse> res = new ArrayList<>(); 
        List<Turn> list = turnRepository.findAll();
        
        for (Turn turn : list) {
            TurnResponse turnResponse = TurnResponse.builder()
                    .id(turn.getId())
                    .name(turn.getName())
                    .stateTurn(turn.getStateTurn())
                    .category(turn.getCategory().getId())
                    .build();   
            
            res.add(turnResponse);        
        }
        return res;
    }
    
    

    public List<DisplayResponse> displayTurns() {
        List<DisplayResponse> res = new ArrayList<>(); 
        List<Turn> listTurns = turnRepository.findByStateTurnOrderByUpdatedDesc("llamando");
        
        for (Turn turn : listTurns) {
            DisplayResponse displayResponse = DisplayResponse.builder()
                    .name(turn.getName())
                    .adviserId(turn.getAdviser().getId())
                    .updated(turn.getUpdated())
                    .build();
            
            res.add(displayResponse);
        }
        
        return res;
    }
    
    
}
