package smaple.cafekiosk.learning;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class GuavaLearningTest {


    @Test
    @DisplayName("주어진 개수만큼 list를 파티셔닝한다.")
    void test() throws Exception {
        //given
        List<Integer> integers = List.of(1, 2, 3, 4, 5, 6);

        // when
        List<List<Integer>> partition = Lists.partition(integers, 3);
        //3개씩 나누기.

        //then
        assertThat(partition).hasSize(2)
                .isEqualTo(List.of(
                        List.of(1,2,3),
                        List.of(4,5,6)
                        )
                );
    }

    @Test
    @DisplayName("나누는 숫자를 4로 설정해 파티셔닝한다.")
    void test2() throws Exception {
        //given
        List<Integer> integers = List.of(1, 2, 3, 4, 5, 6);

        // when
        List<List<Integer>> partition = Lists.partition(integers, 4);
        //3개씩 나누기.

        //then
        assertThat(partition).hasSize(2)
                .isEqualTo(List.of(
                                List.of(1,2,3,4),
                                List.of(5,6)
                        )
                );
    }


    @Test
    @DisplayName("멀티맵 기능 확인.")
    void test3() throws Exception {
        //given
        Multimap<String , String > multimap = ArrayListMultimap.create();
        multimap.put("coffee", "Americano");
        multimap.put("coffee", "Latte");
        multimap.put("coffee", "cappuccino");

        // when
        Collection<String> coffee = multimap.get("coffee");

        //then
        assertThat(coffee).hasSize(3)
                .isEqualTo(List.of("Americano","Latte","cappuccino"));
    }

    @TestFactory
    @DisplayName("멀티맵 기능 확인2")
    Collection<DynamicTest> test4() throws Exception {
        //given
        Multimap<String , String > multimap = ArrayListMultimap.create();
        multimap.put("coffee", "Americano");
        multimap.put("coffee", "Latte");
        multimap.put("coffee", "cappuccino");
        multimap.put("bakery", "croissant");
        multimap.put("bakery", "baguette");

        return List.of(
                DynamicTest.dynamicTest("1개 value를 삭제한다.", () -> {
                    //when
                    multimap.remove("coffee","cappuccino");
                    //then
                    Collection<String> coffee = multimap.get("coffee");
                    assertThat(coffee).hasSize(2)
                            .isEqualTo(List.of("Americano","Latte"));
                        }),

                DynamicTest.dynamicTest("key 전체 삭제한다.", () -> {
                    //when
                    multimap.removeAll("coffee");
                    //then
                    Collection<String> coffee = multimap.get("coffee");
                    assertThat(coffee).isEmpty();
                })
        );
    }
}
