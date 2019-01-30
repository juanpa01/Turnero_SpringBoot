/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.utp.isc.gia.turnero.ws.dto;

import co.edu.utp.isc.gia.turnero.model.Turn;
import java.io.Serializable;
import java.util.List;
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
public class CategoryResponse implements Serializable {
    private long id;
    private String name;
    private String priority;
    private int cont;
    private int turns = 0;
}
