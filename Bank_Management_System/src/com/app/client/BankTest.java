package com.app.client;

import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.app.configuration.HibernateUtil;
import com.app.model.Bank;

/**
 * BankTest class implements basic bank management system operations
 * such as creating an account, depositing money, withdrawing money,
 * and displaying account details using Hibernate ORM.
 */
public class BankTest {

    // Scanner for user input
    Scanner sc = new Scanner(System.in);

    // Bank model object (represents a single customer/account)
    Bank bank = new Bank();

    // Hibernate session factory and session for DB operations
    SessionFactory sf = HibernateUtil.getSessionFactory();
    Session session = sf.openSession();

    /**
     * Create a new bank account and persist it to the database.
     */
    public void createAccount() {
        System.out.println("Create Your Account Number: ");
        bank.setAccountNumber(sc.nextInt());

        System.out.println("Enter Your Name:");
        bank.setCustomerName(sc.next());

        System.out.println("Enter Your Balance: ");
        bank.setBalance(sc.nextDouble());

        // Save object in database using Hibernate
        session.persist(bank);

        // Commit transaction to finalize data save
        session.beginTransaction().commit();

        System.out.println("✅ Account Created Successfully!");
    }

    /**
     * Deposit money into an existing account based on account number.
     */
    public void deposite() {
        System.out.println("Enter your Account Number: ");
        int accNum = sc.nextInt();

        // Check if account number matches the current bank object
        if (bank.getAccountNumber() == accNum) {
            System.out.println("Deposit Your Amount Number:");
            double amount = sc.nextDouble();

            // Validate amount before processing deposit
            if (amount >= 0) {
                double balance = bank.getBalance();
                balance += amount; // add deposit amount to balance
                bank.setBalance(balance);

                System.out.println("Your Balance is: " + bank.getBalance());

                // Update record in database
                session.update(bank);
                session.beginTransaction().commit();
            } else {
                System.out.println("Deposit Valid Amount!");
            }

        } else {
            return;
        }
    }

    /**
     * Withdraw money from an existing account based on account number.
     */
    public void withdraw() {
        System.out.println("Enter your Account Number: ");
        int accNum = sc.nextInt();

        // Validate account number
        if (bank.getAccountNumber() == accNum) {
            System.out.println("Withdraw Your Amount Number");
            double amount = sc.nextDouble();

            // Validate withdrawal amount
            if (amount >= 0) {
                double balance = bank.getBalance();
                balance -= amount; // subtract withdrawal from balance
                bank.setBalance(balance);

                System.out.println("Your Available Balance is: " + bank.getBalance());

                // Update in DB after transaction
                session.update(bank);
                session.beginTransaction().commit();
            } else {
                System.out.println("Withdraw Valid Amount!");
            }

        } else {
            return;
        }
    }

    /**
     * Retrieve and display details of multiple bank accounts from the database.
     */
    public void showDetails() {
        for (int i = 1; i <= 10; i++) {

            // Fetch bank details from DB using primary key (ID)
            Bank b = session.get(Bank.class, i);

            if (b != null) {
                System.out.println("Your Name is: " + b.getCustomerName());
                System.out.println("Your Account Number: " + b.getAccountNumber());
                System.out.println("Your Balance is: " + b.getBalance());
                System.out.println("==============================");
            } else {
                System.out.println("Data Doesn't Found at ID: " + i);
            }
        }
    }

    /**
     * Display a simple console-based menu for banking operations.
     */
    public void menu() {
        while (true) {
            System.out.println("\n==============================");
            System.out.println("      BANK MANAGEMENT SYSTEM  ");
            System.out.println("==============================");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Show details");
            System.out.println("5. Exit");
            System.out.print("Enter Your Choice: ");
            System.out.println("==============================");

            int choice = sc.nextInt();

            // Perform operation based on user input
            switch (choice) {
                case 1:
                    createAccount();
                    break;

                case 2:
                    deposite();
                    break;

                case 3:
                    withdraw();
                    break;

                case 4:
                    showDetails();
                    break;

                case 5:
                    System.out.println("🏁 Exiting... Thank you for using our bank!");
                    System.exit(0);
                    break;

                default:
                    System.out.println("❌ Invalid choice! Please try again.");
            }
        }
    }

    /**
     * Main method — entry point of the application.
     */
    public static void main(String[] args) {
        BankTest bankTest = new BankTest();
        bankTest.menu();
    }
}
