package test.mslb;

import halo.query.Query;
import halo.query.mslb.MSLBStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import test.bean.Food;

import javax.annotation.Resource;

/**
 * Created by akwei on 7/6/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/query-mslb.xml"})
//@Transactional
public class MSLBTest {
    @Resource
    private Query query;

    @Test
    public void select() {
        int count = query.count(Food.class, "where food_id = 1", null);
        Assert.assertEquals(1, count);
    }

    @Test
    public void insert() {
        Food food = new Food();
        food.setName("akwei1");
        food.setCreateTime(System.currentTimeMillis() / 1000);
        int foodId = query.insertForNumber(food).intValue();
        MSLBStatus.setOpSlave();
        Food dbFood = query.objById(Food.class, foodId);
        if (dbFood == null) {
            System.out.println("data not sync on slave");
            MSLBStatus.remove();
            dbFood = query.objById(Food.class, foodId);
        }
        Assert.assertEquals(foodId, dbFood.getFoodId());
        System.out.println("foodId : " + foodId);
    }

    //    @Test
    public void update() {

    }

    //    @Test
    public void delete() {

    }
}
