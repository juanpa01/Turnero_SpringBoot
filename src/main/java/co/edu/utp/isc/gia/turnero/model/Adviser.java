/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.utp.isc.gia.turnero.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 *
 * @author juan
 */
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name="Adviser")
public class Adviser implements Serializable {
        
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    
    @NonNull
    private String name;
    
    @NonNull
    private String cubicle;
    
    @OneToMany(mappedBy="adviser", cascade = CascadeType.ALL)
    private List<Turn> Turns;
    
}
