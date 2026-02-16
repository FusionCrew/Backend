package com.fusioncrew.aikiosk.domain.cart.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fusioncrew.aikiosk.domain.cart.dto.CartDtos;
import com.fusioncrew.aikiosk.domain.cart.entity.Cart;
import com.fusioncrew.aikiosk.domain.cart.entity.CartItem;
import com.fusioncrew.aikiosk.domain.cart.repository.CartRepository;
import com.fusioncrew.aikiosk.domain.menu.entity.MenuItem;
import com.fusioncrew.aikiosk.domain.menu.repository.MenuItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final MenuItemRepository menuItemRepository;
    private final ObjectMapper objectMapper;

    public CartService(CartRepository cartRepository, MenuItemRepository menuItemRepository,
            ObjectMapper objectMapper) {
        this.cartRepository = cartRepository;
        this.menuItemRepository = menuItemRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public Cart createOrGet(String sessionId) {
        if (sessionId == null || sessionId.isBlank())
            throw new IllegalArgumentException("sessionId is required");
        return cartRepository.findBySessionId(sessionId).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setSessionId(sessionId);
            return cartRepository.save(cart);
        });
    }

    public Cart get(String cartId) {
        Long id = CartDtos.parseCartId(cartId);
        return cartRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("cart not found"));
    }

    /**
     * 장바구니 조회 (상세 정보 포함) - 명세 기준 응답
     */
    public CartDtos.CartGetResponse getWithDetails(String cartId) {
        Cart cart = get(cartId);

        List<CartDtos.CartItemDetailResponse> items = new ArrayList<>();
        int totalPrice = 0;

        for (CartItem cartItem : cart.getItems()) {
            // MenuItem 조회
            MenuItem menuItem = menuItemRepository.findByMenuItemId(cartItem.getMenuItemId())
                    .orElse(null);

            String menuCode = menuItem != null ? menuItem.getMenuItemId() : cartItem.getMenuItemId();
            String name = menuItem != null ? menuItem.getName() : "Unknown";
            int unitPrice = menuItem != null ? menuItem.getPrice() : 0;

            // optionsJson -> Map 변환
            Map<String, Object> options = parseOptionsJson(cartItem.getOptionsJson());

            // lineTotal 계산 (unitPrice + 옵션 추가금액) * quantity
            int optionPrice = calculateOptionPrice(options);
            int lineTotal = (unitPrice + optionPrice) * cartItem.getQuantity();

            items.add(new CartDtos.CartItemDetailResponse(
                    CartDtos.formatCartItemId(cartItem.getId()),
                    cartItem.getMenuItemId(),
                    menuCode,
                    name,
                    cartItem.getQuantity(),
                    unitPrice,
                    options,
                    lineTotal));

            totalPrice += lineTotal;
        }

        return new CartDtos.CartGetResponse(
                CartDtos.formatCartId(cart.getId()),
                items,
                totalPrice);
    }

    private Map<String, Object> parseOptionsJson(String optionsJson) {
        if (optionsJson == null || optionsJson.isBlank()) {
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(optionsJson, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    private int calculateOptionPrice(Map<String, Object> options) {
        // TODO: 옵션별 추가 가격 계산 로직 (옵션 가격 테이블 필요)
        // 현재는 옵션 가격을 0으로 처리 (추후 옵션 가격 테이블 연동 시 수정)
        int optionPrice = 0;

        // 예시: CHEESE EXTRA = +500, FRIES_SIZE LARGE = +500
        if (options.containsKey("CHEESE") && "EXTRA".equals(options.get("CHEESE"))) {
            optionPrice += 500;
        }
        if (options.containsKey("FRIES_SIZE") && "LARGE".equals(options.get("FRIES_SIZE"))) {
            optionPrice += 500;
        }

        // v2 kiosk sends selectedOptions with explicit extraPrice numbers.
        // Keep this aligned with OrderService's option parsing so cart totals match order totals.
        Object selectedOptionsObj = options.get("selectedOptions");
        if (selectedOptionsObj instanceof List<?> selectedOptions) {
            for (Object optObj : selectedOptions) {
                if (optObj instanceof Map<?, ?> optMap) {
                    Object extraPriceObj = optMap.get("extraPrice");
                    if (extraPriceObj instanceof Number num) {
                        optionPrice += num.intValue();
                    }
                }
            }
        }

        return optionPrice;
    }

    @Transactional
    public CartDtos.CartItemAddResponse addItem(String cartId, CartDtos.AddItemRequest req) {
        Cart cart = get(cartId);
        if (req.menuItemId() == null || req.menuItemId().isBlank())
            throw new IllegalArgumentException("menuItemId is required");

        int qty = (req.quantity() == null ? 1 : req.quantity());
        if (qty <= 0)
            throw new IllegalArgumentException("quantity must be > 0");

        // Map options -> JSON String 변환
        String optionsJson = null;
        Map<String, Object> options = req.options() != null ? req.options() : new HashMap<>();
        if (!options.isEmpty()) {
            try {
                optionsJson = objectMapper.writeValueAsString(options);
            } catch (Exception e) {
                optionsJson = "{}";
            }
        }

        CartItem item = new CartItem();
        item.setMenuItemId(req.menuItemId());
        item.setQuantity(qty);
        item.setOptionsJson(optionsJson);
        cart.addItem(item);

        Cart savedCart = cartRepository.save(cart);

        // CartItem 저장 후 ID가 생성됨 - 마지막 아이템 가져오기
        CartItem savedItem = savedCart.getItems().get(savedCart.getItems().size() - 1);

        // MenuItem 조회
        MenuItem menuItem = menuItemRepository.findByMenuItemId(req.menuItemId()).orElse(null);
        String menuCode = menuItem != null ? menuItem.getMenuItemId() : req.menuItemId();
        String name = menuItem != null ? menuItem.getName() : "Unknown";
        int unitPrice = menuItem != null ? menuItem.getPrice() : 0;

        // lineTotal 계산
        int optionPrice = calculateOptionPrice(options);
        int lineTotal = (unitPrice + optionPrice) * qty;

        return new CartDtos.CartItemAddResponse(
                CartDtos.formatCartItemId(savedItem.getId()),
                CartDtos.formatCartId(savedCart.getId()),
                req.menuItemId(),
                menuCode,
                name,
                qty,
                unitPrice,
                options,
                lineTotal);
    }

    @Transactional
    public CartDtos.CartItemAddResponse updateQty(String cartId, String itemId, int qty) {
        if (qty <= 0)
            throw new IllegalArgumentException("quantity must be > 0");
        Cart cart = get(cartId);

        Long targetId = CartDtos.parseCartItemId(itemId);

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(targetId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("item not found"));

        item.setQuantity(qty);
        Cart savedCart = cartRepository.save(cart);

        // MenuItem 조회
        MenuItem menuItem = menuItemRepository.findByMenuItemId(item.getMenuItemId()).orElse(null);
        String menuCode = menuItem != null ? menuItem.getMenuItemId() : item.getMenuItemId();
        String name = menuItem != null ? menuItem.getName() : "Unknown";
        int unitPrice = menuItem != null ? menuItem.getPrice() : 0;

        // options 파싱
        Map<String, Object> options = parseOptionsJson(item.getOptionsJson());

        // lineTotal 계산
        int optionPrice = calculateOptionPrice(options);
        int lineTotal = (unitPrice + optionPrice) * qty;

        return new CartDtos.CartItemAddResponse(
                CartDtos.formatCartItemId(item.getId()),
                CartDtos.formatCartId(savedCart.getId()),
                item.getMenuItemId(),
                menuCode,
                name,
                qty,
                unitPrice,
                options,
                lineTotal);
    }

    @Transactional
    public void deleteItem(String cartId, String itemId) {
        Cart cart = get(cartId);
        Long targetId = CartDtos.parseCartItemId(itemId);
        cart.getItems().removeIf(i -> i.getId().equals(targetId));
        cartRepository.save(cart);
    }

    @Transactional
    public void clear(String cartId) {
        Cart cart = get(cartId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
