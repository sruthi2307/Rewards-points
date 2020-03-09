package com.example.RewardPoints.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardResponseEntity implements Comparable<RewardResponseEntity>{
    private String customerId;
    private Double totalPoints;
    private List<TransactionByMonth> listOfTransactionByMonth = new ArrayList<>();

    @Override
    public int compareTo(RewardResponseEntity o) {
        RewardResponseEntity temp = o;
        if(this == o) return 0;
        if(o == null) return 0;
        return o.getCustomerId().compareTo(this.getCustomerId());
    }
}
