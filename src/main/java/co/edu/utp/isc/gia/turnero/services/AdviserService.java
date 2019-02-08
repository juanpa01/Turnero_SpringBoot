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
import co.edu.utp.isc.gia.turnero.ws.dto.AverageResponse;
import co.edu.utp.isc.gia.turnero.ws.dto.DisplayResponse;
import co.edu.utp.isc.gia.turnero.ws.dto.EndTurnResponse;
import co.edu.utp.isc.gia.turnero.ws.dto.NextTurnResponse;
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
    
    public List<Integer> generateList(long categorySize) {
       Map<String, Tuple> categories = new TreeMap<>(); 
        String name = "name_";
        long cont = 0;
        int element = 0;
        Tuple value;
        List<Integer> general = new ArrayList<>();
        
        /*for (long i = 1; i <= categorySize; i++) {
            String name2 = name+i;
            categories.put(name2, new Tuple(0,Arrays.asList(i,i,i,i,i,i,i,i,i,i)));
        }*/
        categories.put("name_1", new Tuple(0, new LinkedList<>(Arrays.asList(1,1,1,1,1,1,1,1))));
        categories.put("name_2", new Tuple(4, new LinkedList<>(Arrays.asList(2,2,2,2,2,2,2,2))));
        categories.put("name_3", new Tuple(3, new LinkedList<>(Arrays.asList(3,3,3,3,3,3,3,3))));
        categories.put("name_4", new Tuple(2, new LinkedList<>(Arrays.asList(4,4,4,4,4,4,4,4))));
        
  
        Iterator<Map.Entry<String, Tuple>> it = categories.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Tuple> pair = it.next();
            value = pair.getValue();
            if(value.getTam() == 0) {
                value.setLista(new ArrayList<>());
                pair.setValue(value);
            }
        }

        while(true) {
            cont = 0;
           Iterator<Map.Entry<String, Tuple>> its = categories.entrySet().iterator();
            while (its.hasNext()) {
                Map.Entry<String, Tuple> pair = its.next();
                value = pair.getValue();
                if (value.getLista().isEmpty()) {
                    cont += 1;
                }
            }
            
            if (cont == categories.size()) {
                break;
            }
            
            Iterator<Map.Entry<String, Tuple>> itss = categories.entrySet().iterator();
            while (itss.hasNext()) {
                Map.Entry<String, Tuple> pair = itss.next();
                value = pair.getValue();
                if (value.getTam() < value.getLista().size()) {    
                    for (int i = 0; i < value.getTam(); i++) {
                        element = value.getLista().get(0);
                        value.getLista().remove(0);
                        general.add(element);
                    }
                } else {
                    while(!value.getLista().isEmpty()) {
                        element = value.getLista().get(0);
                        value.getLista().remove(0);
                        general.add(element);
                    }
                    
                }
            }
        }
        
        

        return general;
    }
    
}
