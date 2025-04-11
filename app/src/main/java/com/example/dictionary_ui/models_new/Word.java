package com.example.dictionary_ui.models_new;

import java.util.List;

public class Word {
    private String _id;
    private String word;
    private String phonetic;
    private List<Phonetics> phonetics;
    private List<Meaning> meanings;
    private License license;
    private List<String> sourceUrls;
    private String created_at;
    private String updated_at;
    private Integer search_count;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public List<Phonetics> getPhonetics() {
        return phonetics;
    }

    public void setPhonetics(List<Phonetics> phonetics) {
        this.phonetics = phonetics;
    }

    public List<Meaning> getMeanings() {
        return meanings;
    }

    public void setMeanings(List<Meaning> meanings) {
        this.meanings = meanings;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }
    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public Integer getSearch_count() {
        return search_count;
    }

    public void setSearch_count(Integer search_count) {
        this.search_count = search_count;
    }

    public String getCategory() {
        return "";
    }

    public String getTitle() {
        return word;
    }

    public String getGrammar() {
        return meanings.get(0).getPartOfSpeech();
    }

    public String getInfl() {
        return "";
    }

    public String getInfo() {
        return "";
    }

    public Phonetics getPronounce() {
        return phonetics.get(0);
    }

    public List<Meaning> getContents() {
        return meanings;
    }
}
