package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.RevenueSearch;
import jpabook.jpashop.service.RevenueDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RevenueRepository {

    private final EntityManager em;

    /**
     * 매출 검색
     */
    public List<RevenueDto> findRevenue(RevenueSearch revenueSearch) {
        //language=JPQL
        String jpql = "select i, oi, o\n" +
                "from OrderItem oi\n" +
                "join Order o\n" +
                "on oi.id = o.id\n" +
                "join Item i\n" +
                "on i.id = oi.id";

        Query query = em.createQuery(jpql);
        List resultList = query.getResultList();
        System.out.println("resultList.size() = " + resultList.size());

        return query.getResultList();
    }
}
