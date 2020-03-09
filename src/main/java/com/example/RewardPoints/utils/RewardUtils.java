package com.example.RewardPoints.utils;

import com.example.RewardPoints.model.CustomerDataInput;
import com.example.RewardPoints.model.RewardResponseEntity;
import com.example.RewardPoints.model.TransactionByMonth;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class RewardUtils {

    private static final String INPUT_DELIMETER = " ";

    //Load data from inout file into a Customer Input Object
    public List<CustomerDataInput> loadInputDataFromFile(){
        List<CustomerDataInput> inputDataList = new ArrayList<>();
        Path path = Paths.get("src/main/resources/InputData.txt");
        try {
            for(String customerRow: Files.readAllLines(path)){
                //Every row in the Input file
                if(isProperInput(customerRow)){
                    inputDataList.add(getCustomerInputFromRow(customerRow));
                }
                else {
                    System.out.println("Please fix the Input File. It is Incorrect");
                    inputDataList.clear();
                }
            }

        } catch (IOException e) {
            System.out.println("Please fix the Input File. It is Incorrect. Exception is :" + e.getMessage());
            inputDataList.clear();
        }
        return inputDataList;
    }

    //Check for null inputs
    private boolean isProperInput(String row) {
        return StringUtils.isNotBlank(row) && isValidCustomer(row);
    }

    //Check the Customer ID. If its Null, Not Valid
    private boolean isValidCustomer(String row) {
        boolean isValidCustomer = false;
        String[] customerRow = row.split(INPUT_DELIMETER);
        if(StringUtils.isNotBlank(customerRow[0])){
            isValidCustomer = isValidTransactionDate(isValidCustomer, customerRow[2]);
        }
        return isValidCustomer;
    }

    //Check the Date of Transaction. It needs to be NonNull
    private boolean isValidTransactionDate(boolean isValidCustomer, String transactionDate) throws DateTimeParseException {
        if(StringUtils.isNotBlank(transactionDate)){
            try{
                LocalDate.parse(transactionDate);
                isValidCustomer = true;
            }
            catch (DateTimeParseException e){
                isValidCustomer = false;
            }
        }
        return isValidCustomer;
    }

    //create input object
    private CustomerDataInput getCustomerInputFromRow(String row) {
        String[] customerRow = row.split(INPUT_DELIMETER);
        String customerID = customerRow[0];
        double transactionAmount = Double.valueOf(customerRow[1]);
        LocalDate transactionDate = LocalDate.parse(customerRow[2]);
        return new CustomerDataInput(customerID,transactionAmount,transactionDate);
    }

    //Calculate points based on the Transaction Amount- Over $50 One Point. Over $100, 2 points plus one for each over $50
    public Double calculatePoints(double transactionAmount) {
        Double totalPointsEarned = 0D;
        double roundedOffTransactionAmount = Math.ceil(transactionAmount);
        if(roundedOffTransactionAmount> 50 && roundedOffTransactionAmount >= 100){
            totalPointsEarned = (roundedOffTransactionAmount-100) * 2 + 50;
        }
        else if(roundedOffTransactionAmount>= 50 && roundedOffTransactionAmount < 100){
            totalPointsEarned =  (roundedOffTransactionAmount-50);
        }
        return totalPointsEarned;
    }

    //Store Map of Month to pointsEarned
    public List<TransactionByMonth> getListOFTransactionsByMonth(CustomerDataInput input, Double totalPointsEarned) {
        List<TransactionByMonth> transactionList = new ArrayList<>();
        Month month = input.getTransactionDate().getMonth();
        transactionList.add(new TransactionByMonth(month,totalPointsEarned));
        return transactionList;
    }

    //Arrange the Response Entity List by Customer and his transactions by month
    public List<? extends RewardResponseEntity> rearrangeByCustomer(List<RewardResponseEntity> rewardResponseEntityList) {
        Map<String,RewardResponseEntity> resultMap = new HashMap<>();
        for(RewardResponseEntity item : rewardResponseEntityList){
            resultMap = addToResultMap(resultMap,item);
        }
        return resultMap.values().stream().sorted().collect(Collectors.toList());
    }

    //If the Key Exists already, Add its existing attributes.
    private Map<String,RewardResponseEntity> addToResultMap(Map<String, RewardResponseEntity> resultMap, RewardResponseEntity item) {
        if(resultMap.containsKey(item.getCustomerId())){
            RewardResponseEntity tempEntity = resultMap.get(item.getCustomerId());
            tempEntity.setTotalPoints(item.getTotalPoints()+tempEntity.getTotalPoints());
            tempEntity.setListOfTransactionByMonth(addBothTransactionLists(tempEntity,item));
        }
        else {
            resultMap.put(item.getCustomerId(),item);
        }
        return resultMap;
    }

    //Merge both Transaction lists
    private List<TransactionByMonth> addBothTransactionLists(RewardResponseEntity tempEntity, RewardResponseEntity item) {
        //Adding all transaction lists from both the entities
        tempEntity.getListOfTransactionByMonth().addAll(item.getListOfTransactionByMonth());
        Map<String,TransactionByMonth> forUniqueTransactions =  new HashMap<>();
        for(TransactionByMonth transactionByMonth : tempEntity.getListOfTransactionByMonth()){
            if(forUniqueTransactions.containsKey(transactionByMonth.getTransactionMonth().toString())){
                TransactionByMonth tempTranMonth = forUniqueTransactions.get(transactionByMonth.getTransactionMonth().toString());
                tempTranMonth.setPointsInMonth(tempTranMonth.getPointsInMonth()+transactionByMonth.getPointsInMonth());
            }
            else {
                forUniqueTransactions.put(transactionByMonth.getTransactionMonth().toString(),transactionByMonth);
            }
        }
        return forUniqueTransactions.values().stream().sorted().collect(Collectors.toList());
    }
}
