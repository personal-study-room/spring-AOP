package lecture.aop;

import lecture.aop.order.OrderRepository;
import lecture.aop.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

@Slf4j
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@SpringBootTest
public class AOPTest {

  private final OrderService orderService;
  private final OrderRepository orderRepository;


  @Test
  void aopInfo() {
    log.info("isAOPProxy, orderService={}", AopUtils.isAopProxy(orderService));
    log.info("isAOPProxy, orderRepository={}", AopUtils.isAopProxy(orderRepository));

    /**
     * 2024-07-28T09:59:42.346+09:00  INFO 9774 --- [aop] [           main] lecture.aop.AOPTest                      : isAOPProxy, orderService=false
     * 2024-07-28T09:59:42.346+09:00  INFO 9774 --- [aop] [           main] lecture.aop.AOPTest                      : isAOPProxy, orderRepository=false
     */
  }

  @Test
  void success() {
    orderService.orderItem("itemA");
  }

  @Test
  void exceptionService() {
    Assertions.assertThatThrownBy(() -> orderService.orderItem("ex")).isInstanceOf(IllegalStateException.class);
  }

}
