package it.crescenziandrea.codicefiscale;

//TODO: importare database

public class CFgenerator {
    private String name;
    private String surname;
    private int day;
    private int month;
    private int year;
    private String sex;
    private String birthplace;
    private FiscalCode crescenziandrea;

    public CFgenerator(String name, String surname, int day, int month, int year, String sex, String birthplace, FiscalCode crescenziandrea) {
        this.name = name;
        this.surname = surname;
        this.day = day;
        this.month = month;
        this.sex = sex;
        this.birthplace = birthplace;
        this.crescenziandrea;
    }

    //creating methods get/set for all the variables
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }
    public void setMonth(int month){
        this.month = month;
    }

    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }

    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }


    public  String calculate() {
        //TODO: metodo che avviera gli script per estrapolare consonanti e calcolare i codici
    }
    //TODO: definire metodo per il calcolo delle consonanti del nome/cognome
    //TODO: definire metodi per calcolare codici relativi alle regioni
    //TODO: metodo per calcolare codice mese e anno
    //TODO: metodo per giorno e sesso
    //TODO: metodo codice comune e carattere di controllo


    //TODO metodo che prende prime 3 consonanti cognome
}

