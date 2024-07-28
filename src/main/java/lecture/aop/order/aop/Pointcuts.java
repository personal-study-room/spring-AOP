package lecture.aop.order.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {
  @Pointcut("execution(* lecture.aop.order..*(..))")
  public void allOrder() {
  }

  @Pointcut("execution(* *..*Service.*(..))") // 클래스 이름 패턴이 *Service 인 것
  public void allService() {
  }

  @Pointcut("allOrder() && allService()")
  public void orderAndService() {}
}
