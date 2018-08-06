package com.example.android.datasellertransactionstracker;

/**
 * Created by HP on 8/5/2018.
 */
/**
 * Custom class for creating transaction objects. */
public class Transaction {
    // The name or number of the customer or service provider for each transaction
    private String name;
    // The unique id of each transaction
    private int id;
    // Whether transaction was with customer or service provider
    private int title;
    // The phone number of the customer or service provider
    private String phone;
    // The date of each transaction
    private String date;
    // The time of creation of each transaction
    private String time;
    // The unit of each transaction
    private int unit;
    // The cost of each transaction
    private int cost;
    // The state of payment for each transaction
    private int paymentState;
    // The description about each transaction. Allows the vendor to add any comment s/he may so wish
    // to add.
    private String description;

    /**
     * Constructs a new Transaction object.
     * @param name
     * @param id
     * @param title
     * @param phone
     * @param unit
     * @param cost
     * @param paymentState
     * @param description
     * @param date
     * @param time
     */
    public Transaction(String name, int id, int title, String phone, int unit, int cost,
                       int paymentState, String description, String date, String time) {
        this.name = name;
        this.id = id;
        this.title = title;
        this.phone = phone;
        this.unit = unit;
        this.cost = cost;
        this.paymentState = paymentState;
        this.description = description;
        this.date = date;
        this.time = time;
    }

    /**
     * @return id*/
    public String getName() { return this.name; }
    /**
     * @return _ID of each transaction. */
    public int getId() { return this.id; }

    /**
     * @return title of the second party for each transaction.*/
    public int getTitle() { return this.title;}

    /**
     * @return phone number of the second party for each transaction.
     */
    public String getPhone() { return this.phone; }

    /**
     * @return date of each transaction.*/
    public String  getDate() { return this.date;}

    /**
     * @return time of creation of the transaction.
     */
    public String getTime() { return this.time; }
    /**
     * @return unit of each transaction.*/
    public int getUnit() { return this.unit;}

    /**
     * @return cost of each transaction. */
    public int getCost() { return this.cost;}

    /**
     * @return paymentState of each transaction.
     */
    public int getPaymentState() { return this.paymentState;}

    /**
      * @return description of each transaction.
     */
    public String getDescription() { return this.description;}
}
