package com.example.RewardPoints.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionByMonth  implements Comparable<TransactionByMonth>{
   private Enum transactionMonth;
   private Double pointsInMonth;

   @Override
   public int compareTo(TransactionByMonth o) {
      TransactionByMonth temp = o;
      if(this == o) return 0;
      if(o == null) return 0;
      return o.getTransactionMonth().compareTo(this.getTransactionMonth());
   }

}
