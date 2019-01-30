/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.utp.isc.gia.turnero.ws.dto;

import co.edu.utp.isc.gia.turnero.model.Adviser;
import co.edu.utp.isc.gia.turnero.model.Category;
import java.time.LocalDateTime;
import java.util.Date;
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
public class TurnResponse {
    
    private long id;
    private String name;
    private String stateTurn;
    private LocalDateTime created;
    private long category;
    
}
