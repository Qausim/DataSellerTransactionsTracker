package com.example.android.datasellertransactionstracker;

/**
 * Created by HP on 8/5/2018.
 */
/**
 * Custom class for creating transaction objects. */
public class Transaction {
    // The name or number of the customer or service provider for each transaction
    private String id;
    // The unique id of each transaction
    private int _ID;
    // Whether transaction was with customer or service provider
    private int title;
    // The phone number of the customer or service provider
    private int phone;
    // The date of each transaction
    private long date;
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
     * Sets the customer's or service provider's id for each transaction.
     * @param id set is passed in and set as the id. */
    public void setId(String id) { this.id = id; }

    /**
     * Sets the unique id of each transaction.
     * @param _ID is passed in and set as the _ID. */
    public void set_ID(int _ID) { this._ID = _ID; }

    /**
     * Sets the title the person transacted for each transaction.
     * @param title */
    public void setTitle(int title) { this.title = title; }

    /**
     * Sets the phone number of each the second person (customer or service provider.
     * @param phone */
    public void setPhone(int phone) { this.phone = phone; }

    /**
     * Sets the date of each transaction.
     * @param date */
    public void setDate(long date) { this.date = date; }

    /**
     * Sets the unit of each transaction.
     * @param unit */
    public void setUnit(int unit) { this.unit = unit; }

    /**
     * Sets the cost of each transaction.
     * @param cost */
    public void setCost(int cost) { this.cost = cost; }

    /** Sets the payment state of each transaction.
     * @param paymentState */
    public void setPaymentState(int paymentState) { this.paymentState = paymentState; }

    /**
     * Sets the description of each transaction.
     * @param description */
    public void setDescription(String description) { this.description = description; }

    /**
     * @return id*/
    public String getId() { return this.id; }

    /**
     * @return _ID of each transaction. */
    public int get_ID() { return this._ID; }

    /**
     * @return title of the second party for each transaction.*/
    public int getTitle() { return this.title;}

    /**
     * @return phone number of the second party for each transaction.*/
    public int getPhone() { return this.phone; }

    /**@return date of each transaction.*/
    public long getDate() { return this.date;}

    /**@return unit of each transaction.*/
    public int getUnit() { return this.unit;}

    /**@return cost of each transaction. */
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
