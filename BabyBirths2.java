/**
 * Print out total number of babies born, as well as for each gender, in a given CSV file of baby name data.
 * 
 * @author Duke Software Team 
 */
import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;

public class BabyBirths2 {
    public void printNames () {
        FileResource fr = new FileResource();
        for (CSVRecord rec : fr.getCSVParser(false)) {
            int numBorn = Integer.parseInt(rec.get(2));
            if (numBorn <= 100) {
                System.out.println("Name " + rec.get(0) +
                           " Gender " + rec.get(1) +
                           " Num Born " + rec.get(2));
            }
        }
    }

    public void totalBirths (FileResource fr) {
        int totalBirths = 0;
        int totalBoys = 0;
        int totalGirls = 0;
        int numOfGirls = 0;
        int numOfBoys = 0;
        for (CSVRecord rec : fr.getCSVParser(false)) {
            int numBorn = Integer.parseInt(rec.get(2));
            totalBirths += numBorn;
            if (rec.get(1).equals("M")) {
                totalBoys += numBorn;
                numOfBoys++;
            }
            else {
                totalGirls += numBorn;
                numOfGirls++;
            }
        }
        System.out.println("total births = " + totalBirths);
        System.out.println("female girls = " + totalGirls);
        System.out.println("male boys = " + totalBoys);
        System.out.println("number of girls = " + numOfGirls);
        System.out.println("number of boys = " + numOfBoys);
    }
    public void testTotalBirths () {
        //FileResource fr = new FileResource();
        FileResource fr = new FileResource("us_babynames/us_babynames_by_year/yob1900.csv");
        totalBirths(fr);
    }
    
    public int yearOfHighestRank(String name, String gender) {
        int highestRankOfFiles = getRank(2012, name, gender);
        int year = getYear(highestRankOfFiles, name, gender);
        
        return year;
        }
    public void testYearOfHighestRank() {
        int fileName = yearOfHighestRank("Genevieve", "F");
        System.out.println(fileName);
    }
    
    public int getSimpleRank(FileResource fr, String name, String gender){
        int rank = 0;
        for (CSVRecord rec: fr.getCSVParser(false)) {
            String recGender = rec.get(1);
            if (!recGender.equals(gender)){
                rank = 0;
            } else {
                rank++;
            }
            String recName = rec.get(0);
            if (recName.equals(name) && recGender.equals(gender)) {
                return rank;
            }
        }
        return -1;
    }
    public Double getAverageRank(String name, String gender) {
        //addedRankings = add up all the rankings from each file
        //countOfFiles = count how many files were selected
        //average = addedRankings/countOfFiles
        double addedRankings = 0;
        int countOfFiles = 0;
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            int currentRank = getSimpleRank(fr, name, gender);
            addedRankings += currentRank;
            countOfFiles++;                                                    
        }
        double average = addedRankings/countOfFiles;
        return average;
    }
    public void testGetAverageRank() {
        double averageRank = getAverageRank("Robert", "M");
        System.out.println(averageRank);
    }
    
    public int getTotalBirthsRankedHigher(int year, String name, String gender){
        int birthsAboveRank = 0;
        FileResource fr = new FileResource();
        for (CSVRecord rec: fr.getCSVParser(false)) {
            String recGender = rec.get(1);
            String recName = rec.get(0);
            String recBirths = rec.get(2);
            int intRecBirths = Integer.parseInt(recBirths);
            if (recGender.equals(gender) && !recName.equals(name)){
                birthsAboveRank+=intRecBirths;
            }     
            if (recName.equals(name) && recGender.equals(gender)) {
                return birthsAboveRank;
            }
        }
        return -1;
    }
    public void testGetTotalBirthsRankedHigher(){
        int birthsRankedHigher = getTotalBirthsRankedHigher(1990, "Emily", "F");
        System.out.println(birthsRankedHigher);
    }
    
    public int getYear(int rank, String name, String gender) {
        int newRank = 0;
        int resultYear = 0;
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()) {
            String getNameFile = f.getName();
            FileResource fr = new FileResource(f);
            for (CSVRecord rec : fr.getCSVParser(false)) {
            String recGender = rec.get(1);
            if (!recGender.equals(gender)){
                newRank = 0;
            } else {
                newRank++;
            }
            String recName = rec.get(0);
            if (recName.equals(name) && newRank == rank) {
                
                    String temp = getNameFile.substring(3, 7);
                    resultYear = Integer.parseInt(temp);
                
            } 
        }
        }
        if (resultYear == 0) {
        return -1;
        } else {
            return resultYear;
        }
    }
    
    public int getRank(int year, String name, String gender) {
        int rank = 0;
        int temp = 0;
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            for (CSVRecord rec : fr.getCSVParser(false)) {
            String recGender = rec.get(1);
            if (!recGender.equals(gender)){
                temp = 0;
            } else {
                temp++;
            }
            String recName = rec.get(0);
            if (recName.equals(name) && recGender.equals(gender)) {
                //and also!...
                if (rank == 0) {
                rank = temp;
                } else if (temp < rank){
                    rank = temp;
                }
                
            } 
                
            
        }
        }
        if (rank != 0) {
            return rank;
        } else {
        return -1;
    }
    }
    public void testGetRank() {
        System.out.println("rank: " + getRank(1971, "Frank", "M"));
    }
    
    public String getName(int year, int rank, String gender) {
        String name;
        int totalRanks = 0;
        DirectoryResource dr = new DirectoryResource();
        for (File f: dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            for (CSVRecord rec: fr.getCSVParser(false)) {
            String recGender = rec.get(1);
            if (!recGender.equals(gender)) {
                totalRanks = 0;
            } else {
                totalRanks++;
                if (totalRanks == rank) {
                    return rec.get(0);
                }
            }
        }
        }                                  
        return "NO NAME";
    }
    public void testGetName() {
        System.out.println(getName(1980, 350, "F"));
    } 
    
    public void whatIsNameInYear(String name, int year, 
    int newYear, String gender){
        //getRank(int year, String name, String gender)
        int rank = getRank(year, name, gender);
        //getName(int year, int rank, String gender)
        String newName = getName(newYear, rank, gender);
        System.out.println(name + " born in " + year + " would be " + 
        newName + " if she was born in " + newYear + ".");
    }
    public void testWhatIsNameInYear() {
        whatIsNameInYear("Owen", 1974, 2014, "M");
    }
    
}
