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
import co.edu.utp.isc.gia.turnero.ws.dto.AverageResponse;
import co.edu.utp.isc.gia.turnero.ws.dto.DisplayResponse;
import co.edu.utp.isc.gia.turnero.ws.dto.EndTurnResponse;
import co.edu.utp.isc.gia.turnero.ws.dto.NextTurnResponse;
import co.edu.utp.isc.gia.turnero.ws.dto.TurnResponse;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
    
    @Autowired
    CategoryRepository categoryRepository;
    
    List<Turn> orderTurns = new ArrayList<>(); 
   
    public NextTurnResponse nextTurn(long adviserId){
        Adviser adviser = adviserRepository.getOne(adviserId);
        List<Turn> listTurnsWait = turnRepository.findByStateTurnOrderByCreatedAsc("listado");
        List<Turn> listTurnsCalled = turnRepository.findByStateTurnAndAdviser("llamando", adviser);
        Turn turn = null;
        
        if ((listTurnsWait.isEmpty() && listTurnsCalled.isEmpty() && orderTurns.isEmpty()) || adviser == null) {
            return null;
        } 
        
        if (orderTurns.isEmpty() && !listTurnsWait.isEmpty()) {
            orderTurns =  this.generateListTurn();
        }
        
        if (orderTurns.isEmpty() && !listTurnsCalled.isEmpty()) {
            updateStateTurn(listTurnsCalled, "llamando", adviser);
            return null;
        } 
        
        if (!orderTurns.isEmpty() && listTurnsCalled.isEmpty()) {
            turn = updateStateTurn(orderTurns, "listado", adviser);
        }
        
        if (!orderTurns.isEmpty() && !listTurnsCalled.isEmpty()) {
            turn = updateStateTurn(orderTurns, "listado", adviser);
            updateStateTurn(listTurnsCalled, "llamando", adviser);
        } 
        
        NextTurnResponse nextTurnResponse = NextTurnResponse.builder()
                .id(turn.getId())
                .name(turn.getName())
                .stateTurn(turn.getStateTurn())
                .timeTurn(turn.getTimeTurn())
                .created(turn.getCreated())
                .updated(turn.getUpdated())
                .adviser(turn.getAdviser().getId())
                .category(turn.getCategory().getId())
                .build();
        
        return nextTurnResponse;
    }

    public DisplayResponse reCall(long adviserId) {
        Adviser adviser = adviserRepository.getOne(adviserId);
        List<Turn> listTurnsCalled = turnRepository.findByStateTurnAndAdviser("llamando", adviser);
        Turn turnCalled = null;
        LocalDateTime updated = LocalDateTime.now();
        
        if (listTurnsCalled.isEmpty()) {
            return null;
        }
        
        
        
        turnCalled = listTurnsCalled.remove(0);
        turnCalled.setUpdated(updated);
        turnRepository.save(turnCalled);
        
            DisplayResponse displayResponse = DisplayResponse.builder()
                    .name(turnCalled.getName())
                    .adviserId(turnCalled.getAdviser().getId())
                    .updated(turnCalled.getUpdated())
                    .build();

        return displayResponse;
    }
    
    public DisplayResponse reCallLost(long adviserId, long turnId) {
       try {
            Adviser adviser = adviserRepository.getOne(adviserId);
            Turn lostTurn = turnRepository.getOne(turnId);
            List<Turn> listTurnsLost = new ArrayList<>(); 
            List<Turn> listTurnsCalled = turnRepository.findByStateTurnAndAdviserOrderByUpdatedAsc("llamando", adviser);
            Turn turn = null;
            
            if ("perdido".equals(lostTurn.getStateTurn())) {
                listTurnsLost.add(lostTurn);
           }
            
            if ((listTurnsLost.isEmpty() && listTurnsCalled.isEmpty()) ) {
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


            DisplayResponse displayResponse = DisplayResponse.builder()
                        .name(turn.getName())
                        .adviserId(turn.getAdviser().getId())
                        .updated(turn.getUpdated())
                        .build();

            return displayResponse;
       } catch (Exception e) {
           return null;
       }
    }
    
    public EndTurnResponse endTurn(long adviserId) {
        Adviser adviser = adviserRepository.getOne(adviserId);
        List<Turn> listTurnsCalled = turnRepository.findByStateTurnAndAdviser("llamando", adviser);
        Turn turn = null;
        
        if (adviser == null || listTurnsCalled.isEmpty()) {
            return null;
        }
        
        turn = listTurnsCalled.remove(0);
        
        turn = setEndTurn(turn);
       
        turnRepository.save(turn);
        
        EndTurnResponse endTurnResponse = EndTurnResponse.builder()
                .id(turn.getId())
                .name(turn.getName())
                .stateTurn(turn.getStateTurn())
                .finalTime(turn.getFinalTime())
                .created(turn.getCreated())
                .updated(turn.getUpdated())
                .adviser(turn.getAdviser().getId())
                .category(turn.getCategory().getId())
                .build();
        
        return endTurnResponse;
        
    }
    
    
    
    public List<DisplayResponse> lost() {
        List<DisplayResponse> res = new ArrayList<>(); 
        List<Turn> listTurnsLost = turnRepository.findByStateTurnOrderByUpdatedAsc("perdido");
        
        if (listTurnsLost.isEmpty()) {
            return res;
        }
        
        for (Turn turn : listTurnsLost) {
            DisplayResponse displayResponse = DisplayResponse.builder()
                    .name(turn.getName())
                    .turnId(turn.getId())
                    .updated(turn.getUpdated())
                    .build();
            
            res.add(displayResponse);
        }
        return res;
    }
    
    public List<DisplayResponse> listReport(long adviserId) {
        Adviser adviser = adviserRepository.getOne(adviserId);
        List<DisplayResponse> res = new ArrayList<>(); 
        List<Turn> listReport = turnRepository.findByStateTurnAndAdviserOrderByUpdatedAsc("terminado", adviser);
        
        if (listReport.isEmpty()) {
            return res;
        }
        
        for (Turn turn : listReport) {
            DisplayResponse displayResponse = DisplayResponse.builder()
                    .name(turn.getName())
                    .adviserId(turn.getAdviser().getId())
                    .minutes((long) turn.getFinalTime().getMinute())
                    .updated(turn.getUpdated())
                    .build();
            
            res.add(displayResponse);
        }
        return res;
    }
    
    public AverageResponse average(long adviserId) {
        Adviser adviser = adviserRepository.getOne(adviserId);
        List<Turn> listReport = turnRepository.findByStateTurnAndAdviserOrderByUpdatedAsc("terminado", adviser);
        int minutes = 0;
               
        if (listReport.isEmpty()) {
            return null;
        }
        int tam = listReport.size();
        
        
        for(Turn turn : listReport) {
            minutes += turn.getFinalTime().getMinute();
        }

        double finalMinutes = minutes / tam;
        
        AverageResponse averageResponse = AverageResponse.builder()
                .minutes((int)(long)Math.floor(finalMinutes))
                .build();
        
        return averageResponse;
        
    }
    
    
    private Turn updateStateTurn(List<Turn> listTurns, String state, Adviser adviser) {
        Turn  turn = listTurns.remove(0);
         LocalDateTime updated = LocalDateTime.now();
        if ("llamando".equals(state)) {
            turn.setUpdated(updated);
            turn.setAdviser(null);
            turn.setStateTurn("perdido");
        } else if ("listado".equals(state)) {
            turn.setTimeTurn(updated);
            turn.setUpdated(updated);
            turn.setStateTurn("llamando");
            turn.setAdviser(adviser);
        } else if ("perdido".equals(state)) {
            turn.setTimeTurn(updated);
            turn.setAdviser(adviser);
            turn.setUpdated(updated);
            turn.setStateTurn("llamando");
        }
        turnRepository.save(turn);
        return turn;
    }
    
    private Turn setEndTurn(Turn turn) {
        LocalDateTime updated = LocalDateTime.now();
        LocalTime localTimeTurn = turn.getTimeTurn().toLocalTime();
        LocalTime localTimeNow = LocalTime.now();
        LocalTime localTimeTurnUpdated;
        long hours;
        long minutes;
        
        hours = ChronoUnit.HOURS.between(localTimeTurn, localTimeNow);
        minutes = ChronoUnit.MINUTES.between(localTimeTurn, localTimeNow);
        localTimeTurnUpdated = LocalTime.of((int) hours, (int) minutes);
        
        turn.setStateTurn("terminado");
        turn.setFinalTime(localTimeTurnUpdated);
        turn.setUpdated(updated);
        
        return turn;
    }
    
    private List<Turn> generateListTurn() {
        Map<Long, Tuple> categories = new TreeMap<>();
        long cont = 0;
        Tuple value;
        Turn temporal;
        List<Turn> general;
        List<Turn> res = new ArrayList<>();
        List<Category> listCategory = this.categoryRepository.findAll();
        
        for (Category category: listCategory) {
            general = this.turnRepository.findByStateTurnAndCategory("listado", category);
            categories.put(category.getPriority(), new Tuple(category.getRestriction(),new LinkedList<>(general)));
        }
        
        while(true) {
            cont = 0;
           Iterator<Map.Entry<Long, Tuple>> its = categories.entrySet().iterator();
            while (its.hasNext()) {
                Map.Entry<Long, Tuple> pair = its.next();
                value = pair.getValue();
                if (value.getTurns().isEmpty()) {
                    cont += 1;
                }
            }
            
            if (cont == categories.size()) {
                break;
            }
            
            Iterator<Map.Entry<Long, Tuple>> itss = categories.entrySet().iterator();
            while (itss.hasNext()) {
                Map.Entry<Long, Tuple> pair = itss.next();
                value = pair.getValue();
                if (value.getRestriction() < value.getTurns().size()) {    
                    for (int i = 0; i < value.getRestriction(); i++) {
                        temporal = value.getTurns().get(0);
                        value.getTurns().remove(0);
                        res.add(temporal);
                    }
                } else {
                    while(!value.getTurns().isEmpty()) {
                        temporal = value.getTurns().get(0);
                        value.getTurns().remove(0);
                        res.add(temporal);
                    }                 
                }
            }
        }
        return res;
    }
    
}
