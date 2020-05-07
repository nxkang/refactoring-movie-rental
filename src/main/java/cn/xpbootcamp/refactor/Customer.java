package cn.xpbootcamp.refactor;

import java.util.Vector;

public class Customer {

    private String name;
    private Vector<Rental> rentals = new Vector<>();

    Customer(String name) {
        this.name = name;
    }

    void addRental(Rental rental) {
        rentals.addElement(rental);
    }

    public String getName() {
        return name;
    }

    String statement() {
        double totalAmount = getTotalAmount();
        int frequentRenterPoints = getFrequentRenterPoints();
        return getOutput(totalAmount, frequentRenterPoints);
    }

    private String getOutput(double totalAmount, int frequentRenterPoints) {
        StringBuilder result = new StringBuilder("Rental Record for " + getName() + "：\n");
        appendRentalFigure(result);

        //add footer lines
        result.append("Amount owed is ").append(totalAmount).append("\n");
        result.append("You earned ").append(frequentRenterPoints).append(" frequent renter points");
        return result.toString();
    }

    private void appendRentalFigure(StringBuilder result) {
        for (Rental each : rentals) {
            double thisAmount = getThisAmount(each);
            //show figures for this rental
            showRentalFigure(result, each, thisAmount);
        }
    }

    private void showRentalFigure(StringBuilder result, Rental each, double thisAmount) {
        result.append("\t")
                .append(each.getMovie().getTitle())
                .append("\t")
                .append(thisAmount).append("\n");
    }

    private int getFrequentRenterPoints() {
        int frequentRenterPoints = 0;
        for (Rental each : rentals) {
            //add frequent renter points
            frequentRenterPoints = getFrequentRenterPoints(frequentRenterPoints, each);
        }
        return frequentRenterPoints;
    }

    private int getFrequentRenterPoints(int frequentRenterPoints, Rental each) {
        frequentRenterPoints++;
        if ((each.getMovie().getPriceCode() == Movie.NEW_RELEASE) && each.getDaysRented() > 1)
            frequentRenterPoints++;
        return frequentRenterPoints;
    }

    private double getTotalAmount() {
        return rentals.stream().mapToDouble(this::getThisAmount).sum();
    }

    private double getThisAmount(Rental each) {
        double thisAmount = 0d;
        switch (each.getMovie().getPriceCode()) {
            case Movie.HISTORY:
                thisAmount += 2;
                if (each.getDaysRented() > 2)
                    thisAmount += (each.getDaysRented() - 2) * 1.5;
                break;
            case Movie.NEW_RELEASE:
                thisAmount += each.getDaysRented() * 3;
                break;
            case Movie.CAMPUS:
                thisAmount += 1.5;
                if (each.getDaysRented() > 3)
                    thisAmount += (each.getDaysRented() - 3) * 1.5;
                break;
        }
        return thisAmount;
    }

}
