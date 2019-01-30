    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.utp.isc.gia.turnero.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Null;
import jdk.internal.jline.internal.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

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
@Table(name="Turn")
public class Turn implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    
    @NonNull
    private String name;
    
    @CreationTimestamp
    private LocalDateTime created;
    
    @Nullable
    private LocalDateTime updated;
    
    @Nullable
    private LocalDateTime timeTurn;
    
    @NonNull
    private String stateTurn;
    
    //Relacion Asesor
    @ManyToOne(optional = true)
    @JoinColumn(name = "adviser_id")
    private Adviser adviser;
    
    //Relacion Categoria
    @ManyToOne
    @JoinColumn(name = "category_id") 
    private Category category;
        
}
