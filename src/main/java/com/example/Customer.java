package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Customer implements Comparable<Customer> {
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private final String ID;
    private final List<Account> accounts = new ArrayList<>();

    /**
     * Method to add an account to the customer.
     * @param account
     */
    public synchronized void addAccount(Account account){
        Objects.requireNonNull(account);
        if (account.getOwner() != this) {
            throw new IllegalArgumentException("The account belongs to a different customer.");
        }
        if (!accounts.contains(account)) {
            accounts.add(account);
        }
    }

    /**
     * toString method for Customer class.
     * @return String
     */
    @Override
    public String toString() {
        return "Customer[ID=" + ID + ", name=" + name + ", email=" + email + ", phoneNumber=" + phoneNumber + ", address=" + address + "]";
    }

    /**
     * compareTo method for Customer class.
     * @param other
     * @return int
     */
    @Override
    public int compareTo(Customer other){
        return this.ID.compareTo(other.ID);
    }

    /**
     * equals method for Customer class.
     * @param obj
     * @return boolean
     */
    @Override
    public boolean equals (Object obj){
        if (this == obj) return true;
        if (obj == null || getClass ()!= obj.getClass()) return false;
        Customer other = (Customer) obj;
        return this.ID.equals(other.ID);
    }


    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    /**
     * Constructor for Customer class.
     * @param name
     * @param email
     * @param phoneNumber
     * @param address
     * @param ID
     */
    public Customer(String name, String email, String phoneNumber, String address, String ID){
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.ID = Objects.requireNonNull(ID);
    }

    /**
     *  Get the customer's name.
     */
    public String getName(){
        return name;

    }

    /**
     * Get the customer's email.
     */
    public String getEmail(){
        return email;
    }

    /**
     * Get the customer's phone number.
     */
    public String getPhoneNumber(){
        return phoneNumber;
    }

    /**
     * Get the customer's address.
     */
    public String getAddress(){
        return address;
    }

    /**
     * Get the customer's ID.
     */
    public String getID(){
        return ID;
    }

    /**
     * Get the customer's accounts.
     */
    public synchronized List<Account> getAccounts(){
        return List.copyOf(accounts);
    }

    /**
     * Set the customer's name.
     * @param name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Set the customer's email.
     */
    public void setEmail(String email){
        this.email = email;
    }

    /**
     * Set the customer's phone number.
     */
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    /**
     * Set the customer's address.
     */
    public void setAddress(String address){
        this.address = address;
    }

    public void setID(String ID){
        // ID is final and cannot be changed after initialization
        throw new UnsupportedOperationException("ID cannot be changed after initialization.");
    }
}
