package com.click.rollingstone.click2;

/**
 * Created by Aniruddha_Saundattik on 9/19/2015.
 */
public class Mail {
    Integer _id;
    String _filename;

    public Mail(){}

    public Mail(String _filename){
        this._filename = _filename;
    }

    public Mail(Integer _id, String _filename){
        this._id = _id;
        this._filename = _filename;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public String get_filename() {
        return _filename;
    }

    public void set_filename(String _filename) {
        this._filename = _filename;
    }
}
