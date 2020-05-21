package com.gtw.drools.rule;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.gtw.drools.model.Coupon;
import com.gtw.drools.model.Customer;
import com.gtw.drools.model.Item;
import com.gtw.drools.model.Order;
import com.gtw.drools.model.OrderLine;
import com.gtw.drools.model.types.IsGoldCustomer;
import com.gtw.drools.model.types.IsLowRangeItem;
import com.gtw.drools.util.KieSessionUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.ObjectFilter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class RHSTests {

    private KieSession kieSession;

    @Before
    public void before() throws FileNotFoundException {
        String drlFilePath = this.getClass().getClassLoader().getResource("drools/rule04.drl").getPath();
        kieSession = KieSessionUtil.buildKieSessionFromFiles(drlFilePath);
    }

    @Test
    public void testInsertModifyAndDelete() {
        Customer customer = new Customer();
        customer.setName("John");
        Coupon coupon = new Coupon();
        coupon.setValidUntil(new Date(System.currentTimeMillis() - 3600)); // expired one hour ago
        Order order = new Order();
        List<OrderLine> orderLines = new ArrayList<OrderLine>();
        for (int i = 0; i <= 10; i++) {
            orderLines.add(new OrderLine());
        }
        order.setItems(orderLines);
        order.setCustomer(customer);
        Item item = new Item("Cheap item", 9.00, 8.00);
        kieSession.insert(customer);
        kieSession.insert(coupon);
        kieSession.insert(order);
        kieSession.insert(item);
        int firedRUles = kieSession.fireAllRules();
        assertThat(5, equalTo(firedRUles));

        //check it contains one object of type IsGoldCustomer
        Collection<?> goldCustomerObjs = kieSession.getObjects(new ObjectFilter() {
            @Override
            public boolean accept(Object obj) {
                return obj instanceof IsGoldCustomer;
            }
        });
        assertThat(goldCustomerObjs, notNullValue());
        assertThat(1, equalTo(goldCustomerObjs.size()));
        IsGoldCustomer obj1 = (IsGoldCustomer) goldCustomerObjs.iterator().next();
        assertThat(obj1.getCustomer(), equalTo(customer));

        //check it contains one object of type IsLowRangeItem
        Collection<?> lowRangeItemObjs = kieSession.getObjects(new ObjectFilter() {
            @Override
            public boolean accept(Object obj) {
                return obj instanceof IsLowRangeItem;
            }
        });
        assertThat(lowRangeItemObjs, notNullValue());
        assertThat(1, equalTo(lowRangeItemObjs.size()));
        IsLowRangeItem obj2 = (IsLowRangeItem) lowRangeItemObjs.iterator().next();
        assertThat(obj2.getItem(), equalTo(item));
    }

    @After
    public void after() {
        kieSession.dispose();
    }
}
