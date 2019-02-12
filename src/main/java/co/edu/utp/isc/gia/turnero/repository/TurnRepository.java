/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.utp.isc.gia.turnero.repository;

import co.edu.utp.isc.gia.turnero.model.Adviser;
import co.edu.utp.isc.gia.turnero.model.Category;
import co.edu.utp.isc.gia.turnero.model.Turn;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author juan
 */
@Repository
public interface TurnRepository extends JpaRepository<Turn, Long> {
    
    //@Query("FROM Turn t WHERE t.stateTurn = :state ORDER BY t.created ASC")
    //List<Turn> findAllByState(@Param("state") String satetTurn);

    public List<Turn> findByStateTurnOrderByCreatedAsc(String stateTurn);

    public List<Turn> findByStateTurnAndAdviser(String stateTurn, Adviser adviser);

    public List<Turn> findByStateTurnOrderByUpdatedAsc(String stateTurn);

    public List<Turn> findByStateTurnOrderByUpdatedDesc(String stateTurn);

    public List<Turn> findByStateTurnAndAdviserOrderByUpdatedDesc(String llamando, Adviser adviser);

    public List<Turn> findByStateTurnAndAdviserOrderByUpdatedAsc(String llamando, Adviser adviser);

    public List<Turn> findByStateTurnAndCategoryOrderByCreatedDesc(String listado, Category category);

    public List<Turn> findByStateTurnAndCategory(String listado, Category category);

    public List<Turn> findByStateTurnAndPriorityOrderByCreatedDesc(String listado, long priority);

    public List<Turn> findByStateTurnAndPriorityOrderByCreatedAsc(String listado, long priority);

    public List<Turn> findByStateTurnAndCategoryOrderByCreatedAsc(String listado, Category category2);

}
