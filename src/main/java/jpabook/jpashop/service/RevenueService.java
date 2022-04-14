package jpabook.jpashop.service;

import jpabook.jpashop.domain.RevenueSearch;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.RevenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RevenueService {
    private final RevenueRepository revenueRepository;

    /** 매출 검색 */
    public List<RevenueDto> findRevenue(RevenueSearch revenueSearch) {
        return revenueRepository.findRevenue(revenueSearch);
    }
}
