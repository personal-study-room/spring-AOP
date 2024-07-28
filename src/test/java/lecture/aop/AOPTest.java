package lecture.aop;

import lecture.aop.order.OrderRepository;
import lecture.aop.order.OrderService;
import lecture.aop.order.aop.AspectV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestConstructor;

@Slf4j
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@SpringBootTest
@Import(AspectV1.class) // 간단하게 스프링 빈에 추가하는 방법. 주로 설정 파일을 추가할 때 사용
public class AOPTest {

  private final OrderService orderService;
  private final OrderRepository orderRepository;


  @Test
  void aopInfo() {
    log.info("isAOPProxy, orderService={}", AopUtils.isAopProxy(orderService));
    log.info("isAOPProxy, orderRepository={}", AopUtils.isAopProxy(orderRepository));
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
