//Author: Katherine Castro
//Module 3: Critical Thinking Assignment
//Mortgage Calculator
//CSC 475 Platform Based Development
//Colorado State University Global Campus
//Professor Russell Firth

package com.example.mortagecalculator;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.NumberFormat;
import static java.lang.Math.pow;


//MainActivity class for the Mortgage Calculator App
public class MainActivity extends AppCompatActivity
{
    // currency and percent formatter objects
    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    private static final NumberFormat percentFormat = NumberFormat.getPercentInstance();
    private static final NumberFormat integerFormat = NumberFormat.getIntegerInstance();

    private double priceAmount = 0.0;                 // price amount entered by the user
    private double payment = 0.0;                     // down payment
    private double interest = 0.0;                    // initial interest rate percentage
    private int years = 0;                            // number of years to pay off mortgage
    private TextView priceTextView;                   // shows formatted purchase price
    private TextView paymentTextView;                 // shows formatted down payment
    private TextView interestTextView;                // shows interest percentage
    private TextView yearsSeekBarTextView;            // shows calculated tip amount
    private TextView loanTextView;                    // shows calculated loan total
    private TextView totalTextView;                   // shows calculated total mortgage amount
    private TextView monthlyPaymentBoxTextView;       // shows calculated monthly payments
    private TextView monthlyTextView;

    //Called when activity is first created
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);         //Calls superclass onCreate
        setContentView(R.layout.activity_main);     //Inflates the GUI

        // get references to programmatically manipulated TextViews
        priceTextView = (TextView) findViewById(R.id.priceTextView);
        paymentTextView = (TextView) findViewById(R.id.paymentTextView);
        interestTextView = (TextView) findViewById(R.id.interestTextView);
        yearsSeekBarTextView = (TextView) findViewById(R.id.yearsSeekBarTextView);
        loanTextView = (TextView) findViewById(R.id.loanTextView);
        totalTextView = (TextView) findViewById(R.id.totalTextView);
        monthlyPaymentBoxTextView = (TextView) findViewById(R.id.monthlyPaymentBoxTextView);
        monthlyTextView = (TextView) findViewById(R.id.monthlyTextView);
        loanTextView.setText(currencyFormat.format(0));
        totalTextView.setText(currencyFormat.format(0));
        monthlyPaymentBoxTextView.setText(currencyFormat.format(0));


        // set priceEditText's TextWatcher
        EditText priceEditText = (EditText) findViewById(R.id.priceEditText);
        priceEditText.addTextChangedListener(priceEditTextWatcher);

        // set paymentEditText's TextWatcher
        EditText paymentEditText = (EditText) findViewById(R.id.paymentEditText);
        paymentEditText.addTextChangedListener(paymentEditTextWatcher);

        // set interestEditText's TextWatcher
        EditText interestEditText = (EditText) findViewById(R.id.interestEditText);
        interestEditText.addTextChangedListener(interestEditTextWatcher);

        // set SeekBars' OnSeekBarChangeListener
        SeekBar yearsSeekBar = (SeekBar) findViewById(R.id.yearsSeekBar);
        yearsSeekBar.setMax(99);
        yearsSeekBar.setOnSeekBarChangeListener(seekBarListener);
        yearsSeekBar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
    }

    //calculate and display loan amount, total mortgage, and monthly payments
    private void calculate()
    {
        //format percent and display yearsSeekBarTextView
        yearsSeekBarTextView.setText(integerFormat.format(years) + " Years");

        //convert years to months
        int months = years * 12;

        //convert annual interest to monthly interest
        double monthlyInterest = interest / 12;

        // calculate loan, total mortgage, and monthly payments
        double loan = priceAmount - payment;
        double monthly = (loan*(monthlyInterest * pow((1 + monthlyInterest), months)))/(pow((1 + monthlyInterest), months
        )-1);
        double total = (monthly * months) + payment;

        if (years == 0)
        {
            total = loan;
            monthly = 0;
        }

        //Calculating default 10, 20, and 30 year monthly payment options
        double tenYears = (loan*(monthlyInterest * pow((1 + monthlyInterest), 120)))/(pow((1 + monthlyInterest), 120
        )-1);
        double twentyYears = (loan*(monthlyInterest * pow((1 + monthlyInterest), 240)))/(pow((1 + monthlyInterest), 240
        )-1);
        double thirtyYears = (loan*(monthlyInterest * pow((1 + monthlyInterest), 360)))/(pow((1 + monthlyInterest), 360
        )-1);



        // display loan, total, and monthly payments formatted as currency
        loanTextView.setText(currencyFormat.format(loan));
        monthlyPaymentBoxTextView.setText(currencyFormat.format(monthly));
        totalTextView.setText(currencyFormat.format(total));



        monthlyTextView.setText("Monthly Payment Options\n" + "\n10 Years: " + currencyFormat.format(tenYears) + "\n" + "20 Years: "
                                + currencyFormat.format(twentyYears) + "\n" + "30 Years: "
                                + currencyFormat.format(thirtyYears) + "\n");
    }

    // listener object for the SeekBar's progress changed events
    private final OnSeekBarChangeListener seekBarListener = new OnSeekBarChangeListener()
    {
        // update percent, then call calculate
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
        {
            years = progress; // set percent based on progress

            calculate(); // calculate and display loan, total, and monthly payments


        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) { }

    };



    // listener object for the EditText's text-changed events
    private final TextWatcher priceEditTextWatcher = new TextWatcher()
    {
        // called when the user modifies the price amount
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {

            try
            { // get price amount and display currency formatted value
                priceAmount = Double.parseDouble(s.toString()) / 100.0;

                //NumberFormat defaultFormat = NumberFormat.getPercentInstance();

                priceTextView.setText(currencyFormat.format(priceAmount));
            }
            catch (NumberFormatException e)
            { // if s is empty or non-numeric
                priceTextView.setText("");
                priceAmount = 0.0;
            }

            calculate(); // updates the loan, total, and custom monthly amounts
        }

        @Override
        public void afterTextChanged(Editable s)
        {

        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {

        }
    };

    // listener object for the EditText's text-changed events
    private final TextWatcher paymentEditTextWatcher = new TextWatcher()
    {
        // called when the user modifies the down payment amount
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            try
            { // get down payment amount and display currency formatted value
                payment = Double.parseDouble(s.toString()) / 100.0;
                paymentTextView.setText(currencyFormat.format(payment));
            }
            catch (NumberFormatException e)
            { // if s is empty or non-numeric
                paymentTextView.setText("");
                payment = 0.0;
            }

            calculate(); // update the loan, total, and monthly payment values
        }
        @Override
        public void afterTextChanged(Editable s)
        {
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {

        }
    };


    // listener object for the EditText's text-changed events
    private final TextWatcher interestEditTextWatcher = new TextWatcher()
    {
        // called when the user modifies the interest rate
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            try
            { // get interest rate and display percentage
                interest = Double.parseDouble(s.toString())/100.00;

                NumberFormat defaultFormat = NumberFormat.getPercentInstance();
                defaultFormat.setMinimumFractionDigits(2);
                interestTextView.setText((defaultFormat.format(interest)));

                //interestTextView.setText(percentFormat.format(interest));
            }
            catch (NumberFormatException e)
            { // if s is empty or non-numeric
                interestTextView.setText("");
                interest = 0.0;
            }

            calculate(); // update the loan, total, and monthly payment values
        }

        @Override
        public void afterTextChanged(Editable s)
        {
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
        }
    };

}