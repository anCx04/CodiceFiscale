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

    //Public method to start the calculation e get the result
    public  String Calculate() {
        String SurnameCode = this.ConsonantCalculatorSurname(this.surname);
        String NameCode = this.ConsonantCalculatorName(this.name);
        String BirthSex = this.BirthSexYearCalculator(this.year, this.month, this.day,this.sex);

        String birthplace = this.birthplace;
        String result = SurnameCode + NameCode + BirthSex + birthplace; //first result that will be sended to the control character calculator

        String ControlCharacter = this.ControlCharacterCalculator(result);  //calculate the control character
        result += ControlCharacter; //final result

        return result;
    }

    // Method to calculate the consonant of the given name according to the standard algorithm
    private String ConsonantCalculatorName(String string) {
        int i;
        String consonant = new String();
        String result = new String();
        for(i=0; i<=string.length()-1; i++) { //making new string full of consonant
            char character = string.charAt(i);
            if (character != 'a' && character != 'e' && character != 'i' && character != 'o' && character != 'u' &&
                    character != 'A' && character != 'E' && character != 'I' && character != 'O' && character != 'U') {
                consonant += Character.toString(character);
            }
        }

        //checking if there's more of 3 consonant in the consonant's string
            if(consonant.length() > 3) {
                for(i=0; i < consonant.length();i++){
                    char cons = consonant.charAt(i);
                    if(i != 1) { //if is true will be inserted in the result the first, third and fourth consonant
                        result += Character.toString(cons);
                    }
                    if(result.length() < 3) continue;
                    else break;
                }
            }
            else result = consonant; //if there's exactly 3 consonant in the consonant's string will be directly returned

            if(consonant.length() < 3) { //if there's not enough consonant will be added the first vocal in the given string
                for (i = 0; i <= string.length() - 1; i++) {
                    char character = string.charAt(i);
                    if (character == 'a' || character == 'e' || character == 'i' || character == 'o' || character == 'u' &&
                            character == 'A' || character == 'E' || character == 'I' || character == 'O' || character == 'U') {
                        consonant += Character.toString(character);
                        if(consonant.length() == 3) break;
                    }
                }
                if(consonant.length() < 3) consonant += "X"; //if i haven't enough consonant or vocals will be added a X
                result = consonant;
            }
        return result;
    }



    private String ConsonantCalculatorSurname(String string) { //same as ConsonantCalculatorName
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

        if(consonant.length() < 3) {
            for (i = 0; i <= string.length() - 1; i++) {
                char character = string.charAt(i);
                if (character == 'a' || character == 'e' || character == 'i' || character == 'o' || character == 'u' ||
                        character == 'A' || character == 'E' || character == 'I' || character == 'O' || character == 'U') {
                    consonant += Character.toString(character);
                    if(consonant.length() == 3) break;
                }
            }
            if(consonant.length() < 3) consonant += "X";
            result = consonant;
        }


        if(consonant.length() > 3) {
            for(i=0; i<consonant.length();i++) {
                char cons = consonant.charAt(i);
                result += Character.toString(cons);
                if(result.length()==3) break;
            }
        }
        else result = consonant;

        return result;
    }

    //this method return the birth, sex and year code calculated using specific method
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
    } //return the 2 last number of the year given

    private String MonthCalculator(int month) { //mapping of month and letter
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

    private String DaySexCalculator(int day, String sex) { //return de code of the day value
        String dayCode = String.format("%02d", day);

        if(sex.equals("F")) { //case if the user is female
            int fullDayCode;
            fullDayCode = Integer.parseInt(dayCode);
            fullDayCode += 40;
            dayCode = Integer.toString(fullDayCode);
        }
        return dayCode;
    }

    //calculation of the control character
    private String ControlCharacterCalculator(String code) {
        // 1 generate separeted string of odd and eqal character
        String eqal = getEqualString(code);
        String odd  = getOddString(code);
        // 2 convertion of the value
        int oddSum = OddCharacterConvertion(odd);
        int equalSum = EqualCharacterConvertion(eqal);
        //3 final convertion
        int sum = oddSum + equalSum;
        int rest = (int) sum % 26;
        char restConverted = restConvertion(rest);

        return Character.toString(restConverted);

    }


    //generate a string full of characters whit char positive value
    private String getEqualString(String string) {
        String result = "";
        int i;
        for(i=0; i<string.length();i++) {
            if((i+1)%2 == 0) {
                result += Character.toString(string.charAt(i));
            }
        }
        return result;
    }

    //generate a string full of characters whit char odd value
    private String getOddString(String string) {
        String result = "";
        int i;
        for(i=0; i<string.length(); i++) {
            if((i+1)%2 == 1){
                result += Character.toString(string.charAt(i));
            }
        }
        return result;
    }

    private int EqualCharacterConvertion(String string) {
        int result = 0, i;
        for(i=0; i<string.length(); i++) {
            char character = string.charAt(i);
            int num = Character.getNumericValue(character);
            if(Character.isLetter(character)) {
                num = character - 65;
                result += num;
            }
            else result += num;
        }
        return result;
    }

    private int OddCharacterConvertion(String string) {
        int result = 0, i;
        for(i=0; i<string.length();i++) {
            char character = string.charAt(i);
            switch (character) {
                case '0':
                case 'A':
                    result += 1;
                    break;
                case '1':
                case 'B':
                    result += 0;
                    break;
                case '2':
                case 'C':
                    result += 5;
                    break;
                case '3':
                case 'D':
                    result += 7;
                    break;
                case '4':
                case 'E':
                    result += 9;
                    break;
                case '5':
                case 'F':
                    result += 13;
                    break;
                case '6':
                case 'G':
                    result += 15;
                    break;
                case '7':
                case 'H':
                    result += 17;
                    break;
                case '8':
                case 'I':
                    result += 19;
                    break;
                case '9':
                case 'J':
                    result += 21;
                    break;
                case 'K':
                    result += 2;
                    break;
                case 'L':
                    result += 4;
                    break;
                case 'M':
                    result += 18;
                    break;
                case 'N':
                    result += 20;
                    break;
                case 'O':
                    result += 11;
                    break;
                case 'P':
                    result += 3;
                    break;
                case 'Q':
                    result += 6;
                    break;
                case 'R':
                    result += 8;
                    break;
                case 'S':
                    result += 12;
                    break;
                case 'T':
                    result += 14;
                    break;
                case 'U':
                    result += 16;
                    break;
                case 'V':
                    result += 10;
                    break;
                case 'W':
                    result += 22;
                    break;
                case 'X':
                    result += 25;
                    break;
                case 'Y':
                    result += 24;
                    break;
                case 'Z':
                    result += 23;
                    break;
            }
        }
        return result;
    }

    private char restConvertion(int rest) {
        return (char) (rest + 65);
    }
}

