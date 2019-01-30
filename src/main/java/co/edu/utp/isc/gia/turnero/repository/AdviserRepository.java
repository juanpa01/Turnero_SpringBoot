/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.utp.isc.gia.turnero.repository;

import co.edu.utp.isc.gia.turnero.model.Adviser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

/**
 *
 * @author juan
 */
@RepositoryRestResource(path="advisers")
@Repository
public interface AdviserRepository extends JpaRepository<Adviser, Long>{
    
}
