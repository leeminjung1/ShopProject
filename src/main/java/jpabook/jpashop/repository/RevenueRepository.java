package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.RevenueSearch;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.RevenueDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jpabook.jpashop.domain.OrderStatus.CANCEL;

@Repository
@RequiredArgsConstructor
public class RevenueRepository {

    private final EntityManager em;

    /**
     * 매출 검색
     */
    public List<RevenueDto> findRevenue(RevenueSearch revenueSearch) {
        // JPQL
        String jpql = "select oi from OrderItem oi" +
                " left join fetch oi.item i" +
                " left join fetch oi.order o";

        boolean isFirst = false;

        //상품 이름 검색
        if (StringUtils.hasText(revenueSearch.getItemName())) {
            isFirst = true;
            jpql += " where" +
                    " i.name like :name";
        }

        if (StringUtils.hasText(revenueSearch.getOrderDateFrom())) {
            if (!isFirst) {
                jpql += " where";
            } else {
                jpql += " and";
            }
            isFirst = true;
            jpql += " o.orderDate >= :orderDateFrom";
        }

        if (StringUtils.hasText(revenueSearch.getOrderDateTo())) {
            if (!isFirst) {
                jpql += " where";
            } else {
                jpql += " and";
            }
            jpql += " o.orderDate < :orderDateTo";
        }

        TypedQuery<OrderItem> query = em.createQuery(jpql, OrderItem.class);

        if (StringUtils.hasText(revenueSearch.getItemName())) {
            query = query.setParameter("name", "%" + revenueSearch.getItemName() + "%");
        }
        if (StringUtils.hasText(revenueSearch.getOrderDateFrom())) {
            query = query.setParameter("orderDateFrom", LocalDateTime.parse(revenueSearch.getOrderDateFrom()));
        }
        if (StringUtils.hasText(revenueSearch.getOrderDateTo())) {
            query = query.setParameter("orderDateTo", LocalDateTime.parse(revenueSearch.getOrderDateTo()).plusMinutes(1));
        }

        List<OrderItem> orderItems = query.getResultList();

        List<RevenueDto> list = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            Item item = orderItem.getItem();
            Order order = orderItem.getOrder();
            if (order.getStatus() == CANCEL) {
                continue;
            }
            list.add(new RevenueDto(item.getId(), item.getName(), orderItem.getOrderPrice(), orderItem.getCount(), order.getTotalPrice(), order.getOrderDate()));
        }

        return list;
    }
}
