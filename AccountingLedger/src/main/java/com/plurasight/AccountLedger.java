package com.plurasight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class AccountLedger {
    static Scanner input = new Scanner(System.in);
    static ArrayList<Transactions> transactions = new ArrayList<>();
    static String line;
    public static void main(String[] args) {
        goHome();
    }
    private static void goHome() {
        System.out.println("Please make a selection:");
        System.out.println(" (D) to Add Deposit \n (P) to Make Payment (DEBIT) \n (L) to see Ledger \n (X) to Exit");
        String selection = input.nextLine();
        switch (selection) {
            case "D" -> addDeposit();
            case "P" -> makePayment();
            case "L" -> toSeeLedger(transactions);
            case "X" -> exit();
            default -> {
                System.out.println("Error,invalid response");
                goHome();
            }
        }
    }
    // add Deposit method, prompting user to enter transaction details
    // date formatter used
    //file writer to create the transactions file
    private static void addDeposit() {
        System.out.println("Please Enter your transaction details below:");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd|HH:mm:ss");
        LocalDateTime currentTime = LocalDateTime.now();
        String userDateTime = currentTime.format(formatter);
        System.out.println("Description:");
        String description = input.nextLine();
        System.out.println("Vendor: ");
        String vendor = input.nextLine();
        System.out.println("Amount: ");
        float amount = input.nextFloat();
        // write to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true))){
            writer.write(userDateTime + "|" + description + "|" + vendor + "|" + amount +"\n") ;
            System.out.println("Your transaction has been posted to the Ledger");
        } catch (IOException e) {
            System.out.println("error");
        }
        goHome();
    }
    // see Ledger method, prompts user to make a selection
    private static void toSeeLedger(ArrayList<Transactions> transactions) {
        System.out.println("Please Make a Selection");
        System.out.println(" (A) Display all entries \n (D) Display only Deposits \n (P) Display payment \n (R) Reports \n (H) to return Home ");
        String choice = input.nextLine();
        switch (choice) {
            case "A" -> allEntries(transactions);
            case "D" -> depositEntries(transactions);
            case "P" -> paymentEntries(transactions);
            case "R" -> reports(transactions);
            case "H" -> goHome();
            default -> {
                System.out.println("Error,invalid response");
                toSeeLedger(transactions);
            }
        }
    }
    private static void allEntries(ArrayList<Transactions> transactions){
        System.out.println("\t ~ All Transaction Entries ~ \n");
        try (BufferedReader br = new BufferedReader(new FileReader("transactions.csv"))){
            line = br.readLine();
            while ((line= br.readLine()) != null) {//starts on second line
                System.out.println(line);
            }System.out.println(" \n\t ~ End of All Transactions ~ \t ");
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        toSeeLedger(transactions);
    }
    private static void depositEntries(ArrayList<Transactions> transactions){
        System.out.println("Only Deposit Transactions");
        try (BufferedReader br = new BufferedReader(new FileReader("transactions.csv"))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\|");
                float deposit = Float.parseFloat(data[4]);
                if (deposit > 0) {
                    System.out.println(line);
                }
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        toSeeLedger(transactions);
    }
    private static void makePayment(){
        System.out.println("Please Enter your transaction details below:");
        LocalTime currentTime = LocalTime.now();
        LocalDate currentDate = LocalDate.now();
        System.out.println("Description:");
        String paymentDescription = input.nextLine();
        System.out.println("Vendor: ");
        String paymentVendor = input.nextLine();
        System.out.println("Amount(enter as a negative number:)");
        float paymentAmount = input.nextFloat();
        // write to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true))){
            writer.write(currentDate + "|" + currentTime + "|" + paymentDescription + "|" + paymentVendor + "|" + paymentAmount +"\n");
            System.out.println("Your transaction has been posted to the Ledger");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error, please try again");;
        }
        goHome();
    }
    private static void paymentEntries(ArrayList<Transactions> transactions){
        System.out.println("\t ~Payment Transactions~ ");
        try (BufferedReader br = new BufferedReader(new FileReader("transactions.csv"))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\|");
                //Transactions transactions = new Transactions(data[0],data[1],data[2],data[3],Float.parseFloat(data[4]));
                float payment;
                payment = Float.parseFloat(data[4]);
                if (payment < 0) {
                    System.out.println(line);}
            }
            System.out.println("\n\t ~ End of Payment Transactions ~");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        toSeeLedger(transactions);
    }
    public static void reports(ArrayList<Transactions> transactions){
        System.out.println(" Please make a selection of what kind of report you would like to see");
        System.out.println("(1) Month To Date, (2) Previous Month, (3) Year-to-Date, (4) Previous Year (5) Search by vendor");
        int choice = input.nextInt();
        switch (choice) {
            case 1 -> monthToDate();
            case 2 -> previousMonth(transactions);
            case 3 -> yeartoDate();
            case 4 -> previousYear();
            case 5 -> vendorSearch();
            case 6 -> reports(transactions);
            default -> {
                System.out.println("Error,invalid response");
                reports(transactions);
            }
        }
    }
    // Month to date method, splitting the date format so i am able to reference just one variable
    private static void monthToDate() {
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        try (BufferedReader br = new BufferedReader(new FileReader("transactions.csv"))){
            while ((line = br.readLine()) != null) {
                // first have to split the array
                String[] data = line.split("\\|");
                //then have to split the specific data piece you want to pull,
                String [] data2 = data[0].split("-");
                int curMonth = Integer.parseInt(data2[1]);
                if (curMonth == currentMonth) {
                    System.out.println(line);}
                else {
                    System.out.println("There are no transactions in the current Month");
                }
            }} catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // previous month method will print out the transaction from the month prior to current date
            private static void previousMonth(ArrayList<Transactions> transactions) {
                System.out.println("Previous Month Transactions");
                LocalDate currentDate = LocalDate.now();
                int currentMonth = currentDate.getMonthValue();
                try (BufferedReader br = new BufferedReader(new FileReader("transactions.csv"))){
                    while ((line = br.readLine()) != null) {
                        String[] data = line.split("-");
                        String [] data2 = data[0].split("-");
                        int inputMonth = Integer.parseInt(data2[1]);
                        if(inputMonth == currentMonth--){
                            System.out.println(line);
                        }else {
                            System.out.println("There are no Transactions");}
                    }} catch (IOException e) {
                    throw new RuntimeException(e);
                     }
                        reports(transactions);
                 }
            // year to date method will display all entries in the current year
            private static void yeartoDate() {
                System.out.println("Month to date Transactions");
                LocalDate currentDate = LocalDate.now();
                int currentYear = currentDate.getYear();
                try (BufferedReader br = new BufferedReader(new FileReader("transactions.csv"))){
                    while ((line = br.readLine()) != null) {
                        String[] data = line.split("\\|");
                        String [] data2 = data[0].split("-");
                        int inputYear = Integer.parseInt(data2[0]);
                        if(inputYear == currentYear){
                            System.out.println(line);
                        } else {System.out.println("There are no Transactions");}
                    }} catch (IOException e) {
                    throw new RuntimeException(e);
                }
                reports(transactions);
            }
            // previous year method
            private static void previousYear( ) {
                System.out.println("Transactions for the Previous Year \n");
                LocalDate currentDate = LocalDate.now();
                int currentYear= currentDate.getYear();
                try (BufferedReader br = new BufferedReader(new FileReader("transactions.csv"))){
                    while ((line = br.readLine()) != null) {
                        String[] data = line.split("\\|");
                        String [] data2 = data[0].split("-");
                       int inputYear = Integer.parseInt(data2[0]);
                        if( inputYear == currentYear--){
                            System.out.println(line);}
                            else {
                                System.out.println("There are no Transactions");}
            }} catch (IOException e) {
                    throw new RuntimeException(e);
            }
            reports(transactions);
            }
            private static void vendorSearch() {
                System.out.println("Please Enter the name of the Vendor");
                String inputVendor = input.nextLine();
                try (BufferedReader br = new BufferedReader(new FileReader("transactions.csv"))){
                    br.readLine();
                    while ((line = br.readLine()) != null) {
                        String[] data = line.split("\\|");
                        String vendor = (data[3]);
                        if (inputVendor.equalsIgnoreCase(vendor)){
                            System.out.println(line);}
                            else{ System.out.println("Please enter a valid vendor");
                }}}  catch (IOException e) {
                        throw new RuntimeException(e);
                }
                reports(transactions);
            }
            private static void exit(){
                System.out.println("~~ Program Closed~~");
            }}



