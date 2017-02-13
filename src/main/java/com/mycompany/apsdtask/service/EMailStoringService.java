/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.apsdtask.service;

import com.mycompany.apsdtask.domain.EMail;

/**
 *
 * @author Andrii_Kozak1
 */
public interface EMailStoringService {

    EMail findById(Long id);

    <S extends EMail> Iterable<S> save(Iterable<S> itrbl);
    
}
