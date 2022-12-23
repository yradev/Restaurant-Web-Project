package source.restaurant_web_project.models.entity;

import source.restaurant_web_project.models.entity.superClass.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "delivery_item")
public class DeliveryItem extends BaseEntity {
    private long count;

    @ManyToOne(targetEntity = Delivery.class)
    private Delivery delivery;

    @ManyToOne(targetEntity = Item.class)
    private Item item;
    
    public DeliveryItem() {
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
