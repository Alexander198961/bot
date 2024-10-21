package com.trading.api;

import com.ib.client.Bar;
import com.ib.client.Decimal;

import java.util.Objects;

public class CustomBar extends Bar {
    public CustomBar(String s, double v, double v1, double v2, double v3, Decimal decimal, int i, Decimal decimal1) {
        super(s, v, v1, v2, v3, decimal, i, decimal1);
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true; // If comparing the same object, it's equal
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false; // If null or not the same class, it's not equal
        }
        Bar otherBar = (Bar) obj; // Cast the object to Bar
        return Objects.equals(this.time() ,otherBar.time()); // Compare m_time fields using Objects.equals
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.time()); // Implement hashCode for consistency
    }
}
