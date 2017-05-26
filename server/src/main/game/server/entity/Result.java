/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.entity;

/**
 *
 * @author TRANQUANGTRUNG
 */
public class Result {
    private String message;
    private int index;
    
    public Result() {
        this.message = "";
        this.index = 0;
    }
    
    public Result(String message, int index) {
        this.message = message;
        this.index = index;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public int getIndex() {
        return this.index;
    }
}
