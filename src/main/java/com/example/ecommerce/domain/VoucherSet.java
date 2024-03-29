package com.example.ecommerce.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "vouchers")
@DiscriminatorValue("voucher_set")
public class VoucherSet extends PromotionSet{

    @OneToMany(mappedBy = "voucherSet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Voucher> vouchers;

    @Override
    public int getQuantityAvailable() {
        return vouchers.stream().filter(voucher -> voucher.getCustomer() == null).toArray().length;
    }

    @Override
    public void addItems(int quantity) {
        for (int i = 0; i < quantity; i++) {
            Voucher voucher = new Voucher();
            voucher.setVoucherSet(this);
            voucher.setCreatedAt(LocalDateTime.now());
            vouchers.add(voucher);
        }
    }

    public List<Promotion> subtractVouchers(int quantity) {
        return subtractItems(vouchers, quantity);
    }

    @Override
    public Promotion getAnUnUsedItem() {
        return vouchers.stream()
                .filter(voucher -> !voucher.isUsed())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No voucher available for this voucher set"));
    }
}
