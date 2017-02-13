/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.apsdtask.repository;

import com.mycompany.apsdtask.domain.EMail;
import java.util.Date;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EMailRepository extends CrudRepository<EMail, Long>, JpaSpecificationExecutor {

    @Query("select m from EMail m where m.date>?1 and m.date<?2")
    public Stream<EMail> findByDate(Date after, Date before);

    @Query("select m from EMail m where m.date>?1 and m.date<?2 and m.author like %?3%")
    public Stream<EMail> findByDateAndAuthor(Date after, Date before, String author);

    @Query("select m from EMail m where m.date>?1 and m.date<?2 and m.subject like %?3%")
    public Stream<EMail> findByDateAndSubject(Date after, Date before, String subject);

    @Query("select m from EMail m where m.date>?1 and m.date<?2 and m.author like %?3% and m.subject like %?4%")
    public Stream<EMail> findByDateAuthorAndSubject(Date after, Date before, String author, String subject);

    public EMail findTopByOrderByDateDesc();
}
