package com.ps.sr.model

/**
 * 
 * Author: cnbuckley
 * Date: 11/16/11
 * Time: 9:31 AM
 */
public enum ResponseStatus {

    WAITING('WAITING'),
    RECIEVED_CORRECT('REC_COR'),
    RECEIVED_INCORRECT('REC_INCOR')

    String name

    ResponseStatus(String name){
        this.name = name
    }

}