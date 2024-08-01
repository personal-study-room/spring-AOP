package lecture.aop.pointcut;

import lecture.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ExecutionTest {
  AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
  Method helloMethod;

  @BeforeEach
  void beforeEach() throws NoSuchMethodException {
    helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
  }

  @Test
  void printMethod() {
    // helloMethod=public java.lang.String lecture.aop.member.MemberServiceImpl.hello(java.lang.String)
    log.info("helloMethod={}", helloMethod);
  }


  @Test
  void exactMatch() {
    pointcut.setExpression("execution(public String lecture.aop.member.MemberServiceImpl.hello(String))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
  }

  @Test
  void allMatch() {
    pointcut.setExpression("execution(* *(..))");

    log.info("{}", pointcut.getClassFilter());
    log.info("{}", pointcut.getPointcutExpression());
    log.info("{}", pointcut.getExpression());

    assertThat(pointcut.matches(MemberServiceImpl.class)).isTrue();
  }

  @Test
  void nameMatch() {
    pointcut.setExpression("execution(* hello(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
  }

  @Test
  void nameMatchStar1() {
    pointcut.setExpression("execution(* hel*(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
  }

  @Test
  void nameMatchStar2() {
    pointcut.setExpression("execution(* *el*(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
  }

  @Test
  void nameMatchNoMatch() {
    pointcut.setExpression("execution(* nono(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
  }

  //public String lecture.aop.member.MemberServiceImpl.hello(String)
  @Test
  void packageMatch1() {
    pointcut.setExpression("execution(* lecture.aop.member.MemberServiceImpl.hello(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
  }

  @Test
  void packageMatch2() {
    pointcut.setExpression("execution(* lecture.aop.member.*.*(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
  }

  @Test
  void packageMatchFalse1() {
    pointcut.setExpression("execution(* lecture.aop.*.*(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
  }

  @Test
  void packageMatchSubPackage1() {
    pointcut.setExpression("execution(* lecture.aop..*.*(..))"); //  . : 정확한 패키지, .. : 해당 패키지 하위까지 포함
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
  }

  @Test
  void typeExactMatch() {
    pointcut.setExpression("execution(* lecture.aop.member.MemberServiceImpl.*(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
  }

  @Test
  void typeExactSuperTypeMatch() {
    pointcut.setExpression("execution(* lecture.aop.member.MemberService.*(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
  }

  @Test
  void typeMatchInternal1() throws NoSuchMethodException {
    pointcut.setExpression("execution(* lecture.aop.member.MemberService.*(..))");
    Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);

    assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isFalse();
    // 여기서 알 수 있는 점
    // super type으로 매칭된다는 것은 해당 인터페이스(super)에 명세된 메서드의 경우에만 매칭이 된다.
    // 따라서 internal 메서드는 MemberServiceImpl(sub)에서 구현한 메서드이므로 매칭되지 않는다.
  }


  @Test
  void typeMatchInternal2() throws NoSuchMethodException {
    pointcut.setExpression("execution(* lecture.aop.member.MemberServiceImpl.*(..))"); // 자식으로 애초에 잡는다면?
    Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);

    assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isTrue();
  }

  // String 타입의 파라미터 허용
  // (String)
  @Test
  void argsMatch() throws NoSuchMethodException {
    pointcut.setExpression("execution(* *(String))");
    Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);

    assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isTrue();
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
  }

  @Test
  void argsMatchNoArgs() {
    pointcut.setExpression("execution(* *())");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
  }

  //(X x)
  @Test
  void argsMatchAllArgs() {
    pointcut.setExpression("execution(* *(*))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
  }

  //(X x, B b, ...)
  @Test
  void argsMatchAllArgs2() {
    pointcut.setExpression("execution(* *(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
  }

  //(String arg, B b)
  @Test
  void argsMatch3() {
    pointcut.setExpression("execution(* *(String, *))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
  }

  //(String arg, ...)
  @Test
  void argsMatch4() {
    pointcut.setExpression("execution(* *(String, ..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
  }
}
