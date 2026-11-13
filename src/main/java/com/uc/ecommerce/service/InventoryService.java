package com.uc.ecommerce.service;

import com.uc.ecommerce.domain.Product;
import com.uc.ecommerce.exception.BusinessException;
import com.uc.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ProductRepository productRepository;

    /** 下单预扣：可售减、冻结加。并发下靠乐观锁防超卖。 */
    @Transactional
    public void tryDeduct(Long productId, int quantity) {
        Product product = getProduct(productId);
        if (product.getAvailableStock() < quantity) {
            throw new BusinessException("库存不足，当前可售：" + product.getAvailableStock());
        }
        product.setAvailableStock(product.getAvailableStock() - quantity);
        product.setFrozenStock(product.getFrozenStock() + quantity);
        // 提交时 JPA 检查 @Version，并发修改会抛 OptimisticLockException，事务回滚
        productRepository.save(product);
    }

    /** 支付确认：冻结转实扣 */
    @Transactional
    public void confirmDeduct(Long productId, int quantity) {
        Product product = getProduct(productId);
        product.setFrozenStock(product.getFrozenStock() - quantity);
        productRepository.save(product);
    }

    /** 超时释放：冻结回可售 */
    @Transactional
    public void releaseDeduct(Long productId, int quantity) {
        Product product = getProduct(productId);
        product.setFrozenStock(product.getFrozenStock() - quantity);
        product.setAvailableStock(product.getAvailableStock() + quantity);
        productRepository.save(product);
    }

    /** 退款回补：可售加 */
    @Transactional
    public void restore(Long productId, int quantity) {
        Product product = getProduct(productId);
        product.setAvailableStock(product.getAvailableStock() + quantity);
        productRepository.save(product);
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException("商品不存在：" + productId));
    }
}
