package it.crescenziandrea.codicefiscale;

import java.util.ArrayList;

public class CFgenerator {
    private String name;
    private String surname;
    private int day;
    private int month;
    private int year;
    private String sex;
    private String birthplace;

    public CFgenerator(String surname, String name, int day, int month, int year, String sex, String birthplace) {
        this.name = name;
        this.surname = surname;
        this.day = day;
        this.month = month;
        this.year = year;
        this.sex = sex;
        this.birthplace = birthplace;
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


    public  String Calculate() {
        String SurnameCode = this.ConsonantCalculator(this.surname);
        String NameCode = this.ConsonantCalculator(this.name);
        String BirthSex = this.BirthSexYearCalculator(this.year, this.month, this.day,this.sex);

        String result = SurnameCode + NameCode + BirthSex;

        return result;
    }

    //TODO: definire metodi per calcolare codici relativi alle regioni
    //TODO: metodo codice comune e carattere di controllo

    private String ConsonantCalculator(String string) {
        int i;
        String consonant = new String();
        String result = new String();
        for(i=0; i<=string.length()-1; i++) {
            char character = string.charAt(i);
            if (character != 'a' && character != 'e' && character != 'i' && character != 'o' && character != 'u' &&
                    character != 'A' && character != 'E' && character != 'I' && character != 'O' && character != 'U') {
                consonant += Character.toString(character);
            }
        }

            if(consonant.length() > 3) {
                for(i=0; i < consonant.length();i++){
                    char cons = consonant.charAt(i);
                    if(i != 1) {
                        result += Character.toString(cons);
                    }
                    if(result.length() <= 3) continue;
                    else break;
                }
            }
            else result = consonant;

            if(consonant.length() < 3) {
                for (i = 0; i <= string.length() - 1; i++) {
                    char character = string.charAt(i);
                    if (character == 'a' || character == 'e' || character == 'i' || character == 'o' || character == 'u' &&
                            character == 'A' || character == 'E' || character == 'I' || character == 'O' || character == 'U') {
                        consonant += Character.toString(character);
                        if(consonant.length() == 3) break;
                    }
                }
                if(consonant.length() < 3) consonant += "x";
                result = consonant;
            }
        return result;
    }

    private String BirthSexYearCalculator(int year, int month, int day, String sex){
        String BirthSexYearCode;
        String YearCode;
        String MonthCode;
        String DaySexCode;

        YearCode = YearCalculator(year);
        MonthCode = MonthCalculator(month);
        DaySexCode = DaySexCalculator(day, sex);

        BirthSexYearCode = YearCode + MonthCode + DaySexCode;

        return BirthSexYearCode;
    }

    private String YearCalculator(int year) {
        return Integer.toString(year).substring(2);
    }

    private String MonthCalculator(int month) {
        String result;
        switch (month) {
            case 1:
                result = "A";
                break;
            case 2:
                result = "B";
                break;
            case 3:
                result = "C";
                break;
            case 4:
                result = "D";
                break;
            case 5:
                result = "E";
                break;
            case 6:
                result = "H";
                break;
            case 7:
                result = "L";
                break;
            case 8:
                result = "M";
                break;
            case 9:
                result = "P";
                break;
            case 10:
                result = "R";
                break;
            case 11:
                result = "S";
                break;
            case 12:
                result = "T";
                break;
            default:
                result = " ";
                break;
        }
        return result;
    }

    private String DaySexCalculator(int day, String sex) {
        String dayCode = String.format("%02d", day);

        if(sex.equals("F")) {
            int fullDayCode;
            fullDayCode = Integer.parseInt(dayCode);
            fullDayCode += 40;
            dayCode = Integer.toString(fullDayCode);
        }
        return dayCode;
    }
}

