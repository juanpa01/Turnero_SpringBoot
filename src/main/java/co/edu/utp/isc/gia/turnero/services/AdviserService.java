/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.utp.isc.gia.turnero.services;

import co.edu.utp.isc.gia.turnero.model.Adviser;
import co.edu.utp.isc.gia.turnero.model.Turn;
import co.edu.utp.isc.gia.turnero.repository.AdviserRepository;
import co.edu.utp.isc.gia.turnero.repository.TurnRepository;
import co.edu.utp.isc.gia.turnero.ws.dto.DisplayResponse;
import co.edu.utp.isc.gia.turnero.ws.dto.NextTurnResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author juan
 */

@Service
public class AdviserService {
    
    @Autowired
    TurnRepository turnRepository;
    
    @Autowired
    AdviserRepository adviserRepository;
   
    public NextTurnResponse nextTurn(long adviserId){
        Adviser adviser = adviserRepository.getOne(adviserId);
        List<Turn> listTurnsWait = turnRepository.findByStateTurnOrderByCreatedAsc("listado");
        List<Turn> listTurnsCalled = turnRepository.findByStateTurnAndAdviser("llamando", adviser);
        Turn turn = null;
        
        if ((listTurnsWait.isEmpty() && listTurnsCalled.isEmpty()) || adviser == null) {
            return null;
        } 
        
        if (listTurnsWait.isEmpty() && !listTurnsCalled.isEmpty()) {
            updateStateTurn(listTurnsCalled, "llamando", adviser);
            return null;
        } 
        
        if (!listTurnsWait.isEmpty() && listTurnsCalled.isEmpty()) {
            turn = updateStateTurn(listTurnsWait, "listado", adviser);
        }
        
        if (!listTurnsWait.isEmpty() && !listTurnsCalled.isEmpty()) {
            turn = updateStateTurn(listTurnsWait, "listado", adviser);
            updateStateTurn(listTurnsCalled, "llamando", adviser);
        } 
        
        NextTurnResponse nextTurnResponse = NextTurnResponse.builder()
                .id(turn.getId())
                .name(turn.getName())
                .stateTurn(turn.getStateTurn())
                .created(turn.getCreated())
                .updated(turn.getUpdated())
                .adviser(turn.getAdviser().getId())
                .category(turn.getCategory().getId())
                .build();
        
        return nextTurnResponse;
    }

    public List<DisplayResponse> reCall(long adviserId) {
        List<DisplayResponse> res = new ArrayList<>(); 
        Adviser adviser = adviserRepository.getOne(adviserId);
        List<Turn> listTurnsCalled = turnRepository.findByStateTurnAndAdviser("llamando", adviser);
        List<Turn> listTurns = new ArrayList<>();
        Turn turnCalled = null;
        LocalDateTime updated = LocalDateTime.now();
        
        turnCalled = listTurnsCalled.remove(0);
        turnCalled.setUpdated(updated);
        turnRepository.save(turnCalled);
        
        listTurns = turnRepository.findByStateTurnOrderByUpdatedDesc("llamando");
        
        
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
    
    public List<DisplayResponse> reCallLost(long adviserId) {
        List<DisplayResponse> res = new ArrayList<>(); 
        Adviser adviser = adviserRepository.getOne(adviserId);
        List<Turn> listTurnsLost = turnRepository.findByStateTurnAndAdviserOrderByUpdatedAsc("perdido", adviser);
        List<Turn> listTurnsCalled = turnRepository.findByStateTurnAndAdviserOrderByUpdatedAsc("llamando", adviser);
        Turn turn = null;
        
        if ((listTurnsLost.isEmpty() && listTurnsCalled.isEmpty()) || adviser == null) {
            return null;
        }
        
        if (!listTurnsCalled.isEmpty() && listTurnsLost.isEmpty()) {
            return null;
        }
        
        if (listTurnsCalled.isEmpty() && !listTurnsLost.isEmpty()) {
            turn = updateStateTurn(listTurnsLost,"perdido", adviser);
        }
        
        if (!listTurnsCalled.isEmpty() && !listTurnsLost.isEmpty()) {
            turn = updateStateTurn(listTurnsLost,"perdido", adviser);
            updateStateTurn(listTurnsCalled,"llamando", adviser);
            
        }
        
        listTurnsLost = turnRepository.findByStateTurnOrderByUpdatedAsc("perdido");
        
        for (Turn turn2 : listTurnsLost) {
            DisplayResponse displayResponse = DisplayResponse.builder()
                    .name(turn2.getName())
                    .adviserId(turn2.getAdviser().getId())
                    .updated(turn2.getUpdated())
                    .build();
            
            res.add(displayResponse);
        }
        
        return res;
    }
    
    private Turn updateStateTurn(List<Turn> listTurns, String state, Adviser adviser) {
        Turn  turn = listTurns.remove(0);
         LocalDateTime updated = LocalDateTime.now();
        if ("llamando".equals(state)) {
            turn.setUpdated(updated);
            turn.setStateTurn("perdido");
        } else if ("listado".equals(state)) {
            turn.setUpdated(updated);
            turn.setStateTurn("llamando");
            turn.setAdviser(adviser);
        } else if ("perdido".equals(state)) {
            turn.setUpdated(updated);
            turn.setStateTurn("llamando");
        }
        turnRepository.save(turn);
        return turn;
    }
}
