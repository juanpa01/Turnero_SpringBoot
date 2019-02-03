/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.utp.isc.gia.turnero.ws.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author juan
 */
@Builder
@Setter 
@Getter
public class EndTurnResponse {
    private long id;
    private String name;
    private LocalDateTime timeTurn;
    private String stateTurn;
    private LocalDateTime created;
    private LocalDateTime updated;
    private LocalTime finalTime;
    private long category;
    private long adviser;
}
