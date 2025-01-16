package com.soigo.romashkako.service.impl;

import com.soigo.romashkako.dto.request.SaleCreateRequest;
import com.soigo.romashkako.dto.request.SaleUpdateRequest;
import com.soigo.romashkako.exception.EntityNotFoundException;
import com.soigo.romashkako.model.Sale;
import com.soigo.romashkako.repository.SaleRepository;
import com.soigo.romashkako.repository.specification.SaleSpecification;
import com.soigo.romashkako.service.ProductService;
import com.soigo.romashkako.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.soigo.romashkako.utils.SortUtil.createSort;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {
    private final ProductService productService;
    private final SaleRepository saleRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<Sale> findAll(
            String name,
            Long productId,
            Integer page,
            Integer size,
            String sortBy,
            Boolean reverse
    ) {
        Sort sort = createSort(sortBy, reverse);
        Specification<Sale> spec = SaleSpecification.hasNameAndProductId(name, productId);
        return saleRepository.findAll(
                spec,
                PageRequest.of(
                        page,
                        size,
                        sort
                )
        );
    }

    @Override
    public Sale findById(Long id) {
        checkSaleExists(id);
        return saleRepository.findById(id).orElseThrow();
    }

    @Override
    public void delete(Long id) {
        checkSaleExists(id);
        Sale saleFound = saleRepository.findById(id).orElseThrow();
        // Добавляем у продукта кол-во
        productService.adjustCount(
                saleFound.getProduct().getId(),
                saleFound.getCount()
        );
    }

    @Override
    public Sale update(Long id, SaleUpdateRequest saleUpdateRequest) {
        checkSaleExists(id);
        Sale saleNew = modelMapper.map(saleUpdateRequest, Sale.class);
        Sale saleOld = saleRepository.findById(id).orElseThrow();

        if (saleNew.getName() != null) {
            saleOld.setName(saleNew.getName());
        }

        return saleRepository.save(saleOld);
    }

    @Override
    public Sale create(SaleCreateRequest saleCreateRequest) {
        Sale sale = modelMapper.map(saleCreateRequest, Sale.class);
        // Убавляем у продукта кол-во
        productService.adjustCount(
                sale.getProduct().getId(),
                -sale.getCount()
        );
        setSaleCost(sale.getProduct().getId(), sale);
        return saleRepository.save(sale);
    }

    private void setSaleCost(Long productId, Sale sale) {
        BigDecimal price = productService.findById(productId).getPrice();
        BigDecimal cost = price.multiply(BigDecimal.valueOf(sale.getCount()));
        sale.setCost(cost);
    }

    private void checkSaleExists(Long id) {
        if (!saleRepository.existsById(id)) {
            throw new EntityNotFoundException(String.format("Продажа товара с id %s не найден", id));
        }
    }
}
